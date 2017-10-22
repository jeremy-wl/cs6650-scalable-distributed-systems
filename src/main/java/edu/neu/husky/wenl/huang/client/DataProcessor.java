package edu.neu.husky.wenl.huang.client;


import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class DataProcessor implements Runnable {
    private BlockingQueue<String> queue;
    private String dataSourcePath;

    public DataProcessor(BlockingQueue<String> queue, String dataSourcePath) {
        this.queue = queue;   // this queue is shared with WorkerThread, WorkerThreads send http
        this.dataSourcePath = dataSourcePath; // requests while DataProcessor read & add entry to q
    }

    @Override
    public void run() {
        String line;
        String[] keys = null;
        int records = -1;

        try {
            String data = readFromFile(dataSourcePath);  // read all content from csv
            try (BufferedReader bufferedReader = new BufferedReader(new StringReader(data))) {
                StringBuilder batchRecords = new StringBuilder("[");

                // reads each record convert it as a json string for future http requests
                while ((line = bufferedReader.readLine()) != null) {
                    if (++records == 0) {
                        keys = line.split(",");
                        continue;
                    }

                    String[] vals = line.split(",");
                    String serializedJSON =
                            String.format("{\"%s\":%s,\"%s\":%s,\"%s\":%s,\"%s\":%s,\"%s\":%s},",
                                    keys[0], vals[0], keys[1], vals[1], keys[2], vals[2],
                                    keys[3], vals[3], keys[4], vals[4]);
                    batchRecords.append(serializedJSON);
//                    System.out.println(serializedJSON);

                    if (records % 25 == 0) {
                        batchRecords.setCharAt(batchRecords.length()-1, ']');
                        queue.add(batchRecords.toString());
                        batchRecords = new StringBuilder("[");
                    }
                }
                System.out.println("Total records: " + records);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads all file content into a String
     */
    private String readFromFile(String filePath) throws IOException {
        return new Scanner(new File(filePath)).useDelimiter("\\Z").next();
    }
}
