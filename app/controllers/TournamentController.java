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


public class TournamentController extends Controller {
    @Inject FormFactory formFactory;


//    public JsonNode validateTournament(Form<Tournament> form)
//    {
//        if(form.hasErrors()) {
//            JsonNode node = form.errorsAsJson();    // Get errors
//            Tournament error = new Tournament();
//
//            if(form.error("tournament_name") != null)
//                error.setTournament_name( node.get("tournament_name").get(0).asText() );
//            if(form.error("game") != null)
//                error.setGame( node.get("game").get(0).asText() );
//            if(form.error("location") != null)
//                error.setLocation( node.get("location").get(0).asText() );
//            if(form.error("date") != null)
//                error.setDate(node.get("date").get(0).asText());
//            if(form.error("tOrganizer") != null)
//                error.settOrganizer( node.get("tOrganizer").get(0).asText() );
//            if(form.error("platform") != null)
//                error.setPlatform( node.get("platform").get(0).asText() );
//            if(form.error("tournamentDetails") != null)
//                error.setTournamentDetail( node.get("tournamentDetail").get(0).asText() );
//            if(form.error("tournamentPicture") != null)
//                error.setTournamentPicture( node.get("tournamentPicture").get(0).asText() );
//            return Json.toJson(error);
//        }
//        return null;
//    }

//    @Security.Authenticated(ActionAuthenticator.class)
    public Result createTournament(){
        Tournament tournament = Json.fromJson(request().body().asJson(), Tournament.class);
        if(tournament == null)
            return badRequest();
        Form<Tournament> form = formFactory.form(Tournament.class).bindFromRequest();

        if(form.hasErrors())
            return badRequest(form.errorsAsJson());

        User user = Ebean.find(User.class, 1);
        tournament.setOwner(user);
        if(tournament.getId() != 0)
            Ebean.update(tournament);
        else
            Ebean.save(tournament);
        return ok();
    }

//    @Security.Authenticated(ActionAuthenticator.class)
    public Result getAllTournaments()
    {
        User user = Ebean.find(User.class, session().get("User.id"));
        return ok(Json.toJson(user.getTournaments()));
    }

//    @Security.Authenticated(ActionAuthenticator.class)
    public Result getTournament(Long id)
    {
        Tournament tournament = Ebean.find(Tournament.class,id);
        return tournament == null ? notFound() : ok(Json.toJson(tournament));
    }

//    @Security.Authenticated(ActionAuthenticator.class)
    public Result deleteTournament(Long id)
    {
        Tournament tournament = Ebean.find(Tournament.class, id);
        // Check if owner of document
        User user = Ebean.find(User.class, session().get("User.id"));
        if(user.getId() != tournament.gettOrganizer().getId())
            return unauthorized();
        Ebean.delete(tournament);
        return ok();
    }


    public Result getTournament(String search_id) {
        User user = null;
        if(search_id == "")
            user = Ebean.find(User.class).select("id").findUnique();
        else
            user = Ebean.find(User.class)
                    .where()
                    .eq("id", search_id)
                    .findUnique();
        JsonContext jc = Ebean.json();
        return ok(jc.toJson(user));
    }


}