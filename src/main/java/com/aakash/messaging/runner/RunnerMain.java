package com.aakash.messaging.runner;

import com.aakash.messaging.database.DBConnectionFactory;
import com.aakash.messaging.exceptions.MessagingException;
import com.aakash.messaging.utils.Constants;
import com.aakash.messaging.utils.MessagingFactory;
import com.aakash.messaging.verticles.MessagingController;
import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunnerMain extends AbstractVerticle {


  private static final Logger LOG = LoggerFactory.getLogger(RunnerMain.class);


  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new RunnerMain()).onFailure(
        handler -> {
          LOG.error("Failed to deploy Main");
        }
    );
  }

  public void start(Promise<Void> startPromise) throws Exception {
    DeploymentOptions deploymentOptions = new DeploymentOptions();
    deploymentOptions.setInstances(4);

    WorkerExecutor workerExecutor = vertx.createSharedWorkerExecutor(Constants.WORKER_POOL_NAME);

    LOG.info("Connecting to Database");
    Future<Void> databaseFuture = startDBAsync(workerExecutor, startPromise);

    LOG.info("Deploying Verticles");
    Future<MessagingFactory> verticleFutures = startVerticleAsync(workerExecutor, startPromise);

    CompositeFuture.all(databaseFuture, verticleFutures).onSuccess(handler -> {
      LOG.info("####### Messaging Server Started ##########");
      startPromise.complete();
    }).onFailure(handler -> {
      LOG.error("####### Service startup failed with error ########## ", handler.getCause());
      startPromise.fail("Failed");

    });
  }

  private Future<MessagingFactory> startVerticleAsync(WorkerExecutor workerExecutor, Promise<Void> startPromise) {

    return workerExecutor.<MessagingFactory>executeBlocking(handler -> {
      try {
        MessagingFactory messagingFactory = new MessagingFactory();
        handler.complete(messagingFactory);
      } catch (Exception e) {
        startPromise.tryFail("Failed to deploy Verticles.");
        handler.fail("Failed to deploy Verticles.");
      }
    }).onSuccess(messagingFactory -> {
      MessagingController messagingController = getMessagingController(messagingFactory);
      startVerticles(startPromise, messagingController.getClass()).onSuccess(handler -> {
        LOG.info("All Verticles Deployed");
      }).onFailure(handler -> {
        startPromise.tryFail("Failed to deploy Verticles");
        LOG.error("Failed to deploy Verticles ", handler.getCause());
      });
    });

  }

  private MessagingController getMessagingController(MessagingFactory messagingFactory) {
    MessagingController messagingController = new MessagingController();
    messagingController.setResponseHandler(messagingFactory.getResponseHandler());
    messagingController.setTopicHandler(messagingFactory.getTopicHandler());
    return messagingController;
  }

  private Future<String> startVerticles(Promise<Void> startPromise, final Class verticleClass) {
    DeploymentOptions deploymentOptions = new DeploymentOptions().setInstances(4).setWorker(true);
    return vertx.deployVerticle(verticleClass.getName(), deploymentOptions).onComplete(ar -> {
      if (ar.succeeded()) {
        LOG.info(verticleClass.getSimpleName() + " deployed with " + deploymentOptions.getInstances() + " instances");
      }else {
        LOG.error("Failed to deploy " + verticleClass.getSimpleName());
      }
    });
  }

  private Future<Void> startDBAsync(WorkerExecutor workerExecutor, Promise<Void> startPromise) {
    return workerExecutor.executeBlocking(handler -> {
      try {
        DBConnectionFactory.getInstance().bootstrapDB();
        handler.complete();
      } catch (MessagingException e) {
        handler.fail("Error starting DB :" + e.getErrorMessage());
        LOG.error("Error starting DB :" + e.getErrorMessage());
        startPromise.tryFail("Error starting DB :" + e.getErrorMessage());
        shutdown();
      }
    });
  }

  private static void startDB(Vertx vertx) throws MessagingException {
    DBConnectionFactory.getInstance().bootstrapDB(vertx);
  }

  private void shutdown() {
    vertx.deploymentIDs().forEach(vertx::undeploy);
    vertx.close();
  }
}
