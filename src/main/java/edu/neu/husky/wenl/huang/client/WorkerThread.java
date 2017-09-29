package edu.neu.husky.wenl.huang.client;

import java.util.concurrent.*;

public class WorkerThread implements Callable<long[]> {
    private int nIterations;
    public int nRequests;
    public int nResponses;
    private HTTPClient client;
    private CyclicBarrier barrier;

    public WorkerThread(int nIterations, CyclicBarrier barrier, HTTPClient client) {
        this.nIterations = nIterations;
        this.barrier = barrier;
        this.client = client;
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

    public long[] call() {
        long startTime = System.currentTimeMillis();
        long endTime;
        for (int i = 0; i < nIterations; i++) {
            doGet(client);
            doPost(client);
//            System.out.println(String.format("tid: %d, requests sent: %d", Thread.currentThread().getId(), nRequests));
        }
//        System.out.println(String.format("# of waiting at barrier now = %d", barrier.getNumberWaiting() + 1));
        endTime = System.currentTimeMillis();
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        return new long[] {this.nRequests, this.nResponses, startTime, endTime};
    }
}
