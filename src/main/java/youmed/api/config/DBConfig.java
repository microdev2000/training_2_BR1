package youmed.api.config;

import io.vertx.core.json.JsonObject;

public class DBConfig {
	public static final String DB_URI = "mongodb://localhost:27017";
	public static final String DB_NAME = "youmed";
	
	public static JsonObject dbConfig() {
		JsonObject config = new JsonObject();
		config.put("connection_string", DB_URI);
		config.put("db_name", DB_NAME);
		return config;
	}
}
    
