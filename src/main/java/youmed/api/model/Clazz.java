package youmed.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.json.JsonObject;

public class Clazz {

	@JsonProperty("_id")
	private String _id;

	@JsonProperty("name")
	private String name;

	public String getId() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}