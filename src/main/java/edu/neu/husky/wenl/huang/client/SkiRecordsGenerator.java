package edu.neu.husky.wenl.huang.client;

import edu.neu.husky.wenl.huang.client.http.GetClient;
import edu.neu.husky.wenl.huang.client.http.HTTPClient;

import java.util.*;

class SkiRecordsGenerator {
    private static final String ENDPOINT_GEN_DAILY_SKI_RECORDS = "/api/records/generate-daily-ski-records";

    static void generate(int dayNum) {
        System.out.println("===============================================================");

        long timeStart = System.currentTimeMillis();
        System.out.println("Generating daily ski records for all skiers...... Time: "
                + new Date(System.currentTimeMillis()));

        HTTPClient clientGenDailySkiRecords = new GetClient(Main.DOMAIN + ENDPOINT_GEN_DAILY_SKI_RECORDS);
        clientGenDailySkiRecords.request("day=" + dayNum);

        System.out.println("---------------------------------------------------------------");
        double timeFinished = (System.currentTimeMillis() - timeStart) / 1000;
        System.out.printf("Finished in %.3f seconds.", timeFinished);

        System.out.println("===============================================================");
    }
}
