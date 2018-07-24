package com.cc.purecloud.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cc.purecloud.client.model.util.CustomMetricUtil;
import com.google.gson.Gson;

public class UserPresenceAggregateMetricsModel extends ArrayList<UserPresenceAggregateMetricsRow>{
  
  UserPresenceAggregateMetricsRow total = null;
  List<CustomPresenceDefinition> presenceDefinitions = null;
  Map<String, String> presenceDef = null;
  
  public UserPresenceAggregateMetricsModel() {
    total = new UserPresenceAggregateMetricsRow();
    this.presenceDefinitions = new ArrayList<CustomPresenceDefinition>();
    presenceDef = new HashMap<String, String>();
  }
  
  public UserPresenceAggregateMetricsModel(List<CustomPresenceDefinition> presenceDefinitions) {
    total = new UserPresenceAggregateMetricsRow();
    this.presenceDefinitions = presenceDefinitions;
    presenceDef = new HashMap<String, String>();
    this.setPresenceDefinitions(presenceDefinitions);
    
    
    
    
    
    
    
    
  }
  
  public void append(UserPresenceAggregateMetricsModel model) {
    
    for (UserPresenceAggregateMetricsRow row : model) {
      this.add(row);
    }
  }

  public UserPresenceAggregateMetricsRow getTotal() {
    return total;
  }

  public void setTotal(UserPresenceAggregateMetricsRow total) {
    this.total = total;
  }
  
  public void addPresenceDefinition(CustomPresenceDefinition presenceDefinition) {
    this.presenceDefinitions.add(presenceDefinition);
  }
  
  public List<CustomPresenceDefinition> getPresenceDefinitions() {
    
    List<CustomPresenceDefinition> presenceDefinitions = new ArrayList<CustomPresenceDefinition>();
    
    Iterator i = presenceDef.keySet().iterator();
    
    while (i.hasNext()) {
      String key = (String)i.next();
      presenceDefinitions.add(new CustomPresenceDefinition(key, this.presenceDef.get(key)));
    }
    
    /*for (CustomPresenceDefinition pd : presenceDefinitions)
      System.out.println(" > " + pd.getDesc() + "   " + pd.getId());
    */
    
    return presenceDefinitions;
  }

  public void setPresenceDefinitions(List<CustomPresenceDefinition> presenceDefinitions) {
    this.presenceDefinitions = presenceDefinitions;
    
    for (CustomPresenceDefinition presenceDefinition : presenceDefinitions) {
      this.presenceDef.put(presenceDefinition.getId(), presenceDefinition.getDesc());
    }
  }
  

  public void totalize(String userId, String interval) {
    
    UserPresenceAggregateMetricsRow r = this.newRow("total");
    r.setInterval(interval);
    for (UserPresenceAggregateMetricsRow row : this) {
      
      if (row.getUserId().equals(userId)) {
        r.getAvailable().setTime(r.getAvailable().getTime().add(row.getAvailable().getTime()));
        r.getOffline().setTime(r.getOffline().getTime().add(row.getOffline().getTime()));
        
        r.getOnBreak().setTime(r.getOnBreak().getTime().add(row.getOnBreak().getTime()));
        r.getAway().setTime(r.getAway().getTime().add(row.getAway().getTime()));
        r.getOnQueue().setTime(r.getOnQueue().getTime().add(row.getOnQueue().getTime()));
        r.getMeal().setTime(r.getMeal().getTime().add(row.getMeal().getTime()));
        r.getBusy().setTime(r.getBusy().getTime().add(row.getBusy().getTime()));
        
        r.setUserId(row.getUserId());
        r.setUserName(row.getUserName());
        
        Iterator i = row.getData().keySet().iterator();
        
        while (i.hasNext()) {
          String key = (String)i.next();
          //System.out.println("  Sums " + key + " : " + r.getData(key).getTime() + " + " + row.getData(key).getTime() + " = " + CustomMetricUtil.sum(r.getData(key), row.getData(key)).getTime());
          r.setData(key, CustomMetricUtil.sum(r.getData(key), row.getData(key)));
        }
      }
    }
    
    this.add(r);
    
    //System.out.println(" Totals > " + new Gson().toJson(r));
  }
  
  public UserPresenceAggregateMetricsRow newRow() {
    return new UserPresenceAggregateMetricsRow(this.presenceDefinitions);
  }
  
  public UserPresenceAggregateMetricsRow newRow(String type) {
    return new UserPresenceAggregateMetricsRow(type, this.presenceDefinitions);
  }
  
}
