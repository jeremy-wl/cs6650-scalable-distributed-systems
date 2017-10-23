package edu.neu.husky.wenl.huang.server.daos;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

// Singleton Class
class DBConnection {
    private static MongoDatabase db;
    private static MongoCollection<Document> liftRecordsCollection;
    private static MongoCollection<Document> dailyRecordsCollection;

    private static MongoDatabase getDB() {
        if (db == null) {
            db = new MongoClient("localhost", 27017).getDatabase("ski-records");
        }
        return db;
    }

    static MongoCollection<Document> getCollectionLiftRecords() {
        if (liftRecordsCollection == null) {
            liftRecordsCollection = getDB().getCollection("lift-records");
        }
        return liftRecordsCollection;
    }

    static MongoCollection<Document> getCollectionDailyRecords() {
        if (dailyRecordsCollection == null) {
            dailyRecordsCollection = getDB().getCollection("daily-ski-records");
        }
        return dailyRecordsCollection;
    }
}
