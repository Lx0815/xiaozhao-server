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
2. 腾讯云 配置（源代码见：`com.xiaozhao.xiaozhaoserver.configProp.TencentApiPublicProperties`）

    1. 在 **环境变量** 中配置腾讯云API密钥

        - 若无密钥可在此处进行创建 https://console.cloud.tencent.com/cam/capi
        - `SecretId` 对应的的默认环境变量名称为 `XIAO_ZHAO_DEFAULT_TENCENT_SECRET_ID`，也可通过在 application.yml
          中配置 `tencent.secretIdEnvName` 来指定
        - `SecretKey` 对应的的默认环境变量名称为 `XIAO_ZHAO_DEFAULT_TENCENT_SECRET_KEY`，也可通过在 application.yml
          中配置 `tencent.secretKeyEnvName` 来指定

    2. 在 application.yml 中配置
        - `domainName`：腾讯云 API 的域名
        - `region`：请求时选择的区域

3. 七牛云配置（源代码见：`com.xiaozhao.xiaozhaoserver.configProp.QiNiuProperties`）
    1. 在 **环境变量** 中配置 七牛云存储API密钥

        - 若无密钥请前往 https://portal.qiniu.com/user/key
        - `accessKey` 对应的环境变量名称默认为 `XIAO_ZHAO_DEFAULT_QINIU_ACCESS_KEY`，也可通过在 application.yml
          中配置 `qiniu.accessKeyEnvName` 来指定
        - `secretKey` 对应的环境变量名称默认为 `XIAO_ZHAO_DEFAULT_QINIU_SECRET_KEY`，也可通过在 application.yml
          中配置 `qiniu.secretKeyEnvName` 来指定

    2. 在 application.yml 中配置
        - `bucket`：存储桶名称
        - `region`：区域
        - `domain`：域名
        - `accelerateUploadDomain`：加速域名（好像不是必须的）

4. 微信小程序配置（源代码见：`com.xiaozhao.xiaozhaoserver.configProp.Code2SessionRequestProperties`)
    1. 在 **环境变量** 中配置 微信小程序 appid 和 密钥

        - 若无密钥请前往
        - `appid` 对应的的默认环境变量名称为 `XIAO_ZHAO_DEFAULT_WX_APPID`，也可通过在 application.yml
          中配置 `wx.appidEnvName` 来指定
        - `secret` 对应的的默认环境变量名称为 `XIAO_ZHAO_DEFAULT_WX_SECRET`，也可通过在 application.yml
          中配置 `wx.secretEnvName` 来指定

    2. 在 application 中配置

        - `url`：登录凭证校验接口的接口地址，默认是：`https://api.weixin.qq.com/sns/jscode2session`
        - `grant_type`：登录凭证校验接口 的授权类型，默认为：`authorization_code`

5. JWT 配置

    1. 在 **环境变量** 中配置 JWT 的 `JWT_SECRET_KEY`
   
    - `JWT_SECRET_KEY` 对应的默认环境变量名称为 `XIAO_ZHAO_DEFAULT_JWT_SECRET_KEY`。此环境变量名称不支持手动配置

6. 环境变量管理

    - 为了方便环境变量的管理，建议使用项目根目录下的 initEnvironment.ps1 和 deleteEnvironment.ps1 进行管理。需要注意的是这两个文件需要手动运行，并且不建议将其纳入 git 管理。因为当前程序设置的环境变量需要重启才能被获取到
    - initEnvironment.ps1 示例
   ```
      
      "start init ..."
      
      # Tencent
      [Environment]::SetEnvironmentVariable("XIAO_ZHAO_DEFAULT_TENCENT_SECRET_ID", "xxx", [EnvironmentVariableTarget]::User)
      [Environment]::SetEnvironmentVariable("XIAO_ZHAO_DEFAULT_TENCENT_SECRET_KEY", "xxx", [EnvironmentVariableTarget]::User)
      
      # QiNiu
      [Environment]::SetEnvironmentVariable("XIAO_ZHAO_DEFAULT_QINIU_ACCESS_KEY", "xxx", [EnvironmentVariableTarget]::User)
      [Environment]::SetEnvironmentVariable("XIAO_ZHAO_DEFAULT_QINIU_SECRET_KEY", "xxx", [EnvironmentVariableTarget]::User)
      
      # Wechat Mini Program
      [Environment]::SetEnvironmentVariable("XIAO_ZHAO_DEFAULT_WX_APPID", "xxx", [EnvironmentVariableTarget]::User)
      [Environment]::SetEnvironmentVariable("XIAO_ZHAO_DEFAULT_WX_SECRET", "xxx", [EnvironmentVariableTarget]::User)
   
      # JWT
      [Environment]::SetEnvironmentVariable("XIAO_ZHAO_DEFAULT_JWT_SECRET_KEY", "xiaozhao", [EnvironmentVariableTarget]::User)
   
   
      "init successed..."
      exit
   
   ```
    - deleteEnvironment.ps1 示例

   ```

      "start delete ..."

      # Tencent
      [Environment]::SetEnvironmentVariable("XIAO_ZHAO_DEFAULT_TENCENT_SECRET_ID", "", [EnvironmentVariableTarget]::User)
      [Environment]::SetEnvironmentVariable("XIAO_ZHAO_DEFAULT_TENCENT_SECRET_KEY", "", [EnvironmentVariableTarget]::User)
      
      # QiNiu
      [Environment]::SetEnvironmentVariable("XIAO_ZHAO_DEFAULT_QINIU_ACCESS_KEY", "", [EnvironmentVariableTarget]::User)
      [Environment]::SetEnvironmentVariable("XIAO_ZHAO_DEFAULT_QINIU_SECRET_KEY", "", [EnvironmentVariableTarget]::User)
      
      # Wechat Mini Program
      [Environment]::SetEnvironmentVariable("XIAO_ZHAO_DEFAULT_WX_APPID", "", [EnvironmentVariableTarget]::User)
      [Environment]::SetEnvironmentVariable("XIAO_ZHAO_DEFAULT_WX_SECRET", "", [EnvironmentVariableTarget]::User)

      # JWT
      [Environment]::SetEnvironmentVariable("XIAO_ZHAO_DEFAULT_JWT_SECRET_KEY", "xiaozhao", [EnvironmentVariableTarget]::User)
   
      "delete successed..."
      exit

   ```
