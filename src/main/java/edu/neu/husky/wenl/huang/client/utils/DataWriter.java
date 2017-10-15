package edu.neu.husky.wenl.huang.client.utils;

import java.io.*;
import java.util.*;

public class DataWriter {
    public static void writeToFile(String dirPath, List<long[]> latencies) {
        String fileName = getFileName(dirPath);

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

    /**
     * Given a directory path, returns a proper file name for test data.
     */
    private static String getFileName(String dirPath) {
        File[] files = new File(dirPath).listFiles();
        if (files.length == 0) return "results1.csv";

        String filePath = files[files.length-1].toString();
        String[] pathParts = filePath.split("/");
        String[] nameParts = pathParts[pathParts.length-1].split("(?=[\\d|.]+)");
        String fileName = nameParts[0];
        int nameNumbering = Integer.valueOf(nameParts[1]);
        String fileExt = nameParts[2].substring(1);

        return String.format("%s%d.%s", fileName, nameNumbering+1, fileExt);
    }
}
