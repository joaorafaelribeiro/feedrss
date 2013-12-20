package controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import exceptions.ReaderFeedRSSException;
import models.Category;
import models.Chats;
import models.Feed;
import models.Summary;
import models.User;
import models.helper.FeedScreen;
import play.Logger;
import play.cache.Cache;
import play.data.validation.Error;
import play.data.validation.Valid;
import play.db.jpa.JPA;
import play.mvc.Controller;
import play.mvc.With;
@With(Secure.class)
public class Config extends Controller {

	

	public static void rss() {
		List<Feed> feeds = Feed.findAllOrderByTitle();
		List<Category> categories = Category.findAllOrderByName();
		render(feeds,categories);
	}
	
	public static void conta() {
		User user = User.find("email", session.get("username")).first(); 
		render(user);
	}
	
	public static void updateUser(@Valid User user) {
		user.save();
		conta();
	}
	
	public static void register(String url) {
		try {
			Error e = validation.required(url).error;
			if(e == null) {
				new Feed(url);
			}
		} catch (Exception e) {
			validation.addError("url", e.getMessage(), new String[]{});
			Logger.error(e, e.getMessage());
		}finally{
			if(validation.hasErrors()) {
		          validation.keep(); 
		      }
			rss();
		}
	}
	public static void changeCategory(long idCategory, long idFeed) {
		validation.required(idCategory);
		validation.required(idFeed);
		if(!validation.hasErrors()) {
			Feed feed = Feed.findById(idFeed);
			feed.category = Category.findById(idCategory);
			feed.save();
		}
	}
	
	public static void removeFeed(long idFeed) {
		Feed feed = Feed.findById(idFeed);
		feed.delete();
		rss();
	}
	
	public static void summary() {
		EntityManager em = JPA.em();
		List<Chats> chats = em.createQuery("select new models.Chats(f.id,f.title,(select count(*) from FeedMessage m where f.id = m.feed.id)) from Feed f").getResultList();
		long[] v = new long[chats.size()];
		String[] l = new String[chats.size()];
		for(int i=0; i<chats.size(); i++) {
			v[i] = chats.get(i).getCount();
			l[i] = "\""+chats.get(i).getTitle()+"\"";
		}
		String values = Arrays.toString(v);
		String labels = Arrays.toString(l);
		render(values,labels);
	}
	
	
}
