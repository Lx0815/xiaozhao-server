package com.xiaozhao.xiaozhaoserver.service.impl;

import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.xiaozhao.xiaozhaoserver.configProp.QiNiuProperties;
import com.xiaozhao.xiaozhaoserver.exception.BadParameterException;
import com.xiaozhao.xiaozhaoserver.service.QiNiuYunService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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
        directory = StringUtils.isBlank(directory) ? qiNiuProperties.getRootDirectory() : directory;
        if (directory.startsWith("/"))
            directory = directory.substring(1);
        String path;
        String accessPath;
        List<String> accessPathList = new LinkedList<>();
        try {
            log.info(LocalDateTime.now() + "  准备开始上传文件到七牛云存储");
            log.info("基础目录为：" + directory);
            StopWatch stopWatch = new StopWatch();
            for (String base64 : base64List) {
                String fileName = UUID.randomUUID().toString();
                stopWatch.start(fileName + ".jpg");
                // 去掉前缀 data:image/jpg;base64,
                if (base64.startsWith("data:"))
                    base64 = base64.substring(base64.indexOf(",") + 1);

                path = sb.append(directory).append(fileName).append(".jpg").toString();

                log.info("开始上传文件：" + path);
                Response response = uploadManager.put(decoder.decodeBuffer(base64), path, getUploadToken());

                if (!response.isOK()) {
                    throw new BadParameterException("上传文件失败");
                }

                sb.delete(0, sb.length());
                accessPath = sb.append("http://").append(qiNiuProperties.getDomain()).append(path).toString();
                log.info("成功上传文件：" + accessPath);
                accessPathList.add(accessPath);
                sb.delete(0, sb.length());

                stopWatch.stop();
            }
            log.info(String.format("上传结束，本次共计上传 %d 个文件，耗时：%d ms", stopWatch.getTaskCount(), stopWatch.getTotalTimeMillis()));
            return accessPathList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
