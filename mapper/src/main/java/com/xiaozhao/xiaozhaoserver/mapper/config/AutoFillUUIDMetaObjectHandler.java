package com.xiaozhao.xiaozhaoserver.mapper.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @description: 自动填充 UUID 处理器
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-06 22:42:41
 * @modify:
 */

@Slf4j
@Component
public class AutoFillUUIDMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入时自动填充
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始自动填充");
        this.strictInsertFill(metaObject, "createDateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateDateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "client_id", String.class, UUID.randomUUID().toString());
        log.info("自动填充成功");
    }

    /**
     * 更新时自动填充
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始自动填充");
        this.strictInsertFill(metaObject, "updateDateTime", LocalDateTime.class, LocalDateTime.now());
        log.info("自动填充成功");
    }
}
