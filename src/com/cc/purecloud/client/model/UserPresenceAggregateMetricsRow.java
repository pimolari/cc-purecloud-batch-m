package com.cc.purecloud.client.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cc.purecloud.client.model.util.CustomMetricUtil;
import com.cc.purecloud.util.Utils;

public class UserPresenceAggregateMetricsRow implements Serializable {
  
  
  
  String type = null;
  String interval = null;
  LocalDateTime dateIni = null;
  LocalDateTime dateEnd = null;
  String userName = null;
  String userId = null;
  CustomMetric offline = null;
  CustomMetric available = null;
  CustomMetric onBreak = null;
  CustomMetric away = null;
  CustomMetric onQueue = null;
  CustomMetric meal = null;
  CustomMetric busy = null;
  CustomMetric meeting = null;
  CustomMetric idle = null;
  
  CustomMetric totalOnline = null;
  CustomMetric totalOffline = null;
  
  Map<String, CustomMetric> data = null;
  
  public UserPresenceAggregateMetricsRow() {
    init();
    
  }
  
  public UserPresenceAggregateMetricsRow(String type) {
    init();
    this.type = type;
  }
  
  public UserPresenceAggregateMetricsRow(List<CustomPresenceDefinition> presenceDefinitions) {
    init();
    // Initializes data map based on the the userpresence list provided with empty metrics
    for (CustomPresenceDefinition presenceDefinition : presenceDefinitions) {
      data.put(presenceDefinition.getId(), new CustomMetric(0L));
    }
  }
  
  public UserPresenceAggregateMetricsRow(String type, List<CustomPresenceDefinition> presenceDefinitions) {
    init();
    this.type = type;
    // Initializes data map based on the the userpresence list provided with empty metrics
    for (CustomPresenceDefinition presenceDefinition : presenceDefinitions) {
      data.put(presenceDefinition.getId(), new CustomMetric(0L));
    }
  }
  
  private void init() {
    type = new String("interval");
    offline = new CustomMetric();
    available = new CustomMetric();
    onBreak = new CustomMetric();
    away = new CustomMetric();
    onQueue = new CustomMetric();
    meal = new CustomMetric();
    busy = new CustomMetric();
    meeting = new CustomMetric();
    idle = new CustomMetric();
    totalOnline = new CustomMetric();
    totalOffline = new CustomMetric();
    data = new HashMap<String, CustomMetric>();
  }
  

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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

  public Map<String, CustomMetric> getData() {
    return data;
  }

  public void setData(Map<String, CustomMetric> data) {
    this.data = data;
  }

  public String getUserName() {
    return userName;
  }


  public void setUserName(String userName) {
    this.userName = userName;
  }


  public String getUserId() {
    return userId;
  }


  public void setUserId(String userId) {
    this.userId = userId;
  }
  
  
  
  public CustomMetric getOffline() {
    return offline;
  }

  public void setOffline(CustomMetric offline) {
    this.offline = offline;
  }

  public CustomMetric getAvailable() {
    return available;
  }

  public void setAvailable(CustomMetric available) {
    this.available = available;
  }
  
  
  
  public CustomMetric getOnBreak() {
    return onBreak;
  }

  public void setOnBreak(CustomMetric onBreak) {
    this.onBreak = onBreak;
  }

  public CustomMetric getAway() {
    return away;
  }

  public void setAway(CustomMetric away) {
    this.away = away;
  }

  public CustomMetric getOnQueue() {
    return onQueue;
  }

  public void setOnQueue(CustomMetric onQueue) {
    this.onQueue = onQueue;
  }

  public CustomMetric getMeal() {
    return meal;
  }

  public void setMeal(CustomMetric meal) {
    this.meal = meal;
  }

  public CustomMetric getBusy() {
    return busy;
  }

  public void setBusy(CustomMetric busy) {
    this.busy = busy;
  }
  
  
  
  public CustomMetric getMeeting() {
    return meeting;
  }

  public void setMeeting(CustomMetric meeting) {
    this.meeting = meeting;
  }

  public CustomMetric getIdle() {
    return idle;
  }

  public void setIdle(CustomMetric idle) {
    this.idle = idle;
  }

  public CustomMetric getTotalOnline() {
    return totalOnline;
  }

  public void setTotalOnline(CustomMetric totalOnline) {
    this.totalOnline = totalOnline;
  }

  public CustomMetric getTotalOffline() {
    return totalOffline;
  }

  public void setTotalOffline(CustomMetric totalOffline) {
    this.totalOffline = totalOffline;
  }

  public void sumData(String key, CustomMetric metric) {
    
    CustomMetric source = this.getData(key);
    this.setData(key, CustomMetricUtil.sum(source, metric));
  }
  
  public void setData(String key, CustomMetric metric) {
    CustomMetric m = this.data.get(key);
    
    if (metric != null) {
      if (m == null) {
        System.err.println("Warning: Presence Definition with id " + key + " not retrieved in initial presence query!");
      } else {
        m.setTime(metric.getTime());
        m.setCount(metric.getCount());
      }
    }
  }
  
  public CustomMetric getData(String key) {
    return this.data.get(key);
  }
  
  public void consolidate() {
    //this.setTotalOnline(this.getAvailable().getTime()
                   //.sum(this.getAway().getTime()
                   //.sum(this.getBusy().getTime());
  }
  
  public class UserPresenceRow {
    
    private String id;
    private String desc;
    private CustomMetric metric;
    
    public UserPresenceRow() {
      this.metric = new CustomMetric();
    }
    
    public UserPresenceRow(String id, String desc, BigDecimal time) {
      this.id = id;
      this.desc = desc;
      this.metric = new CustomMetric();
      this.metric.setTime(time);
    }
    
    public UserPresenceRow(String id, String desc, CustomMetric metric) {
      this.id = id;
      this.desc = desc;
      this.metric = metric;
    }
    
    public String getId() {
      return id;
    }
    public void setId(String id) {
      this.id = id;
    }
    public String getDesc() {
      return desc;
    }
    public void setDesc(String desc) {
      this.desc = desc;
    }
    public CustomMetric getMetric() {
      return metric;
    }
    public void setMetric(CustomMetric metric) {
      this.metric = metric;
    }
  }

 
  
    
}
