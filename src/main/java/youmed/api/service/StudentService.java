package youmed.api.service;

import java.util.List;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import youmed.api.builder.DBConfigBuilder;
import youmed.api.constant.Collection;
import youmed.api.repository.StudentRepository;

@ProxyGen
public class StudentService extends AbstractVerticle implements StudentRepository {

	static MongoClient client;

	@Override
	public void start() {
		client = DBConfigBuilder.mongoClient;
	}

	@Override
	public void addStudent(String jsonPayload, String clazzId, Handler<AsyncResult<JsonObject>> resultHandler) {
		JsonObject jsonQuery = new JsonObject();
		jsonQuery.put("_id", clazzId);
		client.findOne(Collection.LIMIT, jsonQuery, null, res -> {
			int total = res.result().getInteger("total");

			JsonObject jsonUpdate = new JsonObject();
			jsonUpdate.put("$set", new JsonObject().put("total", total + 1));

			client.findOneAndUpdate(clazzId, jsonQuery, jsonUpdate, result -> {
				if (result.succeeded()) {
					System.out.println("Update Success");
				} else {
					result.cause();
				}
			});
		});
		client.insert(Collection.STUDENT, new JsonObject(jsonPayload), res -> {
			if (res.succeeded()) {
				resultHandler.handle(Future.succeededFuture(new JsonObject(res.result())));
			} else {
				resultHandler.handle(Future.failedFuture("Error"));
			}
		});
	}

	@Override
	public void getStudentById(String studentId, Handler<AsyncResult<JsonObject>> resultHandler) {
		JsonObject query = new JsonObject().put("_id", studentId);
		client.find(Collection.STUDENT, query, ar -> {
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
	public void getALLStudent(Handler<AsyncResult<List<JsonObject>>> resultHandler) {
		client.find(Collection.STUDENT, new JsonObject(), ar -> {
			if (ar.succeeded()) {
				List<JsonObject> listJsonObj = ar.result();
				resultHandler.handle(Future.succeededFuture(listJsonObj));
			} else {
				ar.cause();
			}
		});
	}

	@Override
	public void updateStudent(String studentId, String payload, Handler<AsyncResult<JsonObject>> resultHandler) {
		JsonObject query = new JsonObject();
		query.put("_id", studentId);

		JsonObject update = new JsonObject();
		update.put("$set", new JsonObject(payload));

		client.findOneAndUpdate(Collection.STUDENT, query, update, res -> {
			if (res.succeeded()) {
				resultHandler.handle(Future.succeededFuture(res.result()));
			} else {
				resultHandler.handle(Future.failedFuture("Error"));
			}
		});

	}

	@Override
	public void deleteStudent(String studentId, String clazzId, Handler<AsyncResult<String>> resultHandler) {
		JsonObject query = new JsonObject();
		query.put("_id", studentId);

		JsonObject jsonQuery = new JsonObject();
		jsonQuery.put("_id", clazzId);
		client.findOne(Collection.LIMIT, jsonQuery, null, res -> {
			int total = res.result().getInteger("total");

			JsonObject jsonUpdate = new JsonObject();
			jsonUpdate.put("$set", new JsonObject().put("total", total - 1));

			client.findOneAndUpdate(clazzId, jsonQuery, jsonUpdate, result -> {
				if (result.succeeded()) {
					System.out.println("Update Success");
				} else {
					result.cause();
				}
			});

		});

		client.findOneAndDelete(Collection.STUDENT, query, res -> {
			if (res.succeeded()) {
				resultHandler.handle(Future.succeededFuture());
			} else {
				resultHandler.handle(Future.failedFuture("Error"));
			}
		});
	}
}
