package edu.neu.husky.wenl.huang.server.daos;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import java.util.*;

interface Credentials {
    static AmazonDynamoDB getDBClient() {
        Map<String, String> env = System.getenv();
        String accessKey = env.get("AWS_ACCESS_KEY");
        String secretKey = env.get("AWS_SECRET_KEY");

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonDynamoDBClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_WEST_2)
                .build();
    }
}
