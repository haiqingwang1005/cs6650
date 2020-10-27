package com.haiqing.db;

import com.haiqing.model.Liftride;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLConnection {
  private Connection conn;

  public MySQLConnection() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
      conn = DriverManager.getConnection(MySQLDBUtil.URL);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void close() {
    if (conn != null) {
      try {
        conn.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void saveLiftride(Liftride liftride) {
    if (conn == null) {
      System.out.println("DB connection failed");
      return;
    }
    String sql = "INSERT IGNORE INTO liftrides VALUES(?, ?, ?, ?, ?, ?)";
    try {
      PreparedStatement statement = conn.prepareStatement(sql);
      statement.setInt(1, liftride.getLiftID());
      statement.setString(2, liftride.getResortID());
      statement.setInt(3, liftride.getDayID());
      statement.setInt(4, liftride.getSkierID());
      statement.setInt(5, liftride.getTime());
      statement.setInt(6, liftride.getLiftID() * 10);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public int getTotalVertSkierDay(String resortID, int dayID, int skierID) {
    int totalVertNum = 0;
    String sql = "SELECT vertical FROM liftrides WHERE skier_id=? AND resort_id=? "
        + "AND day_id=?;";
    try {
      PreparedStatement statement = conn.prepareStatement(sql);
      statement.setInt(1, skierID);
      statement.setString(2, resortID);
      statement.setInt(3, dayID);
      ResultSet res = statement.executeQuery();
      while (res.next()) {
        totalVertNum += res.getInt("vertical");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return totalVertNum;
  }

  public int getTotalVertPerResort(int skierID, String resortID) {
    int totalVertNum = 0;
    String sql = "SELECT vertical FROM liftrides WHERE skier_id=? AND resort_id=?;";
    try {
      PreparedStatement statement = conn.prepareStatement(sql);
      statement.setInt(1, skierID);
      statement.setString(2, resortID);
      ResultSet res = statement.executeQuery();
      while (res.next()) {
        totalVertNum += res.getInt("vertical");
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return totalVertNum;

  }


}
