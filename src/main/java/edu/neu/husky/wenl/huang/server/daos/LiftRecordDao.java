package edu.neu.husky.wenl.huang.server.daos;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class LiftRecordDao {
    private static LiftRecordDao liftRecordDao;
    private static MongoCollection<Document> dbCollection;

    public static LiftRecordDao getLiftRecordDao() {
        if (liftRecordDao == null) {
            liftRecordDao = new LiftRecordDao();
        }
        return liftRecordDao;
    }

    private LiftRecordDao() {
        dbCollection = DBConnection.getCollectionLiftRecords();
    }

    public Document create(Document liftRecord) {
        dbCollection.insertOne(liftRecord);
        return liftRecord;
    }

    public void createMany(List<Document> liftRecords) {
        dbCollection.insertMany(liftRecords);
    }

    /**
     * @param skierId skier id
     * @param day day number
     * @return all lift records for that skier on that day
     */
    public FindIterable<Document> getLiftRecords(int skierId, int day) {
        return dbCollection.find(and(eq("skierId", skierId), eq("day", day)));
    }
}
