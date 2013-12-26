package controllers;

import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
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
		/*System.getProperties().put("proxySet", "true");
		System.getProperties().put("http.proxyHost", "10.2.250.9");             
		System.getProperties().put("http.proxyPort", "3128");
		//System.getProperties().put("http.proxyUser", "u03122");
		//System.getProperties().put("http.proxyPassword", "Cera1234".toCharArray());
		Authenticator.setDefault(new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication("PRODEBTI\\u031222","Cera1234".toCharArray());
		    }
		});*/
			List<Feed> feeds = Feed.findFeedsExpired();
			if(feeds != null && !feeds.isEmpty())
			for(int i=0; i< feeds.size(); i++) {
					Feed f =Feed.findById(feeds.get(i).id);
					f.update();
			}
		
	}
}
