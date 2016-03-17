package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's index page.
 */
public class HomeController extends Controller {

    public  Result registration() {
        return ok(registration.render("registration page"));
    }


    public Result playerProfile() {return ok(playerProfile.render("Player Profile Page"));}


    public  Result about() {
        return ok(about.render("about"));
    }

    public Result home() {
        return ok(Home.render("Your new application is ready."));
    }

    public Result index() {return ok(index.render("Your new application is ready"));}
}
