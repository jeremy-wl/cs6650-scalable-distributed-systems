package edu.neu.husky.wenl.huang.server.services;

import edu.neu.husky.wenl.huang.server.daos.LiftRecordDao;
import edu.neu.husky.wenl.huang.server.models.LiftRecord;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/records")
public class LiftRecordService {
    private LiftRecordDao liftRecordDao;

    public LiftRecordService() {
        liftRecordDao = new LiftRecordDao();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/load")    // creates a lift record per request
    public LiftRecord createSkiRecord(LiftRecord liftRecord) {
        String day = liftRecord.getDay();
        int time = liftRecord.getTime();

        liftRecord.setDay(day + "#" + time);

        return liftRecordDao.create(liftRecord);
    }
}
