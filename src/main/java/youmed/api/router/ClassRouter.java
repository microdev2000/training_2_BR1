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
import youmed.api.dto.ClazzDto;
import youmed.api.handler.ClazzHandler;
import youmed.api.model.Clazz;
import youmed.api.service.ClassService;

public class ClassRouter extends AbstractVerticle {

	@Override
	public void start() {
		HttpServer server = vertx.createHttpServer();
		Router classRouter = Router.router(vertx);
		classRouter.route("/api/v1/class/*").handler(BodyHandler.create());
		classRouter.post("/api/v1/class/post").handler(this::addClazz);
		classRouter.put("/api/v1/class/:clazzId").handler(this::updateClazz);
		classRouter.get("/api/v1/class/get/all").handler(this::getAllClazz);
		classRouter.get("/api/v1/class/get/:clazzId").handler(this::getClazzById);
		classRouter.delete("/api/v1/class/delete/:clazzId").handler(this::deleteClazz);
		server.requestHandler(classRouter).listen(9999);

	}

	private void addClazz(RoutingContext rc) {
		String payload = rc.getBodyAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		Clazz clazz = null;
		String jsonPayload = null;
		try {
			clazz = objectMapper.readValue(payload, Clazz.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		if (clazz != null) {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			try {
				jsonPayload = ow.writeValueAsString(clazz);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			vertx.eventBus().request(ClazzEventBus.HANDLE_ADD_CLAZZ, new JsonObject(jsonPayload), reply -> {
				if (reply.succeeded()) {
					String body = reply.result().body().toString();
					if (body == null) {
						rc.request().response().setStatusCode(400)
								.putHeader("Content-Type", "application/json ;charset=utf-8")
								.end(new JsonObject().put("Error", "Class already exist!").encodePrettily());
					} else if (body.equalsIgnoreCase("Error")) {
						rc.request().response().setStatusCode(400)
								.putHeader("Content-Type", "application/json ;charset=utf-8")
								.end(new JsonObject().put("Error", "Add new class error").encodePrettily());
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

	private void getAllClazz(RoutingContext rc) {
		vertx.eventBus().request(ClazzEventBus.HANDLE_GET_CLAZZ_BY_ID,null, reply -> {
			if (reply.succeeded()) {
				String result = reply.result().body().toString();
				if (result == null) {
					rc.response().setStatusCode(404).putHeader("Content-Type", "application/json; charset=utf-8")
							.end(new JsonObject().put("error", "No class existed").encodePrettily());
				} else if (result.equalsIgnoreCase("Error")) {
					rc.response().setStatusCode(400).putHeader("Content-Type", "application/json; charset=utf-8")
							.end(new JsonObject().put("error", "Get all class error").encodePrettily());
				} else {
					rc.response().setStatusCode(200).putHeader("Content-Type", "application/json; charset=utf-8")
							.end(result);
				}
			} else
				rc.fail(reply.cause());
		});

	}

	private void getClazzById(RoutingContext rc) {
		String clazzId = rc.request().getParam("clazzId");
		JsonObject payload = new JsonObject();
		payload.put("clazzId", clazzId);
		vertx.eventBus().request(ClazzEventBus.HANDLE_GET_CLAZZ_BY_ID, payload, reply -> {
			if (reply.succeeded()) {
				String result = reply.result().body().toString();
				if (result == null) {
					rc.response().setStatusCode(404).putHeader("Content-Type", "application/json; charset=utf-8")
							.end(new JsonObject().put("error", "Class does not exist").encodePrettily());
				} else if (result.equalsIgnoreCase("Error")) {
					rc.response().setStatusCode(400).putHeader("Content-Type", "application/json; charset=utf-8")
							.end(new JsonObject().put("error", "Get class error").encodePrettily());
				} else {
					rc.response().setStatusCode(200).putHeader("Content-Type", "application/json; charset=utf-8")
							.end(result);
				}
			} else
				rc.fail(reply.cause());
		});

	}

	private void updateClazz(RoutingContext rc) {
		String clazzId = rc.request().getParam("clazzId");
		String json = rc.getBodyAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		ClazzDto clazzDto = null;
		try {
			clazzDto = objectMapper.readValue(json, ClazzDto.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String jsonPayload = null;
		try {
			jsonPayload = ow.writeValueAsString(clazzDto);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		JsonObject payload = new JsonObject(jsonPayload);
		payload.put("clazzId", clazzId);
		vertx.eventBus().request(ClazzEventBus.HANDLE_UPDATE_CLAZZ, payload, reply -> {
			if (reply.succeeded()) {
				String result = reply.result().body().toString();
				if (result == null) {
					rc.response().setStatusCode(404).putHeader("Content-Type", "application/json; charset=utf-8")
							.end(new JsonObject().put("error", "Class does not exist").encodePrettily());
				} else if (result.equalsIgnoreCase("Error")) {
					rc.response().setStatusCode(400).putHeader("Content-Type", "application/json; charset=utf-8")
							.end(new JsonObject().put("error", "Update class error").encodePrettily());
				} else {
					rc.response().setStatusCode(201).putHeader("Content-Type", "application/json; charset=utf-8")
							.end(result);
				}
			} else
				rc.fail(reply.cause());
		});

	}

	private void deleteClazz(RoutingContext rc) {
		String clazzId = rc.request().getParam("clazzId");
		JsonObject payload = new JsonObject();
		payload.put("clazzId", clazzId);
		vertx.eventBus().request(ClazzEventBus.HANDLE_DELETE_CLAZZ, payload, reply -> {
			if (reply.succeeded()) {
				String result = reply.result().body().toString();
				if (result == null) {
					rc.response().setStatusCode(404).putHeader("Content-Type", "application/json; charset=utf-8")
							.end(new JsonObject().put("error", "Class does not exist").encodePrettily());
				} else if (result.equalsIgnoreCase("Error")) {
					rc.response().setStatusCode(400).putHeader("Content-Type", "application/json; charset=utf-8")
							.end(new JsonObject().put("error", "Delete class error").encodePrettily());
				} else {
					rc.response().setStatusCode(200).putHeader("Content-Type", "application/json; charset=utf-8")
							.end(result);
				}
			} else
				rc.fail(reply.cause());
		});
	}

}
