package com.cc.purecloud.report;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.cc.purecloud.util.ArgumentException;
import com.cc.purecloud.util.Config;
import com.cc.purecloud.util.ConfigException;
import com.cc.purecloud.util.OutputException;

public class GenericReport  {
  
  private String type = null;
  private String outputType = null;
  private String outputPath = null;
  private String reportName = null;
  
  public GenericReport(String reportName) {
    this.reportName = reportName;
  }
  
  public GenericReport(String reportName, String outputType) {
    this.reportName = reportName;
    this.outputType = outputType;
  }
  
  public GenericReport(String reportName, String outputType, String type) {
    this.reportName = reportName;
    this.outputType = outputType;
    this.type = type;
  }
  
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getOutputType() {
    return this.outputType;
  }

  public void setOutputType(String outputType) {
    this.outputType = outputType;
  }

  public String getOutputPath() {
    return outputPath;
  }

  public void setOutputPath(String outputPath) {
    this.outputPath = outputPath;
  }

  public String getReportName() {
    return reportName;
  }

  public void setReportName(String reportName) {
    this.reportName = reportName;
  }

  public String generate(String outputType) throws OutputException, ArgumentException, ConfigException {
    
    if (this.reportName == null || this.getReportName().equals("") || Config.get(this.reportName + ".filename").equals("") || Config.get(this.reportName + ".filename") == null)
      throw new ArgumentException("Must specify a report name prefix in properties file");
    
    this.outputType = outputType;
    String output = null;
    if ("CSV".equals(outputType.toUpperCase())) {
      output = this.generateCSV();
    }
    
    if ("JSON".equals(outputType.toUpperCase())) {
      output = this.generateJson();
    }
      
    if ("HTTP".equals(outputType.toUpperCase())) {
      output = this.upload();
    }
    
    return this.prepareOutput(output);
  }
  public String generateJson() {
    return null;
  }
  
  public String generateCSV() {
    return null;
  }
  
  public String upload() {
    return null;
  }
  
  private String prepareOutput(String data) throws OutputException, ConfigException {
    if (this.outputPath != null && !this.outputPath.equals("")) {
      return this.dumpToFile(data);
    } else {
      System.out.println(data);
      return null;
    }
  }
  
  private String dumpToFile(String data) throws OutputException, ConfigException {
  
    String fileName = this.calculateFileName();
    try {
      
      //BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputPath + Config.get("program.separator") + fileName));
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.outputPath + Config.get("program.separator") + fileName), StandardCharsets.UTF_8));
      writer.write(data);
      writer.close();
      
    } catch(IOException e) {
      throw new OutputException("Could not open outputPath " + this.outputPath);
    }
    
    return fileName;
  }
  
    private String calculateFileName() throws ConfigException {
      StringBuffer fileName = new StringBuffer(Config.get(this.reportName + ".filename"));
      if (!fileName.toString().endsWith("-")) fileName.append("-"); 
      fileName.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
      fileName.append(".");
      fileName.append(this.outputType.toLowerCase());
      return fileName.toString();
    }
}
