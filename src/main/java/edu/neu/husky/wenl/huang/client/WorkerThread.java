package edu.neu.husky.wenl.huang.client;

import java.util.*;
import java.util.concurrent.*;

public class WorkerThread implements Callable<int[]> {
    private int nIterations;
    public int nRequests;
    public int nResponses;
    private HTTPClient client;
    private List<Integer> latencies;
    private CyclicBarrier barrier;

    public WorkerThread(int nIterations, CyclicBarrier barrier,
                        HTTPClient client, List<Integer> latencies) {
        this.nIterations = nIterations;
        this.barrier = barrier;
        this.client = client;
        this.latencies = latencies;
        this.nRequests = 0;
        this.nResponses = 0;
    }

    public void doGet(HTTPClient client) {
        nRequests++;
        String res = client.getStatus();
        if (res.equals("alive")) {
            nResponses++;
        }
    }

    public void doPost(HTTPClient client) {
        nRequests++;
        String res = client.postText("12345", String.class);
        if (res.equals("5")) {
            nResponses++;
        }
    }

    public int[] call() {
        for (int i = 0; i < nIterations; i++) {
            long startTime = System.currentTimeMillis();
            doGet(client);
            int latency = (int) (System.currentTimeMillis() - startTime);
            latencies.add(latency);

            startTime = System.currentTimeMillis();
            doPost(client);
            latency = (int) (System.currentTimeMillis() - startTime);
            latencies.add(latency);
//            System.out.println(String.format("tid: %d, requests sent: %d", Thread.currentThread().getId(), nRequests));
        }
//        System.out.println(String.format("# of waiting at barrier now = %d", barrier.getNumberWaiting() + 1));
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        return new int[] {this.nRequests, this.nResponses};
    }
}
