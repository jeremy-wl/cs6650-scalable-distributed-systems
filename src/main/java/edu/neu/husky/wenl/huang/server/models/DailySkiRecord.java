package edu.neu.husky.wenl.huang.server.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="daily_ski_records")
public class DailySkiRecord {
    private int skierId;
    private int day;
    private int verticals;
    private int liftRides;

    public DailySkiRecord() {}

    public DailySkiRecord(int skierId, int day, int verticals, int liftRides) {
        this.skierId = skierId;
        this.day = day;
        this.verticals = verticals;
        this.liftRides = liftRides;
    }

    @DynamoDBHashKey(attributeName = "skierId")
    public int getSkierId() {
        return skierId;
    }

    @DynamoDBRangeKey
    public int getDay() {       // unlike the 'day' attr in LiftRecord, this day attr is
        return day;             // just the day number, because skierId+day can form
    }                           // a unique composite key in this table

    public int getVerticals() {
        return verticals;
    }

    public int getLiftRides() {
        return liftRides;
    }

    public void setSkierId(int skierId) {
        this.skierId = skierId;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setVerticals(int verticals) {
        this.verticals = verticals;
    }

    public void setLiftRides(int liftRides) {
        this.liftRides = liftRides;
    }
}
