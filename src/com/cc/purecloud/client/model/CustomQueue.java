package com.cc.purecloud.client.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class CustomQueue implements Serializable {
  
  private String id;
  private String name;
  
  public CustomQueue() {
  }
  
  public CustomQueue(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
