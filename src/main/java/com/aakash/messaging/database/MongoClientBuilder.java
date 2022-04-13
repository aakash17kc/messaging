package com.aakash.messaging.database;

import com.aakash.messaging.exceptions.Errors;
import com.aakash.messaging.exceptions.MessagingException;
import com.aakash.messaging.utils.ConfigHelper;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;

public class MongoClientBuilder {

  private final static String MONGO_PROTOCOL = ConfigHelper.getInstance().getProperty("mongodb.protocol");
  private static final String DBNAME = ConfigHelper.getInstance().getProperty("mongodb.name");
  private static final String USER = ConfigHelper.getInstance().getProperty("mongodb.user");
  private static final String PASSWORD = ConfigHelper.getInstance().getProperty("mongodb.password");
  private static final String HOST = ConfigHelper.getInstance().getProperty("mongodb.host");
  private static final int CONNECTION_PER_HOST = Integer.parseInt(ConfigHelper.getInstance().getProperty("mongodb.connections"));
  private static final Logger LOG = LoggerFactory.getLogger(MongoClientBuilder.class);

  public MongoClient generateMongoClient() throws MessagingException {

    try {
      StringBuilder completeUri = new StringBuilder(MONGO_PROTOCOL);

      if (!StringUtils.isBlank(USER) && !StringUtils.isBlank(PASSWORD)) {
        completeUri.append(URLEncoder.encode(USER, "UTF-8")).append(":" ).append(
            URLEncoder.encode(PASSWORD, "UTF-8")).append("@");
      }
      completeUri.append(HOST);
      return new MongoClient(getUri(completeUri.toString()));
    } catch (Exception e) {
      LOG.error("Error in creating mongo client");
      throw new MessagingException(MessagingException.getGenericExceptionMessage(e), Errors.DATABASE_ERROR, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Gets the mongo URI.
   * @param uri The uri of Mongo to connect to.
   * @return the mongo URI
   */
  private MongoClientURI getUri(String uri) {

    MongoClientOptions.Builder clientOptions = MongoClientOptions.builder().connectionsPerHost(CONNECTION_PER_HOST);
    clientOptions.maxConnectionIdleTime(60000);
    return new MongoClientURI(uri, clientOptions);
  }
}
