package com.yryz.template.xfeign.provider;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yryz.framework.core.util.ResponseUtils;
import com.yryz.framework.core.vo.Response;
import com.yryz.template.xfeign.XFeignUtils;
import com.yryz.template.xfeign.generic.GenericArg;
import com.yryz.template.xfeign.generic.GenericParameter;
import com.yryz.template.xfeign.generic.GenericResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Optional;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/17 下午7:41
 * Created by huangxy
 */
@RestController("XFeign动态泛化接口")
public class XFeignGenericController {

    private static final Logger logger = LoggerFactory.getLogger(XFeignGenericController.class);

    private ApplicationContext applicationContext;

    private ObjectMapper objectMapper;

    public XFeignGenericController(ApplicationContext applicationContext, ObjectMapper objectMapper) {
        this.applicationContext = applicationContext;
        this.objectMapper = objectMapper;
    }

    /**
     * 标准接口
     *
     * @param body
     * @return
     */
    @PostMapping("/xfeign/execute")
    public Response<GenericResult> execute(@RequestBody GenericParameter body) throws Exception {
        Class<?> clazz = Class.forName(body.getApi());

        final String handlerKey = String.format("%s:%s", body.getApi(), body.getMethod());
        final String argJson = objectMapper.convertValue(body.getArgs(), JsonNode.class).toString();
        logger.debug("XFeign From：[{}] Api：[{}] Req：{}", body.getOrigin(), handlerKey, argJson);

        /**
         * 获取本地spring实例,过滤框架代理对象
         */
        Map<String, ?> beanMap = applicationContext.getBeansOfType(clazz);
        Optional<?> beanOptional = beanMap.entrySet().stream().filter(e -> !(e instanceof Proxy)).map(Map.Entry::getValue).findFirst();
        if (!beanOptional.isPresent()) {
            throw new IllegalArgumentException("XFeign provider bean  is missing：" + body.getApi());
        }
        Object bean = beanOptional.get();

        /**
         * 反序列化参数，定位方法
         */
        Object[] input = new Object[body.getArgs().size()];
        Class<?>[] parameterTypes = new Class<?>[body.getArgs().size()];
        for (int i = 0; i < body.getArgs().size(); i++) {
            GenericArg arg = body.getArgs().get(i);
            /**
             * 用形参定位方法(一定存在)
             */
            Class<?> parameterType = Class.forName(arg.getClassType());
            parameterTypes[i] = parameterType;

            /**
             * 用实参数解码参数
             */
            input[i] = this.resolveArgValue(parameterType, arg);
        }


        //定位方法
        Method targetMethod = bean.getClass().getDeclaredMethod(body.getMethod(), parameterTypes);
        if (targetMethod == null) {
            throw new IllegalArgumentException("XFeign provider bean method is missing：" + body.getMethod());
        }

        /**
         * 转换异常
         */
        Object methodResult;
        try {
            methodResult = targetMethod.invoke(bean, input);
        } catch (Exception e) {
            e = XFeignUtils.throwRuntimeException(e);
            /**
             * 打印业务运行时异常
             */
            logger.error(e.getMessage(), e);
            throw e;
        }

        /**
         * 包装响应
         */
        GenericResult genericResult = new GenericResult();
        if (methodResult != null) {
            genericResult.setReturnType(methodResult.getClass().getName());
        }
        genericResult.setReturnData(objectMapper.convertValue(methodResult, JsonNode.class));
        logger.debug("XFeign From：[{}] Api：[{}] Resp：{}", body.getOrigin(), handlerKey, genericResult.getReturnData().toString());
        return ResponseUtils.returnObjectSuccess(genericResult);
    }


    private Object resolveArgValue(Class<?> parameterType, GenericArg arg) {
        //调用方可能传入null，无法取到实际参数类型
        if (arg.getValue() == null) {
            return null;
        }
        /**
         * 优先取实参数
         */
        Class<?> valueType;
        //实参类型 - 和原始形参一致 (无需二次声明)
        if (StringUtils.isEmpty(arg.getArgType()) || arg.getClassType().equalsIgnoreCase(arg.getArgType())) {
            //默认形参
            valueType = parameterType;
        } else {
            /**
             * 高危风险：client如果传入的是形参的子类，server段不一定有该类，会导致异常：ClassNotFoundException
             * 当前之所以优先取实参类型，是因为约定相关dto对象均在api模块声明，client和server端均能保存一致， server端实现类甚至可以有效转换实参
             * server不存在时，使用形参类型序列化
             */
            try {
                valueType = Class.forName(arg.getArgType());
            } catch (Exception e) {
                logger.warn("XFeign ArgType：[{}] class not found", arg.getArgType());
                valueType = parameterType;
            }
        }

        /**
         * 获取调用方实参类型进行反序列化
         */
        JavaType argJavaType = objectMapper.getTypeFactory().constructType(valueType);
        //解码
        return objectMapper.convertValue(arg.getValue(), argJavaType);
    }
}
