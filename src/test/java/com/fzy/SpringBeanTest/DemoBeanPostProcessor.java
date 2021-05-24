package com.fzy.SpringBeanTest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author fucai
 * Date: 2019-12-16
 */
@Component
public class DemoBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        if (o instanceof DemoService){
            DemoService demoService = ((DemoService)o);
            System.out.println("===> beanPostBeforeSet:" + demoService.getName());
            demoService.setName("beanPostBeforeSet");
        }
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        if (o instanceof  DemoService) {
            DemoService demoService = ((DemoService)o);
            System.out.println("===> beanPostAfterSet:" + demoService.getName());
            demoService.setName("===> beanPostAfterSet");
        }
        return o;
    }
}
