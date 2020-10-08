package io.swagger.client;

import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import io.swagger.client.model.SkierVertical;
import io.swagger.client.util.CSVParser;
import io.swagger.client.util.CSVWriter;
import io.swagger.client.util.ParameterPropertiesValues;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PartTwo {
  private static Logger logger = LogManager.getLogger(PartTwo.class);
  private static CountDownLatch totalLatch;
  public static long startTime;
  public static long endTime;
  private static List<List<String>> lines;

  public static void main(String[] arguments) throws IOException, InterruptedException {
    ParameterPropertiesValues.initValues();

    lines = Collections.synchronizedList(new ArrayList<List<String>>());
    CSVWriter.writeTitleToCSV();

    int maxThreads;
    if (arguments.length >= 1) {
      maxThreads = Integer.parseInt(arguments[0]);
    } else {
      maxThreads = ParameterPropertiesValues.getMaxThreads();
    }

    System.out.println("Max Threads: " + maxThreads);

    int phaseOneThreads = maxThreads / 4;
    int phaseTwoThreads = maxThreads;
    int phaseThreeThreads = maxThreads / 4;

    totalLatch = new CountDownLatch(phaseOneThreads + phaseTwoThreads + phaseThreeThreads);

    // phase1

    startTime = System.currentTimeMillis();
    int numSkiers = ParameterPropertiesValues.getNumSkiers();
    int phaseOneGroupNumber = numSkiers / phaseOneThreads;
    CountDownLatch countDownLatch1 = new CountDownLatch((phaseOneThreads + 10)/10);
    int phasheOnestartSkierID = 1;
    int phaseOneendSkierID = phaseOneGroupNumber;
    for (int i = 0; i < phaseOneThreads; i++) {
      if (i == phaseOneThreads - 1) {
        phaseOneendSkierID = numSkiers;
      }

      Thread phaseOneThread = new SkierThread(countDownLatch1, phasheOnestartSkierID,
          phaseOneendSkierID, 1, 90, 100, 5);
      phaseOneThread.start();
      phasheOnestartSkierID += phaseOneGroupNumber;
      phaseOneendSkierID += phaseOneGroupNumber;
    }
    countDownLatch1.await();
    // phase 2
    int phaseTwoGroupNumber = numSkiers / phaseTwoThreads;
    int phaseTwoStartSkierID = 1;
    int phaseTwoEndSkierID = phaseTwoGroupNumber;
    CountDownLatch countDownLatch2 = new CountDownLatch((phaseTwoThreads + 10) / 10);
    for (int i = 0; i < phaseTwoThreads; i++) {
      if (i == phaseTwoThreads - 1) {
        phaseTwoEndSkierID = numSkiers;
      }

      Thread phaseTwoThread = new SkierThread(countDownLatch2,phaseTwoStartSkierID,
          phaseTwoEndSkierID, 91, 360, 100, 5);
      phaseTwoThread.start();
      phaseTwoStartSkierID += phaseTwoGroupNumber;
      phaseTwoEndSkierID += phaseTwoGroupNumber;
    }
    countDownLatch2.await();

    // phase 3
    int phaseThreeGroupNumber = numSkiers / phaseThreeThreads;
    CountDownLatch countDownLatch3 = new CountDownLatch((phaseThreeThreads + 10)/10);
    int phaseThreestartSkierID = 1;
    int phaseThreeendSkierID = phaseThreeGroupNumber;
    for (int i = 0; i < phaseThreeThreads; i++) {
      if (i == phaseThreeThreads - 1) {
        phaseThreeendSkierID = numSkiers;
      }
      Thread phaseThreeThread = new SkierThread(countDownLatch3,phaseThreestartSkierID,
          phaseThreeendSkierID, 361, 420, 100, 10);
      phaseThreeThread.start();
      phaseThreestartSkierID += phaseThreeGroupNumber;
      phaseThreeendSkierID += phaseThreeGroupNumber;
    }
    countDownLatch3.await();

    totalLatch.await();
    endTime = System.currentTimeMillis();
    CSVWriter.writeLinesToCSV(lines);
    printResults();
  }

  private static void printResults() throws IOException {
    CSVParser csvParser = new CSVParser();
    System.out.println(String.format("mean response time for POST: %f ms",
        csvParser.getMeanResponseTimeForPost()));
    System.out.println(String.format("mean response time for GET: %f ms",
        csvParser.getMeanResponseTimeForGet()));
    System.out.println(String.format("median response time for POST: %f ms",
        csvParser.getMedianResponseTimeForPost()));
    System.out.println(String.format("median response time for GET: %f ms",
        csvParser.getMedianResponseTimeForGet()));
    System.out.println(String.format("total wall time: %d ms",
        csvParser.getTotalWallTime()));
    System.out.println(String.format("total request amount: %d", csvParser.getRequestCount()));
    System.out.println(String.format("throughput: %f request/second",
        csvParser.getThroughput()));
    System.out.println(String.format("p99 response time for POST: %d ms",
        csvParser.get99ResponseTimePost()));
    System.out.println(String.format("p99 response time for GET: %d ms",
        csvParser.get99ResponseTimeGet()));
    System.out.println(String.format("max response time for POST: %d ms",
        csvParser.maxPostResponseTime()));
    System.out.println(String.format("max response time for GET: %d ms",
        csvParser.maxGetResponseTime()));
  }

  static class SkierThread extends Thread {
    private int startSkierID;
    private int endSkierID;
    private int startTime;
    private int endTime;
    private CountDownLatch latch;
    private int numOfPost;
    private int numOfGet;

    public SkierThread(CountDownLatch latch, int startSkierID, int endSkierID,
        int startTime, int endTime, int numOfPost, int numOfGet) {
      this.latch = latch;
      this.startSkierID = startSkierID;
      this.endSkierID = endSkierID;
      this.startTime = startTime;
      this.endTime = endTime;
      this.numOfPost = numOfPost;
      this.numOfGet = numOfGet;
    }

    @Override
    public void run() {
      int numLifts = ParameterPropertiesValues.getNumLifts();
      String resortID = ParameterPropertiesValues.getResortID();
      String dayID = ParameterPropertiesValues.getSkiDay();
      Random rand = new Random();

      int time = rand.nextInt(endTime + 1 - startTime) + startTime;
      SkiersApi skiersApiInstance = new SkiersApi();

      for (int i = 0; i < numOfPost; i++) {
        int span = endSkierID + 1 - startSkierID;
        if (span <= 0) {
          System.out.println(span);
        }

        int skierID = rand.nextInt(span) + startSkierID;

        int liftID = rand.nextInt(numLifts) + 1;
        try {
          LiftRide liftRide = new LiftRide();
          liftRide.setDayID(dayID);
          liftRide.setLiftID(String.valueOf(liftID));
          liftRide.setResortID(resortID);
          liftRide.setSkierID(String.valueOf(skierID));
          liftRide.setTime(String.valueOf(time));
          long sendTime = System.currentTimeMillis();
          ApiResponse<Void> apiResponse = skiersApiInstance.writeNewLiftRide(liftRide);
          int code = apiResponse.getStatusCode();

          long receiveTime = System.currentTimeMillis();
          long latency = receiveTime - sendTime;

          List<String> data = Arrays.asList(String.valueOf(sendTime), "POST",
              String.valueOf(latency), String.valueOf(code));

          lines.add(data);
        } catch (ApiException e) {
          logger.error("Fail to post in Phase", e);
        }
      }

      for (int i = 0; i < numOfGet; i++) {
        int skierID = rand.nextInt(endSkierID + 1 - startSkierID) + startSkierID;
        try {
          long sendTime = System.currentTimeMillis();
          ApiResponse<SkierVertical> apiResponse = skiersApiInstance.getSkierDayVertical(resortID, dayID, String.valueOf(skierID));
          int code = apiResponse.getStatusCode();
          long receiveTime = System.currentTimeMillis();
          long latency = receiveTime - sendTime;
          List<String> data = Arrays.asList(String.valueOf(sendTime), "GET",
              String.valueOf(latency), String.valueOf(code));

          lines.add(data);
        } catch (ApiException e) {
          logger.error("Fail to get in Phase", e);
        }
      }
      latch.countDown();
      totalLatch.countDown();
    }
  }

}
