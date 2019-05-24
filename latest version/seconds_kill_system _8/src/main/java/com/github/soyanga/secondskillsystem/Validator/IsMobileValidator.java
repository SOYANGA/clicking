package com.github.soyanga.secondskillsystem.Validator;

import com.github.soyanga.secondskillsystem.util.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @program: seconds_kill_system _2
 * @Description: 编写注解验证器类(约束)
 * @Author: SOYANGA
 * @Create: 2019-05-16 16:20
 * @Version 1.0
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    //IsMobile中的成员变量
    private boolean required = false;

    //从注解的参数中获取required变量
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //不可以为空 默认required为true 不允许为空
        if (required) {
            //判断手机号码格式
            return ValidatorUtil.isMobile(value);
        } else {//可以为空
            //手机号码为空
            if (StringUtils.isEmpty(value)) {
                return true;
            } else {
                //判断手机号码格式
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
