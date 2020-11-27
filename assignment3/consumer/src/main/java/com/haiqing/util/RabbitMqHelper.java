package com.haiqing.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.haiqing.db.DBCPDataSource;
import com.haiqing.model.Liftride;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RabbitMqHelper {

  private static final Logger logger = LogManager.getLogger(RabbitMqHelper.class);

  public static final String QUEUE_NAME = "cs6650_queue";
  public static final String RABBIT_HOST_NAME = "ec2-35-167-150-123.us-west-2.compute.amazonaws.com";

  private static RabbitMqHelper rabbitMqHelper;

  private Connection connection;
  private DBCPDataSource dataSource;


  private RabbitMqHelper() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(RABBIT_HOST_NAME);
    factory.setUsername("admin");
    factory.setPassword("adminpass");
    factory.setVirtualHost("/");
    try {
      dataSource = new DBCPDataSource();
      connection = factory.newConnection();
    } catch (final Exception e) {
      e.printStackTrace();
      logger.error("Cannot declare channel", e);
      throw e;
    }
  }

  public static RabbitMqHelper getInstance() throws Exception {
    if (rabbitMqHelper == null) {
      rabbitMqHelper = new RabbitMqHelper();
    }
    return rabbitMqHelper;
  }

  public void sendMessage(final String message) throws Exception {
    Channel channel = null;
    try {
      channel = connection.createChannel();
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);

      channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));

      System.out.println(" [x] Sent '" + message + "'");
    } catch (final Exception e) {
      e.printStackTrace();
      logger.error("Cannot send message", e);
      throw e;
    } finally {
      if (channel != null) {
        channel.close();
      }
    }
  }

  public void receiveMessage() throws IOException {

    try {
      final Channel channel = connection.createChannel();
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);

      boolean autoAck = false;
      channel.basicConsume(QUEUE_NAME, autoAck, "myConsumerTag",
          new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                Envelope envelope,
                AMQP.BasicProperties properties,
                byte[] body)
                throws IOException {
              String routingKey = envelope.getRoutingKey();
              String contentType = properties.getContentType();
              long deliveryTag = envelope.getDeliveryTag();

              String message = new String(body, StandardCharsets.UTF_8);
              System.out.println("receive: " + message);
              JsonObject convertedObject = new Gson().fromJson(message, JsonObject.class);
              Liftride liftride = RPCHelper.parseLiftride(convertedObject);
              dataSource.saveLiftride(liftride);

              channel.basicAck(deliveryTag, false);
            }
          });
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }
}
