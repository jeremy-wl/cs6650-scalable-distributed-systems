package edu.neu.husky.wenl.huang.client;

import edu.neu.husky.wenl.huang.client.http.HTTPClient;

import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.*;

abstract class AbstractThread<T> implements Callable<int[]> {
    int nRequests;
    int nResponses;
    HTTPClient client;
    List<long[]> latencies;  // latency against time
    CyclicBarrier barrier;
    BlockingQueue<T> requestContent;

    AbstractThread(CyclicBarrier barrier, HTTPClient client,
                 BlockingQueue<T> requestContent, List<long[]> latencies) {
        this.barrier = barrier;
        this.client = client;
        this.requestContent = requestContent;
        this.latencies = latencies;
        this.nRequests = 0;
        this.nResponses = 0;
    }

    void doHttpRequest(HTTPClient client, String params) {
        nRequests++;
        Response res = client.request(params);
        if (res.getStatus() == 200) {
            nResponses++;
//            System.out.println(res.readEntity(String.class));
        }
        res.close();
    }
}
