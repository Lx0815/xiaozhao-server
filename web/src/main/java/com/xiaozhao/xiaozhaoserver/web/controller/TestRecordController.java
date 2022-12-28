package com.xiaozhao.xiaozhaoserver.web.controller;

import com.xiaozhao.xiaozhaoserver.model.TestRecord;
import com.xiaozhao.xiaozhaoserver.service.TestRecordService;
import com.xiaozhao.xiaozhaoserver.web.pool.ResponseObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-17 19:34:49
 * @modify:
 */

@RestController
@RequestMapping("/xiaozhao/test_record")
public class TestRecordController {

    @Autowired
    private TestRecordService testRecordService;

    @Autowired
    private ResponseObjectPool responseObjectPool;

    @GetMapping("/count")
    public Object getTestCount(Integer userId) {
        Integer count = testRecordService.selectCountById(userId);
        return responseObjectPool.createSuccessResponse(count);
    }

    @GetMapping("/days/{day}")
    public Object getDaysAgo(@PathVariable Integer day, Integer userId) {
        List<TestRecord> testRecordList = testRecordService.listByDayScope(userId, day);
        return responseObjectPool.createSuccessResponse(testRecordList);
    }

    @GetMapping("/daily/days/{day}")
    public Object getDailyByDays(@PathVariable Integer day, Integer userId) {
        List<TestRecord> testRecordList = testRecordService.listAvgDailyByDays(day, userId);
        return responseObjectPool.createSuccessResponse(testRecordList);
    }

    @GetMapping("/date/{year}/{month}/{day}")
    public Object getByDate(@PathVariable Integer year, @PathVariable Integer month, @PathVariable Integer day) {
        List<TestRecord> testRecordList = testRecordService.listByDate(LocalDate.of(year, month, day));
        return responseObjectPool.createSuccessResponse(testRecordList);
    }
}
