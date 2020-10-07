package io.swagger.client.util;

import io.swagger.client.ApiResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {
  public static void writeToCSV(List<List<String>> input) throws IOException {
    FileWriter csvWriter = new FileWriter("output.csv");
    csvWriter.append("sendTime").append(",");
    csvWriter.append("requestType").append(",");;
    csvWriter.append("latency").append(",");;
    csvWriter.append("responseCode").append("\n");;
    for (List<String> data : input) {
      csvWriter.append(String.join(",", data)).append("\n");
    }
    csvWriter.flush();
    csvWriter.close();

  }

}
