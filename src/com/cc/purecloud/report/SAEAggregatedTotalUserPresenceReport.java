package com.cc.purecloud.report;

import java.time.format.DateTimeFormatter;

import com.cc.purecloud.client.model.CustomMetric;
import com.cc.purecloud.client.model.CustomPresenceDefinition;
import com.cc.purecloud.client.model.UserPresenceAggregateMetricsModel;
import com.cc.purecloud.client.model.UserPresenceAggregateMetricsRow;
import com.cc.purecloud.util.Utils;
import com.google.gson.Gson;

public class SAEAggregatedTotalUserPresenceReport extends SAEAggregatedUserPresenceReport {
  
  UserPresenceAggregateMetricsModel model = null;
  
  public SAEAggregatedTotalUserPresenceReport(String reportName, UserPresenceAggregateMetricsModel model) {
    super(reportName, model);
    super.setType("total");
  }
}
