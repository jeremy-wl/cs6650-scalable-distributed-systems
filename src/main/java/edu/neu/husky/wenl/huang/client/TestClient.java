package edu.neu.husky.wenl.huang.client;

import java.util.*;
import java.util.concurrent.*;

public class TestClient {
    public static final int nThreads    = 100;   // TODO: change to user input as args
    public static final int nIterations = 100;  // TODO: change to user input as args

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("===============================================================");
        System.out.println("Client starting ...... Time: " + new Date(System.currentTimeMillis()));
        System.out.println("===============================================================");

        float wallTime;
        long wallTimeStart = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        CyclicBarrier barrier = new CyclicBarrier(nThreads);
        HTTPClient client = new HTTPClient("34.215.15.228", "8080");  // TODO: change to user input
        List<Future<int[]>> futures = new ArrayList<>();
        List<Integer> latencies = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < nThreads; i++) {
            Callable<int[]> thread = new WorkerThread(nIterations, barrier, client, latencies);
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

        Collections.sort(latencies);
        int latencyMedian  = SpreadsheetTool.getMedian(latencies);
        double latencyMean = SpreadsheetTool.getMean(latencies);
        int latencyPercentile95 = SpreadsheetTool.nthPercentile(95, latencies);
        int latencyPercentile99 = SpreadsheetTool.nthPercentile(99, latencies);

        System.out.println("===============================================================");
        System.out.println("All threads complete ...... Time: " + new Date(System.currentTimeMillis()));
        System.out.println("---------------------------------------------------------------");
        System.out.println("Total number of requests sent: " + totalRequests);
        System.out.println("Total number of Successful responses: " + totalResponses);
        System.out.println(String.format("Test Wall Time: %.3f seconds", wallTime / 1000));
        System.out.println("Mean   latencies for all requests: " + latencyMean);
        System.out.println("Median latencies for all requests: " + latencyMedian);
        System.out.println("95th percentile latency: " + latencyPercentile95);
        System.out.println("99th percentile latency: " + latencyPercentile99);
        System.out.println("===============================================================");
    }
}
