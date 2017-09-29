package edu.neu.husky.wenl.huang.client;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class HTTPClient {
    private WebTarget resource;

    public HTTPClient(String ip, String port) {
        String url = "http://127.0.0.1:8080";  // TODO: change to code below
//        String url = "http://" + ip + ":" + port + "/cs6650-assignment/"; // HAVE TO KEEP THE LAST '/'
        this.resource = ClientBuilder.newClient().target(url);
    }

    public <T> T postText(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return resource.request(MediaType.TEXT_PLAIN)
                       .post(Entity.entity(requestEntity, MediaType.TEXT_PLAIN), responseType);
    }

    public String getStatus() throws ClientErrorException {
        return resource.request(MediaType.TEXT_PLAIN).get(String.class);
    }
}
