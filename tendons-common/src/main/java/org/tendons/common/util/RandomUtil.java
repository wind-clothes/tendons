package org.tendons.common.util;

import java.util.Random;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月21日 下午4:43:14
 */
public final class RandomUtil {

  private RandomUtil() {}

  public static int random(int maxSize) {
    final Random random = new Random();
    return random.nextInt(maxSize);
  }
}
