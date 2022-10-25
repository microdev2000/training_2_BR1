package youmed.api.repository;

import java.util.List;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public interface SpecialityRepository {
	void addSpeciality(String payload, Handler<AsyncResult<JsonObject>> resultHandler);

	void getAllSpeciality(Handler<AsyncResult<List<JsonObject>>> resultHandler);

	void getSpecialityById(String specialityId, Handler<AsyncResult<JsonObject>> resultHandler);

	void updateSpeciality(String specialityId, String payload, Handler<AsyncResult<JsonObject>> resulHandler);

	void deleteSpeciality(String specialityId, Handler<AsyncResult<String>> resultHandler);
}
