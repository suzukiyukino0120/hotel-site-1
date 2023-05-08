package com.example.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = {UserIdExistsValidator.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserIdExists {
	 String message() default "そのユーザーIDはすでに使用されています";

	    Class<?>[] groups() default {};

	    Class<? extends Payload>[] payload() default {};

	    @Target(ElementType.FIELD)
	    @Retention(RetentionPolicy.RUNTIME)
	    @Documented
	    public static @interface List {
	    	UserIdExists[] value();
	    }

}
