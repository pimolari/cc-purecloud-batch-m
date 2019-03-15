package com.cc.purecloud.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;


public class Utils {
  
  
  public static String[] parseCommaSeparatedString(String value) {
    
    
    if (value != null && !"".equals(value)) {
      String[] values = value.split(",");
      return values;
    }
    
    return null;
    
  }
  
  
  // TODO: Refactor to optimize 
  public static String[] parseInterval(String value) {
    
    String[] parts = new String[4];
    
    StringTokenizer t = new StringTokenizer(value, "/");
    
    StringTokenizer start = new StringTokenizer(t.nextToken(), "T");
    parts[0] = start.nextToken();
    parts[1] = start.nextToken().substring(0, 8);
    StringTokenizer end = new StringTokenizer(t.nextToken(), "T");
    parts[2] = end.nextToken();
    parts[3] = end.nextToken().substring(0, 8);
    
    return parts;
    
  }
  
  public static BigDecimal convert(BigDecimal value, String sourceUnit, String targetUnit) {
    
    if (sourceUnit == null || sourceUnit.equals("")) {
      sourceUnit = "s";
    }
    
    if (targetUnit != null && !targetUnit.equals("")) {
      if (targetUnit.equals("s") && sourceUnit.equals("ms")) {
        return (value.divide(new BigDecimal(1000), RoundingMode.HALF_EVEN));
      }
    }
    return value;
  }
  
  public static LocalDateTime adjustDate(LocalDateTime value, Long offset) {
    
    //System.out.print("DateTime: " + value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")) + " offset " + offset);
    value = value.plusHours(offset);
    //System.out.println(" >> " + value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
    return value;
  }
  
  public static long convertToLong(String value) {
    
    long v = 0;
    
    try {
      v = Long.parseLong(value);
    } catch(NumberFormatException e) {
      System.out.println("Could not understand value " + value + ". Applying 0");
    }
    
    return v;
  }
  
  public static void main(String[] args) {
    
    BigDecimal x = new BigDecimal(999999999);
    System.out.println(x.intValue());
    System.out.println(x.divide(new BigDecimal(1000)));
    System.out.println(Utils.convert(x, "ms", "s"));
    
    Utils.adjustDate(LocalDateTime.now(), -5l);
    
  }
}
