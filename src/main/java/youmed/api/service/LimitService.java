package youmed.api.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import youmed.api.builder.DBConfigBuilder;
import youmed.api.constant.Collection;
import youmed.api.repository.LimitRepository;

public class LimitService extends AbstractVerticle implements LimitRepository {
	static MongoClient client;

	@Override
	public void start() {
		client = DBConfigBuilder.mongoClient;
	}
	
	@Override
	public void updateMaximumStudent(String clazzId, int value, Handler<AsyncResult<JsonObject>> resultHandler) {
		JsonObject query = new JsonObject();
		query.put("_id", clazzId);

		JsonObject update = new JsonObject();
		update.put("$set", new JsonObject().put("maximum", value));
		
		client.findOneAndUpdate(Collection.LIMIT, query, update, res -> {
			if (res.succeeded()) {
				resultHandler.handle(Future.succeededFuture(res.result()));
			} else {
				resultHandler.handle(Future.failedFuture("Error"));
			}
		});
	}

	@Override
	public void checkMaximum(String clazzId, Handler<AsyncResult<JsonObject>> resultHandler) {
		JsonObject query = new JsonObject();
		query.put("_id", clazzId);
		client.findOne(clazzId, query, null, res -> {
			if (res.succeeded()) {
				resultHandler.handle(Future.succeededFuture(res.result()));
			}
			else {
				resultHandler.handle(Future.failedFuture("Error"));
			}
		});
	}

}
