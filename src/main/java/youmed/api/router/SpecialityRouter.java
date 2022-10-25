package youmed.api.router;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import youmed.api.constant.ClazzEventBus;
import youmed.api.constant.SpecialityEventBus;
import youmed.api.dto.SpecialityDto;
import youmed.api.model.Speciality;
import youmed.api.service.SpecialityService;

public class SpecialityRouter extends AbstractVerticle {
	public SpecialityService specialityService;

	@Override
	public void start() {
		specialityService = new SpecialityService();
		HttpServer server = vertx.createHttpServer();
		Router specialRouter = Router.router(vertx);
		specialRouter.route("/api/v1/speciality/*").handler(BodyHandler.create());
		specialRouter.post("/api/v1/speciality/post").handler(this::addSpeciality);
		specialRouter.put("/api/v1/speciality/:specialityId").handler(this::updateSpeciality);
		specialRouter.get("/api/v1/speciality/get/all").handler(this::getAllSpeciality);
		specialRouter.get("/api/v1/speciality/get/:specialityId").handler(this::getSpecialityById);
		specialRouter.delete("/api/v1/speciality/delete/:").handler(this::deleteSpeciality);

		server.requestHandler(specialRouter).listen(9999);

	}

	private void addSpeciality(RoutingContext rc) {
		String payload = rc.getBodyAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		Speciality speciality = null;
		String jsonPayload = null;
		try {
			speciality = objectMapper.readValue(payload, Speciality.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		if (speciality != null) {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			try {
				jsonPayload = ow.writeValueAsString(speciality);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			vertx.eventBus().request(SpecialityEventBus.HANDLE_ADD_SPECIALITY, new JsonObject(jsonPayload), reply -> {
				if (reply.succeeded()) {
					String body = reply.result().body().toString();
					if (body == null) {
						rc.request().response().setStatusCode(400)
								.putHeader("Content-Type", "application/json ;charset=utf-8")
								.end(new JsonObject().put("Error", "Speciality already exist!").encodePrettily());
					} else if (body.equalsIgnoreCase("Error")) {
						rc.request().response().setStatusCode(400)
								.putHeader("Content-Type", "application/json ;charset=utf-8")
								.end(new JsonObject().put("Error", "Add new speciality error").encodePrettily());
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

	private void getAllSpeciality(RoutingContext rc) {

		vertx.eventBus().request(ClazzEventBus.HANDLE_ADD_CLAZZ, null, reply -> {
			if (reply.succeeded()) {
				String body = reply.result().body().toString();
				if (body == null) {
					rc.request().response().setStatusCode(200)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "No Speciality existed").encodePrettily());
				} else if (body.equalsIgnoreCase("Error")) {
					rc.request().response().setStatusCode(400)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Get all speciality error").encodePrettily());
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

	private void getSpecialityById(RoutingContext rc) {
		String specialityId = rc.request().getParam("specialityId");
		JsonObject payload = new JsonObject();
		payload.put("specialityId", specialityId);
		vertx.eventBus().request(ClazzEventBus.HANDLE_ADD_CLAZZ, payload, reply -> {
			if (reply.succeeded()) {
				String body = reply.result().body().toString();
				if (body == null) {
					rc.request().response().setStatusCode(404)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Speciality does not exist").encodePrettily());
				} else if (body.equalsIgnoreCase("Error")) {
					rc.request().response().setStatusCode(400)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Get speciality error").encodePrettily());
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

	private void updateSpeciality(RoutingContext rc) {
		String specialityId = rc.request().getParam("specialityId");
		String json = rc.getBodyAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		SpecialityDto specialityDto = null;
		try {
			specialityDto = objectMapper.readValue(json, SpecialityDto.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String jsonPayload = null;
		try {
			jsonPayload = ow.writeValueAsString(specialityDto);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		JsonObject payload = new JsonObject(jsonPayload);
		payload.put("specialityId", specialityId);
		vertx.eventBus().request(ClazzEventBus.HANDLE_ADD_CLAZZ, payload, reply -> {
			if (reply.succeeded()) {
				String body = reply.result().body().toString();
				if (body == null) {
					rc.request().response().setStatusCode(400)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Speciality does not exist").encodePrettily());
				} else if (body.equalsIgnoreCase("Error")) {
					rc.request().response().setStatusCode(400)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Update speciality error").encodePrettily());
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

	private void deleteSpeciality(RoutingContext rc) {
		String specialityId = rc.request().getParam("specialityId");
		JsonObject payload = new JsonObject();
		payload.put("specialityId", specialityId);
		vertx.eventBus().request(ClazzEventBus.HANDLE_ADD_CLAZZ, null, reply -> {
			if (reply.succeeded()) {
				String body = reply.result().body().toString();
				if (body == null) {
					rc.request().response().setStatusCode(400)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Speciality does not exist").encodePrettily());
				} else if (body.equalsIgnoreCase("Error")) {
					rc.request().response().setStatusCode(400)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Delete speciality error").encodePrettily());
				} else {
					rc.request().response().setStatusCode(204)
							.putHeader("Content-Type", "application/json ;charset=utf-8").end();
				}
			} else {
				reply.cause();
			}
		});
	}

}
