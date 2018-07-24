package com.cc.purecloud.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.StringTokenizer;

public class Interval {
  
  private LocalDate iniDate = null;
  private LocalTime iniTime = null;
  private LocalDate endDate = null;
  private LocalTime endTime = null;
  
  public Interval() {
    
  }
  
  public Interval(String interval) {
    this.fromString(interval);
  }
  
  public Interval(String iniDateTime, String endDateTime) {
    
  }
  
  public void fromString(String interval) {
    
    if (interval != null) {
      try {
        StringTokenizer st = new StringTokenizer(interval, "/");
        String iniDateTime = st.nextToken();
        String endDateTime = st.nextToken();
        
        this.iniDate = LocalDate.parse(iniDateTime.substring(0, iniDateTime.indexOf("T")));
        this.iniTime = (iniDateTime.indexOf("T") >= 0?LocalTime.parse(iniDateTime.substring(iniDateTime.indexOf("T") + 1)):null);
        this.endDate = LocalDate.parse(endDateTime.substring(0, endDateTime.indexOf("T")));
        this.endTime = LocalTime.parse(endDateTime.substring(endDateTime.indexOf("T") + 1));
      } catch(Exception e) {
        
      }
    } 
    
  }
  
  public String toString() {
    
    return (this.iniDate!=null?this.iniDate.toString() + (this.iniTime!=null?"T"+this.iniTime.toString():""):"") 
         + (this.endDate!=null?"/"+this.endDate.toString() + (this.endTime!=null?"T"+this.endTime.toString():""):"");
  }
  
  public static void main(String[] args) {
    
    String s = "2018-03-01T00:00:00/2018-03-02T13:01:02";
    Interval interval = new Interval(s);
    System.out.println("x" + interval.toString());
    s = "2018-03-01/2018-03-02T13:01:02";
    interval = new Interval(s);
    System.out.println("y" + interval.toString());
    s = "2018-03-01/2018-03-02";
    interval = new Interval(s);
    System.out.println("x" + interval.toString());
    s = "2018-03-01";
    interval = new Interval(s);
    System.out.println("x" + interval.toString());
    s = "2018-03-01T01:03:05";
    interval = new Interval(s);
    System.out.println("x" + interval.toString());
  }
}
