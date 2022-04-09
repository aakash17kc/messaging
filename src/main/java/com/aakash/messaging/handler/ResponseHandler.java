package com.aakash.messaging.handler;

import io.vertx.ext.web.RoutingContext;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ResponseHandler.class);
  private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";

  /** The Constant CONTENT_TYPE. */
  private static final String CONTENT_TYPE = "content-type";


  public static void sendSuccessResponse(RoutingContext routingContext, Object response){

    routingContext.response().putHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8)
        .setStatusCode(HttpStatus.SC_OK)
        .end(String.valueOf(response));

  }

  public static void sendErrorResponse(RoutingContext routingContext, String errorMessage, String errorType){

    Map<String,String> error = new HashMap<>();
    error.put("message",errorMessage);
    error.put("error",errorType);
    routingContext.response().putHeader(CONTENT_TYPE, APPLICATION_JSON_CHARSET_UTF_8)
        .setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
        .end(String.valueOf(error));

  }

}
