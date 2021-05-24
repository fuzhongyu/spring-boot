package com.fzy.SpringBeanTest;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author fucai
 * Date: 2019-12-16
 */
@Service
public class DemoService implements InitializingBean,BeanNameAware {

    private String name;

    public DemoService() {
        System.out.println("===> new instance");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("===> postConstruct:" + name);
        this.name = "postConstruct";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("====> init bean: " + this.name);
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("====> bean name aware:" + this.name);
        this.name = s;
    }
}
