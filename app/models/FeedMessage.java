package models;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import exceptions.CrawlerException;
import models.helper.CrawlerFactory;
import models.helper.CrawlerLink;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
public class FeedMessage extends Model implements JsonSerializer<FeedMessage>{

	public String description;
	public String image;
	public Boolean isRead = false;
	public Boolean isLike = false;
	public String link;
	@Temporal(TemporalType.TIMESTAMP)
	public Date pubDate = new Date();
	public String title;
	@ManyToOne
	public Feed feed;

	public void setLink(String link) {
		this.link = link;
		if(this.link != null)
			this.link = this.link.trim();
	}
	

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeedMessage other = (FeedMessage) obj;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		return true;
	}



	public static void removeAllFeedMessage(long idFeed) {
		EntityManager e = JPA.em();
		e.createQuery("delete FeedMessage f where f.feed.id = :idFeed").setParameter("idFeed", idFeed).executeUpdate();
	}

	
	public void crawler() {
		if(image != null && !"".equals(image)) return;
		try {
			CrawlerLink cl;
			if(image == null) {
				cl = CrawlerFactory.createCrawlerLink(new URL(link));
				image = cl.getImage();
				if(description == null || "".equals(description.trim())) 
					description = cl.getDescription();
			}
			
		} catch (MalformedURLException e) {
			Logger.error(e, e.getMessage());
		} catch (CrawlerException e) {
			Logger.error(e, e.getMessage());
		} finally {
			this.save();
		}
		
	}

	public static void clear() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.MONTH, c.get(Calendar.MONTH)-1);
		EntityManager em = JPA.em();
		em.createQuery("delete from FeedMessage f where month(f.pubDate) = :month and year(f.pubDate) = :year")
		.setParameter("month", c.get(Calendar.MONTH)).setParameter("year", c.get(Calendar.YEAR))
		.executeUpdate();
	}



	@Override
	public JsonElement serialize(FeedMessage arg0, Type arg1,
			JsonSerializationContext arg2) {
		JsonObject x = new JsonObject();
		x.addProperty("title", title);
		x.addProperty("id", id);
		x.addProperty("image", (image != null)?image:"");
		x.addProperty("idFeed", feed.id);
		x.addProperty("nameFeed", feed.title);
		x.addProperty("description", (description!= null)?description:"");
		x.addProperty("date", SimpleDateFormat.getInstance().format(pubDate));
		x.addProperty("link", link);
		return x;
	}
}
