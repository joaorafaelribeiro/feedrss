package models.helper;

import net.htmlparser.jericho.*;

import java.util.*;
import java.io.*;
import java.net.*;

import play.Logger;
import exceptions.CrawlerException;

public class CrawlerLink {

	private String image;

	private String description;

	private Source source;

	public CrawlerLink(URL url) throws CrawlerException {
		// MicrosoftConditionalCommentTagTypes.register();
		// PHPTagTypes.register();
		// PHPTagTypes.PHP_SHORT.deregister(); // remove PHP short tags for this
		// example otherwise they override processing instructions
		// MasonTagTypes.register();
		try {
			source = new Source(url);
			source.fullSequentialParse();
			image = pushImage();
			description = pushDesription();
		} catch (Throwable e) {
			throw new CrawlerException(e.getMessage(), e);
		}
	}
	
	public CrawlerLink(String body)  {
		MicrosoftConditionalCommentTagTypes.register();
		PHPTagTypes.register();
		PHPTagTypes.PHP_SHORT.deregister(); // remove PHP short tags for this
		MasonTagTypes.register();
		
			source = new Source(body);
			source.fullSequentialParse();
			image = pushImage();
			description = pushDesription();
		
	}
	
	/*public static void main(String[] args) throws MalformedURLException, CrawlerException {
		CrawlerLink c = new CrawlerLink(new URL(""));
		System.out.println(c.getImage());
		System.out.println(c.getDescription());
	}*/

	public String getImage() {
		return image;
	}

	public String getDescription() {
		return description;
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
		if (!ps.isEmpty())
			return ps.get(0).getTextExtractor().toString();
		else
			return null;
	}

	/*
	 * private String getTitle() { Element
	 * titleElement=source.getFirstElement(HTMLElementName.TITLE); if
	 * (titleElement==null) return null; // TITLE element never contains other
	 * tags so just decode it collapsing whitespace: return
	 * CharacterReference.decodeCollapseWhiteSpace(titleElement.getContent()); }
	 */

	private String pushImage() {
		String image = null;
		image = getMetaValue("og:image");
		if (image == null || "".equals(image))
			image = getImageByTagIMG();
		return image;
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

	private String getImageByTagIMG() {
		List<Element> imgs = source.getAllElements(HTMLElementName.IMG);
		if (!imgs.isEmpty())
			return imgs.get(0).getAttributeValue("src");
		else
			return null;
	}

}