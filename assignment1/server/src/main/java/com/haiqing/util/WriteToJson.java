package com.haiqing.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class WriteToJson {

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

}
