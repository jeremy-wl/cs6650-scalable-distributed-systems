package edu.neu.husky.wenl.huang.client.utils.data;

import java.io.*;
import java.util.*;

public class DataWriter {
    public static void writeToFile(String dirPath, List<long[]> latencies) {
        String fileName = String.format("%d_%d.csv", System.currentTimeMillis(), latencies.size());

        try (BufferedWriter outputFile = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(dirPath + fileName), "UTF-8"))) {

            for (long[] l : latencies) {
                long time    = l[0];
                long latency = l[1];

                outputFile.write(time + "," + latency + "\n");
            }
        } catch (IOException ioe) {
            System.out.println("Something went wrong! : " + ioe.getMessage());
        }
    }
}
