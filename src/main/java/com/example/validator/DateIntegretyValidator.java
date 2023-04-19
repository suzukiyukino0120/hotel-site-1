package com.example.validator;

import java.time.LocalDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class DateIntegretyValidator implements ConstraintValidator<DateIntegrety, Object>{
	private String startDate;
	private String endDate;
	
	@Override
    public void initialize(DateIntegrety annotation) {
		this.startDate=annotation.startDate();
		this.endDate=annotation.endDate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
    	BeanWrapper beanWrapper = new BeanWrapperImpl(value);
    	LocalDate startDateVal = (LocalDate) beanWrapper.getPropertyValue(startDate);
    	LocalDate endDateVal = (LocalDate) beanWrapper.getPropertyValue(endDate);
    	if(startDateVal==null||endDateVal==null) {//nullチェックはフォームクラスのNotNullでチェックする
    		return true;
    	}else {
    		if(startDateVal.isBefore(endDateVal)) {
    			return true;
    		}else {
    			return false;
    		}
    	}
    }
}
