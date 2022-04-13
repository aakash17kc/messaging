package com.aakash.messaging.entities;

import com.aakash.messaging.utils.Required;
import org.mongodb.morphia.annotations.Entity;

@Entity(noClassnameStored = true)
public class Topic extends BaseEntity {
  @Required
  private String topic;
  @Required
  private String client;
  private String status = "UNSUBSCRIBED";


  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getClient() {
    return client;
  }

  public void setClient(String client) {
    this.client = client;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


}
