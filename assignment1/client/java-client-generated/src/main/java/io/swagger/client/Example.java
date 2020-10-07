package io.swagger.client;

import com.google.gson.JsonObject;
import io.swagger.client.api.ResortsApi;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import io.swagger.client.model.TopTen;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Example {
  public static void main(String[] args) {

    // resort api
    ResortsApi resortsApiInstance = new ResortsApi();
    List<String> resort = Arrays.asList("resort_example"); // List<String> | resort to query by
    List<String> dayID = Arrays.asList("dayID_example"); // List<String> | day number in the season
    try {
      TopTen result = resortsApiInstance.getTopTenVert(resort, dayID);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling ResortsApi#getTopTenVert");
      e.printStackTrace();
    }

    // skiers api
    SkiersApi skiersApiInstance = new SkiersApi();
    LiftRide liftRide = new LiftRide();
    try {
      skiersApiInstance.getSkierDayVertical("123", "123", "123");
    } catch (ApiException e) {
      e.printStackTrace();
    }
    try {
      skiersApiInstance.getSkierResortTotals("123", Arrays.asList("123"));
    } catch (ApiException e) {
      e.printStackTrace();
    }
    try {
      skiersApiInstance.writeNewLiftRide(new LiftRide());
    } catch (ApiException e) {
      e.printStackTrace();
    }
  }

}
