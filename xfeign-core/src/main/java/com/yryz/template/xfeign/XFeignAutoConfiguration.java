package com.yryz.template.xfeign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yryz.framework.core.swagger.docket.DocketBeanDefinitionRegistrar;
import com.yryz.template.xfeign.interceptor.ModulePackageMatchInterceptor;
import com.yryz.template.xfeign.provider.XFeignGenericController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/11/18 下午3:29
 * Created by huangxy
 */
@Configuration
@ConditionalOnProperty(name = "xfeign.client.enable", havingValue = "true")
@EnableConfigurationProperties(XFeignProperties.class)
public class XFeignAutoConfiguration {

    @Bean
    public XFeignGenericController xFeignGenericController(ApplicationContext applicationContext, ObjectMapper objectMapper) {
        return new XFeignGenericController(applicationContext, objectMapper);
    }

    @Bean
    public XFeignBeanPreBinding xFeignBeanCompletedTrigger() {
        return new XFeignBeanPreBinding();
    }


    /**
     * 默认线程池策略
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(XFeignThreadPool.class)
    public XFeignThreadPool xFeignThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(50);
        //最大线程数
        executor.setMaxPoolSize(100);
        //任务队列的大小
        executor.setQueueCapacity(2000);
        //线程前缀名
        executor.setThreadNamePrefix("XFeignThread-");
        //线程存活时间
        executor.setKeepAliveSeconds(60);
        /**
         * 拒绝处理策略
         * CallerRunsPolicy()：交由调用方线程运行，比如 main 线程。
         * AbortPolicy()：直接抛出异常。
         * DiscardPolicy()：直接丢弃。
         * DiscardOldestPolicy()：丢弃队列中最老的任务。
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //线程初始化
        executor.initialize();

        return new XFeignThreadPool("XFeignThreadPool", executor);
    }


    /**
     * 等待 框架层 DocketBeanDefinitionRegistrar 注册完再实例化
     * <p>
     * 当前拦截器只存在开发测试环境，
     * 主要防止模块集中部署时，网关路由屏蔽调模块标识了，均能路由到当前服务造成接口均正常假象，导致后期模块无法快速拆分服务，
     *
     * <p>
     * 采用拦截器对handler的所属包名进行区分
     */
    @Configuration
    @Profile({"dev", "test"})
    @ConditionalOnClass(Docket.class)
    @ConditionalOnProperty(name = "xfeign.path.scopeCheck", havingValue = "true")
    @ConditionalOnBean(DocketBeanDefinitionRegistrar.class)
    static class InterceptorConfigurer implements WebMvcConfigurer {

        @Autowired
        private DocketBeanDefinitionRegistrar docketBeanDefinitionRegistrar;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            InterceptorRegistration registration = registry.addInterceptor(new ModulePackageMatchInterceptor(docketBeanDefinitionRegistrar));
            registration.addPathPatterns("/**");
            /**
             * 剔除通用接口
             */
            registration.excludePathPatterns("/xfeign/execute");
        }
    }
}
