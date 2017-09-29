package edu.neu.husky.wenl.huang.server;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
public class Hello {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
        public String getStatus() {
        return ("alive");
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
        public int postText(String content) {
        return (content.length());
    }
}
