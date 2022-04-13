package com.aakash.messaging.utils;

import com.aakash.messaging.handler.MessagingLayer;
import com.aakash.messaging.handler.ResponseHandler;
import com.aakash.messaging.handler.TopicHandler;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessagingFactory {

  private MessagingHelper messagingHelper;
  private ConfigHelper configHelper;
  private TopicHandler topicHandler;
  private MessagingLayer messagingLayer;
  private ObjectMapper objectMapper;
  private ResponseHandler responseHandler;

  public MessagingFactory() {
    this.messagingHelper = MessagingHelper.getInstance();
    this.configHelper = ConfigHelper.getInstance();
    this.topicHandler = TopicHandler.getInstance();
    this.messagingLayer = MessagingLayer.getInstance();
    this.responseHandler = ResponseHandler.getInstance();
    this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    bootstrapAllClasses();
  }

  private void bootstrapAllClasses() {
    messagingHelper.setObjectMapper(objectMapper);
    messagingLayer.setObjectMapper(objectMapper);
    responseHandler.setObjectMapper(objectMapper);
    responseHandler.setMessagingHelper(messagingHelper);
    messagingLayer.setMessagingHelper(messagingHelper);
    topicHandler.setMessagingLayer(messagingLayer);
    topicHandler.setObjectMapper(objectMapper);
    topicHandler.setResponseHandler(responseHandler);
  }

  public MessagingHelper getMessagingHelper() {
    return messagingHelper;
  }

  public ConfigHelper getConfigHelper() {
    return configHelper;
  }

  public TopicHandler getTopicHandler() {
    return topicHandler;
  }


  public MessagingLayer getMessagingLayer() {
    return messagingLayer;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }


  public ResponseHandler getResponseHandler() {
    return responseHandler;
  }


}
