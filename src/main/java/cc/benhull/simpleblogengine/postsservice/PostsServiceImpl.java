package cc.benhull.simpleblogengine.postsservice;

import cc.benhull.simpleblogengine.apiservice.APIVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

public class PostsServiceImpl implements PostsService {
  private final PgPool client;
  private final Logger logger = LoggerFactory.getLogger(PostsServiceImpl.class);

  static String selectPost() {
    return "SELECT * FROM posts WHERE post_id=$1;";
  }

  static String selectPosts() {
    return "select array_to_json(array_agg(row_to_json(t)))\n" +
      "from (\n" +
      "         select * from posts\n" +
      "     ) t;";
  }

  public PostsServiceImpl(Vertx vertx) {
    logger.info("POsts Service started");
    PgConnectOptions connectOptions = new PgConnectOptions()
      .setPort(5432)
      .setHost("localhost")
      .setDatabase("postgres")
      .setUser("postgres")
      .setPassword("postgres");
    // Pool options
    PoolOptions poolOptions = new PoolOptions()
      .setMaxSize(5);
    // Create the pooled client
    this.client = PgPool.pool(connectOptions, poolOptions);

  }

  @Override
  public void getPost(String id, Handler<AsyncResult<JsonObject>> handler) {
    logger.info("Get post" + id);
    logger.info("hello");
    client
      .preparedQuery(selectPost())
      .execute(Tuple.of(id))
      //only expect 1 post
      .map(rs -> rs.iterator().next())
      .map(Row::toJson)
      .onComplete(handler)
      .onFailure(logger::error);
    handler.handle(Future.succeededFuture(new JsonObject("sdfdsf")));
  }

  @Override
  public void getPosts(Handler<AsyncResult<JsonObject>> handler) {
    logger.info("Get posts");
    client
      .preparedQuery(selectPosts())
      .execute()
      //only expect 1 post
      .map(rs -> rs.iterator().next())
      .map(Row::toJson)
      .onComplete(handler)
      .onFailure(logger::error);
  }

  @Override
  public void test() {
    logger.info("TWEPOITJEWTJRWEOITJIWEOTIJOWETJIOWEOIJT");

  }
}
