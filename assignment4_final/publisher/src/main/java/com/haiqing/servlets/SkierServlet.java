package com.haiqing.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.haiqing.db.DBCPDataSource;
import com.haiqing.dynamodb.DynamoSource;
import com.haiqing.model.Message;
import com.haiqing.model.ResortVertical;
import com.haiqing.redis.RedisConnection;
import com.haiqing.util.RPCHelper;
import com.haiqing.util.RabbitMqHelper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import redis.clients.jedis.Jedis;

@WebServlet(name = "SkiersServlet")
public class SkierServlet extends HttpServlet {

  private static final Logger logger = LogManager.getLogger(SkierServlet.class);
  private Gson gson = new Gson();

  protected void doPost(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    logger.info("Do post for " + request.getPathInfo());

    response.setContentType("application/json");
    String urlPath = request.getPathInfo();
    PrintWriter out = response.getWriter();
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write(gson.toJson(new Message("not found")));
    }
    String[] urlParts = urlPath.split("/");
    if (!isUrlValid(urlParts, request)) {

      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write(gson.toJson(new Message("bad")));
    } else {
      logger.info("Open connection");

      JsonObject input = RPCHelper.readJSONObject(request);
      logger.info("input " + input.toString());

      try {
        RabbitMqHelper rabbitMqHelper = RabbitMqHelper.getInstance();
        rabbitMqHelper.sendMessage(input.toString());
      } catch (final Exception e) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(gson.toJson(new Message("internal error")));
        return;
      }

      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().write(gson.toJson(new Message("ok")));

    }
  }

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {

    response.setContentType("application/json");
    String urlPath = request.getPathInfo();

    // check if we have a URL
    PrintWriter out = response.getWriter();
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write(gson.toJson(new Message("string")));
    }

    String[] urlParts = urlPath.split("/");
    if (!isUrlValid(urlParts, request)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write(gson.toJson(new Message("string")));
    } else {
      int totalVertNum = 0;
      String resortID = null;
      if (urlParts.length == 6) {
        resortID = urlParts[1];
      } else {
        resortID = request.getParameter("resort");
      }
      //DBCPDataSource connection = new DBCPDataSource();
      DynamoSource dynamoSource = DynamoSource.getInstance();
      //Jedis jedis = null;
      try {
        logger.info("try to get cache");
        //RedisConnection redisConnection = RedisConnection.getInstance();
        //jedis = redisConnection.getJedisPool().getResource();
        //String cachedResponse = jedis.get(urlPath);
        //logger.info("Cache response: " + cachedResponse);
        //if (cachedResponse != null) {
        if (false) {
        //  totalVertNum = Integer.parseInt(cachedResponse);
        } else {
          if (urlParts.length == 6) {
            int dayID = Integer.parseInt(urlParts[3]);
            int skierID = Integer.parseInt(urlParts[5]);
            //totalVertNum = connection.getTotalVertSkierDay(resortID, dayID, skierID);
            totalVertNum = dynamoSource.getTotalVertSkierDay(resortID, dayID, skierID);
          } else {
            int skierID = Integer.parseInt(urlParts[1]);
            //totalVertNum = connection.getTotalVertPerResort(skierID, resortID);
            totalVertNum = dynamoSource.getTotalVertPerResort(skierID, resortID);
          }
          //jedis.set(urlPath, String.valueOf(totalVertNum));
        }
      } catch (final Exception e) {
        e.printStackTrace();
        throw e;
      } finally {
        //if (jedis != null) {
        //  jedis.close();
        //}
      }
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().write(gson.toJson(new ResortVertical(resortID, totalVertNum)));
    }
  }


  private boolean isUrlValid(String[] urlPath, HttpServletRequest request)
      throws IllegalArgumentException {
    // /skiers/{resortID}/days/{dayID}/skiers/{skierID}

    if (urlPath.length == 6) {

      try {
        int dayId = Integer.parseInt(urlPath[3]);
        int skierId = Integer.parseInt(urlPath[5]);
      } catch (Exception e) {
        e.printStackTrace();
        throw new IllegalArgumentException("Input parameters must be integers");
      }
      return urlPath[1] != null && urlPath[1].length() > 0 &&
          urlPath[2].equals("days") && urlPath[4].equals("skiers");
      // /skiers/{skierId}/vertical?resort=...
    } else if (urlPath.length == 3) {
      try {
        int skierId = Integer.parseInt(urlPath[1]);
      } catch (Exception e) {
        e.printStackTrace();
        throw new IllegalArgumentException("skierId must be an integer");
      }
      return urlPath[2].equals("vertical") && request.getParameter("resort") != null;
    } else if (urlPath.length == 2) {
      return urlPath[1].equals("liftrides");
    }
    return false;
  }
}
