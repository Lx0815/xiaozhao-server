package com.xiaozhao.xiaozhaoserver.web.controller;

import com.tencentcloudapi.iai.v20200303.models.DetectFaceRequest;
import com.xiaozhao.xiaozhaoserver.common.constants.Constants;
import com.xiaozhao.xiaozhaoserver.model.ClientLocation;
import com.xiaozhao.xiaozhaoserver.model.TestRecord;
import com.xiaozhao.xiaozhaoserver.service.ClientService;
import com.xiaozhao.xiaozhaoserver.web.pool.ResponseObjectPool;
import com.xiaozhao.xiaozhaoserver.web.response.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-07 20:42:55
 * @modify:
 */

@Api("客户端接口")
@Slf4j
@RestController
@RequestMapping("/xiaozhao")
public class ClientController {

    @Autowired
    private ClientService clientService;
    
    @Autowired
    private ResponseObjectPool responseObjectPool;

    @ApiOperation("初始化人员库，使客户端与人员库绑定")
    @PostMapping("/person-group")
    public Object initPersonGroup(@RequestBody ClientLocation clientLocation, HttpServletRequest request,
                                  HttpServletResponse response) {
        // 查看 Cookie 中是否存在 人员库ID，若有则直接返回
        Cookie[] cookies = request.getCookies();
        if (! ObjectUtils.isEmpty(cookies)) {
            for (Cookie cookie : cookies) {
                if (Constants.PERSON_GROUP_ID_COOKIE_KEY.equals(cookie.getName())) {
                    log.info(String.format("已有人员库 %s ，无需重复创建", cookie.getValue()));
                    return responseObjectPool.createResponse(ResponseCode.DATA_ALREADY_EXISTS, "已有人员库，无需重复初始化");
                }
            }
        }
        log.info("开始初始化人员库");
        log.info("clientLocation: " + clientLocation);
        String personGroupId = clientService.initClient(clientLocation);
        log.info("初始化完成，初始化的得到的 personGroupId: " + personGroupId);
        Cookie cookie = new Cookie(Constants.PERSON_GROUP_ID_COOKIE_KEY, personGroupId);
        cookie.setMaxAge(Constants.PERSON_GROUP_ID_COOKIE_MAX_AGE);
        cookie.setPath("/");
        response.addCookie(cookie);
        return responseObjectPool.createSuccessResponse("初始化成功");
    }

    @ApiOperation("添加人员")
    @PostMapping("/person")
    public Object addPerson(@RequestBody DetectFaceRequest detectFaceRequest, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (ObjectUtils.isEmpty(cookies))
            return responseObjectPool.createResponse(ResponseCode.MISSING_PARAMETER_EXCEPTION, "检测到设备没有初始化，请先进行初始化");
        String personGroupId = null;
        for (Cookie cookie : cookies) {
            if (Constants.PERSON_GROUP_ID_COOKIE_KEY.equals(cookie.getName())) {
                personGroupId = cookie.getValue();
                if (ObjectUtils.isEmpty(personGroupId))
                    return responseObjectPool.createResponse(ResponseCode.MISSING_PARAMETER_EXCEPTION, "检测到设备没有初始化，请先进行初始化");
            }
        }
        TestRecord testRecord = clientService.analyzeAndSaveFaceInformation(detectFaceRequest, personGroupId);

        return responseObjectPool.createSuccessResponse(testRecord);
    }

}
