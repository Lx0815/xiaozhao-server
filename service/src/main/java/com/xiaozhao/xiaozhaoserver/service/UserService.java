package com.xiaozhao.xiaozhaoserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaozhao.xiaozhaoserver.model.User;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-14 11:33:00
 * @modify:
 */

public interface UserService extends IService<User> {

    /**
     * 保存一个用户。包括 向腾讯云请求新增人员、向数据库新增用户、新增或替换用户的人脸等
     * @param imageBase64Str 人脸图片的 base64 字符串
     * @param personFaceScore 当前人脸的质量分数
     * @param groupId 人员库id，是 UUID，用于创建人员时的请求参数
     * @return 返回新增的用户，或已存在的用户
     */
    User saveOneUser(String imageBase64Str, Long personFaceScore, String groupId);

}
