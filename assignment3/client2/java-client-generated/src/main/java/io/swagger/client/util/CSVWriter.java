package io.swagger.client.util;

import io.swagger.client.ApiResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {
  public static void writeTitleToCSV() throws IOException {
    FileWriter csvWriter = new FileWriter("output.csv");
    csvWriter.append("sendTime").append(",");
    csvWriter.append("requestType").append(",");
    csvWriter.append("latency").append(",");;
    csvWriter.append("responseCode").append("\n");

    csvWriter.flush();
    csvWriter.close();
  }
  public static void writeLineToCSV(List<String> data) throws IOException {
    FileWriter csvWriter = new FileWriter("output.csv", true);

    csvWriter.append(String.join(",", data)).append("\n");

    csvWriter.flush();
    csvWriter.close();
  }

  public static void writeLinesToCSV(List<List<String>> lines) throws IOException {
    FileWriter csvWriter = new FileWriter("output.csv", true);

    for (List<String> data: lines) {
      csvWriter.append(String.join(",", data)).append("\n");
    }

    csvWriter.flush();
    csvWriter.close();

  }

}
