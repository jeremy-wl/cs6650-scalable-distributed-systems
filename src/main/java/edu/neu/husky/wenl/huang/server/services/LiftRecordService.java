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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/batch-load")    // batch creates 25 lift records per request
    public List<LiftRecord> createLiftRecord(List<LiftRecord> liftRecords) {
        for (LiftRecord liftRecord : liftRecords) {
            setDay(liftRecord);
        }
        return liftRecordDao.batchCreate(liftRecords);
    }

    public List<LiftRecord> getLiftRecords(int skierId, int day) {
        return null;  // FIXME: 10/16/17
    }

    private void setDay(LiftRecord liftRecord) {
        String day = liftRecord.getDay();
        int time = liftRecord.getTime();

        liftRecord.setDay(day + "#" + time);
    }
}
