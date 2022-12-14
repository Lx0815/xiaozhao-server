package com.xiaozhao.xiaozhaoserver.service.impl;

import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.xiaozhao.xiaozhaoserver.config.qiniu.QiNiuProperties;
import com.xiaozhao.xiaozhaoserver.service.QiNiuYunService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-11-16 22:26:58
 * @modify:
 */

@Slf4j
@Service
public class QiNiuYunServiceImpl implements QiNiuYunService {

    private UploadManager uploadManager;

    @Autowired
    public void setUploadManager(UploadManager uploadManager) {
        this.uploadManager = uploadManager;
    }

    private Auth auth;

    @Autowired
    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    private QiNiuProperties qiNiuProperties;

    @Autowired
    public void setqiNiuProperties(QiNiuProperties qiNiuProperties) {
        this.qiNiuProperties = qiNiuProperties;
    }

    // 定义七牛云上传的相关策略
    private StringMap putPolicy;

    private long expireSeconds = 3600;

    private static final BASE64Decoder decoder = new BASE64Decoder();

    private static String uploadToken;

    /**
     * 获取上传凭证
     */
    private String getUploadToken() {
        if (ObjectUtils.isEmpty(uploadToken)) {
            if (Objects.isNull(putPolicy)) {
                putPolicy = new StringMap();
                putPolicy.put("returnBody", "{\"fileUrl\":\"" + qiNiuProperties.getDomain() + "$(key)\")}");
            }
            uploadToken = this.auth.uploadToken(qiNiuProperties.getBucket(), null, expireSeconds, putPolicy);
        }
        return uploadToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> saveMultipartFileList(List<MultipartFile> fileList, String directory) {
        directory = Objects.isNull(directory) ? "" : directory;
        StringBuilder path;
        com.qiniu.http.Response response;
        String accessPath = null;
        List<String> accessPathList = new LinkedList<>();
        try {
            StringBuilder sb = new StringBuilder();
            for (MultipartFile multipartFile : fileList) {
                String fileName = multipartFile.getOriginalFilename();
                path = sb.append(directory).append(UUID.randomUUID()).append('.').append(StringUtils.substringAfterLast(fileName, "."));
                response = uploadManager.put(multipartFile.getBytes(), path.toString(), getUploadToken());

                if (!response.isOK()) {
                    throw new RuntimeException("上传文件失败");
                }
                sb.delete(0, sb.length());
                accessPath = sb.append("http://").append(qiNiuProperties.getDomain()).append(path).toString();
                accessPathList.add(accessPath);
                sb.delete(0, sb.length());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.debug("商品图片上传成功，访问路径为：" + accessPath);
        return accessPathList;
    }

    /**
     * {@inheritDoc}
     */
    public List<String> saveMultipartBase64ImageList(List<String> base64List, String directory) {
        StringBuilder sb = new StringBuilder();
        directory = Objects.isNull(directory) ? "" : directory;
        if (directory.startsWith("/"))
            directory = directory.substring(1);
        String path;
        String accessPath;
        List<String> accessPathList = new LinkedList<>();
        try {
            for (String base64 : base64List) {
                // 去掉前缀 data:image/jpg;base64,
                if (base64.startsWith("data:image/jpg;base64,"))
                    base64 = base64.substring(22);
                String fileName = UUID.randomUUID().toString();
                path = sb.append(directory).append(fileName).append(".jpg").toString();

                Response response = uploadManager.put(decoder.decodeBuffer(base64), path, getUploadToken());

                if (!response.isOK()) {
                    throw new RuntimeException("上传文件失败");
                }

                sb.delete(0, sb.length());
                accessPath = sb.append("http://").append(qiNiuProperties.getDomain()).append(path).toString();
                accessPathList.add(accessPath);
                sb.delete(0, sb.length());

            }
            return accessPathList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
