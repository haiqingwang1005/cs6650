package com.haiqing.listener;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.haiqing.db.DBCPDataSource;
import com.haiqing.model.Liftride;
import com.haiqing.util.RPCHelper;
import com.haiqing.util.RabbitMqHelper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppListener implements ServletContextListener {
  private static final Logger logger = LogManager.getLogger(AppListener.class);

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    System.out.println("app is started");
    logger.info("app is started");

    DBCPDataSource dataSource = new DBCPDataSource();
    logger.info("Connection is open");

    try {
      RabbitMqHelper rabbitMqHelper = RabbitMqHelper.getInstance();
//      final ExecutorService launcher = Executors.newScheduledThreadPool(8);
//
//      launcher.submit(() -> {
//        try {
//          //System.out.println("try listen");
//          rabbitMqHelper.receiveMessage();
//        } catch (Exception e) {
//          e.printStackTrace();
//        }
//      });

    } catch (final Throwable e) {
      e.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
  }
}
