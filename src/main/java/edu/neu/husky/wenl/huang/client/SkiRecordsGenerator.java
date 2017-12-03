package edu.neu.husky.wenl.huang.client;

import edu.neu.husky.wenl.huang.client.http.GetClient;
import edu.neu.husky.wenl.huang.client.http.HTTPClient;

import java.util.*;

class SkiRecordsGenerator {
    private static final String ENDPOINT_GEN_DAILY_SKI_RECORDS = "/records/generate-skier-day-record";


    static void generate(int dayNum) {
        System.out.println("===============================================================");

        long timeStart = System.nanoTime();
        System.out.println("Generating daily ski records for all skiers...... Time: "
                + new Date(System.nanoTime()));

        HTTPClient clientGenDailySkiRecords = new GetClient(Main.DOMAIN + ENDPOINT_GEN_DAILY_SKI_RECORDS);
        clientGenDailySkiRecords.request("day=" + dayNum);

        System.out.println("---------------------------------------------------------------");
        double timeFinished = (System.nanoTime() - timeStart) / 1_000_000_000.0;
        System.out.println(String.format("Finished in %.3f seconds.", timeFinished));

        System.out.println("===============================================================");
    }
}
