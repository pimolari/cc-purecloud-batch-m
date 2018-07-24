package com.cc.purecloud.client.model;

import java.util.ArrayList;

import javax.management.modelmbean.ModelMBean;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.google.gson.Gson;

public class UserQueueAggregateMetricsModel extends ArrayList<UserQueueAggregateMetricsRow>{
  
  UserQueueAggregateMetricsRow total = null;
  
  public UserQueueAggregateMetricsModel() {
    total = new UserQueueAggregateMetricsRow();
  }
  
  public void append(UserQueueAggregateMetricsModel model) {
    
    for (UserQueueAggregateMetricsRow row : model) {
      this.add(row);
    }
  }
  
  public void totalize() {
    
    for (UserQueueAggregateMetricsRow row : this) {
      // ...
    }
    
    System.out.println(" Totals > " + new Gson().toJson(total));
  }
  
  public String toString() {
    
    StringBuffer buffer = new StringBuffer();
    
    for (UserQueueAggregateMetricsRow row : this) {
      buffer.append("\n" + row.toString());
    }
    
    return buffer.toString();
    
  }
  
}
