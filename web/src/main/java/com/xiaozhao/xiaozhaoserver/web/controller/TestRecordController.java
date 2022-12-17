package com.xiaozhao.xiaozhaoserver.web.controller;

import com.xiaozhao.xiaozhaoserver.common.constants.Constants;
import com.xiaozhao.xiaozhaoserver.model.TestRecord;
import com.xiaozhao.xiaozhaoserver.service.TestRecordService;
import com.xiaozhao.xiaozhaoserver.web.pool.ResponseObjectPool;
import com.xiaozhao.xiaozhaoserver.web.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping("/count/")
    public Object getTestCount(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_HEADER_KEY);
        Integer count = testRecordService.selectCountById(JWTUtils.getUserId(token));
        return responseObjectPool.createSuccessResponse(count);
    }

    @GetMapping("/days/{day}")
    public Object getDaysAgo(@PathVariable Integer day, HttpServletRequest request) {
        Integer userId = JWTUtils.getUserId(request.getHeader(Constants.TOKEN_HEADER_KEY));

        List<TestRecord> testRecordList = testRecordService.listByDayScope(userId, day);
        return responseObjectPool.createSuccessResponse(testRecordList);
    }
}
