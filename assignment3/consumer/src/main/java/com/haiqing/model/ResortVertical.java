package com.haiqing.model;

public class ResortVertical {
  private String resortID;
  private int totalVert;

  public ResortVertical(String resortID, int totalVert) {
    this.resortID = resortID;
    this.totalVert = totalVert;
  }

  public String getResortID() {
    return resortID;
  }

  public int getTotalVert() {
    return totalVert;
  }
}
