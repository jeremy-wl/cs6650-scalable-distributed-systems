package edu.neu.husky.wenl.huang.client;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(dataSourcePath))) {

            // reads each record in csv and convert it as a json string for future http requests
            while ((line = bufferedReader.readLine()) != null) {
                if (++records == 0) {
                    keys = line.split(",");
                    continue;
                }
                String[] vals = line.split(",");
                String serializedJSON =
                        String.format("{\"%s\":%s,\"%s\":%s,\"%s\":%s,\"%s\":%s,\"%s\":%s}",
                                        keys[0], vals[0], keys[1], vals[1], keys[2], vals[2],
                                        keys[3], vals[3], keys[4], vals[4]);
                queue.add(serializedJSON);
//                System.out.println(serializedJSON);
            }
            System.out.println("Total records: " + records);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
