package com.cc.purecloud.client.model;

import java.util.ArrayList;

import javax.management.modelmbean.ModelMBean;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.google.gson.Gson;

public class QueueAggregateMetricsModel extends ArrayList<QueueAggregateMetricsRow>{
  
  QueueAggregateMetricsRow total = null;
  
  public QueueAggregateMetricsModel() {
    total = new QueueAggregateMetricsRow();
  }
  
  public void append(QueueAggregateMetricsModel model) {
    
    for (QueueAggregateMetricsRow row : model) {
      this.add(row);
    }
  }
  
  /*public void totalize() {
    
    for (QueueAggregateMetricsRow row : this) {
      total.getAbandon5s().setCount(total.getAbandon5s().getCount() + row.getAbandon5s().getCount());
      // ...
    }
    
    System.out.println(" Totals > " + new Gson().toJson(total));
  }*/
  
  
  
}
