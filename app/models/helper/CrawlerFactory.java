package models.helper;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.MasonTagTypes;
import net.htmlparser.jericho.MicrosoftConditionalCommentTagTypes;
import net.htmlparser.jericho.PHPTagTypes;
import net.htmlparser.jericho.Source;
import exceptions.CrawlerException;
import net.htmlparser.jericho.*;

import java.util.*;
import java.io.*;
import java.net.*;

public class CrawlerFactory {

	public static CrawlerLink createCrawlerLink(URL url) throws CrawlerException{
		try {
			CrawlerFactory factory = new CrawlerFactory(url);
			CrawlerLink cl = new CrawlerLink();
			cl.setDescription(factory.pushDesription());
			cl.setImage(factory.pushImage());
			cl.setIcon(factory.pushIcon());
			if(cl.getIcon() == null)
				cl.setIcon(cl.getImage());
			return cl;
		} catch (Throwable e) {
			throw new CrawlerException(e.getMessage(), e);
		}
	}
	
	public static CrawlerLink createCrawlerLink(String url) {
			CrawlerFactory factory = new CrawlerFactory(url);
			CrawlerLink cl = new CrawlerLink();
			cl.setDescription(factory.pushDesription());
			cl.setImage(factory.pushImage());
			cl.setIcon(factory.pushIcon());
			if(cl.getIcon() == null)
				cl.setIcon(cl.getImage());
			return cl;
	}
	private Source source;
	
	private CrawlerFactory(URL url) throws IOException{
		source = new Source(url);
		source.fullSequentialParse();
	}
	
	public CrawlerFactory(String body)  {
		MicrosoftConditionalCommentTagTypes.register();
		PHPTagTypes.register();
		PHPTagTypes.PHP_SHORT.deregister(); // remove PHP short tags for this
		MasonTagTypes.register();
		source = new Source(body);
		source.fullSequentialParse();
	}
	
	private String pushDesription() {
		String description = null;
		description = getMetaValue("og:description");
		if (description == null || "".equals(description.trim()))
			description = getMetaValue("description");
		if (description == null || "".equals(description.trim()))
			description = getDesriptionByTagP();
		return description;
	}
	
	private String getDesriptionByTagP() {
		List<Element> ps = source.getAllElements(HTMLElementName.P);
		if (!ps.isEmpty() && ps.get(0).getTextExtractor() != null)
			return ps.get(0).getTextExtractor().toString().replaceAll("\\<.*?>","").replaceAll("&.*?;","").replaceAll("\\[a-zA-Z]", "").trim();
		else
			return null;
	}
	
	private String getMetaValue(String key) {
		List<Element> metas = source.getAllElements(HTMLElementName.META);
		for (Element meta : metas) {
			if (key.equals(meta.getAttributeValue("name"))
					|| key.equals(meta.getAttributeValue("property")))
				return meta.getAttributeValue("content");
		}
		return null;
	}
	private String pushImage() {
		String image = null;
		image = getMetaValue("og:image");
		if (image == null || "".equals(image))
			image = getImageByTagIMG();
		return image;
	}



	private String getImageByTagIMG() {
		List<Element> imgs = source.getAllElements(HTMLElementName.IMG);
		if (!imgs.isEmpty())
			return imgs.get(0).getAttributeValue("src");
		else
			return null;
	}
	
	private String pushIcon() {
		List<Element> links = source.getAllElements(HTMLElementName.LINK);
		for(Element e : links) {
			if(e.getAttributeValue("rel") != null && e.getAttributeValue("rel").indexOf("icon") > 0)
				return e.getAttributeValue("href");
		}
			return null;
	}
}
