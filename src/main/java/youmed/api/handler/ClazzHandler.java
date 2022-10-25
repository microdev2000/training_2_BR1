package youmed.api.handler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import youmed.api.constant.ClazzEventBus;
import youmed.api.constant.StudentEventBus;
import youmed.api.service.ClassService;

public class ClazzHandler extends AbstractVerticle {
	private ClassService classService;

	@Override
	public void start() {
		classService = new ClassService();
		MessageConsumer<JsonObject> addClazz = vertx.eventBus().consumer(ClazzEventBus.HANDLE_ADD_CLAZZ);
		addClazz.handler(msg -> {
			JsonObject payload = msg.body();
			classService.addClazz(payload.toString(), res -> {
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

		MessageConsumer<JsonObject> getAllClazz = vertx.eventBus().consumer(ClazzEventBus.HANDLE_GET_ALL_CLAZZ);
		getAllClazz.handler(msg -> {
			classService.getAllClazz(res -> {
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

		MessageConsumer<JsonObject> getClazzById = vertx.eventBus().consumer(ClazzEventBus.HANDLE_GET_CLAZZ_BY_ID);
		getClazzById.handler(msg -> {
			String clazzId = msg.body().getString("clazzId");
			classService.getClazzById(clazzId, res -> {
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

		MessageConsumer<JsonObject> updateClazz = vertx.eventBus().consumer(ClazzEventBus.HANDLE_UPDATE_CLAZZ);
		updateClazz.handler(msg -> {
			JsonObject payload = msg.body();
			String clazzId = msg.body().getString("clazzId");
			payload.remove(clazzId);
			classService.updateClazz(clazzId, payload.toString(), res -> {
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

		MessageConsumer<JsonObject> deleteClazz = vertx.eventBus().consumer(ClazzEventBus.HANDLE_UPDATE_CLAZZ);
		deleteClazz.handler(msg -> {
			String clazzId = msg.body().getString("clazzId");
			classService.deleteClazz(clazzId, res -> {
				if (res.succeeded()) {
					if (res.result() != null) {
						msg.reply("Success");
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
