package com.aakash.messaging.database;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Criteria;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DBLayer {

  private static final Logger LOG = LoggerFactory.getLogger(DBLayer.class);

  public static <T> String save(T object) {
    Datastore datastore = DBConnectionFactory.getInstance().getDatastore();
    Key<T> key = datastore.save(object);
    return key.getId().toString();
  }

  public static <T> List<T> findAll(Map<String, Object> andConditions,
                                    Map<String, Object> orConditions, Class<T> clazzValue, boolean disableValidation) {
    Query<T> query = createQuery(andConditions, orConditions, clazzValue, disableValidation);
    return query.asList();
  }

  public static <T> T find(Map<String, Object> andConditions, Map<String, Object> orConditions, Class<T> clazzValue, boolean disableValidation) {
    Query<T> query = createQuery(andConditions, orConditions, clazzValue, disableValidation);
    return query.get();

  }

  private static <T> Query createQuery(Map<String, Object> andConditions,
                                       Map<String, Object> orConditions, Class<T> clazzValue, boolean disableValidation) {
    Datastore datastore = DBConnectionFactory.getInstance().getDatastore();
    Query<T> query = datastore.createQuery(clazzValue);

    if (disableValidation) {
      query = query.disableValidation();
    }

    // creating AND conditions
    mapAndConditions(andConditions, query);

    // Creating OR conditions
    mapOrConditions(orConditions, query);

    return query;
  }

  /**
   * Map or conditions.
   *
   * @param <T>          the generic type
   * @param orConditions the or conditions
   * @param query        the query
   */
  private static <T> void mapOrConditions(Map<String, Object> orConditions, Query<T> query) {
    if (orConditions != null) {
      List<Criteria> orCriterias = new ArrayList<>();
      for (Map.Entry<String, Object> entry : orConditions.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        if (value instanceof Boolean) {
          orCriterias.add(query.criteria(key).equal(Boolean.parseBoolean(value.toString())));
        } else if (value instanceof Integer) {
          orCriterias.add(query.criteria(key).equal(Integer.parseInt(value.toString())));
        } else if (value instanceof Float) {
          orCriterias.add(query.criteria(key).equal(Float.parseFloat(value.toString())));
        } else {
          orCriterias.add(query.criteria(key).equal(value.toString()));
        }
      }
      query.or(orCriterias.toArray(new Criteria[]{}));
    }
  }

  /**
   * Map and conditions.
   *
   * @param <T>           the generic type
   * @param andConditions the and conditions
   * @param query         the query
   */
  private static <T> void mapAndConditions(Map<String, Object> andConditions, Query<T> query) {
    if (andConditions != null) {
      for (Map.Entry<String, Object> entry : andConditions.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        if (value instanceof Boolean) {
          query.filter(key + " = ", Boolean.parseBoolean(value.toString()));
        } else if (value instanceof Integer) {
          query.filter(key + " = ", Integer.parseInt(value.toString()));
        } else if (value instanceof Float) {
          query.filter(key + " = ", Float.parseFloat(value.toString()));
        } else if (value instanceof List<?>) {
          String[] parts = key.trim().split(" ");
          if (Arrays.stream(parts).anyMatch("not"::equals))
            query.filter(parts[0] + " nin", value);
          else
            query.filter(key + " in", value);
        } else {
          query.filter(key + " = ", value.toString());
        }
      }
    }
  }

}
