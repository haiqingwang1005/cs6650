package com.haiqing.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.haiqing.db.MySQLConnection;
import com.haiqing.model.Liftride;
import com.haiqing.model.Message;
import com.haiqing.model.ResortVertical;
import com.haiqing.util.RPCHelper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SkiersServlet")
public class SkierServlet extends HttpServlet {

  private Gson gson = new Gson();

  protected void doPost(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json");
    String urlPath = request.getPathInfo();
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
      MySQLConnection connection = new MySQLConnection();
      JsonObject input = RPCHelper.readJSONObject(request);
      Liftride liftride = RPCHelper.parseLiftride(input);
      connection.saveLiftride(liftride);
      connection.close();
      response.setStatus(HttpServletResponse.SC_OK);
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
      MySQLConnection connection = new MySQLConnection();
      int totalVertNum = 0;
      String resortID = null;
      if (urlParts.length == 6) {
        resortID = urlParts[1];
        int dayID = Integer.parseInt(urlParts[3]);
        int skierID = Integer.parseInt(urlParts[5]);
        totalVertNum = connection.getTotalVertSkierDay(resortID, dayID, skierID);
      } else {
        int skierID = Integer.parseInt(urlParts[1]);
        resortID = request.getParameter("resort");
        totalVertNum = connection.getTotalVertPerResort(skierID, resortID);
      }
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().write(gson.toJson(new ResortVertical(resortID, totalVertNum)));
    }
  }


  private boolean isUrlValid(String[] urlPath, HttpServletRequest request) throws IllegalArgumentException {
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
