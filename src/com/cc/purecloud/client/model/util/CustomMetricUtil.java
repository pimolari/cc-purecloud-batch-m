package com.cc.purecloud.client.model.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.cc.purecloud.client.model.CustomMetric;

public class CustomMetricUtil {

  
  public static CustomMetric sum(CustomMetric a, CustomMetric b) {
    
    CustomMetric c = new CustomMetric();
    
    c.setCount(a.getCount() + b.getCount());
    c.setTime(a.getTime().add(b.getTime()));
    //c.setTime(Utils.convert(a.getTime(), "ms", "s").add(Utils.convert(b.getTime(), "ms", "s")));
    return c;
    
  }
  
public static CustomMetric averageTime(CustomMetric a) {
    
    CustomMetric c = new CustomMetric();
    //System.out.println(" Average time for " + a.getTime().intValue() + "/" + a.getCount());
    c.setCount(a.getCount());
    c.setTime(a.getTime().divide(new BigDecimal(c.getCount()), RoundingMode.DOWN));
    //System.out.println("    Result " + a.getTime().intValue() + "/" + a.getCount() + " = " + c.getTime().doubleValue());
    return c;
    
  }
}
