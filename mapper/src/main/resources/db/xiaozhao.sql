DROP DATABASE IF EXISTS xiaozhao;
CREATE DATABASE IF NOT EXISTS xiaozhao;

USE xiaozhao;



DROP TABLE IF EXISTS `client`;
CREATE TABLE IF NOT EXISTS `client`
(
    `id`                    INT      NOT NULL AUTO_INCREMENT COMMENT '主键id，无实际业务含义',
    `client_id`             CHAR(36) NOT NULL COMMENT 'UUID，客户端的唯一标识',
    `person_group_id`       INT      NULL COMMENT 'person_group.id，该设备搜索人脸时优先从该库搜索。',
    `last_upload_date_time` DATETIME NOT NULL COMMENT '最后一次上传时间，用于判断客户端使用情况',
    `longitude`             DOUBLE   NOT NULL COMMENT '经度',
    `latitude`              DOUBLE   NOT NULL COMMENT '纬度',
    `create_date_time`      DATETIME NOT NULL COMMENT '创建时间',
    `update_date_time`      DATETIME NOT NULL COMMENT '上次修改时间',
    PRIMARY KEY (`id`)
);
INSERT INTO client (id, client_id, person_group_id, last_upload_date_time, longitude, latitude,
                    create_date_time, update_date_time)
VALUES (1, 'c1138a74-7173-4ee4-930f-fa3ebdc643d4', 1, '2022-12-14 10:20:09', '113.84343', '22.771793',
        '2022-12-14 10:20:09', '2022-12-14 10:20:09');

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user`
(
    `id`               INT          NOT NULL AUTO_INCREMENT COMMENT '主键id，无实际业务含义',
    `person_id`        CHAR(36)     NULL COMMENT '人员id，UUID生成',
    `openid`           VARCHAR(128) NULL COMMENT '微信用户的唯一ID',
    `nick_name`        VARCHAR(16)  NULL COMMENT '人员昵称，后期用户在小程序中填写',
    `real_gender`      CHAR(1)      NULL COMMENT '性别，后期用户在小程序中填写',
    `real_age`         INT          NULL COMMENT '年龄，后期用户在小程序中填写',
    `create_date_time` DATETIME     NOT NULL COMMENT '创建时间',
    `update_date_time` DATETIME     NOT NULL COMMENT '上次修改时间',
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `person_face`;
CREATE TABLE IF NOT EXISTS `person_face`
(
    `id`                  INT          NOT NULL AUTO_INCREMENT COMMENT '主键id，无实际业务含义',
    `user_id`             INT          NOT NULL COMMENT '用户id，映射 user.id',
    `face_id`             VARCHAR(32)  NOT NULL COMMENT '人脸 id，添加人脸之后返回',
    `image_url`           VARCHAR(255) NOT NULL COMMENT '人脸图片的url，存放来自七牛云的url，后期记得改字段长度',
    `image_quality_score` LONG         NOT NULL COMMENT '图片质量分数',
    `create_date_time`    DATETIME     NOT NULL COMMENT '创建时间',
    `update_date_time`    DATETIME     NOT NULL COMMENT '上次修改时间',
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `person_group`;
CREATE TABLE IF NOT EXISTS `person_group`
(
    `id`               INT      NOT NULL AUTO_INCREMENT COMMENT '主键id，无实际业务含义',
    `group_id`         CHAR(36) NOT NULL COMMENT 'UUID，唯一标识人员库。',
    `client_id`        INT      NOT NULL COMMENT '客户端id，映射client.id，可作为人员库名称使用',
    `create_date_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_date_time` DATETIME NOT NULL COMMENT '上次修改时间',
    PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `test_record`;
CREATE TABLE IF NOT EXISTS `test_record`
(
    `id`               INT      NOT NULL AUTO_INCREMENT COMMENT '主键id，无实际业务含义',
    `user_id`          INT      NOT NULL COMMENT '用户id，映射 user.id',
    `client_id`        INT      NOT NULL COMMENT '检测时所使用的客户端',
    `temperature`      FLOAT    NOT NULL COMMENT '体温',
    `age`              INT      NOT NULL COMMENT '年龄，当用户通过小程序手动设置后，此字段不应显示',
    `beauty`           INT      NOT NULL COMMENT '0-100，数据由腾讯云人脸识别接口提供',
    `heart_rate`       INT      NOT NULL COMMENT '心率',
    `create_date_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_date_time` DATETIME NOT NULL COMMENT '上次修改时间',
    PRIMARY KEY (`id`)
);

