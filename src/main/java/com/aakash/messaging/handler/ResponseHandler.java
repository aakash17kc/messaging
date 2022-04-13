package com.aakash.messaging.handler;

import com.aakash.messaging.utils.MessagingHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ResponseHandler.class);
  private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";

  private static ResponseHandler responseHandler;
  private MessagingHelper messagingHelper;

  private ResponseHandler(){

  }

  public static ResponseHandler getInstance(){
    if(responseHandler == null){
      responseHandler = new ResponseHandler();
    }
    return responseHandler;
  }

  public void setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  private static ObjectMapper objectMapper;

  /** The Constant CONTENT_TYPE. */
  private static final String CONTENT_TYPE = "content-type";


  public static void sendSuccessResponse(RoutingContext routingContext, Object response) throws JsonProcessingException {

    routingContext.response().putHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8)
        .setStatusCode(HttpStatus.SC_OK)
        .end(objectMapper.writeValueAsString(response));

  }

  public static void sendErrorResponse(RoutingContext routingContext, String errorMessage, String errorType, int scBadRequest){

    Map<String,String> error = new HashMap<>();
    error.put("message",errorMessage);
    error.put("error",errorType);
    try {
      routingContext.response().putHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8)
          .setStatusCode(HttpStatus.SC_BAD_REQUEST)
          .end(objectMapper.writeValueAsString(error));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

  }

  public  void testHandler(RoutingContext routingContext){
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("status","Service is UP");

    getRoutingContext(routingContext).setStatusCode(HttpStatus.SC_OK)
        .end(String.valueOf(jsonObject));
  }

  public  void allRoutes(RoutingContext routingContext){
    JsonObject jsonObject = new JsonObject();
    jsonObject.put("/topic","GET, POST");

    getRoutingContext(routingContext).setStatusCode(HttpStatus.SC_OK)
        .end(String.valueOf(jsonObject));

  }

  private HttpServerResponse getRoutingContext(RoutingContext routingContext){
    return routingContext.response().putHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8);
  }

  public void setMessagingHelper(MessagingHelper messagingHelper) {

  }
}
