package io.swagger.client.util;

import io.swagger.client.PartTwo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CSVParser {
  private List<Long> getLatencyList1;
  private List<Long> getLatencyList2;
  private List<Long> postLatencyList;
  private long totalWallTime = PartTwo.endTime - PartTwo.startTime;

  public CSVParser() throws IOException {
    this.getLatencyList1 = new ArrayList<>();
    this.getLatencyList2 = new ArrayList<>();
    this.postLatencyList = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader("output.csv"));
    reader.readLine();

    String row;
    while ((row = reader.readLine()) != null) {
      String[] data = row.split(",");
      if (data[1].equals("POST")) {
        postLatencyList.add(Long.parseLong(data[2]));
      } else if (data[1].equals("GET1")){
        getLatencyList1.add(Long.parseLong(data[2]));
      } else {
        getLatencyList2.add(Long.parseLong(data[2]));
      }
    }
    Collections.sort(getLatencyList1);
    Collections.sort(getLatencyList2);
    Collections.sort(postLatencyList);
  }

  public double getMeanResponseTimeForPost() {
    int total = 0;
    for (long l : postLatencyList) {
      total += l;
    }
    return total * 1.0 / postLatencyList.size();
  }

  public double getMeanResponseTimeForGet1() {
    int total = 0;
    for (long l : getLatencyList1) {
      total += l;
    }
    return total * 1.0 / getLatencyList1.size();
  }

  public double getMeanResponseTimeForGet2() {
    int total = 0;
    for (long l : getLatencyList2) {
      total += l;
    }
    return total * 1.0 / getLatencyList2.size();
  }

  public double getMedianResponseTimeForPost() {
    int size = postLatencyList.size();
    if (size % 2 == 0) {
      return (postLatencyList.get(size/2 - 1) + postLatencyList.get(size/2)) / 2.0;
    } else {
      return postLatencyList.get(size/2) * 1.0;
    }
  }

  public double getMedianResponseTimeForGet1() {
    int size = getLatencyList1.size();
    if (size % 2 == 0) {
      return (getLatencyList1.get(size/2 - 1) + getLatencyList1.get(size/2)) / 2.0;
    } else {
      return getLatencyList1.get(size/2) * 1.0;
    }
  }

  public double getMedianResponseTimeForGet2() {
    int size = getLatencyList2.size();
    if (size % 2 == 0) {
      return (getLatencyList2.get(size/2 - 1) + getLatencyList2.get(size/2)) / 2.0;
    } else {
      return getLatencyList2.get(size/2) * 1.0;
    }
  }

  public int getTotalWallTime() {
    return Math.toIntExact(this.totalWallTime);
  }

  public double getThroughput() {
    int totalRequest = getLatencyList1.size() + getLatencyList2.size() + postLatencyList.size();
    return totalRequest * 1000.0 / totalWallTime;
  }


  public int get99ResponseTimeGet1() {
    int index = (int) ((int) getLatencyList1.size() * 0.99);
    return Math.toIntExact(getLatencyList1.get(index));
  }

  public int get99ResponseTimeGet2() {
    int index = (int) ((int) getLatencyList2.size() * 0.99);
    return Math.toIntExact(getLatencyList2.get(index));
  }

  public int get99ResponseTimePost() {
    int index = (int) ((int) postLatencyList.size() * 0.99);
    return Math.toIntExact(postLatencyList.get(index));
  }

  public int maxPostResponseTime() {
    return Math.toIntExact(postLatencyList.get(postLatencyList.size() - 1));
  }

  public int maxGet1ResponseTime() {
    return Math.toIntExact(getLatencyList1.get(getLatencyList1.size() - 1));
  }

  public int maxGet2ResponseTime() {
    return Math.toIntExact(getLatencyList2.get(getLatencyList2.size() - 1));
  }

  public int getRequestCount() {
    return getLatencyList1.size() + getLatencyList2.size()+ postLatencyList.size();
  }
}
