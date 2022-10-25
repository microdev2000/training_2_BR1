package youmed.api.repository;

import java.util.List;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public interface ClassRepository {
	public void addClazz(String payload, Handler<AsyncResult<JsonObject>> resultHandler);

	public void getAllClazz(Handler<AsyncResult<List<JsonObject>>> resultHandler);

	public void getClazzById(String clazzId, Handler<AsyncResult<JsonObject>> resultHandler);

	public void updateClazz(String clazzId, String payload, Handler<AsyncResult<JsonObject>> resultHandler);

	public void deleteClazz(String clazzId, Handler<AsyncResult<String>> resultHandler);

}
