package com.aakash.messaging.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigHelper {
  static ConfigHelper configHelper;
  static Properties properties;

  private static final String CONFIG_PATH = System.getProperty("user.dir") +"/target/classes";

  private static final String ACCESS_PROPERTY_FILENAME = "application.properties";

  private static final Logger LOG = LoggerFactory.getLogger(ConfigHelper.class);

  private ConfigHelper() {
  }

  public static ConfigHelper getInstance() {
    if (configHelper == null) {
      configHelper = new ConfigHelper();
      properties = new Properties();
      try (InputStream inputStream = new FileInputStream(CONFIG_PATH + "/" + ACCESS_PROPERTY_FILENAME)) {
        properties.load(inputStream);
      } catch (FileNotFoundException e) {
        LOG.error("Unable to load properties", e.getMessage());

      } catch (IOException e) {
        LOG.error("Unable to load properties ", e.getMessage());
      }
    }
    return configHelper;
  }

  public String getProperty(String property) {
    return properties.getProperty(property);
  }
}
