package com.cc.purecloud.report;

import java.time.format.DateTimeFormatter;
import java.util.List;

import com.cc.purecloud.client.model.CustomMetric;
import com.cc.purecloud.client.model.CustomPresenceDefinition;
import com.cc.purecloud.client.model.CustomUser;
import com.cc.purecloud.client.model.UserPresenceAggregateMetricsModel;
import com.cc.purecloud.client.model.UserPresenceAggregateMetricsRow;
import com.cc.purecloud.util.Utils;
import com.google.gson.Gson;

public class UserListReport extends GenericReport {
  
  List<CustomUser> model = null;
  
  public UserListReport(String reportName, List<CustomUser> model) {
    super(reportName);
    this.model = model;
    
    System.out.println(model);
    System.out.println(model.size());
  }
  
  public List<CustomUser> getModel() {
    return this.model;
  }

  public void setModel(List<CustomUser> model) {
    this.model = model;
  }
  
  public String generateJson() {
    return new Gson().toJson(this.model);
  }
  
  public String generateCSV() {
    
    StringBuffer buffer = new StringBuffer();
   
    buffer.append("id,"
                + "name");
    for (CustomUser user : this.model) {
      buffer.append("\n");
      buffer.append(user.getId()).append(",");
      buffer.append(user.getName());
    }

    return buffer.toString();
  }
}
