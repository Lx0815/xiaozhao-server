package com.xiaozhao.xiaozhaoserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.iai.v20200303.IaiErrorCode;
import com.tencentcloudapi.iai.v20200303.models.CreateFaceRequest;
import com.tencentcloudapi.iai.v20200303.models.CreateFaceResponse;
import com.tencentcloudapi.iai.v20200303.models.DetectFaceRequest;
import com.tencentcloudapi.iai.v20200303.models.DetectFaceResponse;
import com.xiaozhao.xiaozhaoserver.exception.BadParameterException;
import com.xiaozhao.xiaozhaoserver.exception.NoFaceInPhotoException;
import com.xiaozhao.xiaozhaoserver.mapper.PersonFaceMapper;
import com.xiaozhao.xiaozhaoserver.mapper.UserMapper;
import com.xiaozhao.xiaozhaoserver.model.PersonFace;
import com.xiaozhao.xiaozhaoserver.model.User;
import com.xiaozhao.xiaozhaoserver.configProp.PublicTencentApiProperty;
import com.xiaozhao.xiaozhaoserver.service.PersonFaceService;
import com.xiaozhao.xiaozhaoserver.utils.TencentApiUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-14 11:38:00
 * @modify:
 */

@Service
@Transactional
public class PersonFaceServiceImpl extends ServiceImpl<PersonFaceMapper, PersonFace> implements PersonFaceService {

    @Autowired
    private PublicTencentApiProperty publicTencentApiProperty;

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
            throw new BadParameterException("CreatePersonRequest 中至少需要包含 Image 和 Url 其中之一");
        try {
            return TencentApiUtils.executeIciClientRequest(detectFaceRequest,
                    DetectFaceResponse.class, publicTencentApiProperty);
        } catch (TencentCloudSDKException e) {
            if (ObjectUtils.nullSafeEquals(e.getErrorCode(), IaiErrorCode.INVALIDPARAMETERVALUE_NOFACEINPHOTO.getValue())) {
                throw new NoFaceInPhotoException(e);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void save(CreateFaceRequest createFaceRequest, Long personScore) {

        CreateFaceResponse createFaceResponse;
        try {
            createFaceResponse = TencentApiUtils.executeIciClientRequest(createFaceRequest, CreateFaceResponse.class,
                    publicTencentApiProperty);
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException(e);
        }
        Assert.notNull(createFaceResponse, "添加人脸失败");

        if (createFaceResponse.getSucFaceNum() != 1)
            throw new RuntimeException("添加人脸失败");

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
        int rows = personFaceMapper.insert(personFace);
        if (rows != 1) throw new RuntimeException("添加人脸失败");
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
