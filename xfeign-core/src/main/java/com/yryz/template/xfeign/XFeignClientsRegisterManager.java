package com.yryz.template.xfeign;

import com.yryz.template.xfeign.delegator.XFeignClientFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cloud.openfeign.EnableFeignProxyClients;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/17 下午3:41
 * Created by huangxy
 */
public class XFeignClientsRegisterManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Environment environment;

    private ResourceLoader resourceLoader;

    public XFeignClientsRegisterManager(Environment environment, ResourceLoader resourceLoader) {
        this.environment = environment;
        this.resourceLoader = resourceLoader;
    }


    public Set<String> registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        /**
         * 解析XFeignClient
         * 获取服务名映射，每个服务实例化一个原始FeignClient对象
         */
        XFeignDefinitionScanner scanner = new XFeignDefinitionScanner(registry);
        scanner.setResourceLoader(resourceLoader);
        scanner.setEnvironment(environment);
        scanner.addIncludeFilter(new AnnotationTypeFilter(XFeignClient.class));
        /**
         * 扫描
         */
        Map<String, Object> attrs = metadata.getAnnotationAttributes(EnableFeignProxyClients.class.getName());
        String[] basePackages = (String[]) attrs.getOrDefault("basePackages", new String[]{});

        /**
         * 注册XFeign代理
         * 获取已声明的模块
         */
        return Stream.of(basePackages).flatMap(basePackage -> {
            Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);
            return candidateComponents.stream().filter(e -> e instanceof AnnotatedBeanDefinition).map(e -> {
                /**
                 * 获取XFeignClient注解信息
                 */
                AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) e;
                AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(XFeignClient.class.getCanonicalName());

                //注册代理实现
                return this.feignDelegatorBeanRegister(registry, annotationMetadata, attributes);
            });
        }).collect(Collectors.toSet());
    }


    /**
     * 接口代理注册
     *
     * @param registry
     * @param annotationMetadata
     * @param attributes
     * @return
     */
    private String feignDelegatorBeanRegister(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {

        String module = (String) attributes.getOrDefault("value", "");
        //统一代理
        String className = annotationMetadata.getClassName();
        String beanName = className + "#XFeignDelegator";

        String moduleBindServiceId = this.serviceIdFormat(module);
        if (StringUtils.isEmpty(moduleBindServiceId)) {
            throw new IllegalArgumentException(String.format("module：%s don't setting target serviceId，for class：%s", module, className));
        }
        /**
         * FactoryBean声明
         */
        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(XFeignClientFactoryBean.class);
        definition.addPropertyValue("type", annotationMetadata.getClassName());
        definition.addPropertyValue("module", module);
        definition.addPropertyValue("moduleBindServiceId", moduleBindServiceId);
        definition.addPropertyValue("typeBeanName", beanName);
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
        beanDefinition.setPrimary(true);

        registry.registerBeanDefinition(beanName, beanDefinition);
        return moduleBindServiceId;
    }

    public String serviceIdFormat(String module) {
        return environment.getProperty("xfeign.client.modules." + module);
    }
}
