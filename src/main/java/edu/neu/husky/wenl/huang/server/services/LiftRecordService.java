package edu.neu.husky.wenl.huang.server.services;

import edu.neu.husky.wenl.huang.server.daos.LiftRecordDao;
import edu.neu.husky.wenl.huang.server.models.LiftRecord;
import org.bson.Document;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Response createLiftRecord(String liftRecordJSON) {
        Document liftRecord = Document.parse(liftRecordJSON);
        liftRecordDao.create(liftRecord);
        return Response.ok().build();   // return the doc as json? probably not needed here
    }

    public List<LiftRecord> getLiftRecords(int skierId, int day) {
        return null;  // FIXME: 10/16/17
    }
}
