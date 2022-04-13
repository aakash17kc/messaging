package com.aakash.messaging.exceptions;


import org.apache.commons.lang3.exception.ExceptionUtils;

public class MessagingException extends Exception {

  private String errorMessage;
  private String errorType;


  private int errorCode;

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }


  public String getErrorType() {
    return errorType;
  }

  public void setErrorType(String errorType) {
    this.errorType = errorType;
  }

  public MessagingException(String errorMessage, String errorType, int errorCode) {
    super(errorMessage);
    this.errorMessage = errorMessage;
    this.errorType = errorType;
    this.errorCode = errorCode;
  }

  public int getErrorCode() {
    return errorCode;
  }
  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }
  public MessagingException(Exception exception) {
    super(exception.getMessage());
    this.errorMessage = getGenericExceptionMessage(exception);
    this.errorType = "";
  }

  public static String getGenericExceptionMessage(Exception e) {
    String errorMessage;
    if (e.getCause() != null && e.getCause().getMessage() != null) {
      errorMessage = String.format("Message %s - Stacktrace %s", e.getCause().getMessage(),
          ExceptionUtils.getStackTrace(e.getCause()));
    } else if (e.getMessage() != null) {
      errorMessage = String
          .format("Message %s - Stacktrace %s", e.getMessage(), ExceptionUtils.getStackTrace(e));
    } else {
      errorMessage = ExceptionUtils.getStackTrace(e);
    }
    return errorMessage;
  }
}
