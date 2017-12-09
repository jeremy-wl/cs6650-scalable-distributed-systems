package edu.neu.husky.wenl.huang.client;

class Main {

//    static final String DOMAIN = "http://localhost:8080";
//    static final String DOMAIN = "http://34.216.3.162:8080/cs6650-assignment2";
//    static final String DOMAIN = "http://first-load-balancer-188437085.us-west-2.elb." +
//                                 "amazonaws.com/cs6650-assignment2";
//    static final String DOMAIN = "http://localhost:3000";
    static final String DOMAIN = "https://9ozxh6xq36.execute-api.us-west-2.amazonaws.com/dev";

//    private static final int DAY_NUM = 1;
    private static final int DAY_NUM = 1;

    static final String CLIENT_DIR = "src/main/java/edu/neu/husky/wenl/huang/client/";
    private static final int N_THREADS_WRITER = 100;
    private static final int N_THREADS_READER = 10;
    private static final int SKIER_ID_RANGE = 200;  // TODO: change to 40000

    public static void main(String[] args) {
        LiftRecordsWriter.write(N_THREADS_WRITER); // This one sends all records with n threads
//        LiftRecordsBatchWriter.write();            // This one send all records in bulk in 1 request, 10x faster
//        SkiRecordsGenerator.generate(DAY_NUM);
//        DailySkiRecordsReader.read(N_THREADS_READER, DAY_NUM, SKIER_ID_RANGE);
    }
}
