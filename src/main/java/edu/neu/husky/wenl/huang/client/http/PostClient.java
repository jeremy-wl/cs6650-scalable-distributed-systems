package edu.neu.husky.wenl.huang.client.http;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PostClient implements HTTPClient {
    private static final String URL = "http://localhost:8080/api/records/load";
    private WebTarget resource;

    public PostClient() {
        this.resource = ClientBuilder.newClient().target(URL);
    }

    public Response request(String requestBody) {
        return resource.request(MediaType.APPLICATION_JSON).post(Entity.json(requestBody));
    }

    public static void main(String[] args) {
        PostClient pc = new PostClient();
        Response res = pc.request("{\"resortId\":1,\"day\":1,\"skierId\":2,\"liftId\":9,\"time\":1}");

        String jsonRes = null;
        if (res.getStatus() == 200) { // returns the json sent by client, if request successful
            jsonRes = res.readEntity(String.class);
        }
        System.out.println(jsonRes);
    }
}
