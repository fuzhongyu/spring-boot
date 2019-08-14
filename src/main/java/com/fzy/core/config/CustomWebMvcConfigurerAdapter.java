package com.fzy.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.fzy.core.interceptor.LogInterceptor;
import com.fzy.core.interceptor.TokenInterceptor;

/**
 * 自定义拦截器在spring mvc中注册
 *
 * @author Fucai
 * @date 2018/3/19
 */

@Configuration
public class CustomWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {


  @Override
  public void addInterceptors(InterceptorRegistry registry) {

//    //添加日志拦截器
//    registry.addInterceptor(logInterceptor())
//            .addPathPatterns("/console/v1/**")
//            .excludePathPatterns("/api/v1/upload/**");
//
//
//    //登录验证拦截器
//    registry.addInterceptor(tokenInterceptor())
//            .addPathPatterns("/console/v1/**")
//            .addPathPatterns("/api/v1/**")
//            .excludePathPatterns("/console/v1/login");

    super.addInterceptors(registry);
  }


  @Bean
  public MethodValidationPostProcessor methodValidationPostProcessor() {
    return new MethodValidationPostProcessor();
  }

  @Bean
  public LogInterceptor logInterceptor() {
    return new LogInterceptor();
  }

  @Bean
  public TokenInterceptor tokenInterceptor() {
    return new TokenInterceptor();
  }


  /**
   * 配置访问静态资源
   *
   * @param registry
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler(CustomConfigProperties.UPLOAD_FILE_PATH + "**").addResourceLocations("file:" + CustomConfigProperties.UPLOAD_FILE_PATH);
    super.addResourceHandlers(registry);
  }

}
