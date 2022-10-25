package youmed.api.main;

import io.vertx.core.Vertx;
import youmed.api.builder.DBConfigBuilder;
import youmed.api.handler.ClazzHandler;
import youmed.api.handler.LimitHandler;
import youmed.api.handler.SpecialityHandler;
import youmed.api.handler.StudentHanlder;
import youmed.api.router.ClassRouter;
import youmed.api.router.LimitRouter;
import youmed.api.router.SpecialityRouter;
import youmed.api.router.StudentRouter;
import youmed.api.service.ClassService;
import youmed.api.service.LimitService;
import youmed.api.service.SpecialityService;
import youmed.api.service.StudentService;

public class Main {
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(DBConfigBuilder.class.getName(), res -> {
			if (res.succeeded()) {
				vertx.deployVerticle(StudentRouter.class.getName());
				vertx.deployVerticle(ClassRouter.class.getName());
				vertx.deployVerticle(SpecialityRouter.class.getName());
				vertx.deployVerticle(LimitRouter.class.getName());
				
				vertx.deployVerticle(new StudentService());
				vertx.deployVerticle(new ClassService());
				vertx.deployVerticle(new SpecialityService());
				vertx.deployVerticle(new LimitService());
				
				vertx.deployVerticle(StudentHanlder.class.getName());
				vertx.deployVerticle(ClazzHandler.class.getName());
				vertx.deployVerticle(SpecialityHandler.class.getName());
				vertx.deployVerticle(LimitHandler.class.getName());
			}
			else {
				res.cause();
			}
		});
		

	}

}
 