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
        dynamoDBMapper.save(liftRecord);  // after this operation, liftRecord is the obj returned
        return liftRecord;                // from the db (obj gets changed in-place)
    }
}
