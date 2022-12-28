package com.xiaozhao.xiaozhaoserver.web.config;

import com.xiaozhao.xiaozhaoserver.common.constants.Constants;
import com.xiaozhao.xiaozhaoserver.service.exception.BadParameterException;
import com.xiaozhao.xiaozhaoserver.web.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-17 22:23:31
 * @modify:
 */

@Slf4j
@Component
public class TokenPayloadArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> type = parameter.getParameterType();
        return ! parameter.hasParameterAnnotations()
                && (ClassUtils.isPrimitiveOrWrapper(type) || type.equals(String.class));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader(Constants.TOKEN_HEADER_KEY);
        Map<String, String> map = JWTUtils.getTokenInfo(token);
        String parameterName = parameter.getParameterName();
        Class<?> parameterType = parameter.getParameterType();
        String arg = map.get(parameterName);
        if (StringUtils.isBlank(arg)) {
            throw new BadParameterException("Token 中的参数不能为空");
        }
        if (String.class.equals(parameterType)) {
            return arg;
        } else if (Integer.class.equals(parameterType) || int.class.equals(parameterType)) {
            return Integer.parseInt(arg);
        } else if (Double.class.equals(parameterType) || double.class.equals(parameterType)) {
            return Double.parseDouble(arg);
        } else if (Float.class.equals(parameterType) || float.class.equals(parameterType)) {
            return Float.parseFloat(arg);
        } else if (Boolean.class.equals(parameterType) || boolean.class.equals(parameterType)) {
            return Boolean.parseBoolean(arg);
        } else if (Character.class.equals(parameterType) || char.class.equals(parameterType)) {
            return arg.toCharArray()[0];
        } else if (Long.class.equals(parameterType) || long.class.equals(parameterType)) {
            return Long.parseLong(arg);
        } else if (Short.class.equals(parameterType) || short.class.equals(parameterType)) {
            return Short.parseShort(arg);
        } else {
            throw new BadParameterException(arg + "的 类型为不受支持的 " + parameterType);
        }
    }
}
