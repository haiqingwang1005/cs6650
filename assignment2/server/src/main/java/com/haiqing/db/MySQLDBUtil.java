package com.haiqing.db;

public class MySQLDBUtil {
  public static final String INSTANCE = "cs6650-instance.ckwydrghvhsx.us-west-2.rds.amazonaws.com";
  public static final String PORT_NUM = "3306";
  public static final String DB_NAME = "cs6650";
  public static final String USERNAME = "admin";
  public static final String PASSWORD = "cs6650db";

  public static final String URL = "jdbc:mysql://"
      + INSTANCE + ":" + PORT_NUM + "/" + DB_NAME
      + "?user=" + USERNAME + "&password=" + PASSWORD
      + "&autoReconnect=true&serverTimezone=UTC";

}
