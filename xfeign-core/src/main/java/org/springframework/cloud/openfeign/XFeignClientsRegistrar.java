package org.springframework.cloud.openfeign;

import com.yryz.template.xfeign.XFeignClientsRegisterManager;
import com.yryz.template.xfeign.generic.XFeignGenericApi;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/19 下午1:50
 * Created by huangxy
 */
public class XFeignClientsRegistrar extends FeignClientsProxyRegistrar {

    private Environment environment;

    private ResourceLoader resourceLoader;

    public XFeignClientsRegistrar() {
        super();
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        super.setResourceLoader(resourceLoader);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        super.setEnvironment(environment);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        /**
         * 默认feign注册
         */
        super.registerBeanDefinitions(metadata, registry);

        /**
         * 自定义xfeign注册
         */
        XFeignClientsRegisterManager registrar = new XFeignClientsRegisterManager(environment, resourceLoader);
        Set<String> serviceIds = registrar.registerBeanDefinitions(metadata, registry);

        /**
         * 代理feign注册
         */
        AnnotatedBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(XFeignGenericApi.class);
        AnnotationMetadata apiMeta = beanDefinition.getMetadata();

        Map<String, Object> attributes = apiMeta.getAnnotationAttributes(FeignClient.class.getCanonicalName());
        serviceIds.forEach(e -> {
            Map<String, Object> copy = new HashMap<>(attributes);
            copy.put("name", e);
            copy.put("serviceId", e);
            copy.put("value", e);
            //配置项
            String name = getClientName(copy);
            registerClientConfiguration(registry, name, copy.get("configuration"));
            //注册
            this.registerFeignClient(registry, apiMeta, copy, "#" + e);
        });
    }
}
