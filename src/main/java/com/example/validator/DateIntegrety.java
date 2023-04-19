package com.example.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = {DateIntegretyValidator.class})
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DateIntegrety {
String message() default "チェックアウト日はチェックイン日より後の日付を入力して下さい。";
	
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    
    String startDate();
    String endDate();

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public static @interface List {
    	DateIntegrety[] value();
    }

}
