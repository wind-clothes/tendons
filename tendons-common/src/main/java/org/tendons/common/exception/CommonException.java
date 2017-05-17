package org.tendons.common.exception;

public class CommonException extends RuntimeException {

  private static final long serialVersionUID = -6335242594183232634L;

  public CommonException() {
    super();
  }

  public CommonException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public CommonException(String message, Throwable cause) {
    super(message, cause);
  }

  public CommonException(String message) {
    super(message);
  }

  public CommonException(Throwable cause) {
    super(cause);
  }

}
