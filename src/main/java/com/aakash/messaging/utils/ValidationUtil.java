package com.aakash.messaging.utils;

import io.vertx.core.json.JsonObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ValidationUtil {

  public static List<String> validateFields(JsonObject jsonString, Class<?> tClass){
    Field[] classFields = tClass.getDeclaredFields();
    List<String> validationList = new ArrayList<>();
    for (Field field: classFields) {
      if(jsonString.containsKey(field.getName())){
        validationList.add(field.getName());
      }
    }
    return validationList;

  }
}
