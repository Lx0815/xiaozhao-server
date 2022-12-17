package com.xiaozhao.xiaozhaoserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaozhao.xiaozhaoserver.model.TestRecord;

import java.time.LocalDate;
import java.util.List;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-17 19:53:12
 * @modify:
 */

public interface TestRecordService extends IService<TestRecord> {


    Integer selectCountById(Integer userId);

    List<TestRecord> listByDayScope(Integer userId, Integer day);

    List<TestRecord> listAvgDailyByDays(Integer day, Integer userId);

    List<TestRecord> listByDate(LocalDate date);
}
