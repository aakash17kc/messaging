package com.aakash.messaging.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.json.JsonObject;

import java.util.UUID;

public class MessagingHelper {

  private ObjectMapper objectMapper;

  private static MessagingHelper messagingHelper;

  private MessagingHelper() {
  }

  public static MessagingHelper getInstance(){
    if(messagingHelper == null){
      messagingHelper = new MessagingHelper();
    }
    return messagingHelper;
  }


  public String getUuid() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  public <T> T getJsonMappedToClass(JsonObject jsonObject, Class<T> tClass) throws JsonProcessingException {
    return objectMapper.readValue(jsonObject.toString(),tClass);
  }


  public void setObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

}
