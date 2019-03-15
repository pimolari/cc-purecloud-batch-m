package com.cc.purecloud.report;

import java.time.format.DateTimeFormatter;

import com.cc.purecloud.client.model.CustomMetric;
import com.cc.purecloud.client.model.CustomPresenceDefinition;
import com.cc.purecloud.client.model.UserPresenceAggregateMetricsModel;
import com.cc.purecloud.client.model.UserPresenceAggregateMetricsRow;
import com.cc.purecloud.util.Utils;
import com.google.gson.Gson;

public class SAEAggregatedUserPresenceReport extends GenericReport {
  
  UserPresenceAggregateMetricsModel model = null;
  
  public SAEAggregatedUserPresenceReport(String reportName, UserPresenceAggregateMetricsModel model) {
    super(reportName);
    this.model = model;
    this.setType("interval");
  }
  
  public UserPresenceAggregateMetricsModel getModel() {
    return model;
  }

  public void setModel(UserPresenceAggregateMetricsModel model) {
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
                + "user.name,");

    for (CustomPresenceDefinition presenceDefinition : this.model.getPresenceDefinitions()) {
      buffer.append(presenceDefinition.getDesc()).append(",");
    }
    
    buffer.append("s.available.time,"
                + "s.offline.time,"
                + "s.break.time,"
                + "s.away.time,"
                + "s.onqueue.time,"
                + "s.meal.time,"
                + "s.busy.time,"
                + "s.meeting.time,"
                + "s.idle.time,"
                + "s.work.total.time,"
                + "s.offline.total.time");
   
    for (UserPresenceAggregateMetricsRow row : this.model) {
      
      if (row.getType().equals(this.getType())) {
        buffer.append("\n");
        //buffer.append(row.getDateIni().plusHours(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",");
        //buffer.append(row.getDateIni().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(",");
        //buffer.append(row.getDateEnd().plusHours(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",");
        //buffer.append(row.getDateEnd().plusHours(2).format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(",");
        
        buffer.append(row.getDateIni().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",");
        buffer.append(row.getDateIni().format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(",");
        buffer.append(row.getDateEnd().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(",");
        buffer.append(row.getDateEnd().format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append(",");
        buffer.append(row.getUserName()).append(",");
        
        for (CustomPresenceDefinition presenceDefinition : this.model.getPresenceDefinitions()) {
          
          CustomMetric metric = row.getData(presenceDefinition.getId());
          
          if (metric != null)
            
            buffer.append(Utils.convert(metric.getTime(), "ms", "s").intValue());
          else
            buffer.append("");
          
          buffer.append(",");
        }
        
        buffer.append(Utils.convert(row.getAvailable().getTime(), "ms", "s").intValue()).append(",");
        buffer.append(Utils.convert(row.getOffline().getTime(), "ms", "s").intValue()).append(",");
        buffer.append(Utils.convert(row.getOnBreak().getTime(), "ms", "s").intValue()).append(",");
        buffer.append(Utils.convert(row.getAway().getTime(), "ms", "s").intValue()).append(",");
        buffer.append(Utils.convert(row.getOnQueue().getTime(), "ms", "s").intValue()).append(",");
        buffer.append(Utils.convert(row.getMeeting().getTime(), "ms", "s").intValue()).append(",");
        buffer.append(Utils.convert(row.getIdle().getTime(), "ms", "s").intValue()).append(",");
        buffer.append(Utils.convert(row.getMeal().getTime(), "ms", "s").intValue()).append(",");
        buffer.append(Utils.convert(row.getBusy().getTime(), "ms", "s").intValue()).append(",");

        // s.total.time //buffer.append(Utils.convert(row.getOffline().getTime(), "ms", "s").intValue() + Utils.convert(row.getAvailable().getTime(), "ms", "s").intValue()).append(",");
        
        /*buffer.append(Utils.convert(row.getAvailable().getTime(), "ms", "s").intValue() 
                    + Utils.convert(row.getOnBreak().getTime(), "ms", "s").intValue()
                    + Utils.convert(row.getAway().getTime(), "ms", "s").intValue()
                    + Utils.convert(row.getOnQueue().getTime(), "ms", "s").intValue()
                    + Utils.convert(row.getMeal().getTime(), "ms", "s").intValue()
                    + Utils.convert(row.getBusy().getTime(), "ms", "s").intValue()).append(",");
        */
        
        buffer.append(Utils.convert(row.getAvailable().getTime()
                 .add(row.getOnBreak().getTime())
                 .add(row.getAway().getTime())
                 .add(row.getOnQueue().getTime())
                 .add(row.getTraining().getTime())
                 .add(row.getMeal().getTime())
                 .add(row.getBusy().getTime()), "ms", "s").intValue()).append(",");
                 
        
        buffer.append(Utils.convert(row.getOffline().getTime(), "ms", "s").intValue());
      }
    }
    
    return buffer.toString();
  }
}
