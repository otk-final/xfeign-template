package com.yryz.template.xfeign.delegator.dispatcher;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yryz.framework.core.exception.BusinessException;
import com.yryz.framework.core.vo.Response;
import com.yryz.template.xfeign.delegator.XFeignInvocationDispatch;
import com.yryz.template.xfeign.generic.*;
import feign.*;
import feign.codec.Decoder;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/17 下午6:12
 * Created by huangxy
 */
public class RemoteFeignProxyDispatcher implements XFeignInvocationDispatch {

    private ApplicationContext applicationContext;

    private Class<?> interfaceClass;

    private String targetServiceId;

    private String originServiceId;

    private XFeignGenericApi targetApi;

    private Map<String, Type> methodReturnTypes;

    private Decoder decoder;

    private ObjectMapper objectMapper;


    public RemoteFeignProxyDispatcher(ApplicationContext applicationContext, Class<?> interfaceClass, Map<String, Type> methodReturnTypes, Decoder decoder, String originServiceId, String targetServiceId, XFeignGenericApi targetApi) {
        this.applicationContext = applicationContext;
        this.interfaceClass = interfaceClass;
        this.methodReturnTypes = methodReturnTypes;
        this.originServiceId = originServiceId;
        this.targetServiceId = targetServiceId;
        this.targetApi = targetApi;
        this.decoder = decoder;
        this.objectMapper = applicationContext.getBean(ObjectMapper.class);
    }


    private Decoder feignDecoderBuild(ApplicationContext applicationContext) {
        FeignContext context = applicationContext.getBean(FeignContext.class);
        return context.getInstance(targetServiceId, Decoder.class);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


        //基础方法
        if ("equals".equals(method.getName())) {
            return targetApi.equals(args[0]);
        } else if ("hashCode".equals(method.getName())) {
            return targetApi.hashCode();
        } else if ("toString".equals(method.getName())) {
            return toStringConvert();
        }
        //默认方法
        if (Util.isDefault(method)) {
            return method.invoke(targetApi, args);
        }


        //获取方法元数据
        Type returnType = methodReturnTypes.get(Feign.configKey(interfaceClass, method));

        //远程调用
        return this.remote(method, returnType, args);
    }

    @Override
    public String toStringConvert() {
        return targetApi.toString();
    }


    /**
     * 远程feign调用
     *
     * @param method
     * @param argv
     * @return
     */
    private Object remote(Method method, Type returnType, Object[] argv) throws IOException {

        //兼容无参数
        if (argv == null) {
            argv = new Object[0];
        }

        /**
         * 将参数渲染成body进行调用
         */
        GenericParameter parameter = this.encodeGenericParameter(method, returnType, argv);

        /**
         * 因为框架层统一接入了Hystrix，响应对象固定：Response<?>
         */
        Response<GenericResult> resp = targetApi.execute(parameter);
        //错误通过异常抛出
        if (!resp.success()) {
            throw new BusinessException(resp.getCode(), resp.getMsg(), resp.getErrorMsg());
        }

        //反序列化业务方法返回数据
        feign.Response.Builder builder = this.successResponseBuilder(resp.getData().getReturnData());
        return decoder.decode(builder.build(), returnType);
    }


    private GenericParameter encodeGenericParameter(Method method, Type returnType, Object[] argv) throws IOException {
        /**
         * 将参数渲染成body进行调用
         */
        Class<?>[] parameterTypes = method.getParameterTypes();
        GenericParameter parameter = new GenericParameter();
        parameter.setOrigin(originServiceId);
        parameter.setTarget(targetServiceId);
        parameter.setApi(interfaceClass.getName());
        parameter.setMethod(method.getName());
        List<GenericArg> args = new ArrayList<>();
        for (int i = 0; i < argv.length; i++) {
            Object param = argv[i];
            GenericArg arg = new GenericArg();

            //形参
            Class<?> defineParameterType = parameterTypes[i];
            arg.setClassType(defineParameterType.getName());

            //实参
            if (param != null) {
                arg.setArgType(param.getClass().getName());
            }

            arg.setIndex(i);
            arg.setValue(objectMapper.convertValue(param, JsonNode.class));
            args.add(arg);
        }
        parameter.setArgs(args);
        return parameter;
    }

    /**
     * 构建一个成功响应decoder业务返回数据
     *
     * @param returnDataNode
     * @return
     * @throws IOException
     */
    private feign.Response.Builder successResponseBuilder(JsonNode returnDataNode) {
        /**
         * 正常返回数据，仿照feign反序列化方式解码
         */
        feign.Response.Builder builder = feign.Response.builder();
        builder.status(200);

        //默认头
        Map<String, Collection<String>> headerMap = new LinkedHashMap<>();
        headerMap.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_UTF8_VALUE.trim()));
        headerMap.put(HttpHeaders.TRANSFER_ENCODING, Collections.singletonList("chunked"));

        builder.headers(headerMap);
        //获取实际方法返回值
        builder.body(returnDataNode.toString().getBytes());
        return builder;
    }
}
