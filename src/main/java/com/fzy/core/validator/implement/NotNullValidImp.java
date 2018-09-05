package com.fzy.core.validator.implement;

import com.fzy.core.validator.annotation.NotNullValid;
import java.lang.reflect.InvocationTargetException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;

/**
 * 自定义约束验证器实现
 * @author Fucai
 * @date 2018/3/19
 */
public class NotNullValidImp implements ConstraintValidator<NotNullValid,Object> {


    private String fieldName;

    /**
     *  方法 initialize 对验证器进行实例化，它必须在验证器的实例在使用之前被调用，并保证正确初始化验证器，它的参数是约束注解
     */
    @Override
    public void initialize(NotNullValid notNullValid) {
        this.fieldName=notNullValid.fieldName();
    }

    /**
     * 方法 isValid 是进行约束验证的主体方法，其中 value 参数代表需要验证的实例，context 参数代表约束执行的上下文环境。与该注解对应的验证器的实现。
     * @param obj
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {

        try {
            Object value= BeanUtils.getProperty(obj,fieldName);
            if(value!=null){
                return true;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }
}
