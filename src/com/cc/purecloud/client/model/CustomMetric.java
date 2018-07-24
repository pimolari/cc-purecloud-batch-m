package com.cc.purecloud.client.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class CustomMetric implements Serializable {
  
  Long count = 0L;
  BigDecimal time = null;
  BigDecimal max = null;
  BigDecimal min = null;
  
  
  public CustomMetric() {
    this.time = BigDecimal.ZERO;
  }
  
  public CustomMetric(long count) {
    this.count = count;
    this.time = BigDecimal.ZERO;
  }
  
  public CustomMetric(long count, BigDecimal time) {
    this.count = count;
    this.time = time;
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public BigDecimal getTime() {
    return time;
  }

  public void setTime(BigDecimal time) {
    this.time = time;
  }

  public BigDecimal getMax() {
    return max;
  }

  public void setMax(BigDecimal max) {
    this.max = max;
  }

  public BigDecimal getMin() {
    return min;
  }

  public void setMin(BigDecimal min) {
    this.min = min;
  }
  
  
  
  
}
