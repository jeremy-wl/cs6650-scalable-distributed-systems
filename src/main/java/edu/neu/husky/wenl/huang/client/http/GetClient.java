package edu.neu.husky.wenl.huang.client.http;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class GetClient implements HTTPClient {
    private static final String URL = "http://localhost:8080/api/records/myvert";
    private WebTarget target;

    public GetClient() {
        this.target = ClientBuilder.newClient().target(URL);
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

    public static void main(String[] args) {
        GetClient gc = new GetClient();

        Response res = gc.request("dayNum=1&skierID=2");

        String jsonRes = null;
        if (res.getStatus() == 200) {
            jsonRes = res.readEntity(String.class);  // {"liftRides":20,"verticals":500}
        }

        System.out.println(jsonRes);
    }
}
