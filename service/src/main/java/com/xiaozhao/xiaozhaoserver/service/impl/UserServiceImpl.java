package com.xiaozhao.xiaozhaoserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.iai.v20200303.models.*;
import com.xiaozhao.xiaozhaoserver.configProp.TencentApiPublicProperties;
import com.xiaozhao.xiaozhaoserver.exception.BadParameterException;
import com.xiaozhao.xiaozhaoserver.mapper.PersonFaceMapper;
import com.xiaozhao.xiaozhaoserver.mapper.UserMapper;
import com.xiaozhao.xiaozhaoserver.model.PersonFace;
import com.xiaozhao.xiaozhaoserver.model.User;
import com.xiaozhao.xiaozhaoserver.service.PersonFaceService;
import com.xiaozhao.xiaozhaoserver.service.QiNiuYunService;
import com.xiaozhao.xiaozhaoserver.service.UserService;
import com.xiaozhao.xiaozhaoserver.utils.TencentApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;

import java.util.*;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-14 11:33:20
 * @modify:
 */

@Slf4j
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private QiNiuYunService qiNiuYunService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PersonFaceMapper personFaceMapper;

    @Autowired
    private TencentApiPublicProperties tencentApiPublicProperties;

    @Autowired
    private PersonFaceService personFaceService;


    /**
     * {@inheritDoc}
     */
    @Nullable
    public User saveOneUser(String imageBase64Str, Long personFaceScore, String groupId) {

        // 初始化请求对象
        CreatePersonRequest createPersonRequest = new CreatePersonRequest();
        // 用于填充人员名称、人员ID
        String uuid = UUID.randomUUID().toString();
        /*
            设置请求参数
         */
        {
            // 设置人员昵称
            if (StringUtils.isBlank(createPersonRequest.getPersonName()))
                createPersonRequest.setPersonName(uuid);
            // 设置人员ID
            if (StringUtils.isBlank(createPersonRequest.getPersonId()))
                createPersonRequest.setPersonId(uuid);
            // 开启唯一性检测
            if (ObjectUtils.isEmpty(createPersonRequest.getUniquePersonControl())) {
                createPersonRequest.setUniquePersonControl(3L);
            }
            // 设置人脸图片
            createPersonRequest.setImage(imageBase64Str);
            // 设置人员库
            createPersonRequest.setGroupId(groupId);
        }
        HashMap<String, String> map = new HashMap<>();
        createPersonRequest.toMap(map, "");
        log.info("准备向腾讯云API请求创建人员，本次请求对象为：\n" + map);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CreatePersonResponse createPersonResponse;
        try {
            // 发送请求
            createPersonResponse = TencentApiUtils.executeIciClientRequest(createPersonRequest,
                    CreatePersonResponse.class, tencentApiPublicProperties);
            stopWatch.stop();
        } catch (TencentCloudSDKException e) {
            log.error("创建人员请求失败，本次请求对象为：" + map);
            throw new BadParameterException(e);
        }
        log.info("创建人员成功，耗时：" + stopWatch.getTotalTimeMillis() + "ms");

        LinkedList<String> base64List = new LinkedList<>();
        base64List.add(imageBase64Str);
        log.info("准备添加 " + base64List.size() + " 张人脸图片到七牛云存储");
        List<String> accessList = qiNiuYunService.saveMultipartBase64ImageList(base64List, null);
        // 取得该人脸图片的访问路径
        String imageUrl = accessList.get(0);

        /*
            处理添加人员的返回结果
         */
        String similarPersonId = createPersonResponse.getSimilarPersonId();
        // 判断是否有相似人员
        boolean hasSimilarPerson = StringUtils.isBlank(similarPersonId);
        log.info("本次添加人员" + (hasSimilarPerson ? "有" : "无") + "相似人员");
        User user;
        if (hasSimilarPerson) {
            log.info("准备插入 user");

            // 先增加人员
            user = new User();
            user.setPersonId(uuid);
            userMapper.insert(user);

            // 再增加人脸
            log.info("准备插入 personFace");
            PersonFace personFace = new PersonFace();
            personFace.setUserId(user.getId())
                    .setFaceId(createPersonResponse.getFaceId())
                    .setImageUrl(imageUrl)
                    .setImageQualityScore(personFaceScore);
            personFaceMapper.insert(personFace);

        } else {

            // 根据腾讯云接口返回的的相似人员id查询对应的用户
            user = userMapper.selectOne(new QueryWrapper<User>().eq("person_id", similarPersonId));
            // 根据返回的人员 ID 查看数据库中该人员人脸数量是否大于五张
            boolean isFull = personFaceService.checkPersonFaceIsFull(user);
            log.info("该用户的人脸数量" + (isFull ? "满" : "不满") + "上限");
            if (isFull) {
                // 是
                // 删除掉图片质量最小的一张
                // 根据 人员ID 查找人脸质量比当前小的人脸数量

                QueryWrapper<PersonFace> queryWrapper = new QueryWrapper<>();
                queryWrapper
                        .eq("user_id", similarPersonId)
                        .lt("score", personFaceScore);
                log.info("获取人脸质量比当前人脸小的 personFace");
                List<PersonFace> personFaces = personFaceMapper.selectList(queryWrapper);
                if (! ObjectUtils.isEmpty(personFaces)) {
                    // 有一张或多张人脸质量低于当前人脸

                    // 排序以获取人脸质量最低的一张
                    personFaces.sort(Comparator.comparingLong(PersonFace::getImageQualityScore));
                    PersonFace personFace = personFaces.get(0);
                    log.info("准备请求腾讯云API以删除人脸质量最低的");
                    // 删除人脸质量最低的人脸
                    DeleteFaceRequest deleteFaceRequest = new DeleteFaceRequest();
                    deleteFaceRequest.setPersonId(similarPersonId);
                    deleteFaceRequest.setFaceIds(new String[]{personFace.getFaceId()});
                    map = new HashMap<>();
                    deleteFaceRequest.toMap(map, "");
                    stopWatch = new StopWatch();
                    stopWatch.start();
                    try {
                        log.info("开始请求腾讯云API以删除人脸，本次请求对象为：\n" + map);
                        TencentApiUtils.executeIciClientRequest(deleteFaceRequest, DeleteFaceResponse.class,
                                tencentApiPublicProperties);
                        stopWatch.stop();
                    } catch (TencentCloudSDKException e) {
                        log.error("删除失败，本次请求对象为：\n" + map);
                        throw new BadParameterException(e);
                    }
                    log.info("准备删除 personFace");
                    // 从数据库里删除
                    int rows = personFaceMapper.deleteById(personFace);
                    if (rows != 1) throw new RuntimeException("删除人脸失败");

                } else {
                    // 所有的人脸质量都比当前的高
                    // 那就无需增加人员和人脸
                    log.info("所有人脸质量都比当前的高，不进行任何操作");
                    return user;
                }
            }
            // 人脸没有满
            // 为人员添加一张人脸
            log.info("人脸未满，准备添加当前人脸");
            CreateFaceRequest createFaceRequest = new CreateFaceRequest();
            createFaceRequest.setPersonId(similarPersonId);
            createFaceRequest.setUrls(new String[]{imageUrl});
            personFaceService.save(createFaceRequest, personFaceScore);
        }
        return user;
    }

}
