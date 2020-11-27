package com.haiqing.db;

import com.haiqing.model.Liftride;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBCPDataSource {
  private static final Logger logger = LogManager.getLogger(DBCPDataSource.class);

  private static BasicDataSource dataSource;

  static {
    // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
    dataSource = new BasicDataSource();
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      logger.error("Cannot open db", e);
    }
    String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC",
        MySQLDBUtil.INSTANCE,
        MySQLDBUtil.PORT_NUM,
        MySQLDBUtil.DB_NAME);

    dataSource.setUrl(url);
    dataSource.setUsername(MySQLDBUtil.USERNAME);
    dataSource.setPassword(MySQLDBUtil.PASSWORD);
    dataSource.setInitialSize(10);
    dataSource.setMaxTotal(60);
  }


  public void saveLiftride(Liftride liftride) {
    String sql = "INSERT IGNORE INTO liftrides VALUES(?, ?, ?, ?, ?, ?)";
    Connection connection = null;
    PreparedStatement statement = null;
    try {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(sql);
      statement.setInt(1, liftride.getLiftID());
      statement.setString(2, liftride.getResortID());
      statement.setInt(3, liftride.getDayID());
      statement.setInt(4, liftride.getSkierID());
      statement.setInt(5, liftride.getTime());
      statement.setInt(6, liftride.getLiftID() * 10);
      statement.executeUpdate();
      logger.info("Successfully save to db");

    } catch (SQLException e) {
      e.printStackTrace();
      logger.error("cannot saveLiftride", e);
    } finally {
      try {
        if (connection != null) {
          connection.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
        logger.error("cannot close for saveLiftride ", se);
      }
    }
  }

  public int getTotalVertSkierDay(String resortID, int dayID, int skierID) {
    int totalVertNum = 0;
    String sql = "SELECT vertical FROM liftrides WHERE skier_id=? AND resort_id=? "
        + "AND day_id=?;";
    Connection connection = null;
    PreparedStatement statement = null;
    try {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(sql);
      statement.setInt(1, skierID);
      statement.setString(2, resortID);
      statement.setInt(3, dayID);
      ResultSet res = statement.executeQuery();
      while (res.next()) {
        totalVertNum += res.getInt("vertical");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      logger.error("cannot getTotalVertSkierDay", e);
    } finally {
      try {
        if (connection != null) {
          connection.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
        logger.error("cannot close for getTotalVertSkierDay ", se);
      }
    }
    return totalVertNum;
  }

  public int getTotalVertPerResort(int skierID, String resortID) {
    int totalVertNum = 0;
    String sql = "SELECT vertical FROM liftrides WHERE skier_id=? AND resort_id=?;";
    Connection connection = null;
    PreparedStatement statement = null;
    try {
      connection = dataSource.getConnection();
      statement = connection.prepareStatement(sql);
      statement.setInt(1, skierID);
      statement.setString(2, resortID);
      ResultSet res = statement.executeQuery();
      while (res.next()) {
        totalVertNum += res.getInt("vertical");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      logger.error("cannot getTotalVertPerResort", e);

    } finally {
      try {
        if (connection != null) {
          connection.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
        logger.error("cannot close for getTotalVertPerResort ", se);
      }
    }
    return totalVertNum;
  }


}
