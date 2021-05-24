package com.fzy.SpringBeanTest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author fucai
 * Date: 2019-12-16
 */
public class SpringBeanTest {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        DemoService demoService = (DemoService) context.getBean("demoService");
        System.out.println(demoService.getName());
    }

}
