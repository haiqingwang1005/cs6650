package io.swagger.client.util;

import io.swagger.client.PartTwo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CSVParser {
  private List<Long> getLatencyList;
  private List<Long> postLatencyList;
  private long totalWallTime = PartTwo.endTime - PartTwo.startTime;

  public CSVParser() throws IOException {
    this.getLatencyList = new ArrayList<>();
    this.postLatencyList = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new FileReader("output.csv"));
    reader.readLine();

    String row;
    while ((row = reader.readLine()) != null) {
      String[] data = row.split(",");
      if (data[1].equals("POST")) {
        postLatencyList.add(Long.parseLong(data[2]));
      } else {
        getLatencyList.add(Long.parseLong(data[2]));
      }
    }
    Collections.sort(getLatencyList);
    Collections.sort(postLatencyList);
  }

  public double getMeanResponseTimeForPost() {
    int total = 0;
    for (long l : postLatencyList) {
      total += l;
    }
    return total * 1.0 / postLatencyList.size();
  }

  public double getMeanResponseTimeForGet() {
    int total = 0;
    for (long l : getLatencyList) {
      total += l;
    }
    return total * 1.0 / getLatencyList.size();
  }

  public double getMedianResponseTimeForPost() {
    int size = postLatencyList.size();
    if (size % 2 == 0) {
      return (postLatencyList.get(size/2 - 1) + postLatencyList.get(size/2)) / 2.0;
    } else {
      return postLatencyList.get(size/2) * 1.0;
    }
  }

  public double getMedianResponseTimeForGet() {
    int size = getLatencyList.size();
    if (size % 2 == 0) {
      return (getLatencyList.get(size/2 - 1) + getLatencyList.get(size/2)) / 2.0;
    } else {
      return getLatencyList.get(size/2) * 1.0;
    }
  }

  public int getTotalWallTime() {
    return Math.toIntExact(this.totalWallTime);
  }

  public double getThroughput() {
    int totalRequest = getLatencyList.size() + postLatencyList.size();
    return totalRequest * 1000.0 / totalWallTime;
  }

  public int get99ResponseTimeGet() {
    int index = (int) ((int) getLatencyList.size() * 0.99);
    return Math.toIntExact(getLatencyList.get(index));
  }

  public int get99ResponseTimePost() {
    int index = (int) ((int) postLatencyList.size() * 0.99);
    return Math.toIntExact(postLatencyList.get(index));
  }

  public int maxPostResponseTime() {
    return Math.toIntExact(postLatencyList.get(postLatencyList.size() - 1));
  }

  public int maxGetResponseTime() {
    return Math.toIntExact(getLatencyList.get(getLatencyList.size() - 1));
  }

  public int getRequestCount() {
    return getLatencyList.size() + postLatencyList.size();
  }
}
