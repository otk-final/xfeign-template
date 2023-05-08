package com.yryz.template.xfeign.interceptor;

import com.yryz.framework.core.swagger.docket.DocketBeanDefinitionRegistrar;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Copyright (c) 2019-2020 Wuhan LAJ Network Company LTD.
 * All rights reserved.
 * <p>
 * Created on 2021/12/31 上午11:48
 * Created by huangxy
 */
public class ModulePackageMatchInterceptor implements HandlerInterceptor {

    private DocketBeanDefinitionRegistrar docketBeanDefinitionRegistrar;

    private Map<String, String[]> groupPackages = new HashMap<>();

    public ModulePackageMatchInterceptor(DocketBeanDefinitionRegistrar docketBeanDefinitionRegistrar) {
        this.docketBeanDefinitionRegistrar = docketBeanDefinitionRegistrar;
        docketBeanDefinitionRegistrar.getDocketProperties().forEach(e -> groupPackages.put(e.getGroupName().toLowerCase(), e.getBasePackages()));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //网关转换必要参数(服务间Feign 没有该字段)
        String gatewayModuleName = request.getHeader("X-Forwarded-Prefix");
        if (StringUtils.isEmpty(gatewayModuleName) || ignoreGroupMatch()) {
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        /**
         * 只校验项目包
         */
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        final String beanPackage = handlerMethod.getBeanType().getPackage().getName();
        if (beanPackage.startsWith("com.yryz") || beanPackage.startsWith("com.yktc") || beanPackage.startsWith("com.lajsf")) {
            return this.modulePackageCheck(request, response, gatewayModuleName, beanPackage);
        }

        return true;
    }

    /**
     * 兼容未配置swagger项目，仅且当只要存在default时
     *
     * @return
     */
    private boolean ignoreGroupMatch() {
        return groupPackages.isEmpty() || (groupPackages.size() == 1 && groupPackages.containsKey("default"));
    }


    /**
     * 模块包路径匹配
     *
     * @param request
     * @param gatewayForwardedPrefix
     * @param beanPackage
     * @return
     */
    private boolean modulePackageCheck(HttpServletRequest request, HttpServletResponse response, String gatewayForwardedPrefix, String beanPackage) throws IOException {
        if (gatewayForwardedPrefix.startsWith("/")) {
            gatewayForwardedPrefix = gatewayForwardedPrefix.substring(1);
        }
        final String module = gatewayForwardedPrefix.toLowerCase();
        /**
         * 获取当前模块（组）下的package路径
         *
         * 当前拦截器兼容本地开发调试
         * /module-xxx/v1/aaa
         *
         * 匹配规则前缀匹配，存在则校验
         */
        Optional<String> matchGroup = groupPackages.keySet().stream().filter(module::startsWith).findFirst();
        if (!matchGroup.isPresent()) {
            throw new IllegalArgumentException(String.format("current gateway forwarded ：[%s] don't match module", module));
        }

        //实际模块名
        String[] supports = groupPackages.getOrDefault(matchGroup.get().toLowerCase(), new String[]{});
        /**
         * 获取当前bean隶属包
         */
        boolean match = Stream.of(supports).anyMatch(beanPackage::startsWith);
        if (match) {
            return true;
        }
        /**
         * 异常抛出
         */
        throw new IllegalArgumentException(String.format("current request uri ：[%s] don't registered module： %s", request.getRequestURI(), matchGroup.get()));
    }
}
