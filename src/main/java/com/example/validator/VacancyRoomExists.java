package com.example.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = {VacancyRoomExistsValidator.class})
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface VacancyRoomExists {
	String message() default "選択されたお部屋をご用意することができません。お手数ですが、再度検索をお願いいたします。";
	
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    String id();
    String startDate();
    String endDate();

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public static @interface List {
    	VacancyRoomExists[] value();
    }
}
