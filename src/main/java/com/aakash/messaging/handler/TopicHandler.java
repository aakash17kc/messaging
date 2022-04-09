package com.aakash.messaging.handler;

import com.aakash.messaging.database.DBLayer;
import com.aakash.messaging.entities.Topics;
import com.aakash.messaging.exceptions.Errors;
import com.aakash.messaging.exceptions.MessagingException;
import com.aakash.messaging.utils.ValidationUtil;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TopicHandler {

  private static final Logger LOG = LoggerFactory.getLogger(TopicHandler.class);

  public void addTopic(RoutingContext routingContext){
    try {
      JsonObject routeJson = routingContext.getBodyAsJson();
      List<String> validationList = ValidationUtil.validateFields(routeJson,Topics.class);
      errorMessageBuilder(validationList);
      DBLayer.save(routeJson);

    }catch (MessagingException e){
        ResponseHandler.sendErrorResponse(routingContext,e.getMessage(),e.getErrorType());
    }


  }

  private void errorMessageBuilder(List<String> validationList) throws MessagingException {
    if(!validationList.isEmpty()){
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(Errors.MISSING_ENTITY_ERROR).append(" ");
      for (String field: validationList) {
        stringBuilder.append(field).append(", ");
      }
      throw new MessagingException(stringBuilder.toString(),Errors.INVALID_REQUEST);
    }

  }


}
