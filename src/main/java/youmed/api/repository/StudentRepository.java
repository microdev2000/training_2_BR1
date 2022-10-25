package youmed.api.repository;

import java.util.List;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

public interface StudentRepository {

	void addStudent(String jsonPayload, String clazzId,  Handler<AsyncResult<JsonObject>> resultHandler);

	void getStudentById(String studentId, Handler<AsyncResult<JsonObject>> resultHandler);

	void getALLStudent(Handler<AsyncResult<List<JsonObject>>> resultHandler);

	void updateStudent(String studentId, String payload, Handler<AsyncResult<JsonObject>> resultHandler);

	void deleteStudent(String studentId, String clazzId, Handler<AsyncResult<String>> resultHandler);
}
