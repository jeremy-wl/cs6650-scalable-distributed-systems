package edu.neu.husky.wenl.huang.server.services;

import edu.neu.husky.wenl.huang.server.daos.LiftRecordDao;
import org.bson.Document;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/records")
public class LiftRecordService {
    private LiftRecordDao liftRecordDao;

    public LiftRecordService() {
        liftRecordDao = LiftRecordDao.getLiftRecordDao();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/load-lift-records")    // creates a lift record per request
    public Response createLiftRecord(String liftRecordJSON) {
        Document liftRecord = Document.parse(liftRecordJSON);
        liftRecordDao.create(liftRecord);
        return Response.ok().build();   // return the doc as json? probably not needed here
    }
}
