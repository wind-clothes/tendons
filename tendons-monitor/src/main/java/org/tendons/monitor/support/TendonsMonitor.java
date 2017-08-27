package org.tendons.monitor.support;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tendons.common.service.RegisterServiceUrl;
import org.tendons.common.util.concurrrent.DefineThreadFactory;
import org.tendons.monitor.Monitor;
import org.tendons.monitor.MonitorService;
import org.tendons.monitor.Contants;
import org.tendons.monitor.ServiceStatistics;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年5月30日 下午3:41:57
 */
public class TendonsMonitor implements Monitor {

  private static final Logger LOGGER = LoggerFactory.getLogger(TendonsMonitor.class);

  private static final int LENGTH = 10;

  // 定时任务执行器
  private final ScheduledExecutorService scheduledExecutorService =
      Executors.newScheduledThreadPool(3, new DefineThreadFactory("TendonsMonitorSendTimer", true));

  // 统计信息收集定时器
  private final ScheduledFuture<?> sendFuture;

  // 远程的RPC服务,用来发送监控信息
  private final MonitorService monitorService;

  private final long monitorInterval;

  private final ConcurrentMap<ServiceStatistics, AtomicReference<long[]>> statisticsMap =
      new ConcurrentHashMap<ServiceStatistics, AtomicReference<long[]>>();

  public TendonsMonitor(MonitorService monitorService) {
    this.monitorService = monitorService;
    this.monitorInterval = 60000;
    // 启动统计信息收集定时器
    sendFuture = scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
      public void run() {
        // 收集统计信息
        try {
          send();
        } catch (Throwable t) { // 防御性容错
          LOGGER.error("Unexpected error occur at send statistic, cause: " + t.getMessage(), t);
        }
      }
    }, monitorInterval, monitorInterval, TimeUnit.MILLISECONDS);
  }

  public void send() {
    String timestamp = String.valueOf(System.currentTimeMillis());
    for (Map.Entry<ServiceStatistics, AtomicReference<long[]>> entry : statisticsMap.entrySet()) {
      // 获取已统计数据
      ServiceStatistics statistics = entry.getKey();
      AtomicReference<long[]> reference = entry.getValue();
      long[] numbers = reference.get();
      long success = numbers[0];
      long failure = numbers[1];
      long input = numbers[2];
      long output = numbers[3];
      long elapsed = numbers[4];
      long concurrent = numbers[5];
      long maxInput = numbers[6];
      long maxOutput = numbers[7];
      long maxElapsed = numbers[8];
      long maxConcurrent = numbers[9];

      // 发送汇总信息
      final RegisterServiceUrl url = statistics.getUrl().addParameters(Contants.TIMESTAMP,
          timestamp, Contants.SUCCESS, String.valueOf(success), Contants.FAILURE,
          String.valueOf(failure), Contants.INPUT, String.valueOf(input),
          Contants.OUTPUT, String.valueOf(output), Contants.ELAPSED,
          String.valueOf(elapsed), Contants.CONCURRENT, String.valueOf(concurrent),
          Contants.MAX_INPUT, String.valueOf(maxInput), Contants.MAX_OUTPUT,
          String.valueOf(maxOutput), Contants.MAX_ELAPSED, String.valueOf(maxElapsed),
          Contants.MAX_CONCURRENT, String.valueOf(maxConcurrent));
      // 将性能指数通过RPC服务发送到指定的服务中去
      monitorService.collect(url);

      // 减掉已统计数据
      long[] current;
      long[] update = new long[LENGTH];
      do {
        current = reference.get();
        if (current == null) {
          update[0] = 0;
          update[1] = 0;
          update[2] = 0;
          update[3] = 0;
          update[4] = 0;
          update[5] = 0;
        } else {
          update[0] = current[0] - success;
          update[1] = current[1] - failure;
          update[2] = current[2] - input;
          update[3] = current[3] - output;
          update[4] = current[4] - elapsed;
          update[5] = current[5] - concurrent;
        }
      } while (!reference.compareAndSet(current, update));
    }
  }

  @Override
  public void collect(RegisterServiceUrl serviceUrl) {
    // 读写统计变量
    int success = serviceUrl.getParameter(Contants.SUCCESS, 0);
    int failure = serviceUrl.getParameter(Contants.FAILURE, 0);
    int input = serviceUrl.getParameter(Contants.INPUT, 0);
    int output = serviceUrl.getParameter(Contants.OUTPUT, 0);
    int elapsed = serviceUrl.getParameter(Contants.ELAPSED, 0);
    int concurrent = serviceUrl.getParameter(Contants.CONCURRENT, 0);
    // 初始化原子引用
    ServiceStatistics statistics = new ServiceStatistics(serviceUrl);
    AtomicReference<long[]> reference = statisticsMap.get(statistics);
    if (reference == null) {
      statisticsMap.putIfAbsent(statistics, new AtomicReference<long[]>());
      reference = statisticsMap.get(statistics);
    }
    // 并发加入统计数据
    long[] current;
    long[] update = new long[LENGTH];
    do {
      current = reference.get();
      if (current == null) {
        update[0] = success;
        update[1] = failure;
        update[2] = input;
        update[3] = output;
        update[4] = elapsed;
        update[5] = concurrent;
        update[6] = input;
        update[7] = output;
        update[8] = elapsed;
        update[9] = concurrent;
      } else {
        update[0] = current[0] + success;
        update[1] = current[1] + failure;
        update[2] = current[2] + input;
        update[3] = current[3] + output;
        update[4] = current[4] + elapsed;
        update[5] = (current[5] + concurrent) / 2;
        update[6] = current[6] > input ? current[6] : input;
        update[7] = current[7] > output ? current[7] : output;
        update[8] = current[8] > elapsed ? current[8] : elapsed;
        update[9] = current[9] > concurrent ? current[9] : concurrent;
      }
    } while (!reference.compareAndSet(current, update));
  }

  @Override
  public List<RegisterServiceUrl> lookup(RegisterServiceUrl serviceUrl) {
    return monitorService.lookup(serviceUrl);
  }

  public void destroy() {
    try {
      sendFuture.cancel(true);
    } catch (Throwable t) {
      LOGGER.error("Unexpected error occur at cancel sender timer, cause: " + t.getMessage(), t);
    }
  }

}
