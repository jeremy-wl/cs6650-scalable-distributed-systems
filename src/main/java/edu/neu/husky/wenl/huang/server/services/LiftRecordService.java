package edu.neu.husky.wenl.huang.server.services;

import edu.neu.husky.wenl.huang.server.daos.LiftRecordDao;
import edu.neu.husky.wenl.huang.server.models.LiftRecord;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.*;

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
    public LiftRecord createLiftRecord(LiftRecord liftRecord) {
        String day = liftRecord.getDay();
        int time = liftRecord.getTime();

        liftRecord.setDay(day + "#" + time);

        return liftRecordDao.create(liftRecord);
    }

    public List<LiftRecord> getLiftRecords(int skierId, int day) {
        return null;  // FIXME: 10/16/17
    }
}
