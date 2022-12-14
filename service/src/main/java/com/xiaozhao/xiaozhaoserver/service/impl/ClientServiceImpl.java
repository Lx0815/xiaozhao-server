package com.xiaozhao.xiaozhaoserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.iai.v20200303.models.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-09 16:22:18
 * @modify:
 */
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
        // 创建 UUID 为 发送到腾讯云的人员库 ID
        String uuid = UUID.randomUUID().toString();
        // 插入新的客户端地址
        int rows = clientLocationMapper.insert(clientLocation);
        if (rows != 1) throw new RuntimeException("clientLocation 存储失败");
        Client client = new Client();
        client.setClientId(uuid)
                .setLocationId(clientLocation.getId())
                .setLastUploadDateTime(LocalDateTime.now());
        // 插入新的客户端
        rows = clientMapper.insert(client);
        if (rows != 1) throw new RuntimeException("client 存储失败");

        // 创建人员库
        // 封装创建人员库的请求参数
        CreateGroupRequest createGroupRequest = new CreateGroupRequest();
        createGroupRequest.setGroupName(uuid);
        createGroupRequest.setGroupId(uuid);
        createGroupRequest.setGroupExDescriptions(new String[]{String.format("经度：%f，纬度：%f", clientLocation.getLongitude(), clientLocation.getLatitude())});
        try {
            TencentApiUtils.executeIciClientRequest(createGroupRequest, CreateGroupResponse.class,
                    publicTencentApiProperty);
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("创建人员库失败", e);
        }

        // 插入新的人员库
        PersonGroup personGroup = new PersonGroup();
        personGroup.setClientId(client.getId()).setGroupId(uuid);
        rows = personGroupMapper.insert(personGroup);
        if (rows != 1) throw new RuntimeException("personGroup 存储失败");

        // 更新客户端的person_group.id
        rows = clientMapper.updateById(client.setPersonGroupId(personGroup.getId()));
        if (rows != 1) throw new RuntimeException("client 的 person_group.id 更新失败");

        // 返回客户端的 ID，也是人员库ID
        return uuid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestRecord analyzeAndSaveFaceInformation(DetectFaceRequest detectFaceRequest, String groupId) {
        // 进行人脸检测与分析
        DetectFaceResponse detectFaceResponse = personFaceService.faceDetectionAndAnalysis(detectFaceRequest);
        // 获取分析结果
        FaceAttributesInfo faceAttributesInfo = detectFaceResponse.getFaceInfos()[0].getFaceAttributesInfo();
        // 通过分析结果初始化 TestRecord
        TestRecord testRecord = new TestRecord(faceAttributesInfo);

        // 首先根据 groupId 获取该 person_group.id
        PersonGroup personGroup = personGroupMapper.selectOne(new QueryWrapper<PersonGroup>().eq("group_id", groupId));
        // 获取测试的客户端 id
        Client client = clientMapper.selectOne(new QueryWrapper<Client>().eq("person_group_id", personGroup.getId()));
        // 填充客户端 id
        testRecord.setClientId(client.getId());

        // 保存当前用户
        User user = userService.saveOneUser(detectFaceRequest.getImage(), detectFaceResponse.getFaceInfos()[0].getFaceQualityInfo().getScore(), groupId);
        if (ObjectUtils.isEmpty(user)) throw new RuntimeException("user 不存在或创建失败");
        testRecord.setUserId(user.getId());

        // 保存测试记录
        int rows = testRecordMapper.insert(testRecord);
        if (rows != 1) throw new RuntimeException("testRecord 添加失败");
        return testRecord;
    }
}
