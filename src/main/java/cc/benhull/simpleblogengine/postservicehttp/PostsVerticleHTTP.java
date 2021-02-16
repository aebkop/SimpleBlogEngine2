package cc.benhull.simpleblogengine.postservicehttp;

import cc.benhull.simpleblogengine.PgConfig;
import cc.benhull.simpleblogengine.apiservice.APIVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

public class PostsVerticleHTTP extends AbstractVerticle {
  private final Logger logger = LoggerFactory.getLogger(APIVerticle.class);
  private WebClient webclient;
  private PgPool pgPool;

  @Override
  public void start() {
    pgPool = PgPool.pool(vertx, PgConfig.pgConnectOpts(), new PoolOptions());
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    webclient = WebClient.create(vertx);

    router.get("/post/:postid").handler(this::post);
    router.get("/").handler(this::posts);

    server.requestHandler(router).listen(3000);

  }

  private void post(RoutingContext ctx) {

  }

  private void posts(RoutingContext ctx) {
  }


}
}
