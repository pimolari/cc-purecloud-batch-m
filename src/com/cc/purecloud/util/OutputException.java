package com.cc.purecloud.util;

public class OutputException extends Exception {
  
  private String message = null;
  
  public OutputException(String message) {
    super();
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
  
  
}
