package com.java.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheControl {

	CachePolicy[] policy() default { CachePolicy.NO_CACHE };

	int maxAge() default -1;

	int sharedMaxAge() default -1;

}
