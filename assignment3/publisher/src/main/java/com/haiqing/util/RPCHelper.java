package com.haiqing.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.haiqing.model.Liftride;
import com.haiqing.model.Liftride.LiftrideBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;

public class RPCHelper {

  public static JsonObject writeInvalidInput() {
    JsonObject message = new JsonObject();
    message.addProperty("message", "invalid input");
    return message;
  }

  public static JsonObject writeDataNotFound() {
    JsonObject message = new JsonObject();
    message.addProperty("message", "data not found");
    return message;
  }

  public static JsonObject writeSkiersResponseToJson() {
    System.out.println("write1");
    JsonObject message = new JsonObject();
    message.addProperty("resortId", "Mission Ridge");
    message.addProperty("totalVert", 56734);
    System.out.println("write2");
    return message;
  }

  public static JsonObject writeResortResponseToJson() {
    JsonObject message = new JsonObject();
    JsonArray topTenSkiers = new JsonArray();
    JsonObject element = new JsonObject();
    element.addProperty("skierID", 888899);
    element.addProperty("VertcialTotal", 30400);
    topTenSkiers.add(element);
    message.addProperty("topTenSkierstopTenSkiers", topTenSkiers.getAsString());
    return message;
  }

  public static JsonObject readJSONObject(HttpServletRequest request) throws IOException {
    Gson gson = new Gson();
    BufferedReader reader = new BufferedReader(request.getReader());
    return gson.fromJson(reader, JsonObject.class);
  }

  public static Liftride parseLiftride(JsonObject liftride) {
    LiftrideBuilder builder = new LiftrideBuilder();
    //System.out.println(liftride.toString());
    builder.setLiftID(liftride.get("liftID").getAsInt());
    builder.setResortID(liftride.get("resortID").getAsString());
    builder.setDayID(liftride.get("dayID").getAsInt());
    builder.setSkierID(liftride.get("skierID").getAsInt());
    builder.setTime(liftride.get("time").getAsInt());
    return builder.build();
  }


}
