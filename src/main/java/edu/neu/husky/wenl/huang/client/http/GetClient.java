package edu.neu.husky.wenl.huang.client.http;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

public class GetClient {
    private static final String URL = "http://localhost:8080/api/records/myvert";
    private WebTarget resource;

    public GetClient(Map<String, String> queryParams) {
        StringBuilder queryString = new StringBuilder();

        for (String key : queryParams.keySet()) {
            queryString.append(queryString.length() == 0 ? "?" : "&");
            queryString.append(key).append("=");
            queryString.append(queryParams.get(key));
        }

        this.resource = ClientBuilder.newClient().target(URL + queryString.toString());
    }

    public String getRequest() {
        Response response = resource.request(MediaType.APPLICATION_JSON).get();
        String jsonRes = null;

        if (response.getStatus() == 200) {
            jsonRes = response.readEntity(String.class);
            // TODO: parse json response here
        }
        return jsonRes;  // returns JSON string like {"liftRides":20,"verticals":500}
    }

    public static void main(String[] args) {
        GetClient gc = new GetClient(new HashMap<String, String>(){{
            put("dayNum", "1");
            put("skierID", "2");
        }});
        String res = gc.getRequest();
        System.out.println(res);
    }
}
