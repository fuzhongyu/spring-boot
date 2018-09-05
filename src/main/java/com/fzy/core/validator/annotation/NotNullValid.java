package com.fzy.core.validator.annotation;

import com.fzy.core.validator.implement.NotNullValidImp;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 自定义非空验证注解(可设置多值)
 *
 * @author Fucai
 * @date 2018/3/19
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {NotNullValidImp.class})
@Documented
public @interface NotNullValid {

    // 约束注解验证时的输出消息
    String message() default "not null validator message";

    // 约束注解在验证时所属的组别
    Class<?> []groups() default {};

    // 约束注解的有效负载
    Class<? extends Payload>[] payload() default {};

    String fieldName();


    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List{
        NotNullValid[] value();
    }
}
