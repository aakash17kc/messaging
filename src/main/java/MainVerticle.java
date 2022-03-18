import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.createHttpServer().requestHandler( res ->{
            res.response().end("Heeloo from vertx");
        }).listen(8080,httpServerAsyncResult -> {
            if(httpServerAsyncResult.succeeded()){
                startPromise.complete();
                System.out.println("Server running at 8080");
            }else {
                startPromise.fail("Failed");
                System.out.println("Server running at 8080");
            }
        });
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        super.stop(stopPromise);
    }
}
