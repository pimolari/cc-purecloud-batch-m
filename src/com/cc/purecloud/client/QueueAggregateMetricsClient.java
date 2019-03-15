package com.cc.purecloud.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import com.cc.purecloud.client.model.CustomQueue;
import com.cc.purecloud.client.model.CustomUser;
import com.cc.purecloud.client.model.QueueAggregateMetricsModel;
import com.cc.purecloud.client.model.QueueAggregateMetricsRow;
import com.cc.purecloud.client.model.UserQueueAggregateMetricsModel;
import com.cc.purecloud.client.model.UserQueueAggregateMetricsRow;
import com.cc.purecloud.client.model.util.CustomMetricUtil;
import com.google.gson.Gson;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.api.AnalyticsApi;
import com.mypurecloud.sdk.v2.model.AggregateDataContainer;
import com.mypurecloud.sdk.v2.model.AggregateMetricData;
import com.mypurecloud.sdk.v2.model.AggregateQueryResponse;
import com.mypurecloud.sdk.v2.model.AggregateViewData;
import com.mypurecloud.sdk.v2.model.AggregationQuery;
import com.mypurecloud.sdk.v2.model.AggregationRange;
import com.mypurecloud.sdk.v2.model.AnalyticsQueryClause;
import com.mypurecloud.sdk.v2.model.AnalyticsQueryFilter;
import com.mypurecloud.sdk.v2.model.AnalyticsQueryPredicate;
import com.mypurecloud.sdk.v2.model.AnalyticsQueryPredicate.DimensionEnum;
import com.mypurecloud.sdk.v2.model.AnalyticsQueryPredicate.OperatorEnum;
import com.mypurecloud.sdk.v2.model.AnalyticsView;
import com.mypurecloud.sdk.v2.model.StatisticalResponse;

public class QueueAggregateMetricsClient extends AbstractClient {
  
  private String interval = null;
  private List<CustomQueue> queues = null;
  private List<CustomUser> users = null;
  private QueueAggregateMetricsModel model = null;
  
  public QueueAggregateMetricsClient(String clientId, String clientSecret) {
    super(clientId, clientSecret);
  }
  
  
  
  public String getInterval() {
    return interval;
  }
  
  public void setInterval(String interval) {
    this.interval = interval;
  }
  
  public void setQueues(String queueString) {
    
    List<CustomQueue> queues = new ArrayList<CustomQueue>();
    //id,name;id,name
    
    if (queueString != null && !"".equals(queueString)) {
      
      StringTokenizer t = new StringTokenizer(queueString, ";");
      
      while (t.hasMoreTokens()) {
        String qString = t.nextToken();
        
        if (qString != null) {
          StringTokenizer tt = new StringTokenizer(qString, ",");
          
          String qId = tt.nextToken();
          String qName = tt.nextToken();
          
          queues.add(new CustomQueue(qId, qName));
        }
        
      }
      
      this.queues = queues;
    }
    
  }
  
  public void setUsers(String userString) {
    
    List<CustomUser> users = new ArrayList<CustomUser>();
    //id,name;id,name
    
    if (userString != null && !"".equals(userString)) {
      
      StringTokenizer t = new StringTokenizer(userString, ";");
      
      while (t.hasMoreTokens()) {
        String qString = t.nextToken();
        
        if (qString != null) {
          StringTokenizer tt = new StringTokenizer(qString, ",");
          
          String uId = tt.nextToken();
          String uName = tt.nextToken();
          
          users.add(new CustomUser(uId, uName));
        }
        
      }
      
      this.users = users;
    }
    
  }
  
  public QueueAggregateMetricsModel getQueueAggregateMetrics() {
    QueueAggregateMetricsModel model = new QueueAggregateMetricsModel();
    System.out.println("Queue Aggregated Metrics Client is now executing");
    System.out.println("Retrieving queue metrics");
    
    
    for (CustomQueue queue : this.getQueues()) {
      //System.out.println(">> queue " + queue.getName());
      model.append(this.getQueueAggregateMetrics(queue));
    }
    
    return model;
  }
  
  public UserQueueAggregateMetricsModel getUserQueueAggregateMetrics() {
    UserQueueAggregateMetricsModel model = new UserQueueAggregateMetricsModel();
    System.out.println("Queue Aggregated Metrics Client is now executing");
    System.out.println("Retrieving queue inbound metrics per user");
    
    List<CustomQueue> queueList = new ArrayList<CustomQueue>();
    for (CustomQueue queue : this.getQueues()) {
      queueList.add(queue);
    }
    
    for (CustomUser user : this.getUsers()) {
      //System.out.println(">> user " + user.getName());
      model.append(this.getUserQueueAggregateMetrics(user, queueList));
    }
    
    return model;
  }
  
  
  private boolean checkQueues() {
    QueueClient client = new QueueClient(this.getClientId(), this.getClientSecret());
    if (this.queues != null && this.queues.size() > 0) {
      for (CustomQueue queue : queues) {
        if (queue.getId() != null && !"".equals(queue.getId())) {
          if (queue.getName() == null || "".equals(queue.getName())) {
            CustomQueue q = client.get(queue.getId()); // TBD in QuequeClient
            queue.setName(q.getName());
          }
        }
      }
      
      return true;
    }
    
    return false;
  }
  
  private boolean checkUsers() {
    UserClient client = new UserClient(this.getClientId(), this.getClientSecret());
    if (this.users != null && this.users.size() > 0) {
      for (CustomUser user : users) {
        if (user.getId() != null && !"".equals(user.getId())) {
          if (user.getName() == null || "".equals(user.getName())) {
            CustomUser q = client.get(user.getId()); // TBD in 
            user.setName(q.getName());
          }
        }
      }
      
      return true;
    }
    
    return false;
  }
  
  public List<CustomQueue> getQueues() {
    
    if (this.checkQueues()) {
      return this.queues;
    } else {
      return new QueueClient(this.getClientId(), this.getClientSecret()).list();
    } 
  }
  
public List<CustomUser> getUsers() {
    
    if (this.checkUsers()) {
      return this.users;
    } else {
      return new UserClient(this.getClientId(), this.getClientSecret()).list();
    } 
  }
  
  public QueueAggregateMetricsModel getQueueAggregateMetrics(CustomQueue queue) {
    
    //System.out.println("Metrics for queue " + queue.getName() + " : " + queue.getId());
    QueueAggregateMetricsModel model = new QueueAggregateMetricsModel();
    
    AnalyticsApi apiInstance = new AnalyticsApi();
    AggregationQuery body = new AggregationQuery(); // AggregationQuery | query
    
    //System.out.println("2018-02-27T00:00:00/2018-02-27T23:59:59");
    //body.setInterval("2018-02-27T00:00:00/2018-02-27T23:59:59");
    body.setInterval(this.interval);
    body.setTimeZone("Europe/Madrid");
    body.setGranularity("PT30M");
    body.groupBy(Arrays.asList(AggregationQuery.GroupByEnum.QUEUEID));
    body.setMetrics(Arrays.asList(AggregationQuery.MetricsEnum.TTALK, 
        AggregationQuery.MetricsEnum.TTALKCOMPLETE,
        AggregationQuery.MetricsEnum.NOFFERED, 
        AggregationQuery.MetricsEnum.TABANDON,
        AggregationQuery.MetricsEnum.TACD,
        AggregationQuery.MetricsEnum.TACW,
        AggregationQuery.MetricsEnum.THELD,
        AggregationQuery.MetricsEnum.TAGENTRESPONSETIME,
        AggregationQuery.MetricsEnum.TIVR,
        AggregationQuery.MetricsEnum.THANDLE,
        AggregationQuery.MetricsEnum.TANSWERED,
        AggregationQuery.MetricsEnum.TAGENTRESPONSETIME,
        AggregationQuery.MetricsEnum.NOUTBOUNDATTEMPTED,
        AggregationQuery.MetricsEnum.NOUTBOUNDABANDONED,
        AggregationQuery.MetricsEnum.TALERT));
    
    AnalyticsQueryFilter filter = new AnalyticsQueryFilter();
    filter.setType(AnalyticsQueryFilter.TypeEnum.AND);
    
    List<AnalyticsQueryClause> clauses = new ArrayList<AnalyticsQueryClause>();

    AnalyticsQueryClause clause = new AnalyticsQueryClause();
    clause.setType(AnalyticsQueryClause.TypeEnum.AND);
    
    // Clauses
    List<AnalyticsQueryPredicate> predicates = new ArrayList<AnalyticsQueryPredicate>();
    
    AnalyticsQueryPredicate queuePredicate = new AnalyticsQueryPredicate();
    queuePredicate.setType(AnalyticsQueryPredicate.TypeEnum.DIMENSION);
    queuePredicate.setDimension(DimensionEnum.QUEUEID);
    queuePredicate.setOperator(OperatorEnum.MATCHES);
    queuePredicate.setValue(queue.getId()); 
    
    predicates.add(queuePredicate);
    
    AnalyticsQueryPredicate directionPredicate = new AnalyticsQueryPredicate();
    directionPredicate.setType(AnalyticsQueryPredicate.TypeEnum.DIMENSION);
    directionPredicate.setDimension(DimensionEnum.DIRECTION);
    directionPredicate.setOperator(OperatorEnum.MATCHES);
    directionPredicate.setValue("inbound"); 
    predicates.add(directionPredicate);
    
    clause.setPredicates(predicates);
    clauses.add(clause);
    
    //filter.setPredicates(Arrays.asList(predicate));
    filter.setClauses(clauses);
    body.setFilter(filter);
    
    AnalyticsView view1 = new AnalyticsView();
    view1.setTarget(AggregationQuery.MetricsEnum.TABANDON.toString());
    view1.setFunction(AnalyticsView.FunctionEnum.RANGEBOUND);
    view1.setName("tTechnicalAbandon");
    AggregationRange range1 = new AggregationRange();
    range1.setLt(new BigDecimal(5000));
    view1.setRange(range1);
    
    AnalyticsView view2 = new AnalyticsView();
    view2.setTarget(AggregationQuery.MetricsEnum.TANSWERED.toString());
    view2.setFunction(AnalyticsView.FunctionEnum.RANGEBOUND);
    view2.setName("tAnswer15secs");
    AggregationRange range2 = new AggregationRange();
    range2.setLt(new BigDecimal(15000));
    view2.setRange(range2);
    
    body.setViews(Arrays.asList(view1, view2));


    
    try {
      //AggregateQueryResponse result = apiInstance.postConversationsAggregatesQuery(body); // v1 58.0
      AggregateQueryResponse result = apiInstance.postAnalyticsConversationsAggregatesQuery(body);
      
      for (AggregateDataContainer container : result.getResults()) {
        //System.out.println("    > " + container.getGroup().get("mediaType")); // + " en cola " + container.getGroup().get("queueId"));
        
        for (StatisticalResponse stats : container.getData()) {
          
          QueueAggregateMetricsRow row = new QueueAggregateMetricsRow();
          row.setQueueId(container.getGroup().get("queueId"));
          row.setQueueName(queue.getName());
          row.setInterval(stats.getInterval());
          
          String output = "    ";
          output += stats.getInterval();
          output += " :";
          
          for (AggregateMetricData smetric : stats.getMetrics()) {
            output += ": " + smetric.getMetric().name();
            
            switch(smetric.getMetric().name().toUpperCase()) {
              
              case "TABANDON": // Abandoned ok
                row.getAbandon().setCount(smetric.getStats().getCount());
                row.getAbandon().setTime(smetric.getStats().getSum());
                break;
              case "TANSWERED": // Enqueued calls answered by an agent ok
                row.getAnswer().setCount(smetric.getStats().getCount());
                row.getAnswer().setTime(smetric.getStats().getSum());
                break;
              case "TACD": // ACD Time ok
                row.getAcd().setCount(smetric.getStats().getCount());
                row.getAcd().setTime(smetric.getStats().getSum());
                break;
              case "TACW": // ACD Time ok
                row.getAcw().setCount(smetric.getStats().getCount());
                row.getAcw().setTime(smetric.getStats().getSum());
                break;
              case "NOFFERED": // Offered, equivalent to incoming calls in queue ok
                row.getOffered().setCount(smetric.getStats().getCount());
                break;
              case "THANDLE": // Handle time ok
                row.getHandle().setCount(smetric.getStats().getCount());
                row.getHandle().setTime(smetric.getStats().getSum());
                break;
              case "TTALK": //Talking time ok
                row.getTalk().setCount(smetric.getStats().getCount());
                row.getTalk().setTime(smetric.getStats().getSum());
                break;
              case "THELD": //Hold time ok
                row.getHold().setCount(smetric.getStats().getCount());
                row.getHold().setTime(smetric.getStats().getSum());
                break;
              case "TACCEPTED": //Hold time ok
                row.getHold().setCount(smetric.getStats().getCount());
                row.getHold().setTime(smetric.getStats().getSum());
                break;
              case "TTALKCOMPLETE":
                row.getTalkComplete().setCount(smetric.getStats().getCount());
                row.getTalkComplete().setTime(smetric.getStats().getSum());
                break;
              case "TAGENTRESPONSETIME":
                //row.getAgentResponseTime().setCount(smetric.getStats().getCount());
                row.getAgentResponseTime().setTime(smetric.getStats().getSum());
                break;
              case "TIVR":
                row.getIvr().setCount(smetric.getStats().getCount());
                row.getIvr().setTime(smetric.getStats().getSum());
                break;
              case "NOUTBOUNDATTEMPTED":
                row.getOutboundAttempt().setCount(smetric.getStats().getCount());
                row.getOutboundAttempt().setTime(smetric.getStats().getSum());
                break;
              case "NOUTBOUNDABANDONED":
                row.getOutboundAbandon().setCount(smetric.getStats().getCount());
                row.getOutboundAbandon().setTime(smetric.getStats().getSum());
                break;
              case "TALERT":
                row.getAlertTime().setTime(smetric.getStats().getSum());
                row.getAlertTime().setCount(smetric.getStats().getCount());
                row.setAlertAverageTime(CustomMetricUtil.averageTime(row.getAlertTime()));
                break;
                
                
            }
            
            
            
            /*if (smetric.getStats().getSum() != null) {
              output += ": " + smetric.getStats().getSum();  
            } else {
              output += ": " + smetric.getStats().getCount();
            }*/
            
            if (smetric.getStats().getSum() != null) {
              output += ": s:" + smetric.getStats().getSum();  
            } 
            if (smetric.getStats().getCount() != null) {
              output += ": c:" + smetric.getStats().getCount();
            }
            
            /*output += "/target: " + smetric.getStats().getTarget();
            output += "/current: " + smetric.getStats().getCurrent();
            output += "/min: " + smetric.getStats().getMin();
            output += "/max: " + smetric.getStats().getMax();
            output += "/ratio: " + smetric.getStats().getRatio();
            output += "/denominator: " + smetric.getStats().getDenominator();
             */
          }
          
          
          for (AggregateViewData sview : stats.getViews()) {
            switch(sview.getName()) {
              case "tTechnicalAbandon":
                row.getAbandon5s().setCount(sview.getStats().getCount());
                row.getAbandon5s().setTime(sview.getStats().getSum());
                break;
              case "tAnswer15secs":
                row.getAnswer15s().setCount(sview.getStats().getCount());
                row.getAnswer15s().setTime(sview.getStats().getSum());
                break;
            }
            //output += ": " + sview.getName();
            //output += ": " + .getStats().getCount();
            
          }
          
          model.add(row);
          //System.out.println(new Gson().toJson(row));
          //System.out.println("   " + output);
        }
        
      }
      
      //System.out.println(model.size() + " rows in queue " + queue.getName());
      return model;
      
      //System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AnalyticsApi#postConversationsAggregatesQuery");
      e.printStackTrace();
    } catch(IOException e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  
  public UserQueueAggregateMetricsModel getUserQueueAggregateMetrics(CustomUser user, List<CustomQueue> queueList) {
  
    //System.out.println("Metrics for queue " + queue.getName() + " : " + queue.getId());
    UserQueueAggregateMetricsModel model = new UserQueueAggregateMetricsModel();
    
    AnalyticsApi apiInstance = new AnalyticsApi();
    AggregationQuery body = new AggregationQuery(); // AggregationQuery | query
    
    //System.out.println("2018-02-27T00:00:00/2018-02-27T23:59:59");
    //body.setInterval("2018-02-27T00:00:00/2018-02-27T23:59:59");
    body.setInterval(this.interval);
    body.setGranularity("P1D");
    body.groupBy(Arrays.asList(AggregationQuery.GroupByEnum.QUEUEID));
    body.setMetrics(Arrays.asList(AggregationQuery.MetricsEnum.TTALK, 
        AggregationQuery.MetricsEnum.TACW,
        AggregationQuery.MetricsEnum.THELD,
        AggregationQuery.MetricsEnum.THANDLE,
        AggregationQuery.MetricsEnum.TALERT,
        AggregationQuery.MetricsEnum.TANSWERED));
    
    AnalyticsQueryFilter filter = new AnalyticsQueryFilter();
    filter.setType(AnalyticsQueryFilter.TypeEnum.OR);
    
    List<AnalyticsQueryClause> clauses = new ArrayList<AnalyticsQueryClause>();
    
    for (CustomQueue queue : queueList) {
      AnalyticsQueryClause clause = new AnalyticsQueryClause();
      clause.setType(AnalyticsQueryClause.TypeEnum.AND);
      
      List<AnalyticsQueryPredicate> predicates = new ArrayList<AnalyticsQueryPredicate>();
      AnalyticsQueryPredicate queuePredicate = new AnalyticsQueryPredicate();
      queuePredicate.setType(AnalyticsQueryPredicate.TypeEnum.DIMENSION);
      queuePredicate.setDimension(DimensionEnum.QUEUEID);
      queuePredicate.setOperator(OperatorEnum.MATCHES);
      queuePredicate.setValue(queue.getId()); 
      predicates.add(queuePredicate);
      
      AnalyticsQueryPredicate directionPredicate = new AnalyticsQueryPredicate();
      directionPredicate.setType(AnalyticsQueryPredicate.TypeEnum.DIMENSION);
      directionPredicate.setDimension(DimensionEnum.DIRECTION);
      directionPredicate.setOperator(OperatorEnum.MATCHES);
      directionPredicate.setValue("inbound"); 
      predicates.add(directionPredicate);
      
      AnalyticsQueryPredicate userPredicate = new AnalyticsQueryPredicate();
      userPredicate.setType(AnalyticsQueryPredicate.TypeEnum.DIMENSION);
      userPredicate.setDimension(DimensionEnum.USERID);
      userPredicate.setOperator(OperatorEnum.MATCHES);
      userPredicate.setValue(user.getId()); 
      predicates.add(userPredicate);
      
      clause.setPredicates(predicates);
      clauses.add(clause);
    }
    
    filter.setClauses(clauses);
    
    /*AnalyticsQueryPredicate predicate = new AnalyticsQueryPredicate();
    predicate.setType(AnalyticsQueryPredicate.TypeEnum.DIMENSION);
    predicate.setDimension(DimensionEnum.USERID);
    predicate.setOperator(OperatorEnum.MATCHES);
    predicate.setValue(user.getId()); 
        
    filter.setPredicates(Arrays.asList(predicate));*/
    body.setFilter(filter);
    
    
    try {
      AggregateQueryResponse result = apiInstance.postAnalyticsConversationsAggregatesQuery(body);
            
      for (AggregateDataContainer container : result.getResults()) {
        //System.out.println("    > " + container.getGroup().get("mediaType")); // + " en cola " + container.getGroup().get("queueId"));
        
        for (StatisticalResponse stats : container.getData()) {
          
          UserQueueAggregateMetricsRow row = new UserQueueAggregateMetricsRow();
          row.setUserName(user.getName());
          row.setInterval(stats.getInterval());
          
          String output = "    ";
          output += stats.getInterval();
          output += " :";
          
          for (AggregateMetricData smetric : stats.getMetrics()) {
            output += ": " + smetric.getMetric().name();
            
            switch(smetric.getMetric().name().toUpperCase()) {
              
              case "TACW": // ACD Time ok
                row.getAcw().setCount(smetric.getStats().getCount());
                row.getAcw().setTime(smetric.getStats().getSum());
                break;
              case "THANDLE": // Handle time ok
                row.getHandle().setCount(smetric.getStats().getCount());
                row.getHandle().setTime(smetric.getStats().getSum());
                break;
              case "TTALK": //Talking time ok
                row.getTalk().setCount(smetric.getStats().getCount());
                row.getTalk().setTime(smetric.getStats().getSum());
                break;
              case "THELD": //Hold time ok
                row.getHold().setCount(smetric.getStats().getCount());
                row.getHold().setTime(smetric.getStats().getSum());
                break;
              case "TALERT":
                row.getAlert().setTime(smetric.getStats().getSum());
                row.getAlert().setCount(smetric.getStats().getCount());
                break;
              case "TANSWERED":
                row.getAnswer().setTime(smetric.getStats().getSum());
                row.getAnswer().setCount(smetric.getStats().getCount());
                break;
                
                
            }

            if (smetric.getStats().getSum() != null) {
              output += ": s:" + smetric.getStats().getSum();  
            } 
            if (smetric.getStats().getCount() != null) {
              output += ": c:" + smetric.getStats().getCount();
            }
          }
          
          
          model.add(row);
        }
      }
      
      return model;
      
      //System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling AnalyticsApi#postConversationsAggregatesQuery");
      e.printStackTrace();
    } catch(IOException e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
}
