package com.yryz.template.xfeign.delegator;

import com.yryz.template.xfeign.delegator.dispatcher.LocalProcessProxyDispatcher;
import com.yryz.template.xfeign.delegator.dispatcher.RemoteFeignProxyDispatcher;
import com.yryz.template.xfeign.generic.XFeignGenericApi;
import feign.Contract;
import feign.Feign;
import feign.MethodMetadata;
import feign.TypesExport;
import feign.codec.Decoder;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/18 下午7:26
 * Created by huangxy
 */
public class XFeignInvocationHandler implements InvocationHandler {

    private ApplicationContext applicationContext;

    private Class<?> typeClass;

    private String typeBeanName;

    private String module;

    private String moduleBindServiceId;

    private XFeignInvocationDispatch dispatch;


    public XFeignInvocationHandler(ApplicationContext applicationContext, Class<?> typeClass, String typeBeanName, String module, String moduleBindServiceId) {
        this.applicationContext = applicationContext;
        this.typeClass = typeClass;
        this.typeBeanName = typeBeanName;
        this.module = module;
        this.moduleBindServiceId = moduleBindServiceId;
    }

    private XFeignInvocationDispatch getDispatcher() {
        /**
         * 只初始化一次
         */
        if (this.dispatch == null) {

            /**
             * 获取原始feign相关参数
             */
            FeignContext feignContext = applicationContext.getBean(FeignContext.class);
            Contract contract = feignContext.getInstance(moduleBindServiceId, Contract.class);
            Decoder decoder = feignContext.getInstance(moduleBindServiceId, Decoder.class);


            //判断是否有本地实现类注入
            Map<String, ?> impls = applicationContext.getBeansOfType(typeClass);
            impls.remove(typeBeanName);     //排除自身
            if (impls.size() > 0) {         //本地
                this.dispatch = this.buildLocalDispatch(impls, null);
            } else {                        //远程
                this.dispatch = this.buildRemoteDispatch(contract, decoder);
            }
        }
        return this.dispatch;
    }


    /**
     * 本地
     *
     * @param impls
     * @param methodMetaMap
     * @return
     */
    private XFeignInvocationDispatch buildLocalDispatch(Map<String, ?> impls, Map<String, MethodMetadata> methodMetaMap) {
        /**
         * 获取非当前实例对象
         */
        Map.Entry<String, ?> targetEntry = impls.entrySet().iterator().next();
        Object target = targetEntry.getValue();
        return new LocalProcessProxyDispatcher(applicationContext, typeClass, methodMetaMap, target);
    }


    /**
     * 远程
     *
     * @param contract
     * @param decoder
     * @return
     */
    private XFeignInvocationDispatch buildRemoteDispatch(Contract contract, Decoder decoder) {
        /**
         * 标准feign接口
         */
        Map<String, XFeignGenericApi> beanMap = applicationContext.getBeansOfType(XFeignGenericApi.class);
        String beanNameWithServiceId = String.format("%s#%s", XFeignGenericApi.class.getName(), moduleBindServiceId);
        XFeignGenericApi feignGenericApi = beanMap.get(beanNameWithServiceId);


        /**
         * 获取方法的返回值元数据
         */
        Map<String, Type> methodReturnTypes = Stream.of(typeClass.getMethods())
                .collect(Collectors.toMap(
                        e -> Feign.configKey(typeClass, e),
                        e -> TypesExport.resolve(typeClass, typeClass, e.getGenericReturnType())));

        //获取本地服务名
        String originServiceId = applicationContext.getEnvironment().resolvePlaceholders("${spring.application.name}");
        return new RemoteFeignProxyDispatcher(applicationContext, typeClass, methodReturnTypes, decoder, originServiceId, moduleBindServiceId, feignGenericApi);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (this.dispatch != null) {
            return this.dispatch.invoke(proxy, method, args);
        }
        return this.getDispatcher().invoke(proxy, method, args);
    }

    @Override
    public String toString() {
        return "XFeignInvocationHandler{" +
                ", typeClass=" + typeClass +
                ", typeBeanName='" + typeBeanName + '\'' +
                ", module='" + module + '\'' +
                ", serviceId='" + moduleBindServiceId + '\'' +
                ", dispatch=" + dispatch +
                '}';
    }
}
