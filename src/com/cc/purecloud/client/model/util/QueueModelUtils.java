package com.cc.purecloud.client.model.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.cc.purecloud.client.model.QueueAggregateMetricsModel;
import com.cc.purecloud.client.model.QueueAggregateMetricsRow;

public class QueueModelUtils {
  
  
  public static List<LocalDate> getDates(QueueAggregateMetricsModel model) {
    
    List<LocalDate> dates = new ArrayList<LocalDate>();
    
    
    for (QueueAggregateMetricsRow row : model) {
      //LocalDateTime l = new LocalDateTime();
      
      if (!dates.contains(row.getDateIni().toLocalDate()))
        dates.add(row.getDateIni().toLocalDate());
    }
    
    return dates;
  }
  
  public static QueueAggregateMetricsModel aggregateModelDailyData(QueueAggregateMetricsModel model) {
    QueueAggregateMetricsModel nModel = new QueueAggregateMetricsModel();
    //System.out.println("Aggregating model by dates ...");
    
    List<LocalDate> dates = QueueModelUtils.getDates(model);
    
    
    for (LocalDate date : dates) {
      
      QueueAggregateMetricsRow nRow = new QueueAggregateMetricsRow();
      
      //System.out.println("---- " + date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
      for (QueueAggregateMetricsRow row : model) {
        //System.out.print("  " + row.getDateIni().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " ::  " + row.getQueueName() + " ::  " + row.getOffered().getCount());
        if (row.getDateIni().toLocalDate().isEqual(date)) {
          nRow.setDateIni(row.getDateIni());
          nRow.setDateIni(row.getDateEnd());
          nRow.setInterval(row.getInterval());
          nRow.setOffered(CustomMetricUtil.sum(row.getOffered(), nRow.getOffered()));
          nRow.setAbandon(CustomMetricUtil.sum(row.getAbandon(), nRow.getAbandon()));
          nRow.setAlertTime(CustomMetricUtil.sum(row.getAlertTime(), nRow.getAlertTime()));
          nRow.setTalk(CustomMetricUtil.sum(row.getTalk(), nRow.getTalk()));
          nRow.setAnswer(CustomMetricUtil.sum(row.getAnswer(), nRow.getAnswer()));
          nRow.setAnswer15s(CustomMetricUtil.sum(row.getAnswer15s(), nRow.getAnswer15s()));
        }
        //System.out.println("");
      }
      
      nModel.add(nRow);
      
    }

    /*System.out.println("Queue aggregation result...");
    for (QueueAggregateMetricsRow r : nModel) {
      System.out.println(" >> " + r.getDateIni().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " >> " + r.getOffered().getCount() + " >> " + r.getQueueName() );
    }*/
    
    return nModel;
  }
  
  
  
}
