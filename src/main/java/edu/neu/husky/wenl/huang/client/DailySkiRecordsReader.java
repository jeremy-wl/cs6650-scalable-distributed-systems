package edu.neu.husky.wenl.huang.client;

import edu.neu.husky.wenl.huang.client.http.GetClient;
import edu.neu.husky.wenl.huang.client.http.HTTPClient;
import edu.neu.husky.wenl.huang.client.utils.data.DataWriter;

import java.util.*;
import java.util.concurrent.*;

class DailySkiRecordsReader {
    private static final String ENDPOINT_GET_MY_VERTICALS = "/records/myvert";

    /**
     * Given n threads, and the range of skier ids (from 1 to X), returns a partitioned list of
     * smaller ranges each thread is responsible for.
     *
     * For example, if there are 10 threads, and skierIdRange = 30, then the result should be:
     *  - {1,  10}
     *  - {11, 20}
     *  - {21, 30}
     */

    static void read(int nThreads, int dayNum, int skierIdRange) {
        try {
            BlockingQueue<int[]> skierIdRanges = getRanges(nThreads, skierIdRange);
            CyclicBarrier barrier = new CyclicBarrier(nThreads);

            ExecutorService executor = Executors.newFixedThreadPool(nThreads);
            HTTPClient clientGetSkiRecords = new GetClient(Main.DOMAIN + ENDPOINT_GET_MY_VERTICALS);

            List<long[]> latencies = Collections.synchronizedList(new ArrayList<>());
            List<Future<int[]>> futures = new ArrayList<>();

            float wallTime;
            long wallTimeStart = System.nanoTime();

            for (int i = 0; i < nThreads; i++) {
                Callable<int[]> thread = new ReaderThread(barrier, clientGetSkiRecords, skierIdRanges,
                        latencies, dayNum);
                Future<int[]> res = executor.submit(thread);
                System.out.println(String.format("Submitted %d threads", i+1));
                futures.add(res);
            }

            executor.shutdown();  // stop accepting new jobs

            System.out.println("===============================================================");
            System.out.println("All threads running ...... Time: " + new Date(System.nanoTime()));
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

            wallTime = System.nanoTime() - wallTimeStart;

            System.out.println("===============================================================");
            System.out.println("All threads complete ...... Time: " + new Date(System.nanoTime()));
            System.out.println("---------------------------------------------------------------");
            System.out.println("Total number of requests sent: " + totalRequests);
            System.out.println("Total number of Successful responses: " + totalResponses);
            System.out.println(String.format("Test Wall Time: %.3f seconds", wallTime / 1_000_000_000));
            System.out.println("===============================================================");

            String testResultDir = Main.CLIENT_DIR + "data/test_results/reader/";

            System.out.println("Writing latency data to file ......");
            DataWriter.writeToFile(testResultDir, latencies);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static BlockingQueue<int[]> getRanges(int nThreads, int skierIdRange) {
        BlockingQueue<int[]> queue = new LinkedBlockingDeque<>();
        int amount = skierIdRange / nThreads;
        for (int start = 1, id = 1; start <= skierIdRange; start += amount, id++) {
            queue.add(new int[] {start, start + amount - 1});
        }
        return queue;
    }
}
