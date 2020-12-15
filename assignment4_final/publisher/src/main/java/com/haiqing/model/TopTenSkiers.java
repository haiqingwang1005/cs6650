package com.haiqing.model;

import java.util.ArrayList;
import java.util.List;

public class TopTenSkiers {
  private List<SkierVertical> topTenSkiers;

  public TopTenSkiers(List<SkierVertical> topTenList) {
    this.topTenSkiers = topTenList;
  }

  public List<SkierVertical> getTopTenList() {
    return topTenSkiers;
  }
}
