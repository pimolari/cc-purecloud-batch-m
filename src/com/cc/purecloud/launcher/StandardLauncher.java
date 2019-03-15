package com.cc.purecloud.launcher;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.cc.purecloud.client.QueueAggregateMetricsClient;
import com.cc.purecloud.client.QueueClient;
import com.cc.purecloud.client.UserClient;
import com.cc.purecloud.client.UserPresenceAggregatedMetricsClient;
import com.cc.purecloud.client.model.UserPresenceAggregateMetricsModel;
import com.cc.purecloud.client.model.util.QueueModelUtils;
import com.cc.purecloud.report.QueueListReport;
import com.cc.purecloud.report.SAEAggregateServiceNowReport;
import com.cc.purecloud.report.SAEAggregateUserQueueReport;
import com.cc.purecloud.report.SAEAggregatedQueueReport;
import com.cc.purecloud.report.SAEAggregatedTotalUserPresenceReport;
import com.cc.purecloud.report.SAEAggregatedUserPresenceReport;
import com.cc.purecloud.report.UserListReport;
import com.cc.purecloud.util.ArgumentException;
import com.cc.purecloud.util.Config;
import com.cc.purecloud.util.ConfigException;
import com.cc.purecloud.util.OutputException;
import com.cc.purecloud.util.Utils;
import com.cc.servicenow.ServiceNowSimpleClient;
import com.cipher.CryptoUtil;

public class StandardLauncher {
  
  public static final String REPORT_QUEUE_AGGREGATED = "report.queue.aggregate";
  public static final String REPORT_USER_PRESENCE_AGGREGATED = "report.userpresence.aggregate.intervals";
  public static final String REPORT_TOTAL_USER_PRESENCE_AGGREGATED = "report.userpresence.aggregate.totals";
  public static final String REPORT_USER_LIST = "report.userlist";
  public static final String REPORT_QUEUE_LIST = "report.queuelist";
  public static final String REPORT_USERQUEUE_AGGREGATED = "report.userqueue.aggregate";
  public static final String REPORT_SERVICENOW_AGGREGATED = "report.servicenow.aggregate";
  
  
  Config config = null;
  
  private String clientId = null;
  private String clientSecret = null;
  
  private LocalDateTime startDateTime = null; // Set by the -i argument
  private LocalDateTime endDateTime = null; // Set by the -e argument
  private String outputType = null; // Set by the -t argument
  private String reportType = null; // Set by the -r argument
  private String configFilePath = "batch.properties"; // Set by the -c argument
  private String outputPath = null; // Set by the -o argument
  private String interval = null;
  private String secretKey = null;
  
  public StandardLauncher() {
    // this.init(null, null, null);
  }
  
  public StandardLauncher(String clientId, String clientSecret) {
    // this.init(clientId, clientSecret, null);
  }
  
  public StandardLauncher(String clientId, String clientSecret, String interval) {
    // this.init(clientId, clientSecret, interval);
    this.interval = interval;
  }
  
  private void init(String clientId, String clientSecret, String interval) throws IOException, ConfigException {
    // try {
    this.config = new Config(this.configFilePath);
    this.clientId = (clientId == null || clientId.equals("") ? Config.get("purecloud.clientid") : clientId);
    this.clientSecret = (clientSecret == null || clientSecret.equals("") ? Config.get("purecloud.clientsecret")
        : clientSecret);
    
    /*
     * } catch(IOException e) {
     * System.err.println("Could not open properties file " +
     * this.configFilePath); } catch(ConfigException e) {
     * System.err.println(e.getMessage()); }
     */
  }
  
  public void launch(String[] args) {
    
    try {
      for (String arg : args) {
        this.validateArg(arg);
      }
      
      init(null, null, null);
      
      // System.out.println(LocalDateTime.parse("2018-01-01T10:11:12",
      // DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
      
      if (this.startDateTime == null) {
        this.startDateTime = LocalDateTime.from(LocalDate.now().atStartOfDay());
      }
      
      if (this.endDateTime == null) {
        this.endDateTime = this.startDateTime.plusDays(1);
      }
      
      if (!this.endDateTime.isAfter(this.startDateTime)) {
        throw new ArgumentException("Incorrect dates. Make sure that start date is before end date");
      }
      
      if (outputType == null) {
        System.out.println("  Defaults to CSV");
        outputType = "csv";
      }
      if (this.reportType == null) {
        System.out.println("  Defaults to Queue report");
        ;
        reportType = "queue";
      }
      
      //System.out.println("Generating " + reportType + " report in " + outputType + " format for interval " + interval);
      
      /*this.interval = startDateTime.minusHours(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
          .toString()
          // interval =
          // startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")).toString()
          + "/" +
          // endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
          endDateTime.minusHours(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
      */
      
      long utcOffset = Utils.convertToLong(Config.get("program.timezone.offset"));
      System.out.println(" ! Applies UTC offset " + utcOffset + "h");
      
      this.interval = Utils.adjustDate(startDateTime, utcOffset).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
          .toString()
          + "/" +
          Utils.adjustDate(endDateTime, utcOffset).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
      
      System.out.println("Generating " + reportType + " report in " + outputType + " format for interval " + interval);
      
      if (this.reportType.equals("queue")) {
        QueueAggregateMetricsClient queueAggregatedClient = new QueueAggregateMetricsClient(this.clientId, this.clientSecret);
        //QueueAggregateMetricsClient queueAggregatedClient = new QueueAggregateMetricsClient(this.clientId, CryptoUtil.decrypt(this.secretKey, this.clientSecret));
        queueAggregatedClient.setInterval(interval);
        queueAggregatedClient.setQueues(Config.get("report.queue.aggregate.queue.list"));
        SAEAggregatedQueueReport report = new SAEAggregatedQueueReport(StandardLauncher.REPORT_QUEUE_AGGREGATED,
            queueAggregatedClient.getQueueAggregateMetrics());
        report.setOutputPath(this.outputPath);
        report.generate(outputType);
      } else if (this.reportType.equals("queuetest")) {
        QueueAggregateMetricsClient queueAggregatedClient = new QueueAggregateMetricsClient(this.clientId, this.clientSecret);
        //QueueAggregateMetricsClient queueAggregatedClient = new QueueAggregateMetricsClient(this.clientId, CryptoUtil.decrypt(this.secretKey, this.clientSecret));
        queueAggregatedClient.setInterval(interval);
        queueAggregatedClient.setQueues(Config.get("report.queue.aggregate.queue.list"));
        SAEAggregatedQueueReport report = new SAEAggregatedQueueReport(StandardLauncher.REPORT_QUEUE_AGGREGATED,
            queueAggregatedClient.getQueueAggregateMetrics());
        report.setOutputPath(this.outputPath);
        report.generate(outputType);
      } else if (this.reportType.equals("userqueue")) {
        QueueAggregateMetricsClient queueAggregateClient = new QueueAggregateMetricsClient(this.clientId, this.clientSecret);
        //QueueAggregateMetricsClient queueAggregateClient = new QueueAggregateMetricsClient(this.clientId, CryptoUtil.decrypt(this.secretKey, this.clientSecret));
        queueAggregateClient.setInterval(interval);
        queueAggregateClient.setQueues(Config.get("report.userqueue.aggregate.queue.list"));
        queueAggregateClient.setUsers(Config.get("report.userqueue.aggregate.user.list"));
        SAEAggregateUserQueueReport report = new SAEAggregateUserQueueReport(StandardLauncher.REPORT_USERQUEUE_AGGREGATED,
            queueAggregateClient.getUserQueueAggregateMetrics());
        report.setOutputPath(this.outputPath);
        report.generate(outputType);
      } else if (this.reportType.equals("userpresence")) {
        UserPresenceAggregatedMetricsClient userPresenceClient = new UserPresenceAggregatedMetricsClient(this.clientId, this.clientSecret);
        //UserPresenceAggregatedMetricsClient userPresenceClient = new UserPresenceAggregatedMetricsClient(this.clientId, CryptoUtil.decrypt(this.secretKey, this.clientSecret));
        userPresenceClient.setInterval(interval);
        userPresenceClient.setUsers(Config.get("report.userpresence.aggregate.user.list"));
        UserPresenceAggregateMetricsModel model = userPresenceClient.run();
        SAEAggregatedUserPresenceReport intervals = new SAEAggregatedUserPresenceReport(
            StandardLauncher.REPORT_USER_PRESENCE_AGGREGATED, model);
        intervals.setOutputPath(this.outputPath);
        intervals.generate(outputType);
        SAEAggregatedTotalUserPresenceReport totals = new SAEAggregatedTotalUserPresenceReport(
            StandardLauncher.REPORT_TOTAL_USER_PRESENCE_AGGREGATED, model);
        totals.setOutputPath(this.outputPath);
        totals.generate(outputType);
      } else if (this.reportType.equals("userlist")) {
        UserClient userClient = new UserClient(this.clientId, this.clientSecret);
        //UserClient userClient = new UserClient(this.clientId, CryptoUtil.decrypt(this.secretKey, this.clientSecret));
        UserListReport report = new UserListReport(StandardLauncher.REPORT_USER_LIST, userClient.list());
        report.setOutputPath(this.outputPath);
        report.generate(outputType);
      } else if (this.reportType.equals("queuelist")) {
        QueueClient queueClient = new QueueClient(this.clientId, this.clientSecret);
        //QueueClient queueClient = new QueueClient(this.clientId, CryptoUtil.decrypt(this.secretKey, this.clientSecret));
        QueueListReport report = new QueueListReport(StandardLauncher.REPORT_QUEUE_LIST, queueClient.list());
        report.setOutputPath(this.outputPath);
        report.generate(outputType);
      } else if (this.reportType.equals("servicenow")) {
        QueueAggregateMetricsClient queueAggregateClient = new QueueAggregateMetricsClient(this.clientId, this.clientSecret);
        //QueueAggregateMetricsClient queueAggregateClient = new QueueAggregateMetricsClient(this.clientId, CryptoUtil.decrypt(this.secretKey, this.clientSecret));
        queueAggregateClient.setInterval(interval);
        queueAggregateClient.setQueues(Config.get("report.servicenow.aggregate.queue.list"));
        SAEAggregateServiceNowReport report = new SAEAggregateServiceNowReport(StandardLauncher.REPORT_SERVICENOW_AGGREGATED,
            QueueModelUtils.aggregateModelDailyData(queueAggregateClient.getQueueAggregateMetrics()), Config.get("report.servicenow.aggregate.country"), Config.get("report.servicenow.aggregate.company"));
        report.setOutputPath(this.outputPath);
        String fileName = report.generate(outputType);
        ServiceNowSimpleClient snClient = new ServiceNowSimpleClient();
        snClient.uploadAttachment(Config.get("report.servicenow.aggregate.url"), 
                                  Config.get("servicenow.clientId"),
                                  CryptoUtil.decrypt(this.secretKey, Config.get("servicenow.clientSecret")),
                                  //"/Users/gmongelli/work/temp/sae/output/sae-servicenow-metrics-20180709.csv");
                                  //"/Users/gmongelli/work/temp/sae/output/SAE_TEST_ES.xlsx");
                                  this.outputPath + Config.get("program.separator") + fileName);
      }
    } catch (OutputException e) {
      System.err.println(e.getMessage());
    } catch (ArgumentException e) {
      System.err.println(e.getMessage());
    } catch (IOException e) {
      System.err.println(e.getMessage());
    } catch (ConfigException e) {
      System.err.println(e.getMessage());
    } catch(Exception e) {
      System.err.println(e.getMessage());
    } finally {
      System.out.println("Done!");
    }
  }
  
  /*
   * public void setLabConfig() { this.clientId =
   * "0ed53b7f-df14-4fc4-afd0-f80e39ca9532"; this.clientSecret =
   * "e3NPp8pxQ5VHnS0ZbyAAxj3fiBcZiPe0heVNXPjjawo"; this.interval =
   * "2018-01-29T00:00:00/2018-02-02T00:00:00"; }
   */
  
  /*
   * public void setBBVAESCC1Config() { this.clientId =
   * "bb63d9e3-ccd1-4f0b-afef-a731c461cdfd"; //Custom PoC SAE Report - Client
   * Credentials this.clientSecret =
   * "xytK_-47nGBldRND9LSCoLf6UNREjD6OPKS33JCxdv8"; }
   */
  
  public  void validateArg(String arg) throws ArgumentException {
    
    String value = arg.substring(2);
    if (arg.startsWith("-d")) {
      // date
      try {
        this.startDateTime = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
      } catch (Exception e) {
        throw new ArgumentException("Invalid start date supplied" + value);
      }
    } else if (arg.startsWith("-e")) {
      // end date
      try {
        this.endDateTime = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
      } catch (Exception e) {
        throw new ArgumentException("Invalid end date supplied" + value);
      }
    } else if (arg.startsWith("-t")) {
      // output type
      // System.out.println("With output type: " + value);
      this.outputType = value;
    } else if (arg.startsWith("-r")) {
      // report type
      // System.out.println("With report type: " + value);
      this.reportType = value;
    } else if (arg.startsWith("-c")) {
      this.configFilePath = value;
    } else if (arg.startsWith("-o")) {
      // output path
      this.outputPath = value;
    } else if (arg.startsWith("-k")) {
      // output path
      this.secretKey = value;
    } else {
      throw new ArgumentException("Invalid arguments " + arg);
    }
  }
  
  public static void main(String[] args) {
    
    StandardLauncher launcher = null;
    // try {
    launcher = new StandardLauncher();
    // launcher.setBBVAESCC1Config();
    launcher.launch(args);
    
    // } catch(IOException e) {
    // System.out.println("Could not find properties file");
    // }
  }
}
