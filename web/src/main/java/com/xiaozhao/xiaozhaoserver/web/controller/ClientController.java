package com.xiaozhao.xiaozhaoserver.web.controller;

import com.tencentcloudapi.iai.v20200303.models.DetectFaceRequest;
import com.tencentcloudapi.iai.v20200303.models.SearchPersonsRequest;
import com.xiaozhao.xiaozhaoserver.common.constants.Constants;
import com.xiaozhao.xiaozhaoserver.model.Client;
import com.xiaozhao.xiaozhaoserver.model.PersonGroup;
import com.xiaozhao.xiaozhaoserver.model.TestRecord;
import com.xiaozhao.xiaozhaoserver.model.User;
import com.xiaozhao.xiaozhaoserver.service.ClientService;
import com.xiaozhao.xiaozhaoserver.service.PersonGroupService;
import com.xiaozhao.xiaozhaoserver.service.UserService;
import com.xiaozhao.xiaozhaoserver.web.pool.ResponseObjectPool;
import com.xiaozhao.xiaozhaoserver.web.response.ResponseCode;
import com.xiaozhao.xiaozhaoserver.web.response.ResponseObject;
import com.xiaozhao.xiaozhaoserver.web.utils.JWTUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private PersonGroupService personGroupService;

    @Autowired
    private UserService userService;

    @ApiOperation("初始化人员库")
    @PostMapping("/person_group")
    public Object initPersonGroup(@RequestBody Client client, HttpServletRequest request,
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
        log.info("client: " + client);
        String personGroupId = clientService.initClient(client);
        log.info("初始化完成，初始化的得到的 personGroupId: " + personGroupId);
        Cookie cookie = new Cookie(Constants.PERSON_GROUP_ID_COOKIE_KEY, personGroupId);
        cookie.setMaxAge(Constants.PERSON_GROUP_ID_COOKIE_MAX_AGE);
        cookie.setPath("/");
        response.addCookie(cookie);
        return responseObjectPool.createSuccessResponse(null, "初始化成功");
    }

    @ApiOperation("添加人员")
    @PostMapping("/analyze")
    public Object analyzeAndSaveFaceInformation(@RequestBody DetectFaceRequest detectFaceRequest, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        ResponseObject response = responseObjectPool.createResponse(ResponseCode.ERROR_PARAMETER_EXCEPTION, "检测到设备没有初始化，请先进行初始化");
        if (ObjectUtils.isEmpty(cookies)) {
            log.error(response.toString());
            return response;
        }
        String personGroupId = null;
        for (Cookie cookie : cookies) {
            if (Constants.PERSON_GROUP_ID_COOKIE_KEY.equals(cookie.getName())) {
                personGroupId = cookie.getValue();
                if (ObjectUtils.isEmpty(personGroupId))
                    return response;
            }
        }
        log.info("准备更新 client.last_upload_date_time");
        Client client = clientService.updateLastUploadDateTime(personGroupId);
        TestRecord testRecord = clientService.analyzeAndSaveFaceInformation(detectFaceRequest, client, personGroupId);

        return responseObjectPool.createSuccessResponse(testRecord);
    }

    @PostMapping("/analyze/location/{longitude}/{latitude}")
    public Object miniProgramAnalyzeAndSaveFaceInformation(@RequestBody DetectFaceRequest detectFaceRequest,
                                                           @PathVariable Double longitude,
                                                           @PathVariable Double latitude,
                                                           Integer userId) {


        // 查找最近的一个人员库
        Client client = clientService.findMinDistanceClient(longitude, latitude);

        // 获取人员库ID
        PersonGroup personGroup = personGroupService.getById(client.getPersonGroupId());

        // 人脸检测与分析
        TestRecord testRecord = clientService.analyzeAndSaveFaceInformation(detectFaceRequest, client, personGroup.getGroupId());

        // 合并用户信息
        User user = userService.getById(userId);
        if (! StringUtils.isBlank(user.getPersonId())) {
            // 用户信息完整，无需合并
            return responseObjectPool.createSuccessResponse(new Object[]{testRecord});
        }
        SearchPersonsRequest searchPersonsRequest = new SearchPersonsRequest();
        searchPersonsRequest.setImage(detectFaceRequest.getImage());
        user = userService.bindWechatAndPerson(searchPersonsRequest, longitude, latitude, userId);
        return responseObjectPool.createSuccessResponse(new Object[]{
                testRecord,
                JWTUtils.getToken(Constants.USER_ID_TOKEN_PAYLOAD_KEY, String.valueOf(user.getId()))
        });
    }

    @GetMapping("/location/longitude/{longitude}/latitude/{latitude}/distance/{distance}")
    public Object getClientsInScope(@PathVariable Double longitude,
                                    @PathVariable Double latitude,
                                    @PathVariable Integer distance) {

        return responseObjectPool.createSuccessResponse(clientService.listClintInScope(longitude, latitude, distance));
    }
}
