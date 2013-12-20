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
import models.filter.FilterMessage;
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
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import exceptions.CrawlerException;
@With(Secure.class)
public class FeedRSS extends Controller{

	public static void home() {
		FeedScreen fs = new FeedScreen();
		fs.setPage(1);
		Cache.set("feedScreen", fs);
		index();
	}
	
	public static void reader(long idFeedMessage) {
		FeedMessage feedMessage = FeedMessage.findById(idFeedMessage);
		feedMessage.isRead = true;
		feedMessage.save();
		FeedScreen mc = (FeedScreen) Cache.get("feedScreen");
		JsonObject x = new JsonObject();
		long count = FeedMessage.count("isRead = false and feed.id = ?",mc.getIdFeed());
		x.addProperty("count", count);
		if(count > 12) {
			FeedMessage f = (FeedMessage) FeedMessage.find("isRead = false and feed.id = ? order by pubDate desc", mc.getIdFeed()).fetch(mc.getPage(), 12).get(11);
			x.addProperty("id", f.id);
			x.addProperty("title", f.title);
			x.addProperty("image", (f.image != null)?f.image:"");
			x.addProperty("idFeed", f.feed.id);
			x.addProperty("nameFeed", f.feed.title);
			x.addProperty("description", (f.description!= null)?f.description:"");
			x.addProperty("date", SimpleDateFormat.getInstance().format(f.pubDate));
			x.addProperty("link", f.link);
		}
		renderJSON(x);
	}
	public static void setLike(long idFeedMessage) {
		FeedMessage feedMessage = FeedMessage.findById(idFeedMessage);
		feedMessage.isLike = !feedMessage.isLike;
		feedMessage.save();
	}
	
	public static void showDesc(final long idFeedMessage) {
		FeedMessage feedMessage = FeedMessage.findById(idFeedMessage);
		WSRequest request = WS.withEncoding("ISO-8859-1").url(feedMessage.link);
		//request.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
		request.timeout = (30*1000);
		Promise<HttpResponse> p = request.getAsync();
		if(feedMessage.image == null) {
		await(p, new F.Action<HttpResponse>() {
			@Override
			public void invoke(HttpResponse result) {
				CrawlerLink cl = null;
				cl =  new CrawlerLink(result.getString());
				FeedMessage feedMessage = FeedMessage.findById(idFeedMessage);
				feedMessage.image = cl.getImage();
				if(feedMessage.description == null || "".equals(feedMessage.description))
				feedMessage.description = cl.getDescription();
				feedMessage.save();
				feedMessage.em().flush();
				JsonObject x = new JsonObject();
				x.addProperty("image", feedMessage.image);
				x.addProperty("description", feedMessage.description);
				renderJSON(x);
			}
		}); }else {
			JsonObject x = new JsonObject();
			x.addProperty("image", feedMessage.image);
			x.addProperty("description", feedMessage.description);
			renderJSON(x);
		}
	}
	
	public static void index() {
		FeedScreen mc = (FeedScreen) Cache.get("feedScreen");
		mc.setFeeds(FeedView.findAll());
		if(mc.getSearch() == null || "".equals(mc.getSearch())) {
			List<FeedMessage> messages = FeedMessage.find("isRead = false and feed.id = ? order by pubDate desc",mc.getIdFeed()).fetch(mc.getPage(), 12); 
			mc.setFeedMessages(messages);
			mc.setTotal(FeedMessage.count("isRead = false and feed.id = ?",mc.getIdFeed()));
		} else {
			List<FeedMessage> messages = FeedMessage.find("isRead = false and title like ? order by pubDate desc","%"+mc.getSearch()+"%").fetch(mc.getPage(), 12); 
			mc.setFeedMessages(messages);
			mc.setTotal(FeedMessage.count("isRead = false and title = ? ","%"+mc.getSearch()+"%"));
		}
		render(mc);	
	}
	
	public static void next() {
		FeedScreen mc = (FeedScreen) Cache.get("feedScreen");
		mc.setPage(mc.getPage()+1);
		index();
	}
	
	public static void previous() {
		FeedScreen mc = (FeedScreen) Cache.get("feedScreen");
		mc.setPage(mc.getPage()-1);
		index();
	}
	
	public static void change(int idFeed) {
		FeedScreen mc = (FeedScreen) Cache.get("feedScreen");
		mc.setPage(1);
		mc.setIdFeed(idFeed);
		mc.setSearch(null);
		index();
	}


	
	public static void search(String search) {
		FeedScreen mc = (FeedScreen) Cache.get("feedScreen");
		mc.setSearch(search);
		mc.setPage(1);
		index();
	}
	
	public static void messages(long feedId) {
		List<FeedMessage> feedMessages = FeedMessage.find("isRead = false and feed.id = ? order by pubDate desc", feedId).fetch();
		render(feedMessages);
	}
}