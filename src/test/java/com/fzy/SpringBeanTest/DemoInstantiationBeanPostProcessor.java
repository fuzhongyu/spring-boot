package com.fzy.SpringBeanTest;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;

/**
 * @author fucai
 * Date: 2019-12-16
 */
@Component
public class DemoInstantiationBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    /**
     * 实例化之前
     * @param aClass
     * @param s
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> aClass, String s) throws BeansException {
        if (s.equals("demoService")){
            System.out.println("===> before instantiation:" + s);
        }
        return null;
    }

    /**
     * 实例化之后
     * @param o
     * @param s
     * @return
     * @throws BeansException
     */
    @Override
    public boolean postProcessAfterInstantiation(Object o, String s) throws BeansException {
        if (o instanceof DemoService){
            System.out.println("===> after instantiation:" + s);
        }
        return false;
    }

    /**
     * 设置属性
     * @param propertyValues
     * @param propertyDescriptors
     * @param o
     * @param s
     * @return
     * @throws BeansException
     */
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues propertyValues, PropertyDescriptor[] propertyDescriptors, Object o, String s) throws BeansException {
        if (o instanceof DemoService){
            System.out.println("===> post process property");
        }
        return propertyValues;
    }

    /**
     * 初始化之前
     * @param o
     * @param s
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        if (o instanceof  DemoService){
            System.out.println("===> post process before initialization");
        }
        return o;
    }

    /**
     * 初始化之后
     * @param o
     * @param s
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        if (o instanceof  DemoService){
            System.out.println("===> post process after initialization");
        }
        return o;
    }
}
