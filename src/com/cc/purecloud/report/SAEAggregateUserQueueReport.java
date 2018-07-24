package com.cc.purecloud.report;

import java.time.format.DateTimeFormatter;

import com.cc.purecloud.client.model.CustomMetric;
import com.cc.purecloud.client.model.UserQueueAggregateMetricsModel;
import com.cc.purecloud.client.model.UserQueueAggregateMetricsRow;
import com.cc.purecloud.client.model.util.CustomMetricUtil;
import com.cc.purecloud.util.Utils;
import com.google.gson.Gson;

public class SAEAggregateUserQueueReport extends GenericReport {
  
  UserQueueAggregateMetricsModel model = null;
  
  public SAEAggregateUserQueueReport(String reportName, UserQueueAggregateMetricsModel model) {
    super(reportName);
    this.model = model;
  }
  
  public String generateJson() {
    return new Gson().toJson(this.model);
  }
  
  public String generateCSV() {
    
    
    //System.out.println("Model generated...");
    //System.out.println(model.toString());
    
    StringBuffer buffer = new StringBuffer();
   
    buffer.append("start.date,"
                + "start.time,"
                + "end.date,"
                + "end.time,"
                + "username,"
                + "talk.count,"
                + "talk.time,"
                + "hold.time,"
                + "alert.time,"
                + "acw.time,"
                + "answered.count,"
                + "answered.time");
                //+ "answered.count,"
                //+ "answered.time");
    
    UserQueueAggregateMetricsRow aRow = null;
    
    for (UserQueueAggregateMetricsRow row : this.model) {
      
      
      
      
      if (aRow == null) {
        aRow = new UserQueueAggregateMetricsRow();
      } else if (aRow.getUserName() != null && !aRow.getUserName().equals(row.getUserName())) {
        
        buffer.append("\n");
        buffer.append(aRow.getDateIni().plusHours(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",");
        buffer.append(aRow.getDateIni().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(",");
        buffer.append(aRow.getDateEnd().plusHours(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",");
        buffer.append(aRow.getDateEnd().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(",");
        buffer.append(aRow.getUserName()).append(",");
        buffer.append(aRow.getTalk().getCount()).append(",");
        buffer.append(Utils.convert(aRow.getTalk().getTime(), "ms", "s")).append(",");
        buffer.append(Utils.convert(aRow.getHold().getTime(), "ms", "s")).append(",");
        buffer.append(Utils.convert(aRow.getAlert().getTime(), "ms", "s")).append(",");
        buffer.append(Utils.convert(aRow.getAcw().getTime(), "ms", "s")).append(",");
        buffer.append(aRow.getAnswer().getCount()).append(",");
        buffer.append(Utils.convert(aRow.getAnswer().getTime(), "ms", "s"));
        
        aRow = new UserQueueAggregateMetricsRow();
        
      } else {
        //aRow = new UserQueueAggregateMetricsRow();
      }
      
      /*buffer.append("\n");
      buffer.append(row.getDateIni().plusHours(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",");
      buffer.append(row.getDateIni().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(",");
      buffer.append(row.getDateEnd().plusHours(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",");
      buffer.append(row.getDateEnd().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(",");
      buffer.append(row.getUserName()).append(",");
      buffer.append(row.getTalk().getCount()).append(",");
      buffer.append(Utils.convert(row.getTalk().getTime(), "ms", "s")).append(",");
      buffer.append(Utils.convert(row.getHold().getTime(), "ms", "s")).append(",");
      buffer.append(Utils.convert(row.getAlert().getTime(), "ms", "s")).append(",");
      buffer.append(Utils.convert(row.getAcw().getTime(), "ms", "s")).append(" -- ");
      buffer.append(row.getAnswer().getCount()).append(",");
      buffer.append(Utils.convert(row.getAnswer().getTime(), "ms", "s"));*/
      
      aRow.setAcw(CustomMetricUtil.sum(aRow.getAcw(), row.getAcw()));
      aRow.setAlert(CustomMetricUtil.sum(aRow.getAlert(), row.getAlert()));
      aRow.setHandle(CustomMetricUtil.sum(aRow.getHandle(), row.getHandle()));
      aRow.setHold(CustomMetricUtil.sum(aRow.getHold(), row.getHold()));
      aRow.setTalk(CustomMetricUtil.sum(aRow.getTalk(), row.getTalk()));
      aRow.setAnswer(CustomMetricUtil.sum(aRow.getAnswer(), row.getAnswer()));
      aRow.setUserName(row.getUserName());
      aRow.setDateIni(row.getDateIni());
      aRow.setDateEnd(row.getDateEnd());
      aRow.setInterval(row.getInterval());
      
      
      
    }
    
    buffer.append("\n");
    buffer.append(aRow.getDateIni().plusHours(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",");
    buffer.append(aRow.getDateIni().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(",");
    buffer.append(aRow.getDateEnd().plusHours(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",");
    buffer.append(aRow.getDateEnd().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(",");
    buffer.append(aRow.getUserName()).append(",");
    buffer.append(aRow.getTalk().getCount()).append(",");
    buffer.append(Utils.convert(aRow.getTalk().getTime(), "ms", "s")).append(",");
    buffer.append(Utils.convert(aRow.getHold().getTime(), "ms", "s")).append(",");
    buffer.append(Utils.convert(aRow.getAlert().getTime(), "ms", "s")).append(",");
    buffer.append(Utils.convert(aRow.getAcw().getTime(), "ms", "s")).append(",");
    buffer.append(aRow.getAnswer().getCount()).append(",");
    buffer.append(Utils.convert(aRow.getAnswer().getTime(), "ms", "s"));
    //buffer.append(aRow.getAnswer().getCount()).append(",");
    //buffer.append(Utils.convert(aRow.getAnswer().getTime(), "ms", "s"));
    
    return buffer.toString();
  }
}
