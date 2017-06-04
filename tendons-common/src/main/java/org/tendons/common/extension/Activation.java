package org.tendons.common.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p />
 * 对于可以被框架中自动激活加载扩展，此Annotation用于配置扩展被自动激活加载条件,暂时扩展到类级别。
 * <p />
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Activation {

  /** seq号越小，在返回的list<Instance>中的位置越靠前，尽量使用 0-100以内的数字 */
  int sequence() default 20;

  /** spi 的key，获取spi列表时，根据key进行匹配，当key中存在待过滤的search-key时，匹配成功 */
  String[] key() default "";

  /** 是否支持重试的时候也调用 */
  boolean retry() default true;
}
