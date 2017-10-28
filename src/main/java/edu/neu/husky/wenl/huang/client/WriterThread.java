package edu.neu.husky.wenl.huang.client;

import edu.neu.husky.wenl.huang.client.http.HTTPClient;

import java.util.*;
import java.util.concurrent.*;

public class WriterThread extends AbstractThread<String> {

    WriterThread(CyclicBarrier barrier, HTTPClient client, BlockingQueue<String> requestParams, List<long[]> latencies) {
        super(barrier, client, requestParams, latencies);
    }

    @Override
    public int[] call() {
        while (!requestContent.isEmpty()) {
            String requestParam = this.requestContent.poll();

            long startTime = System.currentTimeMillis();

            doHttpRequest(client, requestParam);

//            System.out.println(this.requestParams.size() + " Requests to be sent in the queue");

            int latency = (int) (System.currentTimeMillis() - startTime);
            latencies.add(new long[] {startTime, latency});

            if (nRequests % 50 == 0) {
                System.out.println(String.format("tid: %d, requests sent: %d", Thread.currentThread().getId(), nRequests));
            }

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
