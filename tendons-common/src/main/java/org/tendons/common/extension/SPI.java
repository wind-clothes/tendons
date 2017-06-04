package org.tendons.common.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * 可扩展点的标识
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月31日 下午6:53:37
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

  String value() default "";

}
