package edu.neu.husky.wenl.huang.client;

import edu.neu.husky.wenl.huang.client.http.HTTPClient;

import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.*;

public class WorkerThread implements Callable<int[]> {
    public int nRequests;
    public int nResponses;
    private HTTPClient client;
    private List<long[]> latencies;  // latency against time
    private CyclicBarrier barrier;
    private BlockingQueue<String> requestBodies;

    public WorkerThread(CyclicBarrier barrier, HTTPClient client,
                        BlockingQueue<String> requestBodies, List<long[]> latencies) {
        this.barrier = barrier;
        this.client = client;
        this.requestBodies = requestBodies;
        this.latencies = latencies;
        this.nRequests = 0;
        this.nResponses = 0;
    }

    public void doHttpRequest(HTTPClient client, String params) {
        nRequests++;
        Response res = client.request(params);
        if (res.getStatus() == 200) {
            nResponses++;
        }
        res.close();
    }

    public int[] call() {
        while (!requestBodies.isEmpty()) {
            String requestBody = requestBodies.poll();

            long startTime = System.currentTimeMillis();

            doHttpRequest(client, requestBody);

//            System.out.println(requestBodies.size() + " Requests to be sent in the queue");

            int latency = (int) (System.currentTimeMillis() - startTime);
            latencies.add(new long[] {startTime, latency});

            System.out.println(String.format("tid: %d, requests sent: %d", Thread.currentThread().getId(), nRequests));

        }

        System.out.println(String.format("# of waiting at barrier now = %d", barrier.getNumberWaiting() + 1));
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        return new int[] { this.nRequests, this.nResponses };
    }
}
