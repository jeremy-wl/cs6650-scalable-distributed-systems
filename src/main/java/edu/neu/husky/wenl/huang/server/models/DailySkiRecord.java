package edu.neu.husky.wenl.huang.server.models;

public class DailySkiRecord {
    private int verticals;
    private int liftRides;

    public DailySkiRecord() {}

    public DailySkiRecord(int verticals, int liftRides) {
        this.verticals = verticals;
        this.liftRides = liftRides;
    }

    public int getVerticals() {
        return verticals;
    }

    public void setVerticals(int verticals) {
        this.verticals = verticals;
    }

    public int getLiftRides() {
        return liftRides;
    }

    public void setLiftRides(int liftRides) {
        this.liftRides = liftRides;
    }
}
