package com.aakash.messaging.verticles;

import com.aakash.messaging.handler.ResponseHandler;
import com.aakash.messaging.handler.TopicHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagingController extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MessagingController.class);

  private static ResponseHandler responseHandler;

  private static TopicHandler topicHandler;


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.route().order(-2).handler(BodyHandler.create());
    router.route("/test").order(-1).handler(responseHandler::testHandler);
    router.route("/allRoutes").order(0).handler(responseHandler::allRoutes);

    prepareTopicRoutes(router);

    vertx.createHttpServer().requestHandler(router).listen(8080, "0.0.0.0", ar -> {
      if (ar.succeeded()) {
        startPromise.complete();
        LOG.info(Thread.currentThread().getName() + " SUCCESSFULLY STARTED HTTPSERVER. All routes ACTIVE");
      } else {
        startPromise.fail("Failed to deploy HTTP server");
        LOG.info("Failed to deploy HTTP server");
      }
    });
  }

  private void prepareTopicRoutes(Router router) {

    router.get("/messaging/topic/:topic").blockingHandler(topicHandler::getTopic,false);
    router.post("/messaging/topic").blockingHandler(topicHandler::createTopic, false);
//    router.delete("messaging/topic/:topic").blockingHandler(topicHandler::deleteTopic,false);
//    router.put("messaging/topic/:topic").blockingHandler(topicHandler::editTopic,false);

    router.get("/messaging/topics").blockingHandler(topicHandler::getAllTopics, false);
  }

  public void setResponseHandler(ResponseHandler responseHandler) {
    this.responseHandler = responseHandler;
  }

  public void setTopicHandler(TopicHandler topicHandler) {
    this.topicHandler = topicHandler;
  }

}
