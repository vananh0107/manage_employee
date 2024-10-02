package com.vandev.manage.Validator;
import com.vandev.manage.pojo.UserSystem;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserSystem.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserSystem user = (UserSystem) target;
        if (user.getUsername().isEmpty()) {
            errors.rejectValue("username", "NotEmpty", "Tên đăng nhập không được để trống");
        }
        if (user.getPassword().isEmpty()) {
            errors.rejectValue("password", "NotEmpty", "Mật khẩu không được để trống");
        }
        // Add more validations if necessary
    }
}