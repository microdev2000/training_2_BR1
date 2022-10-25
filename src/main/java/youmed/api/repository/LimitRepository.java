package youmed.api.repository;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public interface LimitRepository {
	void updateMaximumStudent(String limitId, int value, Handler<AsyncResult<JsonObject>> resultHandler);
	void checkMaximum (String clazzId, Handler<AsyncResult<JsonObject>> resultHandler);
}
