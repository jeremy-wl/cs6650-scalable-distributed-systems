package edu.neu.husky.wenl.huang.client.http;

import javax.ws.rs.core.Response;

public interface HTTPClient {
    Response request(String params);
}
