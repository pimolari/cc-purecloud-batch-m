package com.cc.purecloud.report;

import java.time.format.DateTimeFormatter;

import com.cc.purecloud.client.model.QueueAggregateMetricsModel;
import com.cc.purecloud.client.model.QueueAggregateMetricsRow;
import com.cc.purecloud.util.Utils;
import com.google.gson.Gson;

public class SAEAggregatedQueueReport extends GenericReport {
  
  QueueAggregateMetricsModel model = null;
  
  public SAEAggregatedQueueReport(String reportName, QueueAggregateMetricsModel model) {
    super(reportName);
    this.model = model;
  }
  
  public String generateJson() {
    return new Gson().toJson(this.model);
  }
  
  public String generateCSV() {
    StringBuffer buffer = new StringBuffer();
   
    buffer.append("start.date,"
                + "start.time,"
                + "end.date,"
                + "end.time,"
                + "queue,"
                + "offered.count,"
                + "acd.count,"
                + "abandon.count,"
                + "abandon5s.count,"
                + "answer.count,"
                + "answer15s.count,"
                + "abandon.time,"
                + "acd.time,"
                + "hold.time,"
                + "acw.time,"
                + "talk.time,"
                + "talk.count,"
                + "alert.time,"
                + "alert.average.time");
               
    
    for (QueueAggregateMetricsRow row : this.model) {
      buffer.append("\n");
      //buffer.append(row.getDateIni().plusHours(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",");
      //buffer.append(row.getDateIni().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(",");
      //buffer.append(row.getDateEnd().plusHours(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",");
      //buffer.append(row.getDateEnd().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(",");
      
      buffer.append(row.getDateIni().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",");
      buffer.append(row.getDateIni().format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(",");
      buffer.append(row.getDateEnd().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",");
      buffer.append(row.getDateEnd().format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(",");
      buffer.append(row.getQueueName()).append(",");
      buffer.append(row.getOffered().getCount()).append(",");
      buffer.append(row.getAcd().getCount()).append(",");
      buffer.append(row.getAbandon().getCount()).append(",");
      buffer.append(row.getAbandon5s().getCount()).append(",");
      buffer.append(row.getAnswer().getCount()).append(",");
      buffer.append(row.getAnswer15s().getCount()).append(",");
      buffer.append(Utils.convert(row.getAbandon().getTime(), "ms", "s")).append(",");
      buffer.append(Utils.convert(row.getAcd().getTime(), "ms", "s")).append(",");
      buffer.append(Utils.convert(row.getHold().getTime(), "ms", "s")).append(",");
      buffer.append(Utils.convert(row.getAcw().getTime(), "ms", "s")).append(",");
      buffer.append(Utils.convert(row.getTalk().getTime(), "ms", "s")).append(",");
      buffer.append(row.getTalk().getCount()).append(",");
      buffer.append(Utils.convert(row.getAlertTime().getTime(), "ms", "s")).append(",");
      buffer.append(Utils.convert(row.getAlertAverageTime().getTime(), "ms", "s"));
    }
    
    return buffer.toString();
  }
}
