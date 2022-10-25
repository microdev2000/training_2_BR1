package youmed.api.router;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import youmed.api.constant.ClazzEventBus;
import youmed.api.constant.LimitEventBus;
import youmed.api.dto.StudentDto;
import youmed.api.model.Student;
import youmed.api.service.LimitService;
import youmed.api.service.StudentService;

public class StudentRouter extends AbstractVerticle {

	@Override
	public void start() {
		HttpServer server = vertx.createHttpServer();
		Router studentRouter = Router.router(vertx);
		studentRouter.route("/api/v1/student/*").handler(BodyHandler.create());
		studentRouter.post("/api/v1/student/add").handler(this::addStudent);

		studentRouter.post("/api/v1/student/post").handler(this::addStudent);
		studentRouter.get("/api/v1/student/:studentId").handler(this::getStudentById);

		studentRouter.get("/api/v1/student/get/all").handler(this::getAllStudent);
		studentRouter.put("/api/v1/student/:studentId").handler(this::updateStudent);

		server.requestHandler(studentRouter).listen(9999);

	}

	private void addStudent(RoutingContext rc) {
		String payload = rc.getBodyAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		Student student = null;
		String jsonPayload = null;
		try {
			student = objectMapper.readValue(payload, Student.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		if (student != null) {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			try {
				jsonPayload = ow.writeValueAsString(student);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			JsonObject jsonObject = new JsonObject();
			jsonObject.put("clazzId", student.getClazz().getId());
			vertx.eventBus().request(LimitEventBus.HANDLE_CHECK_LIMIT, jsonObject, reply -> {
				if (reply.succeeded()) {
					String body = reply.result().body().toString();
					if (body == null) {
						rc.request().response().setStatusCode(400)
								.putHeader("Content-Type", "application/json ;charset=utf-8")
								.end(new JsonObject().put("Error", "Class does not exist!").encodePrettily());
						return;
					} else if (body.equalsIgnoreCase("Error")) {
						rc.request().response().setStatusCode(400)
								.putHeader("Content-Type", "application/json ;charset=utf-8")
								.end(new JsonObject().put("Error", "Cannot add more student").encodePrettily());
						return;
					} else {

					}
				} else {
					reply.cause();
				}
			});

			vertx.eventBus().request(ClazzEventBus.HANDLE_ADD_CLAZZ, new JsonObject(jsonPayload), reply -> {
				if (reply.succeeded()) {
					String body = reply.result().body().toString();
					if (body == null) {
						rc.request().response().setStatusCode(400)
								.putHeader("Content-Type", "application/json ;charset=utf-8")
								.end(new JsonObject().put("Error", "Student  already exist!").encodePrettily());
					} else if (body.equalsIgnoreCase("Error")) {
						rc.request().response().setStatusCode(400)
								.putHeader("Content-Type", "application/json ;charset=utf-8")
								.end(new JsonObject().put("Error", "Add student class error").encodePrettily());
					} else {
						rc.request().response().setStatusCode(201)
								.putHeader("Content-Type", "application/json ;charset=utf-8")
								.end(reply.result().body().toString());
					}
				} else {
					reply.cause();
				}
			});
		}
	}

	private void getStudentById(RoutingContext rc) {
		String studentId = rc.request().getParam("studentId");
		JsonObject payload = new JsonObject();
		payload.put("studentId", studentId);
		vertx.eventBus().request(ClazzEventBus.HANDLE_ADD_CLAZZ, payload, reply -> {
			if (reply.succeeded()) {
				String body = reply.result().body().toString();
				if (body == null) {
					rc.request().response().setStatusCode(404)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Student does not exist!").encodePrettily());
				} else if (body.equalsIgnoreCase("Error")) {
					rc.request().response().setStatusCode(400)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Get student error").encodePrettily());
				} else {
					rc.request().response().setStatusCode(201)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(reply.result().body().toString());
				}
			} else {
				reply.cause();
			}
		});
	}

	private void getAllStudent(RoutingContext rc) {
		vertx.eventBus().request(ClazzEventBus.HANDLE_ADD_CLAZZ, null, reply -> {
			if (reply.succeeded()) {
				String body = reply.result().body().toString();
				if (body == null) {
					rc.request().response().setStatusCode(200)
							.putHeader("Content-Type", "application/json ;charset=utf-8").end();
				} else if (body.equalsIgnoreCase("Error")) {
					rc.request().response().setStatusCode(400)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Get all student error").encodePrettily());
				} else {
					rc.request().response().setStatusCode(200)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(reply.result().body().toString());
				}
			} else {
				reply.cause();
			}
		});
	}

	private void updateStudent(RoutingContext rc) {
		String studentId = rc.request().getParam("studentId");
		String json = rc.getBodyAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		StudentDto student = null;
		try {
			student = objectMapper.readValue(json, StudentDto.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String jsonPayload = null;
		try {
			jsonPayload = ow.writeValueAsString(student);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		JsonObject payload = new JsonObject(jsonPayload);
		payload.put("studentId", studentId);

		vertx.eventBus().request(ClazzEventBus.HANDLE_ADD_CLAZZ, payload, reply -> {
			if (reply.succeeded()) {
				String body = reply.result().body().toString();
				if (body == null) {
					rc.request().response().setStatusCode(400)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Student does not exits!").encodePrettily());
				} else if (body.equalsIgnoreCase("Error")) {
					rc.request().response().setStatusCode(400)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Update student error").encodePrettily());
				} else {
					rc.request().response().setStatusCode(201)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(reply.result().body().toString());
				}
			} else {
				reply.cause();
			}
		});

	}

	public void deleteStudent(RoutingContext rc) {
		String studentId = rc.request().getParam("studentId");
		JsonObject payload = new JsonObject();
		payload.put("studentId", studentId);
		vertx.eventBus().request(ClazzEventBus.HANDLE_ADD_CLAZZ, payload, reply -> {
			if (reply.succeeded()) {
				String body = reply.result().body().toString();
				if (body == null) {
					rc.request().response().setStatusCode(400)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Student does not exist!").encodePrettily());
				} else if (body.equalsIgnoreCase("Error")) {
					rc.request().response().setStatusCode(400)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Delete student error").encodePrettily());
				} else {
					rc.request().response().setStatusCode(204)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end();
				}
			} else {
				reply.cause();
			}
		});
	}

}