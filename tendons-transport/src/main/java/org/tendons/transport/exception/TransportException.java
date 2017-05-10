package org.tendons.transport.exception;

import org.tendons.common.exception.CommonException;

public class TransportException extends CommonException {

  private static final long serialVersionUID = 4152229715768279880L;

  public TransportException() {
    super();
  }

  public TransportException(String message) {
    super(message);
  }

  public TransportException(Throwable cause) {
    super(cause);
  }

  public TransportException(String message, Throwable cause) {
    super(message, cause);
  }

  public TransportException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }


}
