package edu.neu.husky.wenl.huang.server.daos;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import edu.neu.husky.wenl.huang.server.models.LiftRecord;

public class LiftRecordDao {
    private DynamoDBMapper dynamoDBMapper;

    public LiftRecordDao() {
        AmazonDynamoDB db = Credentials.getDBClient();
        this.dynamoDBMapper = new DynamoDBMapper(db);
    }

    public LiftRecord create(LiftRecord liftRecord) {
        dynamoDBMapper.save(liftRecord);  // liftRecord returned from db is changed in-place
        return liftRecord;                // uuid is added from the db side
    }
}
