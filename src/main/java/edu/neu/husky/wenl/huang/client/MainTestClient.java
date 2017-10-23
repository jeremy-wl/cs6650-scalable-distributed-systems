package edu.neu.husky.wenl.huang.client;

import edu.neu.husky.wenl.huang.client.http.GetClient;
import edu.neu.husky.wenl.huang.client.http.HTTPClient;
import edu.neu.husky.wenl.huang.client.http.PostClient;
import edu.neu.husky.wenl.huang.client.utils.DataWriter;

import java.util.*;
import java.util.concurrent.*;

public class MainTestClient {

    private static final int N_THREADS = 100;  // TODO: parameterize this as command line argument
    private static final String CLIENT_DIR = "src/main/java/edu/neu/husky/wenl/huang/client/";
    private static final String DOMAIN = "http://localhost:8080";
    private static final String ENDPOINT_LOAD_LIFT_RECORDS = "/api/records/load-lift-records";
    private static final String ENDPOINT_GEN_DAILY_SKI_RECORDS = "/api/records/generate-daily-ski-records";
    private static final String ENDPOINT_GET_MY_VERTICALS = "/api/records/myvert";
    private static final int DAY_NUM = 1;


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        BlockingQueue<String> requestBodies = new LinkedBlockingDeque<>();

        String dataSourcePath = CLIENT_DIR + "data/data_day1_100k.csv"; // FIXME: change to 800k data
        CyclicBarrier barrier = new CyclicBarrier(N_THREADS);
        DataProcessor dp = new DataProcessor(requestBodies, dataSourcePath, barrier);

        ExecutorService executor = Executors.newFixedThreadPool(N_THREADS+1); // +1 csv reading thread
        HTTPClient clientLoadLiftRecords = new PostClient(DOMAIN + ENDPOINT_LOAD_LIFT_RECORDS);
        List<long[]> latencies = Collections.synchronizedList(new ArrayList<>());
        List<Future<int[]>> futures = new ArrayList<>();

        float wallTime;
        long wallTimeStart = System.currentTimeMillis();

        executor.submit(dp);

        for (int i = 0; i < N_THREADS; i++) {
            Callable<int[]> thread = new WorkerThread(barrier, clientLoadLiftRecords, requestBodies,
                                                      latencies);
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

        String testResultDir = CLIENT_DIR + "data/test_results/";


        System.out.println("Writing latency data to file ......");
        DataWriter.writeToFile(testResultDir, latencies);



        System.out.println("===============================================================");

        long timeStart = System.currentTimeMillis();
        System.out.println("Generating daily ski records for all skiers...... Time: "
                            + new Date(System.currentTimeMillis()));

        HTTPClient clientGenDailySkiRecords = new GetClient(DOMAIN + ENDPOINT_GEN_DAILY_SKI_RECORDS);
        clientGenDailySkiRecords.request("day=" + DAY_NUM);

        System.out.println("---------------------------------------------------------------");
        System.out.printf("It took %.3f seconds to finish.", (System.currentTimeMillis() - timeStart) / 1000.0);
    }
}
