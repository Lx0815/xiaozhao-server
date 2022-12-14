package com.xiaozhao.xiaozhaoserver.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @description: 实现与七牛云的交互
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-11-16 22:26:41
 * @modify:
 */

public interface QiNiuYunService {

    List<String> saveMultipartFileList(List<MultipartFile> fileList, String directory);

    /**
     * 保存多张图片，该图片必须是 base64 的格式。
     * @param base64List 图片列表
     * @param directory 保存的目录路径
     * @return 返回已经保存的图片的访问地址
     */
    List<String> saveMultipartBase64ImageList(List<String> base64List, String directory);

}
