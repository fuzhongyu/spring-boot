package com.fzy.SpringBeanTest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author fucai
 * Date: 2019-12-16
 */
@Component
public class DemoSpringContext implements ApplicationContextAware,DisposableBean {


    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("=======> spring context holder");
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("======> disposable bean");
        applicationContext = null;
    }
}
