package com.example.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.domain.User;
import com.example.service.UserService;

public class UserIdExistsValidator implements ConstraintValidator<UserIdExists, String>{
	@Autowired
    UserService userService;

    @Override
    public void initialize(UserIdExists annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || value.isEmpty()) {
            return true;
        }
        User user = userService.searchByUserId(value);
        if(user != null) {
            return false;
        } else {
            return true;
        }
    }

}
