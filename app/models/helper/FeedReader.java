package models.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import play.Logger;
import play.libs.WS;
import models.Feed;
import models.FeedMessage;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import exceptions.ReaderFeedRSSException;
/**
 * This class is responsible for accessing, reading and processing the "xml" file, 
 * generating classes of system business.
 * @author joaoraf
 *
 */
public class FeedReader {
	/**
	 * This method is responsible for accessing, reading and processing the "xml" file, 
	 * generating classes of system business.
	 * @param url
	 * @return
	 * @throws ReaderFeedRSSException
	 */
	public Feed lerFeed(String url) throws ReaderFeedRSSException {
		SyndFeed f =null;
		try {
			//HttpURLConnection httpcon = (HttpURLConnection)new URL(url).openConnection();
			//httpcon.setConnectTimeout(5*1000);
			//httpcon.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11");
			SyndFeedInput input = new SyndFeedInput(false);
			f = input.build(new XmlReader(WS.url(url).get().getStream()));
			f.setEncoding("UTF-8");
			Feed feed = parse(f);
			feed.feedMessages.addAll(parseEntries(f,feed));
			feed.url = (url);
			return feed;
		} catch (Throwable e) {
			throw new ReaderFeedRSSException(e.getMessage(),e);
		} 
	}
	
	private List<FeedMessage> parseEntries(SyndFeed f,Feed feed) throws MalformedURLException, IOException {
		@SuppressWarnings("unchecked")
		List<SyndEntry> fs =(List<SyndEntry>)f.getEntries();
		List<FeedMessage> messages = new ArrayList<FeedMessage>();
		for(SyndEntry aux : fs) {
			FeedMessage message = new FeedMessage();
			if(aux.getDescription() != null) {
				message.description = (aux.getDescription().getValue());
			}
			message.link = (aux.getLink());
			message.title = (aux.getTitle());
			message.pubDate = new Date();
			message.isRead = false;
			messages.add(message);
			
		}
		return messages;
	}

	private Feed parse(SyndFeed f) {
		Feed feed = new Feed();
		feed.language = (f.getLanguage());
		feed.link = (f.getLink());
		feed.title = (f.getTitle());
		feed.dateExpired = new Date(System.currentTimeMillis() + (new Random(System.currentTimeMillis()).nextInt(30)*1000*60));
		feed.feedMessages = new ArrayList<FeedMessage>();
		if(f.getImage() !=null)
			feed.image = (f.getImage().getUrl());
		return feed;
	}

}
