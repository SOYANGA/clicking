package com.github.soyanga.secondskillsystem.vo;

import com.github.soyanga.secondskillsystem.Validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * @program: seconds_kill_system _2
 * @Description:
 * @Author: SOYANGA
 * @Create: 2019-05-16 14:10
 * @Version 1.0
 */
@Data
@Component
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;
}
