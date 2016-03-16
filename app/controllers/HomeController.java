package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    public  Result navPage2() {
        return ok(registration.render("registration page"));
    }

    public Result playerProfile() {return ok(playerProfile.render("Player Profile Page"));}


    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

}
