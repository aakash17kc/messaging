package com.aakash.messaging.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

    private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.createHttpServer().requestHandler( res ->{
            res.response().end("Heeloo from vertx");
        }).listen(8080,httpServerAsyncResult -> {
            if(httpServerAsyncResult.succeeded()){
                startPromise.complete();
                LOG.debug("Server running at 8080");
            }else {
                startPromise.fail("Failed");
                LOG.error("Server running at 8080");
            }
        });
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        super.stop(stopPromise);
    }
}
