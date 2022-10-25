package youmed.api.service;

import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import youmed.api.builder.DBConfigBuilder;
import youmed.api.constant.Collection;
import youmed.api.repository.ClassRepository;

public class ClassService extends AbstractVerticle implements ClassRepository {

	static MongoClient client;

	@Override
	public void start() {
		client = DBConfigBuilder.mongoClient;
	}

	@Override
	public void addClazz(String jsonPayload, Handler<AsyncResult<JsonObject>> resultHandler) {

		client.insert(Collection.CLAZZ, new JsonObject(jsonPayload), res -> {
			if (res.succeeded()) {
				String result = res.result();
				resultHandler.handle(Future.succeededFuture(new JsonObject(result)));
			} else {
				resultHandler.handle(Future.failedFuture("Error"));
			}
		});
	}

	@Override
	public void getAllClazz(Handler<AsyncResult<List<JsonObject>>> resultHandler) {
		client.find(Collection.CLAZZ, new JsonObject(), ar -> {
			if (ar.succeeded()) {
				List<JsonObject> listJsonObj = ar.result();
				resultHandler.handle(Future.succeededFuture(listJsonObj));
			} else {
				resultHandler.handle(Future.failedFuture(ar.cause()));
			}

		});
	}

	@Override
	public void getClazzById(String clazzId, Handler<AsyncResult<JsonObject>> resultHandler) {
		JsonObject query = new JsonObject().put("_id", clazzId);
		client.findOne(Collection.CLAZZ, query, null, ar -> {
			if (ar.succeeded()) {
				if (ar.succeeded()) {
					JsonObject result = ar.result();
					if (result == null) {
						resultHandler.handle(Future.failedFuture("Error"));
					} else {
						resultHandler.handle(Future.succeededFuture(result));
					}

				} else {
					ar.cause();
				}

			}
		});
	}

	@Override
	public void updateClazz(String clazzId, String payload, Handler<AsyncResult<JsonObject>> resultHandler) {
		JsonObject query = new JsonObject();
		query.put("_id", clazzId);

		JsonObject update = new JsonObject();
		update.put("$set", new JsonObject(payload));

		client.findOneAndUpdate(Collection.CLAZZ, query, update, res -> {
			if (res.succeeded()) {
				JsonObject result = res.result();
				resultHandler.handle(Future.succeededFuture(result));
			} else {
				resultHandler.handle(Future.failedFuture("Error"));
			}
		});
	}

	@Override
	public void deleteClazz(String clazzId, Handler<AsyncResult<String>> resultHandler) {
		JsonObject query = new JsonObject();
		query.put("_id", clazzId);
		client.findOneAndDelete(Collection.CLAZZ, query, res -> {
			if (res.succeeded()) {
				resultHandler.handle(Future.succeededFuture());
			} else {
				resultHandler.handle(Future.failedFuture("Error"));
			}
		});
	}

}
