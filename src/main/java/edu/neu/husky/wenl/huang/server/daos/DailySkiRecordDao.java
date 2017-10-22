package edu.neu.husky.wenl.huang.server.daos;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import edu.neu.husky.wenl.huang.server.models.DailySkiRecord;
import edu.neu.husky.wenl.huang.server.models.LiftRecord;

import java.util.*;

public class DailySkiRecordDao {
    private DynamoDBMapper dynamoDBMapper;

    public DailySkiRecordDao() {
        AmazonDynamoDB db = Credentials.getDBClient();
        this.dynamoDBMapper = new DynamoDBMapper(db);
    }

    public DailySkiRecord create(List<LiftRecord> liftRecords) {
        if (liftRecords == null)  return null;
        int liftRides = liftRecords.size();
        int verticals = 0;
        for (LiftRecord liftRecord : liftRecords) {
            int liftId = liftRecord.getLiftId();
            verticals += getVerticalByLiftId(liftId);
        }
        LiftRecord liftRecord = liftRecords.get(0);

        int skierId = liftRecord.getSkierId();
        String dayStr = liftRecord.getDay();

        int day = Integer.valueOf(dayStr.split("#")[0]);

        DailySkiRecord res = new DailySkiRecord(skierId, day, verticals, liftRides);
        dynamoDBMapper.save(res);
        return res;
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
