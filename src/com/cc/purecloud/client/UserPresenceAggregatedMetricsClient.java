package com.cc.purecloud.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.cc.purecloud.client.model.CustomMetric;
import com.cc.purecloud.client.model.CustomPresenceDefinition;
import com.cc.purecloud.client.model.CustomUser;
import com.cc.purecloud.client.model.UserPresenceAggregateMetricsModel;
import com.cc.purecloud.client.model.UserPresenceAggregateMetricsRow;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.api.AnalyticsApi;
import com.mypurecloud.sdk.v2.model.AggregateDataContainer;
import com.mypurecloud.sdk.v2.model.AggregateMetricData;
import com.mypurecloud.sdk.v2.model.AggregationQuery;
import com.mypurecloud.sdk.v2.model.AnalyticsQueryFilter;
import com.mypurecloud.sdk.v2.model.AnalyticsQueryPredicate;
import com.mypurecloud.sdk.v2.model.AnalyticsQueryPredicate.DimensionEnum;
import com.mypurecloud.sdk.v2.model.AnalyticsQueryPredicate.OperatorEnum;
import com.mypurecloud.sdk.v2.model.PresenceQueryResponse;
import com.mypurecloud.sdk.v2.model.StatisticalResponse;

public class UserPresenceAggregatedMetricsClient extends AbstractClient {
 
  private String interval = null;
  private List<CustomPresenceDefinition> presenceDefinitions = null;
  private List<CustomUser> users = null;
  
  public UserPresenceAggregatedMetricsClient(String clientId, String clientSecret) {
    super(clientId, clientSecret);
  }

  public String getInterval() {
    return interval;
  }
  
  public void setInterval(String interval) {
    this.interval = interval;
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
          
          String qId = tt.nextToken();
          String qName = tt.nextToken();
          
          users.add(new CustomUser(qId, qName));
        }
        
      }
      
      this.users = users;
    }
    
  }

  public UserPresenceAggregateMetricsModel run() {
    

    System.out.println("User Presence Aggregated Metrics Client is now executing");
    
    this.presenceDefinitions = this.getPresenceDefinitions();
    
    UserPresenceAggregateMetricsModel model = new UserPresenceAggregateMetricsModel(this.presenceDefinitions);

    for (CustomUser user : this.getUsers()) {
      //if (user.getName().toLowerCase().contains("laura"))
        model.append(this.getUserPresenceAggregatedMetrics(user));
    }
    
    //System.out.println(" >>> " + model.getPresenceDefinitions());
    
    return model;
  }
  
  public List<CustomPresenceDefinition> getPresenceDefinitions() {
    
    PresenceClient client = new PresenceClient(this.getClientId(), this.getClientSecret());
    return client.executeOrganizationPresenceListing();
  }
  
  /*private void checkUsers() {
    
    UserClient client = new UserClient(this.getClientId(), this.getClientSecret());
    if (this.users != null && this.users.size() > 0) {
      for (CustomUser user : users) {
        if (user.getId() != null && !"".equals(user.getId())) {
          if (user.getName() == null || "".equals(user.getName())) {
            CustomUser u = client.get(user.getId());
            user.setName(u.getName() );
          }
        }
      }
    }
  }*/
  
  public List<CustomUser> getUsers() {
    
    if (this.users != null) {
      return this.users;
    } else {
      return new UserClient(this.getClientId(), this.getClientSecret()).list();
    } 
  }
  
  public UserPresenceAggregateMetricsModel getUserPresenceAggregatedMetrics(CustomUser user) {
    
    UserPresenceAggregateMetricsModel model = new UserPresenceAggregateMetricsModel();
    model.setPresenceDefinitions(this.presenceDefinitions);
    //UserPresenceAggregatedMetricsRow row = model.newRow();
    
    AnalyticsApi apiInstance = new AnalyticsApi();
    
    AggregationQuery body = new AggregationQuery(); // AggregationQuery | query
    
    body.setInterval(this.interval);
    body.setGranularity("PT30M");
    //body.groupBy(Arrays.asList(AggregationQuery.GroupByEnum.USERID));
    body.setMetrics(Arrays.asList(AggregationQuery.MetricsEnum.TSYSTEMPRESENCE,
                                  AggregationQuery.MetricsEnum.TORGANIZATIONPRESENCE));
    //body.setMetrics(null);

    AnalyticsQueryFilter filter = new AnalyticsQueryFilter();
    filter.setType(AnalyticsQueryFilter.TypeEnum.AND);
    
    AnalyticsQueryPredicate predicate = new AnalyticsQueryPredicate();
    predicate.setType(AnalyticsQueryPredicate.TypeEnum.DIMENSION);
    predicate.setDimension(DimensionEnum.USERID);
    predicate.setOperator(OperatorEnum.MATCHES);
    predicate.setValue(user.getId()); 
    
    filter.setPredicates(Arrays.asList(predicate));
    body.setFilter(filter);
    
    //System.out.println("body: " + new Gson().toJson(body));
    
    try {
      PresenceQueryResponse result = apiInstance.postAnalyticsUsersAggregatesQuery(body);
      
      Map <String, List<String>> mappgings =  result.getSystemToOrganizationMappings();
      
      for (AggregateDataContainer container : result.getResults()) {
        
        //    System.out.println("User " + container.getGroup().get("userId") + " " + user.getName());
        //    System.out.println("" + new Gson().toJson(result) + "");
        int n = 0;
        for (StatisticalResponse stats : container.getData()) {
          n++;
          UserPresenceAggregateMetricsRow row = model.newRow("interval");
          row.setUserId(user.getId());
          row.setUserName(user.getName());
          row.setInterval(stats.getInterval());
          
          for (AggregateMetricData smetric : stats.getMetrics()) {
            switch(smetric.getMetric().name().toUpperCase()) {
              case "TORGANIZATIONPRESENCE":
                row.setData(smetric.getQualifier(), new CustomMetric(0, smetric.getStats().getSum()));
                break;
              case "TSYSTEMPRESENCE":
                if (smetric.getQualifier().toUpperCase().equals("AVAILABLE")) {
                  row.setAvailable(new CustomMetric(0, smetric.getStats().getSum()));
                } else if (smetric.getQualifier().toUpperCase().equals("OFFLINE")) {
                  row.setOffline(new CustomMetric(0, smetric.getStats().getSum()));
                } else if (smetric.getQualifier().toUpperCase().equals("BREAK")) {
                  row.setOnBreak(new CustomMetric(0, smetric.getStats().getSum()));
                } else if (smetric.getQualifier().toUpperCase().equals("AWAY")) {
                  row.setAway(new CustomMetric(0, smetric.getStats().getSum()));
                } else if (smetric.getQualifier().toUpperCase().equals("ON_QUEUE")) {
                  row.setOnQueue(new CustomMetric(0, smetric.getStats().getSum()));
                } else if (smetric.getQualifier().toUpperCase().equals("MEAL")) {
                  row.setMeal(new CustomMetric(0, smetric.getStats().getSum()));
                } else if (smetric.getQualifier().toUpperCase().equals("BUSY")) {
                  row.setBusy(new CustomMetric(0, smetric.getStats().getSum()));
                } else if (smetric.getQualifier().toUpperCase().equals("MEETING")) {
                  row.setMeeting(new CustomMetric(0, smetric.getStats().getSum()));
                } else if (smetric.getQualifier().toUpperCase().equals("IDLE")) {
                  row.setIdle(new CustomMetric(0, smetric.getStats().getSum()));
                
                } else if (smetric.getQualifier().toUpperCase().equals("TRAINING")) {
                  row.setTraining(new CustomMetric(0, smetric.getStats().getSum()));
                
                } else {
                  System.out.println("Unknown qualifier name: " + smetric.getQualifier() + " : " + smetric.getStats().getSum());
                }
                break;
            }
          }
          row.consolidate();
          model.add(row);
        }
      }
      
      model.totalize(user.getId(), this.getInterval());
      return model;
      
    } catch (ApiException e) {
      System.err.println("Exception when calling AnalyticsApi#postConversationsAggregatesQuery");
      e.printStackTrace();
    } catch(IOException e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
}
