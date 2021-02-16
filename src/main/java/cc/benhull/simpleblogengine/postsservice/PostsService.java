package cc.benhull.simpleblogengine.postsservice;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface PostsService {
    static PostsService create(Vertx vertx) {
        return new PostsServiceImpl(vertx);
    }

    static PostsService createProxy(Vertx vertx, String address) {
        return new PostsServiceVertxEBProxy(vertx,address);
    }

    void getPost(String id, Handler<AsyncResult<JsonObject>> handler);

    void getPosts(Handler<AsyncResult<JsonObject>> handler);
    void test();

}
