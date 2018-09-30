package com.cc.purecloud.report;

import java.time.format.DateTimeFormatter;

import com.cc.purecloud.client.model.QueueAggregateMetricsModel;
import com.cc.purecloud.client.model.QueueAggregateMetricsRow;
import com.cc.purecloud.client.model.util.CustomMetricUtil;
import com.cc.purecloud.util.Utils;
import com.google.gson.Gson;

public class SAEAggregateServiceNowReport extends GenericReport {
  
  QueueAggregateMetricsModel model = null;
  String country = "ESPAÑA";
  String company = "1";
  
  public SAEAggregateServiceNowReport(String reportName, QueueAggregateMetricsModel model, String country, String company) {
    super(reportName);
    this.model = model;
    if (country != null && !country.equals("")) this.country = country;
    if (company != null && !company.equals("")) this.company = company;
  }
  
  public String generateJson() {
    return new Gson().toJson(this.model);
  }
  
  public String generateCSV() {
    StringBuffer buffer = new StringBuffer();
   
    buffer.append("'start.date',"
                + "'offered.count',"
                + "'abandon.count',"
                + "'alert.time',"
                + "'talk.time',"
                + "'answer.count',"
                + "'answer15s.count',"
                + "'country',"
                + "'company'");
    
    for (QueueAggregateMetricsRow row : this.model) {
      buffer.append("\n");
      buffer.append("'" + row.getDateIni().plusHours(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append("',");
      
      buffer.append("'" + row.getOffered().getCount()).append("',");
      buffer.append("'" + row.getAbandon().getCount()).append("',");
      //buffer.append(row.getAbandon5s().getCount()).append(",");
      buffer.append("'" + Utils.convert(row.getAlertTime().getTime(), "ms", "s")).append("',");
      buffer.append("'" + Utils.convert(row.getTalk().getTime(), "ms", "s")).append("',");
      buffer.append("'" + row.getAnswer().getCount()).append("',");
      buffer.append("'" + row.getAnswer15s().getCount()).append("',");
      //buffer.append(row.getTalk().getCount()).append(",");
      buffer.append("'" + this.country + "',");
      buffer.append("'" + this.company + "'");
      
    }
    
    return buffer.toString();
  }
  
  public String upload() {
    System.out.println("Invokes ServiceNow endpoint now ... ");
    
    QueueAggregateMetricsRow aRow = new QueueAggregateMetricsRow();
    
    for (QueueAggregateMetricsRow row : this.model) {
      aRow.setTalk(CustomMetricUtil.sum(aRow.getTalk(), row.getTalk()));
      aRow.setAbandon(CustomMetricUtil.sum(aRow.getAbandon(), row.getAbandon()));
      aRow.setAbandon5s(CustomMetricUtil.sum(aRow.getAbandon5s(), row.getAbandon5s()));
      aRow.setAnswer(CustomMetricUtil.sum(aRow.getAnswer(), row.getAnswer()));
      aRow.setAnswer15s(CustomMetricUtil.sum(aRow.getAnswer15s(), row.getAnswer15s()));
      
    }
    
    
    
    System.out.println("getTalk: " + aRow.getTalk().getCount() + " :: " + aRow.getTalk().getTime());
    System.out.println("getAbandon: " + aRow.getAbandon().getCount() + " :: " + aRow.getAbandon().getTime());
    System.out.println("getAbandon5s: " + aRow.getAbandon5s().getCount() + " :: " + aRow.getAbandon5s().getTime());
    System.out.println("getAnswer: " + aRow.getAnswer().getCount() + " :: " + aRow.getAnswer().getTime());
    System.out.println("getAnswer15s: " + aRow.getAnswer15s().getCount() + " :: " + aRow.getAnswer15s().getTime());
    
    /*buffer.append(Utils.convert(row.getAvailable().getTime()
        .add(row.getOnBreak().getTime())
        .add(row.getAway().getTime())
        .add(row.getOnQueue().getTime())
        .add(row.getMeal().getTime())
        .add(row.getBusy().getTime()), "ms", "s").intValue()).append(",");
    */
    
    return "";
  }
}
