package edu.neu.husky.wenl.huang.client;

import java.util.*;
import java.util.concurrent.*;

public class TestClient {
    public static final int nThreads = 10;  // TODO: change to user input as args

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("===============================================================");
        System.out.println("Client starting ...... Time: " + new Date(System.currentTimeMillis()));
        System.out.println("===============================================================");

        float wallTime;
        long wallTimeStart = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        CyclicBarrier barrier = new CyclicBarrier(nThreads);
        HTTPClient client = new HTTPClient("34.215.15.228", "8080");  // TODO: change to user input
        List<Future<long[]>> futures = new ArrayList<>();

        for (int i = 0; i < nThreads; i++) {
            Callable<long[]> thread = new WorkerThread(100, barrier, client);
            Future<long[]> res = executor.submit(thread);
            System.out.println(String.format("Submitted %d threads", i+1));
            futures.add(res);
        }

        executor.shutdown();  // stop accepting new jobs

        System.out.println("===============================================================");
        System.out.println("All threads running ...... Time: " + new Date(System.currentTimeMillis()));
        System.out.println("---------------------------------------------------------------");

        int totalRequests = 0, totalResponses = 0;

        for (int i = 0; i < futures.size(); i++) {
            long[] res = futures.get(i).get();
            long nReq = res[0], nRes = res[1];
            long startTime = res[2], endTime = res[3];
            totalRequests  += nReq;
            totalResponses += nRes;
            long time = endTime - startTime;

            System.out.println(
               String.format(
                   "Thread %03d: took %d ms, %d requests send, %d responses received",
                   i+1, time, nReq, nRes
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
    }
}
