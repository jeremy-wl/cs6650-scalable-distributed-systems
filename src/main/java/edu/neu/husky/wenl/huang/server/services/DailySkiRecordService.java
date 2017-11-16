package edu.neu.husky.wenl.huang.server.services;

import com.mongodb.client.FindIterable;
import edu.neu.husky.wenl.huang.server.daos.DailySkiRecordDao;
import edu.neu.husky.wenl.huang.server.daos.LiftRecordDao;
import edu.neu.husky.wenl.huang.server.rabbitmq.RabbitMQUtils;
import edu.neu.husky.wenl.huang.server.rabbitmq.RoutingKeys;
import org.bson.Document;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.InetAddress;
import java.util.*;

@Path("/records")
public class DailySkiRecordService {
    private LiftRecordDao liftRecordDao;
    private DailySkiRecordDao dailySkiRecordDao;

    private static final int SKIER_ID_RANGE = 40000;

    public DailySkiRecordService() {
        liftRecordDao = LiftRecordDao.getLiftRecordDao();
        dailySkiRecordDao = DailySkiRecordDao.getDailySkiRecordDao();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/myvert")  // returns the skier's daily ski summary
    public String getDailySkiRecord(@DefaultValue("-1") @QueryParam("dayNum")  int dayNum,
                                    @DefaultValue("-1") @QueryParam("skierId") int skierId) {
        int error = 0;
        long responseStart, dbQueryStart, responseTime = -1, dbQueryTime = -1;
        String res = null;
        String hostName = null;

        try {
            responseStart = System.currentTimeMillis();

            if (dayNum == -1 || skierId == -1) {
                throw new BadRequestException("You must only pass dayNum and skierId as params here");
            }

            hostName = InetAddress.getLocalHost().getHostName();

            dbQueryStart = System.currentTimeMillis();
            Document document = dailySkiRecordDao.get(skierId, dayNum);
            dbQueryTime = System.currentTimeMillis() - dbQueryStart;

            res = document.toJson();
            responseTime = System.currentTimeMillis() - responseStart;

        } catch (Exception e) {
            error = 1;
        }
        RabbitMQUtils.publish(RoutingKeys.GET,
                String.format("%d,%d,%d,%s,%s",
                        responseTime, dbQueryTime, error, "GET", hostName));

        return res;
    }

    @GET
    @Path("/generate-daily-ski-records")
    public Response generateDailySkiRecords(@DefaultValue("-1") @QueryParam("day") int day) {
        if (day == -1) {
            throw new BadRequestException("You must only pass valid day num as params here");
        }
        for (int skierId = 1; skierId <= SKIER_ID_RANGE; skierId++) {
            int liftRides = 0;
            int verticals = 0;
            FindIterable<Document> documents = liftRecordDao.getLiftRecords(skierId, day);

            for (Document document : documents) {
                int liftId = Integer.valueOf(document.get("liftId").toString());
                verticals += getVerticalByLiftId(liftId);
                liftRides++;
            }

            Map<String, Object> fields = new HashMap<>();
            fields.put("skierId", skierId);
            fields.put("day", day);
            fields.put("liftRides", liftRides);
            fields.put("verticals", verticals);

            dailySkiRecordDao.create(new Document(fields));
        }
        return Response.ok().build();
    }

    /**
     * Lifts 1-10 rise 200m vertical. Lifts 11-20 are 300m vertical.
     * Lifts 21-30 are 400m vertical, and lifts 31-40 and 500m vertical.
     */
    private int getVerticalByLiftId(int liftId) {
        if (liftId >= 1  && liftId <= 10)  return 200;
        if (liftId >= 11 && liftId <= 20)  return 300;
        if (liftId >= 21 && liftId <= 30)  return 400;
        if (liftId >= 31 && liftId <= 40)  return 500;
        return -1;
    }
}
