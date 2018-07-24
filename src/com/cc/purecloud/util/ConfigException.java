package com.cc.purecloud.util;

public class ConfigException extends Exception {
  
  private String message = null;
  
  public ConfigException(String message) {
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
