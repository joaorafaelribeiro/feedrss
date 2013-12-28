package controllers;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import models.FeedMessage;
import models.FeedView;
import models.Summary;
import models.helper.CrawlerFactory;
import models.helper.CrawlerLink;
import models.helper.FeedScreen;
import play.cache.Cache;
import play.db.jpa.JPA;
import play.libs.F;
import play.libs.F.Promise;
import play.libs.WS;
import play.libs.WS.HttpResponse;
import play.libs.WS.WSRequest;
import play.mvc.Controller;
import play.mvc.With;
import play.utils.Action;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import exceptions.CrawlerException;
@With(Secure.class)
public class FeedRSS extends Controller{

	public static void home() {
		FeedScreen fs = new FeedScreen();
		fs.setPage(1);
		Cache.set("feedScreen", fs);
		index(0);
	}
	
	public static void reader(long idFeedMessage) {
		FeedMessage feedMessage = FeedMessage.findById(idFeedMessage);
		feedMessage.isRead = true;
		feedMessage.save();
		long count = FeedMessage.count("isRead = false and feed.id = ?",feedMessage.feed.id);
		renderJSON(new JsonPrimitive(count));
	}
	public static void setLike(long idFeedMessage) {
		FeedMessage feedMessage = FeedMessage.findById(idFeedMessage);
		feedMessage.isLike = !feedMessage.isLike;
		feedMessage.save();
	}
	
	public static void showDesc(final long idFeedMessage) {
		FeedMessage feedMessage = FeedMessage.findById(idFeedMessage);
		WSRequest request = WS.withEncoding("ISO-8859-1").url(feedMessage.link);
		request.timeout = (30*1000);
		Promise<HttpResponse> p = request.getAsync();
		if(feedMessage.image == null) {
		await(p, new F.Action<HttpResponse>() {
			@Override
			public void invoke(HttpResponse result) {
				CrawlerLink cl = null;
				cl =  CrawlerFactory.createCrawlerLink(result.getString());
				FeedMessage feedMessage = FeedMessage.findById(idFeedMessage);
				feedMessage.image = cl.getImage();
				if(feedMessage.description == null || "".equals(feedMessage.description))
				feedMessage.description = cl.getDescription();
				feedMessage.save();
				feedMessage.em().flush();
				renderJSON(feedMessage,feedMessage);
			}
		}); }else {
			renderJSON(feedMessage,feedMessage);
		}
	}
	
	public static void index(long feedId) {
		List<FeedView> feeds = FeedView.findAll(); 
		List<FeedMessage> feedMessages = FeedMessage.find("isRead = false and feed.id = ? order by pubDate desc", feedId).fetch();
		render(feedId,feeds,feedMessages);	
	}


	
	public static void search(String search) {
		List<FeedMessage> feedMessages = new ArrayList<FeedMessage>();
		if(search != null && !"".equals(search.trim()))
		 feedMessages = FeedMessage.find("title like ? order by pubDate desc","%"+search+"%").fetch(); 
		render(feedMessages);
	}
	
	
}