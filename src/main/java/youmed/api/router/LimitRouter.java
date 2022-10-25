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
import youmed.api.constant.LimitEventBus;
import youmed.api.dto.LimitDto;
import youmed.api.service.LimitService;

public class LimitRouter extends AbstractVerticle {

	public LimitService limitService;

	@Override
	public void start() {
		limitService = new LimitService();
		HttpServer server = vertx.createHttpServer();
		Router classRouter = Router.router(vertx);
		classRouter.route("/api/v1/limit/*").handler(BodyHandler.create());
		classRouter.put("/api/v1/limit/:limitId").handler(this::updateLimit);
		server.requestHandler(classRouter).listen(9999);
	}

	private void updateLimit(RoutingContext rc) {
		String limitId = rc.request().getParam("limitId");
		String payload = rc.getBodyAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		LimitDto limitDto = null;
		try {
			limitDto = objectMapper.readValue(payload, LimitDto.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String jsonPayload = null;
		try {
			jsonPayload = ow.writeValueAsString(limitDto);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		JsonObject jsonObject = new JsonObject(jsonPayload);
		jsonObject.put("limitId", limitId);

		vertx.eventBus().request(LimitEventBus.HANDLE_UPDATE_LIMIT, jsonObject, reply -> {
			if (reply.succeeded()) {
				String body = reply.result().body().toString();
				if (body == null) {
					rc.request().response().setStatusCode(400)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Maximum record does not exits").encodePrettily());
				} else if (body.equalsIgnoreCase("Error")) {
					rc.request().response().setStatusCode(400)
							.putHeader("Content-Type", "application/json ;charset=utf-8")
							.end(new JsonObject().put("Error", "Update limit error").encodePrettily());
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

}
