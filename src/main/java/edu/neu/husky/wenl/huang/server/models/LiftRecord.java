package edu.neu.husky.wenl.huang.server.models;

public class LiftRecord {
    private int resortId;
    private int day;
    private int skierId;
    private int liftId;
    private int time;

    public LiftRecord() {}

    public LiftRecord(int resortId, int day, int skierId, int liftId, int time) {
        this.resortId = resortId;
        this.day = day;
        this.skierId = skierId;
        this.liftId = liftId;
        this.time = time;
    }

    public int getResortId() {
        return resortId;
    }

    public void setResortId(int resortId) {
        this.resortId = resortId;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getSkierId() {
        return skierId;
    }

    public void setSkierId(int skierId) {
        this.skierId = skierId;
    }

    public int getLiftId() {
        return liftId;
    }

    public void setLiftId(int liftId) {
        this.liftId = liftId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
