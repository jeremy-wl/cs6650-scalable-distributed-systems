package edu.neu.husky.wenl.huang.client;

import edu.neu.husky.wenl.huang.client.http.HTTPClient;
import edu.neu.husky.wenl.huang.client.http.PostClient;

import java.util.*;
import java.util.concurrent.*;

public class MainTestClient {

    private static final int nThreads = 50;  // TODO: parameterize this as command line argument

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        BlockingQueue<String> requestBodies = new LinkedBlockingDeque<>();

        String dataSourcePath = "src/main/java/edu/neu/husky/wenl/huang/client/data/data_day1_10k.csv";
        CyclicBarrier barrier = new CyclicBarrier(nThreads);
        DataProcessor dp = new DataProcessor(requestBodies, dataSourcePath, barrier);

        ExecutorService executor = Executors.newFixedThreadPool(nThreads+1); // +1 csv reading thread
        HTTPClient httpClient = new PostClient();
        List<Integer> latencies = Collections.synchronizedList(new ArrayList<>());
        List<Future<int[]>> futures = new ArrayList<>();

        float wallTime;
        long wallTimeStart = System.currentTimeMillis();

        executor.submit(dp);

        for (int i = 0; i < nThreads; i++) {
            Callable<int[]> thread = new WorkerThread(barrier, httpClient, requestBodies, latencies);
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

        System.out.println("===============================================================");
        System.out.println("All threads complete ...... Time: " + new Date(System.currentTimeMillis()));
        System.out.println("---------------------------------------------------------------");
        System.out.println("Total number of requests sent: " + totalRequests);
        System.out.println("Total number of Successful responses: " + totalResponses);
        System.out.println(String.format("Test Wall Time: %.3f seconds", wallTime / 1000));
        System.out.println("===============================================================");

    }
}
