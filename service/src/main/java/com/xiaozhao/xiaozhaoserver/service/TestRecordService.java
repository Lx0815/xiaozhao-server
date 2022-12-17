package com.xiaozhao.xiaozhaoserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaozhao.xiaozhaoserver.model.TestRecord;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-17 19:53:12
 * @modify:
 */

public interface TestRecordService extends IService<TestRecord> {


    Integer selectCountById(String id);
}
