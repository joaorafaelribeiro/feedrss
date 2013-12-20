package models.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import models.FeedMessage;
import models.FeedView;
import models.User;

public class FeedScreen implements Serializable {

/*	public static enum SCREEN  {
		NEWS(1),
		ALL(2),
		FAVORITES(3);
		private int id;
		private SCREEN(int id) {
			this.id = id;
		}
		public int getId() {
			return id;
		}
	}
*/	
	public FeedScreen() {
		user = new User();
		feedMessages = new ArrayList<FeedMessage>();
		feeds = new ArrayList<FeedView>();
		//screen = SCREEN.NEWS;
		page = 1;
		total = 0;
		idFeed = 0;
	}
	
	private User user;
	
	private long idFeed;
	
	private List<FeedMessage> feedMessages;
	
	private List<FeedView> feeds;
	
	
	private long total;
	
	private int page;
	
	private String search;
	
	//private SCREEN screen;
	
	

	public User getUser() {
		return user;
	}

	public List<FeedView> getFeeds() {
		return feeds;
	}

	public void setFeeds(List feeds) {
		this.feeds = feeds;
	}

	public void setUser(User user) {
		this.user = user;
	}
/*
	public SCREEN getScreen() {
		return screen;
	}

	public void setScreen(SCREEN screen) {
		this.screen = screen;
	}
*/
	public long getIdFeed() {
		return idFeed;
	}

	public void setIdFeed(long newTotal) {
		this.idFeed = newTotal;
	}

	public List<FeedMessage> getFeedMessages() {
		return feedMessages;
	}

	public void setFeedMessages(List<FeedMessage> feedMessages) {
		this.feedMessages = feedMessages;
	}
/*
	public long getLikeTotal() {
		return likeTotal;
	}

	public void setLikeTotal(long likeTotal) {
		this.likeTotal = likeTotal;
	}*/

	public long getTotal() {
		return total;
	}

	public void setTotal(long feedTotal) {
		this.total = feedTotal;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
	
	
	
}
