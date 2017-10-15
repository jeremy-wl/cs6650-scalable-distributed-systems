package edu.neu.husky.wenl.huang.client.http;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PostClient {
    private static final String URL = "http://localhost:8080/api/records/load";
    private WebTarget resource;
    private String requestBody;

    public PostClient(String requestBody) {
        this.resource = ClientBuilder.newClient().target(URL);
        this.requestBody = requestBody;
    }

    public String postRequest() throws ClientErrorException {
        Response response = resource.request(MediaType.APPLICATION_JSON).post(Entity.json(requestBody));
        String jsonRes = null;

        if (response.getStatus() == 200) {
            jsonRes = response.readEntity(String.class);
            // TODO: parse json response here
        }
        return jsonRes;  // returns the json sent by client, if request successful
    }

    public static void main(String[] args) {
        PostClient pc = new PostClient("{\"resortId\":1,\"day\":1,\"skierId\":2,\"liftId\":9,\"time\":1}");
        String res = pc.postRequest();
        System.out.println(res);
    }
}
