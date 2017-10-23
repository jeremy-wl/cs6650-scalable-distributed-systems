package edu.neu.husky.wenl.huang.server.models;


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

    public int getSkierId() {
        return skierId;
    }

    public int getDay() {
        return day;
    }

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
