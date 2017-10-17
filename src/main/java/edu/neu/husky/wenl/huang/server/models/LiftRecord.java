package edu.neu.husky.wenl.huang.server.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.io.Serializable;

@DynamoDBTable(tableName="lift_records")
public class LiftRecord implements Serializable, Comparable<LiftRecord> {
    private int skierId;
    private String day;
    private int resortId;
    private int liftId;
    private int time;         // timestamp for the day

    public LiftRecord() {}

    @DynamoDBHashKey(attributeName = "skierId")
    public int getSkierId() {
        return skierId;
    }

    @DynamoDBRangeKey
    public String getDay() {  // day (day#time) is the sort key, string-appended by '#' + time,
        return day;           // in order to form a unique combination with the partition key,
    }                         // and enable efficient queries

    public int getResortId() {
        return resortId;
    }

    public int getLiftId() {
        return liftId;
    }

    public int getTime() {
        return time;
    }

    public void setSkierId(int skierId) {
        this.skierId = skierId;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setResortId(int resortId) {
        this.resortId = resortId;
    }

    public void setLiftId(int liftId) {
        this.liftId = liftId;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int compareTo(LiftRecord that) {
        return this.time - that.getTime();
    }

    @Override
    public String toString() {
        return "LiftRecord{" + "resortId=" + resortId + ", day=" + day + ", " +
               "skierId=" + skierId + ", liftId=" + liftId + ", time=" + time + '}';
    }
}
