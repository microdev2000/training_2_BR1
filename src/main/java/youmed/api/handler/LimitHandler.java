package youmed.api.handler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import youmed.api.constant.LimitEventBus;
import youmed.api.service.LimitService;

public class LimitHandler extends AbstractVerticle {

	private LimitService limitService;

	@Override
	public void start() {
		limitService = new LimitService();
		MessageConsumer<JsonObject> updateLimit = vertx.eventBus().consumer(LimitEventBus.HANDLE_UPDATE_LIMIT);
		updateLimit.handler(msg -> {
			JsonObject payload = msg.body();
			String limitId = payload.getString("limitId");
			String value = payload.getString("value");

			payload.remove(limitId);
			limitService.updateMaximumStudent(limitId, Integer.valueOf(value), res -> {
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

		MessageConsumer<JsonObject> checkLimit = vertx.eventBus().consumer(LimitEventBus.HANDLE_UPDATE_LIMIT);
		checkLimit.handler(msg -> {
			String limitId = msg.body().getString("limitId");
			limitService.checkMaximum(limitId, res -> {
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
