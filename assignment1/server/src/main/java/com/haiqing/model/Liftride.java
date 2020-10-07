package com.haiqing.model;

public class Liftride {
  private String resortID;
  private int dayID;
  private int skierID;
  private int time;
  private int liftID;

  public Liftride(String resortID, int dayID, int skierID, int time, int liftID) {
    this.resortID = resortID;
    this.dayID = dayID;
    this.skierID = skierID;
    this.time = time;
    this.liftID = liftID;
  }

  public String getResortID() {
    return resortID;
  }

  public int getDayID() {
    return dayID;
  }

  public int getSkierID() {
    return skierID;
  }

  public int getTime() {
    return time;
  }

  public int getLiftID() {
    return liftID;
  }
}
