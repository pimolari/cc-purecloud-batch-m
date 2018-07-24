package com.cc.purecloud.client.model;

public class CustomPresenceDefinition {
  
  
  private String id;
  private String desc;
  private String systemPresence;
  
  public CustomPresenceDefinition(String id, String desc) {
    this.id = id;
    this.desc = desc;
  }
  
  public CustomPresenceDefinition(String id, String desc, String systemPresence) {
    this.id = id;
    this.desc = desc;
    this.systemPresence = systemPresence;
  }
  
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getDesc() {
    return desc;
  }
  public void setDesc(String desc) {
    this.desc = desc;
  }
  public String getSystemPresence() {
    return systemPresence;
  }
  public void setSystemPresence(String systemPresenceMapping) {
    this.systemPresence = systemPresenceMapping;
  }
  
  
  
}
