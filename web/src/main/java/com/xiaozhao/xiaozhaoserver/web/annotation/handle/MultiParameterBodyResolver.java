package com.xiaozhao.xiaozhaoserver.web.annotation.handle;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaozhao.xiaozhaoserver.web.annotation.MultiParameterBody;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-16 10:08:15
 * @modify:
 */

@Component
public class MultiParameterBodyResolver implements HandlerMethodArgumentResolver {

    private static final String JSON_REQUEST_BODY = "JSON_REQUEST_BODY";

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MultiParameterBody.class);
    }

    @Override
    public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Object result;
        Object value;
        // 获取请求体
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String requestBody = (String) webRequest.getAttribute(JSON_REQUEST_BODY, NativeWebRequest.SCOPE_REQUEST);
        if (ObjectUtils.isEmpty(requestBody)) {
            try (BufferedReader br = Objects.requireNonNull(request).getReader()) {

                requestBody = IOUtils.toString(br);
                webRequest.setAttribute(JSON_REQUEST_BODY, requestBody, NativeWebRequest.SCOPE_REQUEST);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        JsonNode rootNode = objectMapper.readTree(requestBody);
        // JSON 串为空抛出异常
        Assert.notNull(rootNode, "参数为" + requestBody + " null");
        // 获取注解
        MultiParameterBody multiParameterBody = parameter.getParameterAnnotation(MultiParameterBody.class);
        Assert.notNull(multiParameterBody, "参数" + requestBody + "不存在 MultiRequestBody 注解");

        String key = multiParameterBody.value();
        // 根据注解 value 解析 JSON 串，如果没有根据参数的名字解析 JSON
        if (StringUtils.isNoneBlank(key)) {
            value = rootNode.get(key);
            // 如果为参数必填但未根据 key 成功得到对应 value 抛出异常
            Assert.isTrue(multiParameterBody.required(), key + "为必填参数，但为空");
        } else {
            key = parameter.getParameterName();
            value = rootNode.get(key);
        }

        // 获取参数的类型
        Class<?> parameterType = parameter.getParameterType();
        // 成功从 JSON 解析到对应 key 的 value
        if (!ObjectUtils.isEmpty(value)) {
            return objectMapper.readValue(value.toString(), parameterType);
        }

        // 未从 JSON 解析到对应 key（可能是注解的 value 或者是参数名字） 的值，要么没传值，要么传的名字不对
        // 如果参数为基本数据类型，且为必传参数抛出异常

        Assert.isTrue(!(ClassUtils.isPrimitiveWrapper(parameterType) && multiParameterBody.required()), String.format("required param %s is not present", key));
        // 参数非基本数据类型，如果不允许解析外层属性，且为必传参数 报错抛出异常
        Assert.isTrue(!(!multiParameterBody.parseAllFields() && multiParameterBody.required()), String.format("required param %s is not present", key));

        // 既然找不到对应参数，而且非基本类型，我们可以解析外层属性，将整个 JSON 作为参数进行解析。解析失败会抛出异常
        result = objectMapper.readValue(requestBody, parameterType);
        // 必填参数的话，看解析出来的参数是否对应，非必填直接返回吧
        if (multiParameterBody.required()) {
            Field[] declaredFields = parameterType.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                Assert.notNull(field.get(result), String.format("required param %s is not present", key));
            }
        }
        return result;
    }
}
