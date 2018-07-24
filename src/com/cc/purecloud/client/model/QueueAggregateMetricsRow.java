package com.cc.purecloud.client.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.cc.purecloud.util.Utils;

public class QueueAggregateMetricsRow implements Serializable {
  
  
  
  
  String interval = null;
  LocalDateTime dateIni = null;
  LocalDateTime dateEnd = null;
  String queueName = null;
  String queueId = null;
  
  CustomMetric incoming = null;
  CustomMetric acd = null;
  CustomMetric abandon = null;
  CustomMetric abandon5s = null;
  //Metric abandon15s = null;
  CustomMetric hold = null;
  CustomMetric acw = null;
  CustomMetric answer = null;
  CustomMetric answer15s = null;
  CustomMetric handle = null;
  CustomMetric accepted = null;
  CustomMetric offered = null;
  CustomMetric talk = null;
  CustomMetric talkComplete = null;
  CustomMetric agentResponseTime = null;
  CustomMetric ivr = null;
  CustomMetric outboundAttempt = null;
  CustomMetric outboundAbandon = null;
  CustomMetric alertTime = null;
  CustomMetric alertAverageTime = null;
  
  public QueueAggregateMetricsRow() {
    incoming = new CustomMetric();
    acd = new CustomMetric();
    abandon = new CustomMetric();
    abandon5s = new CustomMetric();
    //abandon15s = new Metric();
    hold = new CustomMetric();
    acw = new CustomMetric();
    answer = new CustomMetric();
    answer15s = new CustomMetric();
    handle = new CustomMetric();
    accepted = new CustomMetric();
    offered = new CustomMetric();
    talk = new CustomMetric();
    talkComplete = new CustomMetric();
    agentResponseTime = new CustomMetric();
    ivr = new CustomMetric();
    outboundAttempt = new CustomMetric();
    outboundAbandon = new CustomMetric();
    alertTime = new CustomMetric();
    alertAverageTime = new CustomMetric();
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
  public String getQueueName() {
    return queueName;
  }
  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }
  public String getQueueId() {
    return queueId;
  }
  public void setQueueId(String queueId) {
    this.queueId = queueId;
  }
  public CustomMetric getIncoming() {
    return incoming;
  }
  public void setIncoming(CustomMetric incoming) {
    this.incoming = incoming;
  }
  public CustomMetric getAcd() {
    return acd;
  }
  public void setAcd(CustomMetric acd) {
    this.acd = acd;
  }
  public CustomMetric getAbandon() {
    return abandon;
  }
  public void setAbandon(CustomMetric abandon) {
    this.abandon = abandon;
  }
  public CustomMetric getAbandon5s() {
    return abandon5s;
  }
  public void setAbandon5s(CustomMetric abandon5s) {
    this.abandon5s = abandon5s;
  }
  /*public Metric getAbandon15s() {
    return abandon15s;
  }
  public void setAbandon15s(Metric abandon15s) {
    this.abandon15s = abandon15s;
  }*/
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
  public CustomMetric getAnswer() {
    return answer;
  }
  public void setAnswer(CustomMetric answer) {
    this.answer = answer;
  }
  public CustomMetric getAnswer15s() {
    return answer15s;
  }
  public void setAnswer15s(CustomMetric answer15s) {
    this.answer15s = answer15s;
  }
  public CustomMetric getHandle() {
    return handle;
  }
  public void setHandle(CustomMetric handle) {
    this.handle = handle;
  }
  public CustomMetric getAccepted() {
    return accepted;
  }
  public void setAccepted(CustomMetric accepted) {
    this.accepted = accepted;
  }
  public CustomMetric getOffered() {
    return offered;
  }
  public void setOffered(CustomMetric offered) {
    this.offered = offered;
  }
  public CustomMetric getTalk() {
    return talk;
  }
  public void setTalk(CustomMetric talk) {
    this.talk = talk;
  }

  public CustomMetric getTalkComplete() {
    return talkComplete;
  }

  public void setTalkComplete(CustomMetric talkComplete) {
    this.talkComplete = talkComplete;
  }

  public CustomMetric getAgentResponseTime() {
    return agentResponseTime;
  }

  public void setAgentResponseTime(CustomMetric agentResponseTime) {
    this.agentResponseTime = agentResponseTime;
  }

  public CustomMetric getIvr() {
    return ivr;
  }

  public void setIvr(CustomMetric ivr) {
    this.ivr = ivr;
  }

  public CustomMetric getOutboundAttempt() {
    return outboundAttempt;
  }

  public void setOutboundAttempt(CustomMetric outboundAttempt) {
    this.outboundAttempt = outboundAttempt;
  }

  public CustomMetric getOutboundAbandon() {
    return outboundAbandon;
  }

  public void setOutboundAbandon(CustomMetric outboundAbandon) {
    this.outboundAbandon = outboundAbandon;
  }

  public CustomMetric getAlertTime() {
    return alertTime;
  }

  public void setAlertTime(CustomMetric alertTime) {
    this.alertTime = alertTime;
  }

  public CustomMetric getAlertAverageTime() {
    return alertAverageTime;
  }

  public void setAlertAverageTime(CustomMetric alertAverageTime) {
    this.alertAverageTime = alertAverageTime;
  }
  
  
    
}
