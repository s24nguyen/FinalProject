package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import org.h2.engine.User;
import play.data.*;
import play.data.validation.Constraints;
import play.mvc.*;
import play.libs.*;
import models.*;
import javax.inject.Inject;
import java.util.List;


public class UserController extends Controller {
    @Inject
    FormFactory formFactory;

    public Result register() {
        User user = Json.fromJson(request().body().asJson(), User.class);
        Form<User> form = formFactory.form(User.class).bindFromRequest();
        if (form == null)
            return badRequest();
// Check form errors
        JsonNode node = validateUser(form);
        if (node != null)
            return badRequest(Json.toJson(node));
// Save the entry
        return createUser();
    }

    public JsonNode validateUser(Form<User> form) {
        if (form.hasErrors()) {
            JsonNode node = form.errorsAsJson(); // Get errors
            User error = new User();
            if (form.error("first_name") != null)
                error.firstName = node.get("first_name").get(0).asText();
            if (form.error("last_name") != null)
                error.lastName = node.get("last_name").get(0).asText();
            return Json.toJson(error);
        }
        return null;
    }

    private Result createUser()
    {
        User user = Json.fromJson(request().body().asJson(), User.class);
        Ebean.save(user);
        return created(Json.toJson(user));
    }
}