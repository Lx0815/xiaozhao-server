# 小昭同学服务器端

## 简介
略

## 功能描述

- 接收前端传输 Base64 格式的图片，解析并返回其中包含的人脸信息：年龄、魅力、体温、心率等

## 未来可期

- 暂无

## 开发者指南

### 开发环境

- JDK 1.8.0_351
- IDEA 2022.2.3
- SpringBoot 2.7.6
- Maven 3.8.6
- Git 2.38.1
- MySQL 8.0.30
- ... 其余详见 pom 文件

### 构建指南
- 修复上传到七牛云的图片无法正确访问的 bug
- 移除配置文件中的敏感数据，替换为在环境变量中访问

1. IDEA 中克隆本项目
2. 在 **环境变量** 中配置腾讯云API密钥
   
    - 若无密钥可在此处进行创建 https://console.cloud.tencent.com/cam/capi
    - `SecretId` 对应的的默认环境变量名称为 `TENCENT_SECRET_ID`，也可通过在 application.yml 中配置 `tencent.secretIdEnvName` 来指定
    - `SecretKey` 对应的的默认环境变量名称为 `TENCENT_SECRET_KEY`，也可通过在 application.yml 中配置 `tencent.secretKeyEnvName` 来指定

3. 在 **环境变量** 中配置 七牛云存储API密钥
   
   - 若无密钥请前往 https://portal.qiniu.com/user/key
   - `accessKey` 对应的环境变量名称默认为 `QINIU_ACCESS_KEY`，也可通过在 application.yml 中配置 `qiniu.accessKeyEnvName` 来指定
   - `secretKey` 对应的环境变量名称默认为 `QINIU_SECRET_KEY`，也可通过在 application.yml 中配置 `qiniu.secretKeyEnvName` 来指定




