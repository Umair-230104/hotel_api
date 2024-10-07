package dat.routes;

import dat.controllers.impl.RoomController;
import dat.security.controllers.SecurityController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class RoomRoute {

    private final RoomController roomController = new RoomController();
    SecurityController securityController = SecurityController.getInstance();

    protected EndpointGroup getRoutes() {

        return () -> {
            post("/hotel/{id}", roomController::create);
            get("/", roomController::readAll);
            get("/{id}", roomController::read);
            put("/{id}", roomController::update);
            delete("/{id}", roomController::delete);
        };
    }
}
