package com.haiqing;

import com.haiqing.db.DBCPDataSource;
import com.haiqing.util.RabbitMqHelper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
      final ExecutorService launcher = Executors.newScheduledThreadPool(128);

      launcher.submit(() -> {
        try {
          //System.out.println("try listen");
          rabbitMqHelper.receiveMessage();
        } catch (Exception e) {
          e.printStackTrace();
        }
      });

    } catch (final Throwable e) {
      e.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
  }
}
