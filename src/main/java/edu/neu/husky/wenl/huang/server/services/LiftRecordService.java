package edu.neu.husky.wenl.huang.server.services;

import edu.neu.husky.wenl.huang.server.models.LiftRecord;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/records")
public class LiftRecordService {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/load")    // creates a lift record per request
    public LiftRecord createSkiRecord(LiftRecord liftRecord) {
        // TODO: insert a liftRecord to db
        return liftRecord;
    }
}
