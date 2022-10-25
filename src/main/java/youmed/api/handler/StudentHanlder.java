package youmed.api.handler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import youmed.api.constant.StudentEventBus;
import youmed.api.service.StudentService;

public class StudentHanlder extends AbstractVerticle {
	private StudentService studentService;

	@Override
	public void start() {
		studentService = new StudentService();
		MessageConsumer<JsonObject> addStudent = vertx.eventBus().consumer(StudentEventBus.HANDLE_ADD_STUDENT);
		addStudent.handler(msg -> {
			JsonObject payload = msg.body();
			String clazzId = payload.getString("clazzId");
			payload.remove(clazzId);
			studentService.addStudent(payload.toString(), clazzId, res -> {
				if (res.succeeded()) {
					if (res.result() != null) {
						msg.reply(res.result());
					} else {
						msg.reply(null);
					}
				} else {
					res.cause();
					msg.reply("Error");
				}
			});

		});

		MessageConsumer<JsonObject> getAllStudent = vertx.eventBus().consumer(StudentEventBus.HANDLE_GET_ALL_STUDENT);
		getAllStudent.handler(msg -> {
			studentService.getALLStudent(res -> {
				if (res.succeeded()) {
					if (res.result() != null) {
						msg.reply(res.result());
					} else {
						msg.reply(null);
					}
				} else {
					res.cause();
					msg.reply("Error");
				}
			});
		});

		MessageConsumer<JsonObject> getStudentById = vertx.eventBus()
				.consumer(StudentEventBus.HANDLE_GET_STUDENT_BY_ID);
		getStudentById.handler(msg -> {
			String studentId = msg.body().getString("studentId");
			studentService.getStudentById(studentId, res -> {
				if (res.succeeded()) {
					if (res.result() != null) {
						msg.reply(res.result());
					} else {
						msg.reply(null);
					}
				} else {
					res.cause();
					msg.reply("Error");
				}
			});
		});

		MessageConsumer<JsonObject> updateStudent = vertx.eventBus().consumer(StudentEventBus.HANDLE_GET_STUDENT_BY_ID);
		updateStudent.handler(msg -> {
			String studentId = msg.body().getString("studentId");
			JsonObject payload = msg.body();
			payload.remove(studentId);

			studentService.updateStudent(studentId, payload.toString(), res -> {
				if (res.succeeded()) {
					if (res.result() != null) {
						msg.reply(res.result());
					} else {
						msg.reply(null);
					}
				} else {
					res.cause();
					msg.reply("Error");
				}
			});
		});

		MessageConsumer<JsonObject> deleteStudent = vertx.eventBus().consumer(StudentEventBus.HANDLE_GET_STUDENT_BY_ID);
		deleteStudent.handler(msg -> {
			JsonObject jsonObject = msg.body();
			String studentId = jsonObject.getString("studentId");
			String clazzId = jsonObject.getString("clazzId");
			studentService.deleteStudent(studentId, clazzId, res -> {
				if (res.succeeded()) {
					if (res.result() != null) {
						msg.reply(res.result());
					} else {
						msg.reply(null);
					}
				} else {
					res.cause();
					msg.reply("Error");
				}
			});
		});
	}

}
