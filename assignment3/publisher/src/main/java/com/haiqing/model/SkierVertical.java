package com.haiqing.model;

public class SkierVertical {
  private int skierID;
  private int VertcialTotal;

  public SkierVertical(int skierID, int verticalTotal) {
    this.skierID = skierID;
    this.VertcialTotal = verticalTotal;
  }

  public int getSkierID() {
    return skierID;
  }

  public int getVerticalTotal() {
    return VertcialTotal;
  }
}
