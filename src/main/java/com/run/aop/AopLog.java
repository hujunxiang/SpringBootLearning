package com.run.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AopLog {
    /**
     * 操作内容
     */
    String description();
    /**
     * 操作模块名称
     */
    String moduleName();
    /**
     * 操作类型 : 见OptType
     */
    String operType();
}
