package org.tendons.common.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 * 自定义的整型原子类，封装了{@link AtomicInteger} TODO
 * </pre>
 * 
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午10:15:57
 */
public class AtomicNativeInteger extends Number {

  private static final long serialVersionUID = -3038533876489105940L;

  private final AtomicInteger value;

  public AtomicNativeInteger() {
    value = new AtomicInteger();
  }

  public AtomicNativeInteger(int initialValue) {
    value = new AtomicInteger(initialValue);
  }

  public final int getAndIncrement() {
    for (;;) {
      int current = value.get();
      int next = (current >= Integer.MAX_VALUE ? 0 : current + 1);
      if (value.compareAndSet(current, next)) {
        return current;
      }
    }
  }

  public final int getAndDecrement() {
    for (;;) {
      int current = value.get();
      int next = (current <= 0 ? Integer.MAX_VALUE : current - 1);
      if (value.compareAndSet(current, next)) {
        return current;
      }
    }
  }

  public final int incrementAndGet() {
    for (;;) {
      int current = value.get();
      int next = (current >= Integer.MAX_VALUE ? 0 : current + 1);
      if (value.compareAndSet(current, next)) {
        return next;
      }
    }
  }

  public final int decrementAndGet() {
    for (;;) {
      int current = value.get();
      int next = (current <= 0 ? Integer.MAX_VALUE : current - 1);
      if (value.compareAndSet(current, next)) {
        return next;
      }
    }
  }

  public final int get() {
    return value.get();
  }

  public final void set(int newValue) {
    if (newValue < 0) {
      throw new IllegalArgumentException("new value " + newValue + " < 0");
    }
    value.set(newValue);
  }

  public final int getAndSet(int newValue) {
    if (newValue < 0) {
      throw new IllegalArgumentException("new value " + newValue + " < 0");
    }
    return value.getAndSet(newValue);
  }

  public final int getAndAdd(int delta) {
    if (delta < 0) {
      throw new IllegalArgumentException("delta " + delta + " < 0");
    }
    for (;;) {
      int current = value.get();
      int next = (current >= Integer.MAX_VALUE - delta + 1 ? delta - 1 : current + delta);
      if (value.compareAndSet(current, next)) {
        return current;
      }
    }
  }

  public final int addAndGet(int delta) {
    if (delta < 0) {
      throw new IllegalArgumentException("delta " + delta + " < 0");
    }
    for (;;) {
      int current = value.get();
      int next = (current >= Integer.MAX_VALUE - delta + 1 ? delta - 1 : current + delta);
      if (value.compareAndSet(current, next)) {
        return next;
      }
    }
  }

  public final boolean compareAndSet(int expect, int update) {
    if (update < 0) {
      throw new IllegalArgumentException("update value " + update + " < 0");
    }
    return value.compareAndSet(expect, update);
  }

  public final boolean weakCompareAndSet(int expect, int update) {
    if (update < 0) {
      throw new IllegalArgumentException("update value " + update + " < 0");
    }
    return value.weakCompareAndSet(expect, update);
  }

  public byte byteValue() {
    return value.byteValue();
  }

  public short shortValue() {
    return value.shortValue();
  }

  public int intValue() {
    return value.intValue();
  }

  public long longValue() {
    return value.longValue();
  }

  public float floatValue() {
    return value.floatValue();
  }

  public double doubleValue() {
    return value.doubleValue();
  }

  public String toString() {
    return value.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + value.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof AtomicNativeInteger))
      return false;
    AtomicNativeInteger other = (AtomicNativeInteger) obj;
    return value.intValue() == other.value.intValue();
  }
}
