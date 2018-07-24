package com.cc.purecloud.client.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class CustomUser implements Serializable {
  
  private String id;
  private String name;
  
  public CustomUser() {
  }
  
  public CustomUser(String id, String name) {
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
