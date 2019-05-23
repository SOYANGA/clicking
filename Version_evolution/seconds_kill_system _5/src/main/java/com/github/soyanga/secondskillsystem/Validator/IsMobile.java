package com.github.soyanga.secondskillsystem.Validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @program: seconds_kill_system _2
 * @Description: 自定义ISMobile校验注解手机号是否合法
 * @Author: SOYANGA
 * @Create: 2019-05-16 16:12
 * @Version 1.0
 */

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {IsMobileValidator.class}
)
public @interface IsMobile {
    //是否允许为空，true表示不允许 默认不允许为空
    boolean required() default true;

    //校验不通过：手机号码格式有误
    String message() default "手机号码格式有误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
