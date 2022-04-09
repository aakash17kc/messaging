package com.aakash.messaging.database;

import com.aakash.messaging.exceptions.Errors;
import com.aakash.messaging.exceptions.MessagingException;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DBLayer {

  private static final Logger LOG = LoggerFactory.getLogger(DBLayer.class);


  public static Future<JsonObject> save (JsonObject object){
    String fg;
    MongoClient mongoClient = DBConnectionFactory.getInstance().getMongoClient();

  mongoClient.save("data",object, response ->{
      if(response.succeeded()){
        LOG.info("Successfully inserted data");
      }else {
        LOG.error("Failed inserted data",response.cause());
      }
    });

  }
}
