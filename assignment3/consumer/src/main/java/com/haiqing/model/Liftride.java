package com.haiqing.model;

import com.google.gson.JsonObject;

public class Liftride {
  private String resortID;
  private int dayID;
  private int skierID;
  private int time;
  private int liftID;

  private Liftride(LiftrideBuilder builder) {
    this.resortID = builder.resortID;
    this.dayID = builder.dayID;
    this.skierID = builder.skierID;
    this.time = builder.time;
    this.liftID = builder.liftID;
  }

  public String getResortID() {
    return resortID;
  }

  public int getDayID() {
    return dayID;
  }

  public int getSkierID() { return skierID; }

  public int getTime() {
    return time;
  }

  public int getLiftID() {
    return liftID;
  }


  public JsonObject toJSONObject() {
    JsonObject obj = new JsonObject();
    obj.addProperty("lift_id", liftID);
    obj.addProperty("resort_id", resortID);
    obj.addProperty("day_id", dayID);
    obj.addProperty("skier_id", skierID);
    obj.addProperty("time", time);
    return obj;
  }


  public static class LiftrideBuilder {
    private String resortID;
    private int dayID;
    private int skierID;
    private int time;
    private int liftID;

    public void setResortID(String resortID) {
      this.resortID = resortID;
    }

    public void setDayID(int dayID) {
      this.dayID = dayID;
    }

    public void setSkierID(int skierID) {
      this.skierID = skierID;
    }

    public void setTime(int time) {
      this.time = time;
    }

    public void setLiftID(int liftID) {
      this.liftID = liftID;
    }

    public Liftride build() {
      return new Liftride(this);
    }
  }
}
