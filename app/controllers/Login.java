package controllers;

import models.User;
import models.helper.FeedScreen;
import play.cache.Cache;
import play.data.validation.Valid;
import play.libs.Codec;
import play.mvc.Controller;

public class Login extends Controller{

	

	
	public static void register() {
		User user = new User();
		render(user);
	}
	
	public static void save(@Valid User user) {
		if(validation.hasErrors()) {
			validation.keep();
			register();
		}
		user.senha = Codec.hexMD5(user.senha);
		user.save();
		FeedRSS.home();
	}
	

	
}
