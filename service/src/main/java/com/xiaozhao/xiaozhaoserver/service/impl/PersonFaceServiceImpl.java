package com.xiaozhao.xiaozhaoserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.iai.v20200303.IaiErrorCode;
import com.tencentcloudapi.iai.v20200303.models.CreateFaceRequest;
import com.tencentcloudapi.iai.v20200303.models.CreateFaceResponse;
import com.tencentcloudapi.iai.v20200303.models.DetectFaceRequest;
import com.tencentcloudapi.iai.v20200303.models.DetectFaceResponse;
import com.xiaozhao.xiaozhaoserver.configProp.TencentApiPublicProperties;
import com.xiaozhao.xiaozhaoserver.exception.BadParameterException;
import com.xiaozhao.xiaozhaoserver.exception.NoFaceInPhotoException;
import com.xiaozhao.xiaozhaoserver.mapper.PersonFaceMapper;
import com.xiaozhao.xiaozhaoserver.mapper.UserMapper;
import com.xiaozhao.xiaozhaoserver.model.PersonFace;
import com.xiaozhao.xiaozhaoserver.model.User;
import com.xiaozhao.xiaozhaoserver.service.PersonFaceService;
import com.xiaozhao.xiaozhaoserver.utils.TencentApiUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;

import java.util.HashMap;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-14 11:38:00
 * @modify:
 */

@Slf4j
@Service
@Transactional
public class PersonFaceServiceImpl extends ServiceImpl<PersonFaceMapper, PersonFace> implements PersonFaceService {

    @Autowired
    private TencentApiPublicProperties tencentApiPublicProperties;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PersonFaceMapper personFaceMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public DetectFaceResponse faceDetectionAndAnalysis(DetectFaceRequest detectFaceRequest) {
        detectFaceRequest.setNeedQualityDetection(1L);
        detectFaceRequest.setNeedFaceAttributes(1L);
        if (StringUtils.isBlank(detectFaceRequest.getImage()) && StringUtils.isBlank(detectFaceRequest.getUrl()))
            throw new BadParameterException("CreatePersonRequest ????????????????????? Image ??? Url ????????????");
        HashMap<String, String> map = new HashMap<>();
        detectFaceRequest.toMap(map, "");
        StopWatch stopWatch = new StopWatch();
        try {
            log.info("?????????????????????????????????");
            stopWatch.start();
            DetectFaceResponse detectFaceResponse = TencentApiUtils.executeIciClientRequest(detectFaceRequest,
                    DetectFaceResponse.class, tencentApiPublicProperties);
            stopWatch.stop();
            log.info("???????????????????????????????????????" + stopWatch.getTotalTimeMillis() + " ms");
            return detectFaceResponse;
        } catch (TencentCloudSDKException e) {
            log.error("??????????????????????????????????????????????????????\n" + map);
            if (ObjectUtils.nullSafeEquals(e.getErrorCode(), IaiErrorCode.INVALIDPARAMETERVALUE_NOFACEINPHOTO.getValue())) {
                throw new NoFaceInPhotoException(e);
            }
            throw new BadParameterException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void save(CreateFaceRequest createFaceRequest, Long personScore) {
        log.info("????????????????????????");
        HashMap<String, String> map = new HashMap<>();
        createFaceRequest.toMap(map, "");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CreateFaceResponse createFaceResponse;
        try {
            createFaceResponse = TencentApiUtils.executeIciClientRequest(createFaceRequest, CreateFaceResponse.class,
                    tencentApiPublicProperties);
            stopWatch.stop();
        } catch (TencentCloudSDKException e) {
            log.error("?????????????????????????????????????????????\n" + map);
            throw new BadParameterException(e);
        }
        if (createFaceResponse.getSucFaceNum() != 1) {
            throw new BadParameterException("??????????????????");
        }
        log.info("????????????????????????" + stopWatch.getTotalTimeMillis() + "ms");

        log.info("???????????? personFace");
        String faceId = createFaceResponse.getSucFaceIds()[0];
        User user = userMapper.selectOne(
                new QueryWrapper<User>()
                        .select("id")
                        .eq("person_id", createFaceRequest.getPersonId()));
        PersonFace personFace = new PersonFace()
                .setUserId(user.getId())
                .setFaceId(faceId)
                .setImageUrl(createFaceRequest.getUrls()[0])
                .setImageQualityScore(personScore);
        personFaceMapper.insert(personFace);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkPersonFaceIsFull(User user) {
        return ObjectUtils.nullSafeEquals(5,
                personFaceMapper.selectCount(
                        new QueryWrapper<PersonFace>()
                                .eq("user_id", user.getId())));
    }

}
