package com.cc.purecloud.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Properties;

public class Config {
  
  public static Properties properties = null;
  
  public Config(Properties properties) throws IOException {
    this.properties = properties;
  }
  
  public Config(String propertiesPath) throws IOException {
    init(propertiesPath);
  }
  
  public void init(String propertiesPath) throws IOException {
    
    System.out.println("Using " + propertiesPath + " for configuration");
    properties = new Properties();
    properties.load(new FileInputStream(new File(propertiesPath)));
    //properties.load(this.getClass().getResourceAsStream("/config/dev/backend.properties"));
    
    System.out.println("" + properties.getProperty("program.name") + " loaded");
  }
  
  public static String get(String key) throws ConfigException {
    String value = properties.getProperty(key);
    
    if (value == null)
      throw new ConfigException("Missing configuration property " + key);
    return value;
  }
  
  public static String get(String key, String... value) throws ConfigException {
    return MessageFormat.format(get(key), (Object[]) value);
  }
  
}
