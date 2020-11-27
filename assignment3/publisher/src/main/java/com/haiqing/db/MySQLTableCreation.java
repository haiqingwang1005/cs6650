package com.haiqing.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MySQLTableCreation {
  // Run this as Java application to reset the database.
  public static void main(String[] args) {
    try {
      // Step 1 Connect to MySQL.
      System.out.println("Connecting to " + MySQLDBUtil.URL);
      Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
      Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);

      if (conn == null) {
        return;
      }

      // Step 2 Drop tables in case they exist.
      Statement statement = conn.createStatement();
      String sql = "DROP TABLE IF EXISTS liftrides";
      statement.executeUpdate(sql);

      sql = "DROP TABLE IF EXISTS messages";
      statement.executeUpdate(sql);

      sql = "DROP TABLE IF EXISTS resortVerticals";
      statement.executeUpdate(sql);

      sql = "DROP TABLE IF EXISTS skierVerticals";
      statement.executeUpdate(sql);

      // Step 3 Create new tables
      sql = "CREATE TABLE liftrides("
          + "lift_id INT NOT NULL,"
          + "resort_id VARCHAR(255),"
          + "day_id INT,"
          + "skier_id INT,"
          + "time INT,"
          + "vertical INT,"
          + "PRIMARY KEY (lift_id)"
          + ")";
      statement.executeUpdate(sql);

      sql = "CREATE TABLE messages("
          + "message_id VARCHAR(255),"
          + "message TEXT,"
          + "PRIMARY KEY(message_id)"
          + ")";
      statement.executeUpdate(sql);

      sql = "CREATE TABLE resortVerticals("
          + "resort_id VARCHAR(255),"
          + "totalVert INT,"
          + "PRIMARY KEY(resort_id)"
          + ")";
      statement.executeUpdate(sql);

      sql = "CREATE TABLE skierVerticals("
          + "skier_id VARCHAR(255),"
          + "verticalTotal INT,"
          + "PRIMARY KEY(skier_id)"
          + ")";
      statement.executeUpdate(sql);

      // Step 4: insert fake lift 1111/3229c1097c00d498a0fd282d586be0
      //sql = "INSERT INTO liftrides VALUES(1, '3229c1097c00d497a0fd282d586be050', 1, 1, 0, 10)";
      //statement.executeUpdate(sql);


      conn.close();
      System.out.println("Import done successfully");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
