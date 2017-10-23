package edu.neu.husky.wenl.huang.server.models;

public class LiftRecord implements Comparable<LiftRecord> {
    private int skierId;
    private String day;
    private int resortId;
    private int liftId;
    private int time;         // timestamp for the day

    public LiftRecord() {}

    public int getSkierId() {
        return skierId;
    }

    public String getDay() {
        return day;
    }

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
