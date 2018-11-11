package com.gerald.mongo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MongoUtils {

  private MongoUtils() {
    throw new UnsupportedOperationException("Should not be initialized for this class");
  }

  private static Properties loadProperties() {
    Properties prop = new Properties();
    try (InputStream resourceAsStream = MongoUtils.class.getResourceAsStream("/mongo-config.properties")) {
      prop.load(resourceAsStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return prop;
  }

  static MongoConfig getMongoConfig() {

    Properties prop = loadProperties();

    MongoConfig mongoConfig = new MongoConfig();
    if (prop != null) {
      mongoConfig.setHost(prop.getProperty("host"));
      mongoConfig.setPassword(prop.getProperty("password"));
      mongoConfig.setUsername(prop.getProperty("username"));
    }
    return mongoConfig;
  }

  public static void main(String[] args) {
    MongoConfig mongoConfig = getMongoConfig();
  }
}
