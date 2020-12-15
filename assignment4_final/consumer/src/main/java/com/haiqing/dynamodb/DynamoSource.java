package com.haiqing.dynamodb;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.haiqing.model.Liftride;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DynamoSource {
  private static final Logger logger = LogManager.getLogger(DynamoSource.class);
  private Table liftRideTable;

  private static DynamoSource instance;

  public static DynamoSource getInstance() {
    if (instance == null) {
      instance = new DynamoSource();
    }
    return instance;
  }

  private DynamoSource() {
    final AmazonDynamoDB client = AmazonDynamoDBClientBuilder
        .standard()
        .withRegion(Regions.US_WEST_2)
        .build();
    final DynamoDB dynamoDB = new DynamoDB(client);
    liftRideTable = dynamoDB.getTable("liftrides");
  }

  public void saveLiftride(Liftride liftride) {
    try {
      PutItemOutcome outcome = liftRideTable.putItem(
          new Item()
              .withPrimaryKey(new PrimaryKey("lift_id", liftride.getLiftID()))
              .withString("resort_id", liftride.getResortID())
              .withInt("day_id", liftride.getDayID())
              .withInt("skier_id", liftride.getSkierID())
              .withInt("time", liftride.getTime())
              .withInt("vertical", liftride.getLiftID() * 10));
      logger.info("PutItem succeeded: " + outcome.getPutItemResult());

    } catch (final Exception e) {
      logger.error("Cannot save liftride " + liftride, e);
    }
  }

  public int getTotalVertSkierDay(String resortID, int dayID, int skierID) {
    int totalVertNum = 0;
    try {
      Index index = liftRideTable.getIndex("skier_id-day_id-index");
      QuerySpec querySpec = new QuerySpec();
      querySpec.withKeyConditionExpression("skier_id = :sid and day_id = :did")
          .withFilterExpression("resort_id = :rid")
          .withValueMap(new ValueMap().withInt(":sid", skierID)
              .withInt(":did", dayID)
              .withString(":rid", resortID));
      ItemCollection<QueryOutcome> items = index.query(querySpec);
      for (final Item ignored : items) {
        totalVertNum++;
      }
    } catch (final Exception e) {
      logger.error("cannot getTotalVertPerResort", e);
    }
    return totalVertNum;
  }

  public int getTotalVertPerResort(int skierID, String resortID) {
    int totalVertNum = 0;
    try {
      Index index = liftRideTable.getIndex("skier_id-day_id-index");
      QuerySpec querySpec = new QuerySpec();
      querySpec.withKeyConditionExpression("skier_id = :sid")
          .withFilterExpression("resort_id = :rid")
          .withValueMap(new ValueMap().withInt(":sid", skierID)
              .withString(":rid", resortID));
      ItemCollection<QueryOutcome> items = index.query(querySpec);
      for (final Item ignored : items) {
        totalVertNum++;
      }
    } catch (final Exception e) {
      logger.error("cannot getTotalVertPerResort", e);
    }
    return totalVertNum;
  }
}
