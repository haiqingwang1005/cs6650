package io.swagger.client.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ParameterPropertiesValues {
  private static Integer maxThreads;
  private static Integer numSkiers;
  private static Integer numLifts;
  private static String skiDay;
  private static String resortID;
  private static String host;
  private static String port;
  private static Integer dayLengthInMin;



  public static void initValues() throws IOException {
    InputStream inputStream = null;

    try {
      Properties prop = new Properties();
      String propFileName = "parameters.properties";

      inputStream = ParameterPropertiesValues.class.getClassLoader().getResourceAsStream(propFileName);

      if (inputStream != null) {
        prop.load(inputStream);
      } else {
        throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
      }


      // get the property value and print it out
      maxThreads = Integer.valueOf(prop.getProperty("maxThreads"));
      numSkiers = Integer.valueOf(prop.getProperty("numSkiers"));
      numLifts = Integer.valueOf(prop.getProperty("numLifts"));
      skiDay = prop.getProperty("skiDay");
      resortID = prop.getProperty("resortID");
      host = prop.getProperty("host");
      port = prop.getProperty("port");
      dayLengthInMin = Integer.valueOf(prop.getProperty("dayLengthInMin"));

    } catch (Exception e) {
      System.out.println("Exception: " + e);
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }
  }

  public static Integer getMaxThreads() {
    return maxThreads;
  }

  public static Integer getNumSkiers() {
    return numSkiers;
  }

  public static Integer getNumLifts() {
    return numLifts;
  }

  public static String getSkiDay() {
    return skiDay;
  }

  public static String getResortID() {
    return resortID;
  }

  public static String getHost() {
    return host;
  }

  public static String getPort() {
    return port;
  }

  public static Integer getDayLengthInMin() {
    return dayLengthInMin;
  }
}
