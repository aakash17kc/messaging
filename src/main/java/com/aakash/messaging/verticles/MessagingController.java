package com.aakash.messaging.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagingController extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MessagingController.class);




  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    Router router = Router.router(vertx);


    super.start(startPromise);
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    super.stop(stopPromise);
  }
}
