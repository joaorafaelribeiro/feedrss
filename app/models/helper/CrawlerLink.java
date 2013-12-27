package models.helper;

import net.htmlparser.jericho.*;

import java.util.*;
import java.io.*;
import java.net.*;

import play.Logger;
import exceptions.CrawlerException;
/**
 * This class represents a summary of a particular web page. 
 * This class is created by CrawlerFactory and a link must be set as the creator method argument.
 * @author joaoraf
 *
 */
public class CrawlerLink {

	private String image;

	private String description;
	
	private String icon;

	CrawlerLink(){
		
	}
	


	public String getImage() {
		return image;
	}

	public String getDescription() {
		return description;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}