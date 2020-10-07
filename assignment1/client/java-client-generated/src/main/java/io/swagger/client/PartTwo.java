package io.swagger.client;

import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import io.swagger.client.model.SkierVertical;
import io.swagger.client.util.CSVWriter;
import io.swagger.client.util.ParameterPropertiesValues;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PartTwo {
  private static Logger logger = LogManager.getLogger(PartTwo.class);
  private static CountDownLatch totalLatch;
  private static List<List<String>> allData = new ArrayList<>();

  public static void main(String[] arguments) throws IOException, InterruptedException {
    ParameterPropertiesValues.initValues();

    int maxThreads = ParameterPropertiesValues.getMaxThreads();
    int phaseOneThreads = maxThreads / 4;
    int phaseTwoThreads = maxThreads;
    int phaseThreeThreads = maxThreads / 4;

    totalLatch = new CountDownLatch(phaseOneThreads + phaseTwoThreads + phaseThreeThreads + 3);


    // p1
    long startTime = System.currentTimeMillis();

    int numSkiers = ParameterPropertiesValues.getNumSkiers();
    int phaseOneGroupNumber = numSkiers / phaseOneThreads;
    CountDownLatch countDownLatch1 = new CountDownLatch((phaseOneThreads + 10)/10);
    int phasheOnestartSkierID = 1;
    int phaseOneendSkierID = phaseOneGroupNumber;
    for (int i = 0; i <= phaseOneThreads; i++) {
      if (i == phaseOneThreads) {
        phaseOneendSkierID = numSkiers;
      }
      Thread phaseOneThread = new SkierThread(countDownLatch1,phasheOnestartSkierID,
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
    for (int i = 0; i <= phaseTwoThreads; i++) {
      if (i == phaseTwoThreads) {
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
    for (int i = 0; i <= phaseThreeThreads; i++) {
      if (i == phaseThreeThreads) {
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


    long endTime = System.currentTimeMillis();
    CSVWriter.writeToCSV(allData);

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
        int skierID = rand.nextInt(endSkierID + 1 - startSkierID) + startSkierID;
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
          System.out.println(String.format("start time: %d, latency: %d, code: %d, method: POST",
              sendTime, latency, code));
          List<String> data = Arrays.asList(String.valueOf(sendTime), "POST",
              String.valueOf(latency), String.valueOf(code));
          allData.add(data);
        } catch (ApiException e) {
          logger.error("Fail to post in Phase 1", e);
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
          allData.add(data);
        } catch (ApiException e) {
          logger.error("Fail to get in Phase 1", e);
        }
      }
      latch.countDown();
      totalLatch.countDown();
    }
  }

}