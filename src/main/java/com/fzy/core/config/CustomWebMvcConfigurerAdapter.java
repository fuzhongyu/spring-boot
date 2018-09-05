package com.fzy.core.config;

import com.fzy.core.interceptor.LogInterceptor;
import com.fzy.core.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 自定义拦截器在spring mvc中注册
 * @author Fucai
 * @date 2018/3/19
 */

@Configuration
public class CustomWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {


  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    //添加日志拦截器
    registry.addInterceptor(logInterceptor())
        .addPathPatterns("/console/v1/**")
        .excludePathPatterns("/console/v1/order/info/action/import_order")
        .excludePathPatterns("/api/v1/upload/**");


    //登录验证拦截器
    registry.addInterceptor(new TokenInterceptor())
        .addPathPatterns("/console/v1/**")
        .addPathPatterns("/api/v1/**")
        .excludePathPatterns("/console/v1/system/login");

    super.addInterceptors(registry);
  }


  /**
   * 配置跨域
   * @param registry
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedOrigins("*").allowedMethods("POST","GET","PUT","PATCH","OPTIONS","HEAD","DELETE")
        .allowedHeaders("Authentication","Origin","X-Requested-With","Content-Type","Token").maxAge(3600L);
  }


  @Bean
  public MethodValidationPostProcessor methodValidationPostProcessor() {
    return new MethodValidationPostProcessor();
  }

  @Bean
  public LogInterceptor logInterceptor(){
    return new LogInterceptor();
  }


  /**
   * 配置访问静态资源
   * @param registry
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler(CustomConfigProperties.UPLOAD_FILE_PATH+"**").addResourceLocations("file:"+CustomConfigProperties.UPLOAD_FILE_PATH);
    super.addResourceHandlers(registry);
  }

}
