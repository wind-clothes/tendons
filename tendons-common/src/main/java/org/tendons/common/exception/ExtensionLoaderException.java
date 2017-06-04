package org.tendons.common.exception;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2017年6月4日 下午8:14:58
 */
public class ExtensionLoaderException extends CommonException {

  private static final long serialVersionUID = 1901818274925259845L;

  public ExtensionLoaderException() {
    super();
  }

  public ExtensionLoaderException(String message) {
    super(message);
  }

  public ExtensionLoaderException(Throwable cause) {
    super(cause);
  }

  public ExtensionLoaderException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExtensionLoaderException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
