package edu.neu.husky.wenl.huang.server.daos;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

// Singleton Class
class DBConnection {
    private static MongoDatabase db;

    private static MongoDatabase getDB() {
        if (db == null) {
            db = new MongoClient("localhost", 27017).getDatabase("ski-records");
        }
        return db;
    }

    static MongoCollection<Document> getCollectionLiftRecords() {
        return getDB().getCollection("lift-records");
    }
}
