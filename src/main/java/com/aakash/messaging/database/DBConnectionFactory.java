package com.aakash.messaging.database;

import com.aakash.messaging.exceptions.Errors;
import com.aakash.messaging.exceptions.MessagingException;
import com.aakash.messaging.utils.ConfigHelper;
import com.mongodb.MongoClient;
import io.vertx.core.Vertx;
import org.apache.http.HttpStatus;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnectionFactory {

  static DBConnectionFactory dbConnectionFactory;

  private static final String DBNAME = ConfigHelper.getInstance().getProperty("mongodb.name");
  private static final String USER = ConfigHelper.getInstance().getProperty("mongodb.user");
  private static final String PASSWORD = ConfigHelper.getInstance().getProperty("mongodb.password");
  private static final String DB_ENTITY_PACKAGE =
      ConfigHelper.getInstance().getProperty("mongodb.entitylocation");

  private static final Logger LOG = LoggerFactory.getLogger(DBConnectionFactory.class);


  public MongoClient mongoClient;

  private Datastore datastore;


  private DBConnectionFactory() {
  }

  public static DBConnectionFactory getInstance(){
    if(dbConnectionFactory == null){
      dbConnectionFactory = new DBConnectionFactory();
    }
    return dbConnectionFactory;
  }

  public void bootstrapDB(Vertx vertx) throws MessagingException {
//    String mongoUri = PROTOCOL + USER + ":" + PASSWORD + "@" + HOST;
//    JsonObject dbConfig = new JsonObject();
//    dbConfig.put("db_name", DBNAME);
//    dbConfig.put("useObjectId",true);
//    mongoClient = MongoClient.createShared(vertx,dbConfig);
//
//    mongoClient.createCollection("data");
//    JsonObject data = new JsonObject();
//    data.put("title","test-doc");

  }


  public MongoClient getMongoClient() {
    return mongoClient;
  }

  public void bootstrapDB() throws MessagingException {
    try {
      mongoClient = new MongoClientBuilder().generateMongoClient();
      Morphia morphia = new Morphia();
      morphia.mapPackage(DB_ENTITY_PACKAGE);
      datastore = morphia.createDatastore(mongoClient,DBNAME);
      datastore.ensureIndexes(true);
    } catch (MessagingException e) {
      throw new MessagingException(e.getErrorMessage(),e.getErrorType(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }catch (Exception e){
      throw new MessagingException(MessagingException.getGenericExceptionMessage(e), Errors.DATABASE_ERROR,HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
  }

  public Datastore getDatastore() {
    return datastore;
  }
}
