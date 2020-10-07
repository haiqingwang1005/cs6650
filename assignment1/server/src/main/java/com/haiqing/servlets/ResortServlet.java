package com.haiqing.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.haiqing.model.Message;
import com.haiqing.model.SkierVertical;
import com.haiqing.model.TopTenSkiers;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.haiqing.util.WriteToJson;

@WebServlet(name = "ResortServlet")
public class ResortServlet extends HttpServlet {

//  protected void doPost(HttpServletRequest request,
//      HttpServletResponse response)
//      throws ServletException, IOException {
//
//  }
  private Gson gson = new Gson();

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
      response.getWriter().write(gson.toJson(gson.toJson(new Message("string"))));
    } else {
      response.setStatus(HttpServletResponse.SC_OK);
      List<SkierVertical> skierVerticalList = new ArrayList<>();
      skierVerticalList.add(new SkierVertical(888899, 30400));
      TopTenSkiers topTenSkiers = new TopTenSkiers(skierVerticalList);
      response.getWriter().write(gson.toJson(topTenSkiers));
    }
  }

  private boolean isUrlValid(String[] urlPath, HttpServletRequest request) throws IllegalArgumentException {
    // /resort/day/top10vert?resort=XXX&dayID=XXX
    if (urlPath.length == 3) {
      System.out.println(111);
      return urlPath[1].equals("day") && urlPath[2].equals("top10vert")
          && request.getParameter("resort") != null && request.getParameter("dayID") != null;
    }
    return false;
  }
}
