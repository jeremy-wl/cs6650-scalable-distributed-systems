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

    public Response request(String params) {  // params is like "a=2&b=3&c=4"
        String[] paramList = params.split("&");
        for (String param : paramList) {
            String[] keyValPair = param.split("=");
            String key = keyValPair[0];
            String val = keyValPair[1];
            target = target.queryParam(key, val);
        }

        return target.request(MediaType.APPLICATION_JSON).get();
    }
}
