package edu.neu.husky.wenl.huang.server.daos;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

// Singleton Class
class DBConnection {
    private static final int CONNECTION_POOL_SIZE = 100;

    private static MongoDatabase db;
    private static MongoCollection<Document> liftRecordsCollection;
    private static MongoCollection<Document> dailyRecordsCollection;

    private static MongoDatabase getDB() {
        if (db == null) {
            MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
            MongoClientOptions options = builder.connectionsPerHost(CONNECTION_POOL_SIZE).build();

            MongoClient client = new MongoClient(new ServerAddress("localhost", 27017), options);
            db = client.getDatabase("ski-records");
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
