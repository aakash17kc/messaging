package com.aakash.messaging.handler;

import com.aakash.messaging.database.DBLayer;
import com.aakash.messaging.entities.Topics;
import com.aakash.messaging.exceptions.Errors;
import com.aakash.messaging.exceptions.MessagingException;
import com.aakash.messaging.utils.Constants;
import com.aakash.messaging.utils.MessagingHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagingLayer {


  private ObjectMapper objectMapper;


  private MessagingHelper messagingHelper;
  private static MessagingLayer messagingLayer;


  private MessagingLayer() {

  }

  public static MessagingLayer getInstance() {
    if (messagingLayer == null) {
      messagingLayer = new MessagingLayer();
    }
    return messagingLayer;
  }

  public List<Topics> getAllMessagingTopics() throws MessagingException {
    try {
      return DBLayer.findAll(null, null, Topics.class, false);

    } catch (Exception e) {
      throw new MessagingException("Failed to fetch all Topics ", Errors.APPLICATION_ERROR,HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
  }

  public Topics createTopic(Topics topics) throws MessagingException {
    if(getTopic(topics.getTopic()) == null) {
      topics.setId(messagingHelper.getUuid());
      final String topicId = DBLayer.save(topics);
      if (topicId == null) {
        throw new MessagingException("Failed to save Topic ", Errors.APPLICATION_ERROR, HttpStatus.SC_INTERNAL_SERVER_ERROR);
      }
      return topics;
    }else {
      throw new MessagingException("Topic already exists", Errors.ENTITY_EXISTS,HttpStatus.SC_BAD_REQUEST);
    }

  }

  public void setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public void setMessagingHelper(MessagingHelper messagingHelper) {
    this.messagingHelper = messagingHelper;
  }

  public Topics getTopic(String topic) throws MessagingException {
    try {
      Map<String, Object> andConditions = new HashMap<>();
      andConditions.put(Constants.TOPIC, topic);
      return DBLayer.find(andConditions, null, Topics.class, false);
    } catch (Exception e) {
      throw new MessagingException("Failed to fetch Topic ", Errors.APPLICATION_ERROR,HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

  }
}
