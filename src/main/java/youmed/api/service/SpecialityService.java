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
import youmed.api.repository.SpecialityRepository;

public class SpecialityService extends AbstractVerticle implements SpecialityRepository {

	static MongoClient client;

	@Override
	public void start() {
		client = DBConfigBuilder.mongoClient;
	}

	@Override
	public void addSpeciality(String payload, Handler<AsyncResult<JsonObject>> resultHandler) {
		client.insert(Collection.SPECIALITY, new JsonObject(payload), res -> {
			if (res.succeeded()) {
				resultHandler.handle(Future.succeededFuture(new JsonObject(res.result())));
			} else {
				resultHandler.handle(Future.failedFuture("Error"));
			}
		});
	}

	@Override
	public void getAllSpeciality(Handler<AsyncResult<List<JsonObject>>> resultHandler) {
		client.find(Collection.SPECIALITY, new JsonObject(), ar -> {
			if (ar.succeeded()) {
				List<JsonObject> listJsonObj = ar.result();
				resultHandler.handle(Future.succeededFuture(listJsonObj));
			} else {
				resultHandler.handle(Future.failedFuture(ar.cause()));
			}

		});
	}

	@Override
	public void getSpecialityById(String specialityId, Handler<AsyncResult<JsonObject>> resultHandler) {
		JsonObject query = new JsonObject().put("_id", specialityId);
		client.find(Collection.SPECIALITY, query, ar -> {
			if (ar.succeeded()) {
				List<JsonObject> listJsonObj = ar.result();
				System.out.println(listJsonObj);
				if (listJsonObj.size() == 0) {
					resultHandler.handle(Future.failedFuture("Error"));
				} else {
					resultHandler.handle(Future.succeededFuture(listJsonObj.get(0)));
				}

			} else {
				ar.cause();
			}
		});
	}

	@Override
	public void updateSpeciality(String specialityId, String payload, Handler<AsyncResult<JsonObject>> resultHandler) {
		JsonObject query = new JsonObject();
		query.put("_id", specialityId);

		JsonObject update = new JsonObject();
		update.put("$set", new JsonObject(payload));

		client.findOneAndUpdate(Collection.SPECIALITY, query, update, res -> {
			if (res.succeeded()) {
				resultHandler.handle(Future.succeededFuture());
			} else {
				resultHandler.handle(Future.failedFuture("Error"));
			}
		});
	}

	@Override
	public void deleteSpeciality(String specialityId, Handler<AsyncResult<String>> resultHandler) {
		JsonObject query = new JsonObject();
		query.put("_id", specialityId);
		client.findOneAndDelete(Collection.SPECIALITY, query, res -> {
			if (res.succeeded()) {
				resultHandler.handle(Future.succeededFuture());
			} else {
				resultHandler.handle(Future.failedFuture("Error"));
			}
		});
	}

}
