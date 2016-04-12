package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.text.json.JsonContext;
import com.fasterxml.jackson.databind.JsonNode;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.mvc.*;
import play.mvc.Controller;
import play.libs.*;
import models.*;

import javax.inject.Inject;
import java.security.SecureRandom;
import java.util.List;


public class UserController extends Controller {
    @Inject FormFactory formFactory;
    public Result register(){
        User user = Json.fromJson(request().body().asJson(), User.class);
        if(user == null) {
            return badRequest();
        }
        Form<User> form = formFactory.form(User.class).bindFromRequest();

        // Check form errors
        JsonNode node = validateUser(form);
        if(node != null)
            return badRequest(Json.toJson(node));

        // Save the entry
        return createUser();
    }

    public JsonNode validateUser(Form<User> form)
    {
        if(form.hasErrors()) {
            JsonNode node = form.errorsAsJson();    // Get errors
            User error = new User();

            if(form.error("first_name") != null)
                error.setFirst_name( node.get("first_name").get(0).asText() );
            if(form.error("last_name") != null)
                error.setLast_name( node.get("last_name").get(0).asText() );
            if(form.error("email") != null)
                error.setEmail( node.get("email").get(0).asText() );
            if(form.error("password") != null)
                error.setPassword(node.get("password").get(0).asText());
            if(form.error("gamerTag") != null)
                error.setGamerTag( node.get("gamerTag").get(0).asText() );
            if(form.error("dobMonth") != null)
                error.setDobMonth( node.get("dobMonth").get(0).asText() );
            if(form.error("dobDay") != null)
                error.setDobDay( node.get("dobDay").get(0).asText() );
            if(form.error("dobYear") != null)
                error.setDobYear( node.get("dobYear").get(0).asText() );
            if(form.error("address") != null)
                error.setAddress( node.get("address").get(0).asText() );
            if(form.error("city") != null)
                error.setCity( node.get("city").get(0).asText() );
            if(form.error("state") != null)
                error.setState( node.get("state").get(0).asText() );
            if(form.error("platform") != null)
                error.setPlatform( node.get("platform").get(0).asText() );
            return Json.toJson(error);
        }
        User user = form.get();
        if(!user.getPassword().equals(user.getConfirm_password()))
        {
            User error = new User();
            error.setPassword("Both passwords must match");
            error.setConfirm_password("Both passwords must match");
            return Json.toJson(error);
        }
        // Check for unique email
        User existingUser = User.findByEmail(user.getEmail());
        if(existingUser != null)
        {
            User error = new User();
            error.setEmail("Uh-oh looks like this email is already taken. Sorry pal.");
            return Json.toJson(error);
        }

        // Check for unique gamerTag
        User existingGamerTag = User.gamerTagLogin(user.getGamerTag());
        if(existingGamerTag != null)
        {
            User error = new User();
            error.setGamerTag("This Gamer Tag is already taken");
            return Json.toJson(error);
        }

        return null;
    }

    // Crud Methods
    private  Result getUser()
    {
        List<User> users = Ebean.find(User.class).findList();
        return ok(Json.toJson(users));
    }

    private Result getUser(Long id)
    {
        User user = Ebean.find(User.class,id);
        return user == null ? notFound() : ok(Json.toJson(user));
    }

    private Result createUser()
    {
        User user = Json.fromJson(request().body().asJson(), User.class);
        Ebean.save(user);
        return created(Json.toJson(user));
    }

    private Result updateUser(Long id)
    {
        User user = Json.fromJson(request().body().asJson(), User.class);
        Ebean.update(user);
        return ok(Json.toJson(user));
    }

    private Result deleteUser(Long id)
    {
        User user = Ebean.find(User.class, id);
        Ebean.delete(user);
        return noContent(); // http://stackoverflow.com/a/2342589/1415732
    }

    // Endpoint for user login
    public Result authenticate() {
        // 1. Define class to send JSON response back
        class Login {
            public Long     id;
            public String gamerTag;
            public String token;

            public Login() {
            }
        }

        // 2. Read email and password from request()
        JsonNode request = request().body().asJson();
        String gamerTag = request.get("gamerTag").asText();
        String password = request.get("password").asText();

        // 3. Find user with given gamerTag
        Login ret = new Login();
        User user = User.gamerTagLogin(gamerTag);
        if (user == null) {
            return unauthorized(Json.toJson(ret));
        }
        // 4. Compare password.
        String sha256 = User.getSha256(request.get("password").asText());
        if (sha256.equals(user.getPassword())) {
            // Success
            String authToken = generateAuthToken();
            user.setToken(authToken);
            Ebean.update(user);
            ret.token = authToken;
            ret.gamerTag = user.getGamerTag();
            ret.id = user.getId();
            return ok(Json.toJson(ret));

        }
        // 5. Unauthorized access
        return unauthorized();
    }

    //@Security.Authenticated(ActionAuthenticator.class)
    private String generateAuthToken() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return bytes.toString();
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result logout(long id) {
        User user = Ebean.find(User.class, id);
        if (user != null) {
            user.setToken(generateAuthToken());
            Ebean.save(user);
            return ok();
        }
        return unauthorized();
    }


    public Result search(String search_string) {
        List<User> users = null;
        String[] tokens = search_string.split(" ");
        if(search_string == "")
            users = Ebean.find(User.class).select("gamerTag, first_name").findList();
        else
            users = Ebean.find(User.class)
                    .select("gamerTag, first_name,last_name,gamerTag,platform,dobMonth,dobDay,dobYear,address,city,state,icon_URL,favGame,gameTeam,playerBio")
                    .where()
                    .or(
                            Expr.in("gamerTag", tokens),
                            Expr.in("first_name", tokens)

                    )
                    .findList();
        JsonContext jc = Ebean.json();
        return ok(jc.toJson(users));
    }


    public Result getProfile(String search_string) {
        User user = null;
        if(search_string == "")
            user = Ebean.find(User.class).select("gamerTag").findUnique();
        else
            user = Ebean.find(User.class)
                    .select("gamerTag, first_name,last_name,gamerTag,platform,dobMonth,dobDay,dobYear,address,city,state,icon_URL,favGame,gameTeam,playerBio")
                    .where()
                    .eq("gamerTag", search_string)
                    .findUnique();
        JsonContext jc = Ebean.json();
        return ok(jc.toJson(user));
    }

    public Result editProfile(){
        User user = Json.fromJson(request().body().asJson(), User.class);
        if(user == null) {
            return badRequest();
        }
//        Form<User> form = formFactory.form(User.class).bindFromRequest();
        User currUser = Ebean.find(User.class)
                .where()
                .eq("id", user.id)
                .findUnique();
        if(user.gameTeam != null)
            currUser.setGameTeam(user.gameTeam);
        if(user.favGame != null)
            currUser.setFavGame(user.favGame);
        if (user.playerBio != null)
            currUser.setPlayerBio(user.playerBio);
        if (user.email != null)
            currUser.setEmail(user.email);
        if (user.state != null)
            currUser.setState(user.state);
        if (user.icon_URL != null)
            currUser.setIcon_URL(user.icon_URL);
        // Check form errors
//        JsonNode node = validateUser(form);
//        if(node != null)
//            return badRequest(Json.toJson(node));

        // Save the entry

        Ebean.save(currUser);
        JsonContext jc = Ebean.json();
        return ok(jc.toJson(user));
    }

}
