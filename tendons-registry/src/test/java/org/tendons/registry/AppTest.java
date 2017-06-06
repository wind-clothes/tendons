package org.tendons.registry;

import java.util.LinkedHashMap;
import java.util.Map;

import org.tendons.registry.loadbalance.ServiceProvider;
import org.tendons.registry.loadbalance.impl.WeightRoundRobinLoadBalancer.DefineInteger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public AppTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(AppTest.class);
  }

  /**
   * Rigourous Test :-)
   * @param <T>
   */
  public <T> void testApp() {
    int currentSequence = 5;
    int weightSum = 8;
    // 权重不一样
    int mod = currentSequence % weightSum;
    int maxWeight = 5;
    LinkedHashMap<ServiceProvider<T>, DefineInteger> invokerToWeightMap =
        new LinkedHashMap<ServiceProvider<T>, DefineInteger>();
    ServiceProvider<T> serviceProvider = new ServiceProvider<>();
    serviceProvider.setServiceName("aaa");
    invokerToWeightMap.put(serviceProvider, new DefineInteger(1));
    ServiceProvider<T> serviceProvider1 = new ServiceProvider<>();
    serviceProvider1.setServiceName("bbb");
    invokerToWeightMap.put(serviceProvider1, new DefineInteger(2));
    ServiceProvider<T> serviceProvider2 = new ServiceProvider<>();
    serviceProvider2.setServiceName("ccc");
    invokerToWeightMap.put(serviceProvider2, new DefineInteger(5));
    for (int i = 0; i < maxWeight; i++) {
      for (Map.Entry<ServiceProvider<T>, DefineInteger> each : invokerToWeightMap.entrySet()) {
        final ServiceProvider<T> k = each.getKey();
        DefineInteger v = each.getValue();
        if (mod == 0 && v.getValue() > 0) {
          System.out.println();
        }
        if (v.getValue() > 0) {
          v.decrement();
          mod--;
        }
      }
    }
    assertTrue(true);
  }
}
