package controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import models.Feed;
import models.helper.CrawlerFactory;
import exceptions.CrawlerException;
import exceptions.ReaderFeedRSSException;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
@Every("1mn")
@OnApplicationStart
public class RefreshFeed extends Job{

	public void doJob() {
			List<Feed> feeds = Feed.findFeedsExpired();
			if(feeds != null && !feeds.isEmpty())
			for(int i=0; i< feeds.size(); i++) {
					Feed f =Feed.findById(feeds.get(i).id);
					f.update();
			}
		
	}
}
