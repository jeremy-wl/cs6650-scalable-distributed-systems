package edu.neu.husky.wenl.huang.client;

import edu.neu.husky.wenl.huang.client.http.HTTPClient;
import edu.neu.husky.wenl.huang.client.http.PostClient;
import edu.neu.husky.wenl.huang.client.utils.data.DataWriter;

import java.util.*;
import java.util.concurrent.*;

class LiftRecordsWriter {

    private static final String ENDPOINT_LOAD_LIFT_RECORDS = "/api/records/load-lift-records";

    static void write(int nThreads) {
        try {
            BlockingQueue<String> requestBodies = new LinkedBlockingDeque<>();

            String dataSourcePath = Main.CLIENT_DIR + "data/data_day999_60k.csv";
            CyclicBarrier barrier = new CyclicBarrier(nThreads);
            DataProcessor dp = new DataProcessor(requestBodies, dataSourcePath);

            ExecutorService executor = Executors.newFixedThreadPool(nThreads+1); // +1 csv reading thread
            HTTPClient clientLoadLiftRecords = new PostClient(Main.DOMAIN + ENDPOINT_LOAD_LIFT_RECORDS);
            List<long[]> latencies = Collections.synchronizedList(new ArrayList<>());
            List<Future<int[]>> futures = new ArrayList<>();

            float wallTime;
            long wallTimeStart = System.currentTimeMillis();

            executor.submit(dp);

            for (int i = 0; i < nThreads; i++) {
                AbstractThread<String> thread = new WriterThread(barrier, clientLoadLiftRecords,
                                                                 requestBodies, latencies);
                Future<int[]> res = executor.submit(thread);
                System.out.println(String.format("Submitted %d threads", i+1));
                futures.add(res);
            }

            executor.shutdown();  // stop accepting new jobs

            System.out.println("===============================================================");
            System.out.println("All threads running ...... Time: " + new Date(System.currentTimeMillis()));
            System.out.println("---------------------------------------------------------------");

            int totalRequests = 0, totalResponses = 0;

            for (int i = 0; i < futures.size(); i++) {
                int[] res = futures.get(i).get();
                int nReq = res[0], nRes = res[1];
                totalRequests  += nReq;
                totalResponses += nRes;

                System.out.println(
                        String.format(
                                "Thread %03d: %d requests send, %d responses received", i+1, nReq, nRes
                        )
                );
            }

            wallTime = System.currentTimeMillis() - wallTimeStart;

            System.out.println("===============================================================");
            System.out.println("All threads complete ...... Time: " + new Date(System.currentTimeMillis()));
            System.out.println("---------------------------------------------------------------");
            System.out.println("Total number of requests sent: " + totalRequests);
            System.out.println("Total number of Successful responses: " + totalResponses);
            System.out.println(String.format("Test Wall Time: %.3f seconds", wallTime / 1000));
            System.out.println("===============================================================");

            String testResultDir = Main.CLIENT_DIR + "data/test_results/writer/";


            System.out.println("Writing latency data to file ......");
            DataWriter.writeToFile(testResultDir, latencies);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
