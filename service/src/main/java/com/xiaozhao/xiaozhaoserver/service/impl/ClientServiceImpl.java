package com.xiaozhao.xiaozhaoserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.iai.v20200303.models.*;
import com.xiaozhao.xiaozhaoserver.mapper.ClientMapper;
import com.xiaozhao.xiaozhaoserver.mapper.PersonGroupMapper;
import com.xiaozhao.xiaozhaoserver.mapper.TestRecordMapper;
import com.xiaozhao.xiaozhaoserver.model.Client;
import com.xiaozhao.xiaozhaoserver.model.PersonGroup;
import com.xiaozhao.xiaozhaoserver.model.TestRecord;
import com.xiaozhao.xiaozhaoserver.model.User;
import com.xiaozhao.xiaozhaoserver.service.ClientService;
import com.xiaozhao.xiaozhaoserver.service.PersonFaceService;
import com.xiaozhao.xiaozhaoserver.service.UserService;
import com.xiaozhao.xiaozhaoserver.service.configProp.TencentApiPublicProperties;
import com.xiaozhao.xiaozhaoserver.service.exception.BadParameterException;
import com.xiaozhao.xiaozhaoserver.service.exception.ResourceNotFoundException;
import com.xiaozhao.xiaozhaoserver.service.utils.TencentApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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
    private ClientMapper clientMapper;

    @Autowired
    private PersonGroupMapper personGroupMapper;

    @Autowired
    private TestRecordMapper testRecordMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private TencentApiPublicProperties tencentApiPublicProperties;

    @Autowired
    private PersonFaceService personFaceService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String initClient(Client client) {
        log.info("开始初始化客户端");
        // 创建 UUID 为 发送到腾讯云的人员库 ID
        String uuid = UUID.randomUUID().toString();
        client.setClientId(uuid)
                .setLastUploadDateTime(LocalDateTime.now());
        log.info("准备插入 client");
        clientMapper.insert(client);

        log.info("准备开始向腾讯云API请求创建人员库");
        CreateGroupRequest createGroupRequest = new CreateGroupRequest();
        createGroupRequest.setGroupName(uuid);
        createGroupRequest.setGroupId(uuid);
        createGroupRequest.setGroupExDescriptions(new String[]{String.format("经度：%f，纬度：%f", client.getLongitude(), client.getLatitude())});

        HashMap<String, String> map = new HashMap<>();
        createGroupRequest.toMap(map, "");
        try {

            log.info("开始创建人员库，请求参数为：\n" + map);
            TencentApiUtils.executeIciClientRequest(createGroupRequest, CreateGroupResponse.class,
                    tencentApiPublicProperties);
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

    /**
     * {@inheritDoc}
     */
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

    @Override
    public List<Client> listClintInScope(Double longitude, Double latitude, Integer distance) {
        log.info(String.format("准备查询范围内的客户端，参数为：longitude: %f, latitude: %f, distance: %d",
                longitude, latitude, distance));
        Wrapper<Client> wrapper = new QueryWrapper<Client>()
                .select(String.format("*, ST_DISTANCE_SPHERE(POINT(%f, %f), POINT(longitude, latitude)) AS `distance`",
                        longitude, latitude))
                .apply("{0} >= ST_DISTANCE_SPHERE(POINT({1}, {2}), POINT(longitude, latitude))",
                        distance, longitude, latitude);
        List<Client> clientDetailList = clientMapper.selectList(wrapper);
        log.info("查询成功，返回数据为：\n" + clientDetailList);
        return clientDetailList;
    }

    @Override
    public Client findMinDistanceClient(Double longitude, Double latitude) {
        int distance = 1000;
        Client client = null;
        for (int i = 0; i < 3; i++) {
            List<Client> clientList = listClintInScope(longitude, latitude, distance * (int) Math.pow(10, i));
            if (! ObjectUtils.isEmpty(clientList)) {
                client = clientList.get(0);
                break;
            }
        }
        if (ObjectUtils.isEmpty(client)) {
            throw new BadParameterException("该地区不支持本服务");
        }
        return client;
    }
}
