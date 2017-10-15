package edu.neu.husky.wenl.huang.server.services;

import edu.neu.husky.wenl.huang.server.models.DailySkiRecord;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/records")
public class DailySkiRecordService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/myvert")  // returns the skier's daily ski summary
    public DailySkiRecord getDailySkiRecord(@DefaultValue("-1") @QueryParam("dayNum")  int dayNum,
                                            @DefaultValue("-1") @QueryParam("skierID") int skierID) {
        if (dayNum == -1 || skierID == -1) {
            throw new BadRequestException("You must only pass dayNum and skierID as params here");
        }
        return new DailySkiRecord(500, 20);
    }
}
