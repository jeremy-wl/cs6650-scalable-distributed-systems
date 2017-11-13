package edu.neu.husky.wenl.huang.server.services;

import edu.neu.husky.wenl.huang.server.daos.LiftRecordDao;
import edu.neu.husky.wenl.huang.server.rabbitmq.RabbitMQUtils;
import edu.neu.husky.wenl.huang.server.rabbitmq.RoutingKeys;
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

    /**********************************************************************************************
     *
     *    You will want to measure three things on your server:
     * 1) Response time - the time it takes from a POST or GET method being invoked to the time
     *    it takes to send the response.
     * 2) Database query times - the latency to submit a query to the database and get a result
     * 3) The number or errors which cause a request to fail
     *
     **********************************************************************************************/
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/load-lift-records")    // creates a lift record per request
    public Response createLiftRecord(String liftRecordJSON) {
        long responseStart, dbQueryStart, endTime, responseTime = -1, dbQueryTime = -1;
        int error = 0;
        Response response = null;

        try {
            responseStart = System.currentTimeMillis();

            Document liftRecord = Document.parse(liftRecordJSON);

            dbQueryStart = System.currentTimeMillis();

            liftRecordDao.create(liftRecord);
            response = Response.ok().build();

            endTime = System.currentTimeMillis();

            responseTime = endTime - responseStart;
            dbQueryTime = endTime - dbQueryStart;

        } catch (Exception e) {
            error = 1;
        }
        RabbitMQUtils.publish(RoutingKeys.POST,
                              String.format("%d,%d,%d",
                                            responseTime, dbQueryTime, error));

        return response;
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
