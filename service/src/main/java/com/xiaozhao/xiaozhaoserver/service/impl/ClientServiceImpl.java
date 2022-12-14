package com.xiaozhao.xiaozhaoserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.iai.v20200303.models.*;
import com.xiaozhao.xiaozhaoserver.exception.BadParameterException;
import com.xiaozhao.xiaozhaoserver.exception.ResourceNotFoundException;
import com.xiaozhao.xiaozhaoserver.mapper.ClientLocationMapper;
import com.xiaozhao.xiaozhaoserver.mapper.ClientMapper;
import com.xiaozhao.xiaozhaoserver.mapper.PersonGroupMapper;
import com.xiaozhao.xiaozhaoserver.mapper.TestRecordMapper;
import com.xiaozhao.xiaozhaoserver.model.*;
import com.xiaozhao.xiaozhaoserver.configProp.PublicTencentApiProperty;
import com.xiaozhao.xiaozhaoserver.service.ClientService;
import com.xiaozhao.xiaozhaoserver.service.PersonFaceService;
import com.xiaozhao.xiaozhaoserver.service.UserService;
import com.xiaozhao.xiaozhaoserver.utils.TencentApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-09 16:22:18
 * @modify:
 */

@Slf4j
@Service
@Transactional
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService {

    @Autowired
    private ClientLocationMapper clientLocationMapper;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private PersonGroupMapper personGroupMapper;

    @Autowired
    private TestRecordMapper testRecordMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PublicTencentApiProperty publicTencentApiProperty;

    @Autowired
    private PersonFaceService personFaceService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String initClient(ClientLocation clientLocation) {
        log.info("开始初始化客户端");
        // 创建 UUID 为 发送到腾讯云的人员库 ID
        String uuid = UUID.randomUUID().toString();
        log.info("准备插入 clientLocation : " + clientLocation);
        clientLocationMapper.insert(clientLocation);

        Client client = new Client();
        client.setClientId(uuid)
                .setLocationId(clientLocation.getId())
                .setLastUploadDateTime(LocalDateTime.now());
        log.info("准备插入 client");
        clientMapper.insert(client);

        log.info("准备开始向腾讯云API请求创建人员库");
        CreateGroupRequest createGroupRequest = new CreateGroupRequest();
        createGroupRequest.setGroupName(uuid);
        createGroupRequest.setGroupId(uuid);
        createGroupRequest.setGroupExDescriptions(new String[]{String.format("经度：%f，纬度：%f", clientLocation.getLongitude(), clientLocation.getLatitude())});

        HashMap<String, String> map = new HashMap<>();
        createGroupRequest.toMap(map, "");
        try {

            log.info("开始创建人员库，请求参数为：\n" + map);
            TencentApiUtils.executeIciClientRequest(createGroupRequest, CreateGroupResponse.class,
                    publicTencentApiProperty);
        } catch (TencentCloudSDKException e) {
            log.error("创建人员库失败，本次请求对象为：" + map);
            throw new BadParameterException("创建人员库失败", e);
        }
        log.info("创建人员库成功");

        log.info("准备插入 personGroup");
        PersonGroup personGroup = new PersonGroup();
        personGroup.setClientId(client.getId()).setGroupId(uuid);
        personGroupMapper.insert(personGroup);

        log.info("更新客户端的 person_group.id");
        clientMapper.updateById(client.setPersonGroupId(personGroup.getId()));

        // 返回客户端的 ID，也是人员库ID
        return uuid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestRecord analyzeAndSaveFaceInformation(DetectFaceRequest detectFaceRequest, Client client, String groupId) {
        log.info("准备进行人脸检测与分析");
        DetectFaceResponse detectFaceResponse = personFaceService.faceDetectionAndAnalysis(detectFaceRequest);

        // 获取分析结果
        FaceAttributesInfo faceAttributesInfo = detectFaceResponse.getFaceInfos()[0].getFaceAttributesInfo();
        // 通过分析结果初始化 TestRecord
        TestRecord testRecord = new TestRecord(faceAttributesInfo);



        // 填充客户端 id
        testRecord.setClientId(client.getId());

        log.info("准备保存当前提供人脸的用户");
        User user = userService.saveOneUser(detectFaceRequest.getImage(), detectFaceResponse.getFaceInfos()[0].getFaceQualityInfo().getScore(), groupId);
        if (ObjectUtils.isEmpty(user)) throw new ResourceNotFoundException("user 不存在或创建失败");
        testRecord.setUserId(user.getId());

        // 保存测试记录
        log.info("准备插入 testRecord");
        testRecordMapper.insert(testRecord);

        return testRecord;
    }

    @Override
    public Client updateLastUploadDateTime(String personGroupId) {
        // 首先根据 groupId 获取该 person_group.id
        PersonGroup personGroup = personGroupMapper.selectOne(new QueryWrapper<PersonGroup>().eq("group_id", personGroupId));
        // 获取测试的客户端 id
        log.info("准备更新 client");
        Client client = clientMapper.selectOne(new QueryWrapper<Client>().eq("person_group_id", personGroup.getId()));
        client.setLastUploadDateTime(LocalDateTime.now());
        clientMapper.insert(client);
        return client;
    }
}
