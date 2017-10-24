package edu.neu.husky.wenl.huang.server.services;

import edu.neu.husky.wenl.huang.server.daos.LiftRecordDao;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

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
        liftRecordDao = LiftRecordDao.getLiftRecordDao();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/load-lift-records")    // creates a lift record per request
    public Response createLiftRecord(String liftRecordJSON) {
        Document liftRecord = Document.parse(liftRecordJSON);
        liftRecordDao.create(liftRecord);
        return Response.ok().build();
    }

    @POST
    @Path("/batch-load-lift-records")    // batch creates lift records per request
    public Response createLiftRecords(String liftRecordsJSON) {

        JSONArray arr = new JSONArray(liftRecordsJSON);
        List<Document> documents = new ArrayList<>();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject jsonObject = arr.getJSONObject(i);
            documents.add(new Document(jsonObject.toMap()));
        }

        LiftRecordDao liftRecordDao = LiftRecordDao.getLiftRecordDao();
        liftRecordDao.createMany(documents);
        return Response.ok().build();
    }
}
