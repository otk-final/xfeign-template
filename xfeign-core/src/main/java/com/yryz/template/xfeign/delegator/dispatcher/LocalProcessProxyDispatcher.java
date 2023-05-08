package com.yryz.template.xfeign.delegator.dispatcher;

import com.yryz.framework.core.base.BaseApiInfo;
import com.yryz.framework.core.context.YryzContext;
import com.yryz.framework.core.context.YryzRequestHeader;
import com.yryz.template.xfeign.XFeignAtomic;
import com.yryz.template.xfeign.XFeignThreadPool;
import com.yryz.template.xfeign.XFeignUtils;
import com.yryz.template.xfeign.delegator.XFeignInvocationDispatch;
import feign.MethodMetadata;
import feign.Util;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/17 下午6:12
 * Created by huangxy
 */
public class LocalProcessProxyDispatcher implements XFeignInvocationDispatch {

    private Class<?> interfaceClass;

    private Object target;

    private ApplicationContext applicationContext;

    private XFeignThreadPool xFeignThreadPool;

    private Map<String, MethodMetadata> methodMetaMap;

    /**
     * 默认
     */
    private String tenantId;

    private String appId;


    public LocalProcessProxyDispatcher(ApplicationContext applicationContext, Class<?> interfaceClass, Map<String, MethodMetadata> methodMetaMap, Object target) {
        this.applicationContext = applicationContext;
        this.interfaceClass = interfaceClass;
        this.methodMetaMap = methodMetaMap;
        this.target = target;
        this.xFeignThreadPool = applicationContext.getBean(XFeignThreadPool.class);

        this.appId = applicationContext.getEnvironment().resolvePlaceholders(BaseApiInfo.CURRENT_DEFAULT_TENANT_ID);
        this.tenantId = applicationContext.getEnvironment().resolvePlaceholders(BaseApiInfo.CURRENT_DEFAULT_APP_ID);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] argv) throws Throwable {

        //基础方法
        if ("equals".equals(method.getName())) {
            return target.equals(argv[0]);
        } else if ("hashCode".equals(method.getName())) {
            return target.hashCode();
        } else if ("toString".equals(method.getName())) {
            return this.toStringConvert();
        }

        //默认方法
        if (Util.isDefault(method)) {
            return method.invoke(target, argv);
        }
        return this.local(method, argv);
    }

    @Override
    public String toStringConvert() {
        return XFeignUtils.shortClassNameParse(target.getClass().getName());
    }


    /**
     * 本地实现调用
     *
     * @param method
     * @param argv
     * @return
     */
    private Object local(Method method, Object[] argv) {


        /**
         * 屏蔽线程调用异常
         * UndeclaredThrowableException
         * InvocationTargetException
         */

        Object targetOut;
        try {
            /**
             * 根据事务注解判断是否同步原子执行
             * 存在同步执行
             */
            XFeignAtomic transaction = AnnotationUtils.findAnnotation(method, XFeignAtomic.class);
            if (transaction != null) {
                /**
                 * 同步执行
                 */
                targetOut = method.invoke(target, argv);
            } else {
                /**
                 * 异步执行
                 */

                //拷贝头 设置默认值
                final YryzRequestHeader header = new YryzRequestHeader();
                BeanUtils.copyProperties(YryzContext.getRequestHeader(), header);
                if (StringUtils.isEmpty(header.getTenantId())) {
                    header.setTenantId(tenantId);
                }
                if (StringUtils.isEmpty(header.getAppId())) {
                    header.setAppId(appId);
                }

                MethodFutureTask futureTask = new MethodFutureTask(method, target, argv, header);
                targetOut = xFeignThreadPool.getTaskExecutor().submit(futureTask).get();
            }
        } catch (Exception e) {
            throw XFeignUtils.throwRuntimeException(e);
        }
        return targetOut;
    }


    private static class MethodFutureTask implements Callable<Object> {

        private Method method;

        private Object target;

        private Object[] argv;

        private YryzRequestHeader header;

        public MethodFutureTask(Method method, Object target, Object[] argv, YryzRequestHeader header) {
            this.method = method;
            this.target = target;
            this.argv = argv;
            this.header = header;
        }

        @Override
        public Object call() throws Exception {
            try {
                //线程传递请求头
                YryzContext.setRequestHeader(header);
                return method.invoke(target, argv);
            } finally {
                YryzContext.reset();
            }
        }
    }
}
