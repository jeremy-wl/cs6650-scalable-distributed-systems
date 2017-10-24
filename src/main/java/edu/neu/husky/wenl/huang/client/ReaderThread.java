package edu.neu.husky.wenl.huang.client;

import edu.neu.husky.wenl.huang.client.http.HTTPClient;

import java.util.*;
import java.util.concurrent.*;

class ReaderThread extends AbstractThread<int[]> {
    private int dayNum;

    ReaderThread(CyclicBarrier barrier, HTTPClient client,
                        BlockingQueue<int[]> ranges, List<long[]> latencies, int dayNum) {
        super(barrier, client, ranges, latencies);
        this.dayNum = dayNum;
    }

    @Override
    public int[] call() {
        // one range for each thread guaranteed, so there is no need to check isEmpty() for queue
        int[] range = requestContent.poll();  // range[0] = ID_START, range[1] = ID_END,

        for (int id = range[0]; id <= range[1]; id++) {
            long startTime = System.currentTimeMillis();
            String queryString = String.format("skierId=%d&dayNum=%d", id, dayNum);
            doHttpRequest(client, queryString);
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
