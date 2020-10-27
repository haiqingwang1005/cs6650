package com.haiqing.db;

public class MySQLDBUtil {
  private static final String INSTANCE = "cs6650-instance.ckwydrghvhsx.us-west-2.rds.amazonaws.com";
  private static final String PORT_NUM = "3306";
  private static final String DB_NAME = "cs6650";
  private static final String USERNAME = "admin";
  private static final String PASSWORD = "cs6650db";

  public static final String URL = "jdbc:mysql://"
      + INSTANCE + ":" + PORT_NUM + "/" + DB_NAME
      + "?user=" + USERNAME + "&password=" + PASSWORD
      + "&autoReconnect=true&serverTimezone=UTC";

}
