/*
 * Copyright 2014 Jeanfrancois Arcand
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/**
 * This code was donated by Dan Vulpe https://github.com/dvulpe/atmosphere-ws-pubsub
 */
package com.yunkouan.lpid.atmosphere.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.yunkouan.lpid.atmosphere.config.resolvers.AtmosphereArgumentResolver;
import com.yunkouan.lpid.atmosphere.config.resolvers.MeteorArgumentResolver;
import com.yunkouan.lpid.commons.interceptor.LoginInterceptor;

@Configuration
@EnableWebMvc
public class WebAppConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("index");
    }

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
    
    /**
     * SpringMVC上传文件时，需要配置MultipartResolver处理器
     * 
     * @return CommonsMultipartResolver对象
     * @since 1.0.0
     * 2014年9月3日-下午1:46:46
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
    	CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    	//指定所上传文件的总大小不能超过50M。注意maxUploadSize属性的限制不是针对单个文件，而是所有文件的容量之和
    	multipartResolver.setMaxUploadSize(50000000);
    	return multipartResolver;
    }
    
    /**
     * 登录拦截器
     * 
     * @return 
     * @since 1.0.0
     * 2014年12月18日-上午10:54:13
     */
    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor();
    }
    
    /**
     * Spring类型转换Bean
     * 
     * @return 
     * @since 1.0.0
     * 2014年9月3日-下午1:46:14
     */
    @Bean 
    public FormattingConversionServiceFactoryBean conversionService() {
        FormattingConversionServiceFactoryBean conversionService = new FormattingConversionServiceFactoryBean();
        Set<Converter> conversionSet = new HashSet<Converter>();
        //String转枚举
//        conversionSet.add(new StringToEnumConverterFactory().getConverter(ImageHandleMode.class));
        
        return conversionService;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJacksonHttpMessageConverter());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new AtmosphereArgumentResolver());
        argumentResolvers.add(new MeteorArgumentResolver());
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources").addResourceLocations("/resources/");
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor());
    }
}
