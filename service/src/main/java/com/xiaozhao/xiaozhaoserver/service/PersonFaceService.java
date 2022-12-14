package com.xiaozhao.xiaozhaoserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tencentcloudapi.iai.v20200303.models.CreateFaceRequest;
import com.tencentcloudapi.iai.v20200303.models.DetectFaceRequest;
import com.tencentcloudapi.iai.v20200303.models.DetectFaceResponse;
import com.xiaozhao.xiaozhaoserver.model.PersonFace;
import com.xiaozhao.xiaozhaoserver.model.User;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-14 11:37:41
 * @modify:
 */

public interface PersonFaceService extends IService<PersonFace> {

    /**
     * 调用腾讯云接口进行人脸检测与分析
     * @param detectFaceRequest 人脸检测与分析请求对象
     * @return 返回分析的响应对象
     */
    DetectFaceResponse faceDetectionAndAnalysis(DetectFaceRequest detectFaceRequest);

    /**
     * 保存一个人脸信息
     * @param createFaceRequest 创建人脸的请求对象
     * @param personScore 人脸质量分数，可由 {@link #faceDetectionAndAnalysis(DetectFaceRequest)} 得到
     */
    void save(CreateFaceRequest createFaceRequest, Long personScore);

    /**
     * 通过 {@link User#getId()} 检查该用户的人脸是否已经超出了限制（每个用户至多 5 张人脸）
     * @param user 用户，必须要包含 id 参数
     * @return 返回人脸图片数量是否已满
     */
    boolean checkPersonFaceIsFull(User user);
}
