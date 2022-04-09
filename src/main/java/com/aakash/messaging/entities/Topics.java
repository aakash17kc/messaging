package com.aakash.messaging.entities;

public class Topics  extends BaseEntity{

  public String topic;
  public String client;


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


}
