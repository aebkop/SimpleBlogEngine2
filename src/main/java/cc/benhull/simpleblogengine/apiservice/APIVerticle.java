package cc.benhull.simpleblogengine.apiservice;

import cc.benhull.simpleblogengine.postsservice.PostsService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.ext.web.handler.LoggerHandler;
import io.vertx.ext.web.handler.impl.LoggerHandlerImpl;

import static io.vertx.ext.web.handler.LoggerFormat.TINY;

public class APIVerticle extends AbstractVerticle {
  private WebClient webClient;
  private final Logger logger = LoggerFactory.getLogger(APIVerticle.class);


  public void start() {
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    String prefix = "";
    WebClient webClient = WebClient.create(vertx);
    this.webClient = webClient;

    //posts
    router.get(prefix + "/post/:postid").handler(this::getPost);
    //TODO handle pagination
    router.get(prefix + "/posts").handler(this::getPosts);
    //comments
    //TODO handle threaded comments
    router.get(prefix + "/:postid/comments").handler(this::comments);


    logger.info("API service started");


    server.requestHandler(router).listen(8080);


  }

  private void getPost(RoutingContext routingContext) {
    logger.info("get post");
    PostsService postsService = PostsService.createProxy(vertx, "posts-service");
    String postID = routingContext.pathParam("postid");
    postsService.test();
    postsService.getPost(postID,
      resp -> {
        if (resp.succeeded()) {
          routingContext.response().putHeader("content-type", "application/json").end(resp.result().encode());
        } else {
          routingContext.response().end("lol");
        }
      });
  }

  private void getPosts(RoutingContext routingContext) {
    PostsService postsService = PostsService.createProxy(vertx, "posts-service");
    logger.info("Get posts");
    postsService.test();
  }

  private void comments(RoutingContext routingContext) {
    webClient
      .get(3001, "localhost", "/posts")
      .expect(ResponsePredicate.SC_SUCCESS)
      .as(BodyCodec.jsonObject())
      .send()
      .map(HttpResponse::body);
  }


}
