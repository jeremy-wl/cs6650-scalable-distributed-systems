package edu.neu.husky.wenl.huang.server.daos;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import edu.neu.husky.wenl.huang.server.models.LiftRecord;

import java.util.*;

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

    /**
     * @param skierId skier id
     * @param day day number
     * @return all lift records for that skier on that day
     */
    public List<LiftRecord> getLiftRecords(int skierId, int day) {
        LiftRecord liftRecord = new LiftRecord();
        liftRecord.setSkierId(skierId);

        Condition rangeKeyCondition = new Condition();
        rangeKeyCondition
                .withComparisonOperator(ComparisonOperator.BEGINS_WITH)
                .withAttributeValueList(new AttributeValue(day + "#"));

        return dynamoDBMapper.query(LiftRecord.class,
                new DynamoDBQueryExpression<LiftRecord>()
                        .withHashKeyValues(liftRecord)
                        .withRangeKeyCondition("day", rangeKeyCondition));
    }
}
