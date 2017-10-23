package edu.neu.husky.wenl.huang.server.daos;

import com.mongodb.client.MongoCollection;
import edu.neu.husky.wenl.huang.server.models.LiftRecord;
import org.bson.Document;

import java.util.*;

public class LiftRecordDao {
    private MongoCollection<Document> dbCollection;

    public LiftRecordDao() {
        dbCollection = DBConnection.getCollectionLiftRecords();
    }

    public Document create(Document liftRecord) {
        dbCollection.insertOne(liftRecord);
        return liftRecord;
    }

    /**
     * @param skierId skier id
     * @param day day number
     * @return all lift records for that skier on that day
     */
    public List<LiftRecord> getLiftRecords(int skierId, int day) {
        LiftRecord liftRecord = new LiftRecord();
        liftRecord.setSkierId(skierId);

        return null;  // FIXME: 10/22/17
    }
}
