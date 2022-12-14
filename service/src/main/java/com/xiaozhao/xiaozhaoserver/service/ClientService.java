package com.xiaozhao.xiaozhaoserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tencentcloudapi.iai.v20200303.models.DetectFaceRequest;
import com.xiaozhao.xiaozhaoserver.model.Client;
import com.xiaozhao.xiaozhaoserver.model.ClientLocation;
import com.xiaozhao.xiaozhaoserver.model.TestRecord;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-09 16:18:35
 * @modify:
 */


public interface ClientService extends IService<Client> {

    /**
     * 初始化客户端。实际上还需要初始化一个人员库，使客户端始终使用同一个人员库
     * @param clientLocation 客户端位置信息
     * @return 返回初始化成功之后的 客户端ID，实际上等同于 人员库 ID，相当于一个凭证
     */
    String initClient(ClientLocation clientLocation);

    /**
     * 分析并保存人脸信息。将分析得到的人脸与腾讯云中的人员进行比对，若无相似人员则新增人员。必填参数为 {@link DetectFaceRequest} 的 Image 变量
     * @param detectFaceRequest 人脸检测与分析的请求对象，包含所有的请求参数
     * @param groupId 人员库信息，新增的人员将存储在此人员库中
     * @return 本次测试记录
     */
    TestRecord analyzeAndSaveFaceInformation(DetectFaceRequest detectFaceRequest, Client client, String groupId);

    Client updateLastUploadDateTime(String personGroupId);
}
