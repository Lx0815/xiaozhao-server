package com.xiaozhao.xiaozhaoserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.iai.v20200303.models.*;
import com.xiaozhao.xiaozhaoserver.configProp.TencentApiPublicProperties;
import com.xiaozhao.xiaozhaoserver.exception.BadParameterException;
import com.xiaozhao.xiaozhaoserver.exception.ResourceNotFoundException;
import com.xiaozhao.xiaozhaoserver.mapper.ClientLocationMapper;
import com.xiaozhao.xiaozhaoserver.mapper.ClientMapper;
import com.xiaozhao.xiaozhaoserver.mapper.PersonGroupMapper;
import com.xiaozhao.xiaozhaoserver.mapper.TestRecordMapper;
import com.xiaozhao.xiaozhaoserver.model.*;
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
    private TencentApiPublicProperties tencentApiPublicProperties;

    @Autowired
    private PersonFaceService personFaceService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String initClient(ClientLocation clientLocation) {
        log.info("????????????????????????");
        // ?????? UUID ??? ?????????????????????????????? ID
        String uuid = UUID.randomUUID().toString();
        log.info("???????????? clientLocation : " + clientLocation);
        clientLocationMapper.insert(clientLocation);

        Client client = new Client();
        client.setClientId(uuid)
                .setLocationId(clientLocation.getId())
                .setLastUploadDateTime(LocalDateTime.now());
        log.info("???????????? client");
        clientMapper.insert(client);

        log.info("????????????????????????API?????????????????????");
        CreateGroupRequest createGroupRequest = new CreateGroupRequest();
        createGroupRequest.setGroupName(uuid);
        createGroupRequest.setGroupId(uuid);
        createGroupRequest.setGroupExDescriptions(new String[]{String.format("?????????%f????????????%f", clientLocation.getLongitude(), clientLocation.getLatitude())});

        HashMap<String, String> map = new HashMap<>();
        createGroupRequest.toMap(map, "");
        try {

            log.info("??????????????????????????????????????????\n" + map);
            TencentApiUtils.executeIciClientRequest(createGroupRequest, CreateGroupResponse.class,
                    tencentApiPublicProperties);
        } catch (TencentCloudSDKException e) {
            log.error("????????????????????????????????????????????????" + map);
            throw new BadParameterException("?????????????????????", e);
        }
        log.info("?????????????????????");

        log.info("???????????? personGroup");
        PersonGroup personGroup = new PersonGroup();
        personGroup.setClientId(client.getId()).setGroupId(uuid);
        personGroupMapper.insert(personGroup);

        log.info("?????????????????? person_group.id");
        clientMapper.updateById(client.setPersonGroupId(personGroup.getId()));

        // ?????????????????? ID??????????????????ID
        return uuid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestRecord analyzeAndSaveFaceInformation(DetectFaceRequest detectFaceRequest, Client client, String groupId) {
        log.info("?????????????????????????????????");
        DetectFaceResponse detectFaceResponse = personFaceService.faceDetectionAndAnalysis(detectFaceRequest);

        // ??????????????????
        FaceAttributesInfo faceAttributesInfo = detectFaceResponse.getFaceInfos()[0].getFaceAttributesInfo();
        // ??????????????????????????? TestRecord
        TestRecord testRecord = new TestRecord(faceAttributesInfo);



        // ??????????????? id
        testRecord.setClientId(client.getId());

        log.info("???????????????????????????????????????");
        User user = userService.saveOneUser(detectFaceRequest.getImage(), detectFaceResponse.getFaceInfos()[0].getFaceQualityInfo().getScore(), groupId);
        if (ObjectUtils.isEmpty(user)) throw new ResourceNotFoundException("user ????????????????????????");
        testRecord.setUserId(user.getId());

        // ??????????????????
        log.info("???????????? testRecord");
        testRecordMapper.insert(testRecord);

        return testRecord;
    }

    @Override
    public Client updateLastUploadDateTime(String personGroupId) {
        // ???????????? groupId ????????? person_group.id
        PersonGroup personGroup = personGroupMapper.selectOne(new QueryWrapper<PersonGroup>().eq("group_id", personGroupId));
        // ???????????????????????? id
        log.info("???????????? client");
        Client client = clientMapper.selectOne(new QueryWrapper<Client>().eq("person_group_id", personGroup.getId()));
        client.setLastUploadDateTime(LocalDateTime.now());
        clientMapper.insert(client);
        return client;
    }
}
