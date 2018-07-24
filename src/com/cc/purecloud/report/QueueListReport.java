package com.cc.purecloud.report;

import java.util.List;

import com.cc.purecloud.client.model.CustomQueue;
import com.cc.purecloud.client.model.CustomUser;
import com.google.gson.Gson;

public class QueueListReport extends GenericReport {
  
  List<CustomQueue> model = null;
  
  public QueueListReport(String reportName, List<CustomQueue> model) {
    super(reportName);
    this.model = model;
    
    System.out.println(model);
    System.out.println(model.size());
  }
  
  public List<CustomQueue> getModel() {
    return this.model;
  }

  public void setModel(List<CustomQueue> model) {
    this.model = model;
  }
  
  public String generateJson() {
    return new Gson().toJson(this.model);
  }
  
  public String generateCSV() {
    
    StringBuffer buffer = new StringBuffer();
   
    buffer.append("id,"
                + "name");
    for (CustomQueue queue : this.model) {
      buffer.append("\n");
      buffer.append(queue.getId()).append(",");
      buffer.append(queue.getName());
    }

    return buffer.toString();
  }
}
