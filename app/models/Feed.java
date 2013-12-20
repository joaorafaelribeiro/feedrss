package models;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import models.helper.FeedReader;

import org.apache.commons.beanutils.BeanUtils;

import exceptions.ReaderFeedRSSException;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Model;
@Entity
public class Feed extends Model {

	public String image;
	public String language;
	public String link;
	public String title;
	public String url;
	@Temporal(TemporalType.TIMESTAMP)
	public Date dateExpired;
	@ManyToOne
	public Category category;
	@OneToMany(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="feed_id",nullable=true)
	public List<FeedMessage> feedMessages;
	
	public static List<Feed> findAllOrderByTitle() {
		return Feed.find("select f from Feed f order by f.title").fetch();
	}
	
	public long getSize() {	
		return FeedMessage.count("feed.id = ? and isRead = false", id);
	}
	
	public void update()  {
		//if(getSize() > 0) return;
		Feed feedAtual;
		try {
			feedAtual = new FeedReader().lerFeed(url.trim());
			for(FeedMessage feedMessage : feedAtual.feedMessages) {
				if(FeedMessage.count("link = ? and feed.id = ?",feedMessage.link, id) == 0) {
					this.feedMessages.add(feedMessage);
				}
			}
		} catch (ReaderFeedRSSException e) {
			Logger.warn("Error :"+e.getMessage()+" "+url);
			//Logger.error(e, e.getMessage());
		} finally {
			this.dateExpired = new Date(System.currentTimeMillis() + (new Random(System.currentTimeMillis()).nextInt(30)*1000*60));
			this.save();			
		}
	}
	
	public static List<Feed> findFeedsExpired() {
		return Feed.find("dateExpired < ?",new Date()).fetch();
	}
	
	public Feed() {
		super();
	}
	
	public Feed(String url)  throws ReaderFeedRSSException, IllegalAccessException, InvocationTargetException {
		Feed feedAtual = new FeedReader().lerFeed(url);
		BeanUtils.copyProperties(this, feedAtual);
		this.save();
	}
}
