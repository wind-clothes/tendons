package org.tendons.common.extension;

import java.util.Comparator;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年6月4日 下午8:12:21
 */
public class ActivationComparator<T> implements Comparator<T> {

  public int compare(Object o1, Object o2) {
    Activation p1 = o1.getClass().getAnnotation(Activation.class);
    Activation p2 = o2.getClass().getAnnotation(Activation.class);
    if (p1 == null) {
      return 1;
    } else if (p2 == null) {
      return -1;
    } else {
      return p1.sequence() - p2.sequence();
    }
  }
}
