package edu.neu.husky.wenl.huang.client.http;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class GetClient implements HTTPClient {
    private WebTarget target;

    public GetClient(String url) {
        this.target = ClientBuilder.newClient().target(url);
    }

    public Response request(String params) {     // params is like "a=2&b=3&c=4"
        String[] paramList = params.split("&");
        WebTarget newTarget = target;               // Bug: should not assign this.target
                                                    //      to queryParam below, because
        for (String param : paramList) {            //      the client is shared among threads
            String[] keyValPair = param.split("="); //      and target val should not be changed
            String key = keyValPair[0];
            String val = keyValPair[1];
            newTarget = newTarget.queryParam(key, val);
        }

        return newTarget.request(MediaType.APPLICATION_JSON).get();
    }
}
