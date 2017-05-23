package org.tendons.common.util.concurrrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: xcw
 * @date:2017年5月23日 下午5:56:28
 */
public class DefineThreadFactory implements ThreadFactory {

  /** 全局线程池计数器 */
  private static final AtomicInteger POOL_ID = new AtomicInteger();

  /**
   * 当前对象的线程池线程计数器
   */
  private final AtomicInteger currentId = new AtomicInteger();
  /**
   * 线程后缀名
   */
  private final String prefix;
  /**
   * 是否是守护线程
   */
  private final boolean daemon;
  /**
   * 优先级
   */
  private final int priority;
  /**
   * 当前线程组
   */
  protected final ThreadGroup threadGroup;

  public DefineThreadFactory(String prefix) {
    this(prefix, false);
  }

  public DefineThreadFactory(String prefix, boolean daemon) {
    this(prefix, daemon, Thread.NORM_PRIORITY);
  }

  public DefineThreadFactory(String prefix, boolean daemon, int priority) {
    this(prefix, daemon, priority, System.getSecurityManager() == null
        ? Thread.currentThread().getThreadGroup()
        : System.getSecurityManager().getThreadGroup());
  }

  public DefineThreadFactory(String prefix, boolean daemon, int priority, ThreadGroup threadGroup) {
    this.prefix = prefix + POOL_ID.incrementAndGet() + "-Thread-";
    this.daemon = daemon;
    this.priority = priority;
    this.threadGroup = threadGroup;
  }


  @Override
  public Thread newThread(Runnable r) {
    final String name = prefix + currentId.incrementAndGet();
    final Thread thread = new Thread(threadGroup, r, name, 0);
    thread.setDaemon(daemon);
    thread.setPriority(priority);
    return thread;
  }

}
