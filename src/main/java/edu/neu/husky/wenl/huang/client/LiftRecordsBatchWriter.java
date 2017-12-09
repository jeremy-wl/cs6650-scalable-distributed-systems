package edu.neu.husky.wenl.huang.client;

import edu.neu.husky.wenl.huang.client.http.HTTPClient;
import edu.neu.husky.wenl.huang.client.http.PostClient;

import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class LiftRecordsBatchWriter {
    private static int records = -1;
    private static final String ENDPOINT_LOAD_LIFT_RECORDS = "/records/batch-load-lift-records";
    private static final String DATA_SOURCE = Main.CLIENT_DIR + "data/data_day1_800k.csv";

    static void write() {
        float wallTime;
        long wallTimeStart = System.nanoTime();

        System.out.println("===============================================================");
        System.out.println("Parsing CSV records, this may take a while ... " + new Date(System.nanoTime()));

        String json = csvToJSON(DATA_SOURCE);

        System.out.println("---------------------------------------------------------------");
        System.out.println(String.format("Sending %d records in bulk", records));

        HTTPClient postClient = new PostClient(Main.DOMAIN + ENDPOINT_LOAD_LIFT_RECORDS);

        int responses = 0;
        Response res = postClient.request(json);
        if (res.getStatus() == 200) {
            responses++;
        }
        wallTime = System.nanoTime() - wallTimeStart;

        System.out.println("===============================================================");
        System.out.println(String.format("All %d records loaded ... Time: %s", records, new Date(System.nanoTime())));
        System.out.println("---------------------------------------------------------------");
        System.out.println("Total number of requests sent: " + 1);
        System.out.println("Total number of Successful responses: " + responses);
        System.out.println(String.format("Test Wall Time: %.3f seconds", wallTime / 1_000_000_000));
        System.out.println("===============================================================");

    }

    private static String csvToJSON(String csvPath) {
        String line;
        String[] keys = null;
        StringBuilder sb = new StringBuilder("[");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvPath))) {

            while ((line = bufferedReader.readLine()) != null) {
                if (++records == 0) {
                    keys = line.split(",");
                    continue;
                }
                String[] vals = line.split(",");
                String serializedJSON =
                        String.format("{\"%s\":%s,\"%s\":%s,\"%s\":%s,\"%s\":%s,\"%s\":%s},",
                                keys[0], vals[0], keys[1], vals[1], keys[2], vals[2],
                                keys[3], vals[3], keys[4], vals[4]);
                //                System.out.println(serializedJSON);
                sb.append(serializedJSON);
            }
            sb.setCharAt(sb.length()-1, ']');
            System.out.println("Total records: " + records);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
