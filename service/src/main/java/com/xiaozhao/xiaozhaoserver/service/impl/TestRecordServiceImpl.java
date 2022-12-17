package com.xiaozhao.xiaozhaoserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaozhao.xiaozhaoserver.mapper.TestRecordMapper;
import com.xiaozhao.xiaozhaoserver.model.TestRecord;
import com.xiaozhao.xiaozhaoserver.service.TestRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-17 19:53:38
 * @modify:
 */

@Slf4j
@Service
public class TestRecordServiceImpl extends ServiceImpl<TestRecordMapper, TestRecord> implements TestRecordService {


    @Override
    public Integer selectCountById(Integer userId) {
        return Math.toIntExact(count(new QueryWrapper<TestRecord>().eq("user_id", userId)));
    }

    @Override
    public List<TestRecord> listByDayScope(Integer userId, Integer day) {
        return list(new QueryWrapper<TestRecord>()
                .eq("user_id", userId)
                .gt("create_date_time", LocalDateTime.now().minusDays(day)));
    }

    @Override
    public List<TestRecord> listAvgDailyByDays(Integer day, Integer userId) {
        return list(new QueryWrapper<TestRecord>()
                .select("id", "user_id", "AVG(temperature) AS `temperature`", "AVG(age) AS `age`", "AVG(beauty) AS `beauty`"
                , "AVG(heart_rate) AS `heart_rate`", "TIMESTAMP(DATE_FORMAT(`create_date_time`, '%Y-%m-%d')) AS `create_date_time`")
                .groupBy("DATE_FORMAT(`create_date_time`, '%Y-%m-%d')"));
    }

    @Override
    public List<TestRecord> listByDate(LocalDate date) {
        return list(new QueryWrapper<TestRecord>()
                .between("create_date_time", date, date.plusDays(1)));
    }
}
