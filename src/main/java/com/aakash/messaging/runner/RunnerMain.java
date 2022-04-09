package com.aakash.messaging.runner;

import com.aakash.messaging.database.DBConnectionFactory;
import com.aakash.messaging.utils.Constants;
import com.aakash.messaging.verticles.MainVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunnerMain {

    private static final Logger LOG = LoggerFactory.getLogger(RunnerMain.class);
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setInstances(4);

        WorkerExecutor workerExecutor = vertx.createSharedWorkerExecutor(Constants.WORKER_POOL_NAME);



        vertx.deployVerticle(MainVerticle.class.getName(),deploymentOptions).onComplete(asyncResultHandler ->{
            if(asyncResultHandler.succeeded()){
                LOG.info("Vertx started");
            }else {
                LOG.error("Vertx start fail with error "+ asyncResultHandler.result());
            }
        });
        startDB(vertx);



    }

    private static void startDB(Vertx vertx){
        DBConnectionFactory.getInstance().bootstrapDB(vertx);
    }
}
