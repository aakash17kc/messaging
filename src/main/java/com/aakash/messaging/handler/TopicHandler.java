package com.aakash.messaging.handler;

import com.aakash.messaging.entities.Topics;
import com.aakash.messaging.exceptions.Errors;
import com.aakash.messaging.exceptions.MessagingException;
import com.aakash.messaging.utils.ValidationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TopicHandler {

  private static final Logger LOG = LoggerFactory.getLogger(TopicHandler.class);
  private MessagingLayer messagingLayer;

  private ObjectMapper objectMapper;
  private static TopicHandler topicHandler;


  private ResponseHandler responseHandler;

  private TopicHandler() {

  }

  public static TopicHandler getInstance() {
    if (topicHandler == null) {
      topicHandler = new TopicHandler();
    }
    return topicHandler;
  }

  public void getAllTopics(RoutingContext routingContext) {
    try {
      List<Topics> topicsList = messagingLayer.getAllMessagingTopics();

      ResponseHandler.sendSuccessResponse(routingContext, topicsList);
      LOG.info("Fetched all Topics successfully");

    } catch (Exception e) {
      LOG.error("Error while fetching all Topics :" + MessagingException.getGenericExceptionMessage(e));
      ResponseHandler.sendErrorResponse(routingContext, MessagingException.getGenericExceptionMessage(e), Errors.APPLICATION_ERROR, HttpStatus.SC_BAD_REQUEST);
    }
  }

  public void getTopic(RoutingContext routingContext) {
    try {
      final String topicName = routingContext.pathParam("topic");
      if (topicName != null) {
        try {
          Topics topics = messagingLayer.getTopic(topicName);
          if (topics == null) {
            ResponseHandler.sendErrorResponse(routingContext, "Topic doesn't exist", Errors.NOT_FOUND, HttpStatus.SC_NOT_FOUND);
          } else {
            ResponseHandler.sendSuccessResponse(routingContext, topics);
            LOG.info(String.format("Topic fetched successfully : %s",  topics.getTopic()));
          }
        } catch (MessagingException e) {
          ResponseHandler.sendErrorResponse(routingContext, e.getErrorMessage(), e.getErrorType(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
      }
    } catch (Exception e) {
      ResponseHandler.sendErrorResponse(routingContext, e.getMessage(), Errors.APPLICATION_ERROR, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
  }

  public void editTopic(RoutingContext routingContext) {

  }

  public void createTopic(RoutingContext routingContext) {
    try {
      try {
        JsonObject routeJson = routingContext.getBodyAsJson();
        List<String> validationList = ValidationUtil.validateFields(routeJson, Topics.class);
        errorMessageBuilder(validationList);
        Topics topics = messagingLayer.createTopic(objectMapper.readValue(routeJson.toString(), Topics.class));

        if (topics == null) {
          ResponseHandler.sendErrorResponse(routingContext, "Error while processing request", Errors.APPLICATION_ERROR, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        } else {
          ResponseHandler.sendSuccessResponse(routingContext, topics);
          LOG.info("Topic create successfully :" + topics.getTopic());
        }
      } catch (MessagingException e) {
        ResponseHandler.sendErrorResponse(routingContext, e.getMessage(), e.getErrorType(), HttpStatus.SC_INTERNAL_SERVER_ERROR);
      }
    } catch (Exception e) {
      ResponseHandler.sendErrorResponse(routingContext, e.getMessage(), Errors.APPLICATION_ERROR, HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

  }

  public void deleteTopic(RoutingContext routingContext) {

  }

  private void errorMessageBuilder(List<String> validationList) throws MessagingException {
    if (!validationList.isEmpty()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Errors.MISSING_ENTITY_ERROR).append(" ");
      for (String field : validationList) {
        stringBuilder.append(field).append(", ");
      }
      throw new MessagingException(stringBuilder.toString(), Errors.INVALID_REQUEST,HttpStatus.SC_BAD_REQUEST);
    }

  }

  public void setMessagingLayer(MessagingLayer messagingLayer) {
    this.messagingLayer = messagingLayer;
  }

  public void setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public void setResponseHandler(ResponseHandler responseHandler) {
    this.responseHandler = responseHandler;
  }

}
