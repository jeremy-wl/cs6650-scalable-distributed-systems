package edu.neu.husky.wenl.huang.client.http;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PostClient implements HTTPClient {
    private WebTarget resource;

    public PostClient(String url) {
        this.resource = ClientBuilder.newClient().target(url);
    }

    public Response request(String requestBody) {
        return resource.request(MediaType.APPLICATION_JSON).post(Entity.json(requestBody));
    }
}
