package youmed.api.handler;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import youmed.api.constant.SpecialityEventBus;
import youmed.api.service.SpecialityService;

public class SpecialityHandler extends AbstractVerticle {

	private SpecialityService specialityService;

	@Override
	public void start() {
		specialityService = new SpecialityService();
		MessageConsumer<JsonObject> addSpeciality = vertx.eventBus().consumer(SpecialityEventBus.HANDLE_ADD_SPECIALITY);
		addSpeciality.handler(msg -> {
			JsonObject payload = msg.body();
			specialityService.addSpeciality(payload.toString(), res -> {
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

		MessageConsumer<JsonObject> getAllSpeciality = vertx.eventBus()
				.consumer(SpecialityEventBus.HANDLE_GET_ALL_SPECIALITY);
		getAllSpeciality.handler(msg -> {
			specialityService.getAllSpeciality(res -> {
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

		MessageConsumer<JsonObject> getSpecialById = vertx.eventBus()
				.consumer(SpecialityEventBus.HANDLE_GET_SPECIALITY_BY_ID);
		getSpecialById.handler(msg -> {
			String specialityId = msg.body().getString("specialityId");
			specialityService.getSpecialityById(specialityId, res -> {
				if (res.succeeded()) {
					if (res.result() != null) {
						msg.reply(res.result());
					}
				} else {
					res.cause();
					msg.reply("Error");
				}

			});
		});

		MessageConsumer<JsonObject> updateSpeciality = vertx.eventBus()
				.consumer(SpecialityEventBus.HANDLE_UPDATE_SPECIALITY);
		updateSpeciality.handler(msg -> {
			JsonObject payload = msg.body();
			String specialityId = payload.getString("specialityId");
			payload.remove(specialityId);

			specialityService.updateSpeciality(specialityId, payload.toString(), res -> {
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

		MessageConsumer<JsonObject> deleteSpeciality = vertx.eventBus()
				.consumer(SpecialityEventBus.HANDLE_DELETE_SPECIALITY);
		deleteSpeciality.handler(msg -> {
			String specialityId = msg.body().getString("specialityId");

			specialityService.deleteSpeciality(specialityId, res -> {
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
