package edu.neu.husky.wenl.huang.server.daos;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;


public class DailySkiRecordDao {
    private static MongoCollection<Document> dbCollection;
    private static DailySkiRecordDao dailySkiRecordDao;

    public static DailySkiRecordDao getDailySkiRecordDao() {
        if (dailySkiRecordDao == null) {
            dailySkiRecordDao = new DailySkiRecordDao();
        }
        return dailySkiRecordDao;
    }


    private DailySkiRecordDao() {
        dbCollection = DBConnection.getCollectionDailyRecords();
    }

    public Document create(Document document) {
        dbCollection.insertOne(document);
        return document;
    }

    public Document get(int skierId, int day) {
        return dbCollection.find(and(eq("skierId", skierId), eq("day", day))).first();
    }
}
