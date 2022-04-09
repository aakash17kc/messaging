package com.aakash.messaging.database;

import com.aakash.messaging.utils.ConfigHelper;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnectionFactory {

  static DBConnectionFactory dbConnectionFactory;

  private static final String DBNAME = ConfigHelper.getInstance().getProperty("mongodb.name");
  private static final String USER = ConfigHelper.getInstance().getProperty("mongodb.user");
  private static final String PASSWORD = ConfigHelper.getInstance().getProperty("mongodb.password");
  private static final String HOST = ConfigHelper.getInstance().getProperty("mongodb.host");
  private static final String PROTOCOL = ConfigHelper.getInstance().getProperty("mongodb.protocol");

  private static final Logger LOG = LoggerFactory.getLogger(DBConnectionFactory.class);


  public MongoClient mongoClient;

  private DBConnectionFactory() {
  }

  public static DBConnectionFactory getInstance(){
    if(dbConnectionFactory == null){
      dbConnectionFactory = new DBConnectionFactory();
    }
    return dbConnectionFactory;
  }

  public void bootstrapDB(Vertx vertx){
//    String mongoUri = PROTOCOL + USER + ":" + PASSWORD + "@" + HOST;
    JsonObject dbConfig = new JsonObject();
    dbConfig.put("db_name", DBNAME);
    dbConfig.put("useObjectId",true);
    mongoClient = MongoClient.createShared(vertx,dbConfig);

    mongoClient.createCollection("data");
    JsonObject data = new JsonObject();
    data.put("title","test-doc");


  }


  public MongoClient getMongoClient() {
    return mongoClient;
  }

}
