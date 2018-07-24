package com.cc.purecloud.client.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.cc.purecloud.util.Utils;

public class UserQueueAggregateMetricsRow implements Serializable {
  
  
  
  
  String interval = null;
  LocalDateTime dateIni = null;
  LocalDateTime dateEnd = null;
  String userName = null;
  CustomMetric hold = null;
  CustomMetric acw = null;
  CustomMetric handle = null;
  CustomMetric talk = null;
  CustomMetric alert = null;
  CustomMetric answer = null;
  
  public UserQueueAggregateMetricsRow() {
    hold = new CustomMetric();
    acw = new CustomMetric();
    handle = new CustomMetric();
    talk = new CustomMetric();
    alert = new CustomMetric();
    answer = new CustomMetric();
  }
  
  public String getInterval() {
    return interval;
  }
  public void setInterval(String interval) {
    this.interval = interval;
    String[] parts = Utils.parseInterval(interval);
    this.setDateIni(LocalDateTime.of(LocalDate.parse(parts[0]), LocalTime.parse(parts[1])));
    this.setDateEnd(LocalDateTime.of(LocalDate.parse(parts[2]), LocalTime.parse(parts[3])));
  }
  public LocalDateTime getDateIni() {
    return dateIni;
  }
  public void setDateIni(LocalDateTime dateIni) {
    this.dateIni = dateIni;
  }
  public LocalDateTime getDateEnd() {
    return dateEnd;
  }
  public void setDateEnd(LocalDateTime dateEnd) {
    this.dateEnd = dateEnd;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public CustomMetric getHold() {
    return hold;
  }

  public void setHold(CustomMetric hold) {
    this.hold = hold;
  }

  public CustomMetric getAcw() {
    return acw;
  }

  public void setAcw(CustomMetric acw) {
    this.acw = acw;
  }

  public CustomMetric getHandle() {
    return handle;
  }

  public void setHandle(CustomMetric handle) {
    this.handle = handle;
  }

  public CustomMetric getTalk() {
    return talk;
  }

  public void setTalk(CustomMetric talk) {
    this.talk = talk;
  }

  public CustomMetric getAlert() {
    return alert;
  }

  public void setAlert(CustomMetric alert) {
    this.alert = alert;
  }
  
  
  
  public CustomMetric getAnswer() {
    return answer;
  }

  public void setAnswer(CustomMetric answer) {
    this.answer = answer;
  }

  public String toString() {
    
    StringBuffer buffer = new StringBuffer();
    
    buffer.append(this.getInterval()).append(",");
    buffer.append(this.getUserName()).append(",");
    buffer.append("tACW: " + this.getAcw()).append(",");
    buffer.append("tAlert: " + this.getAlert()).append(",");
    buffer.append("tHandle: " + this.getHandle()).append(",");
    buffer.append("tTalk: " + this.getTalk()).append(",");
    buffer.append("tHold: " + this.getHold()).append(",");
    
    return buffer.toString();
    
  }
  
    
}
