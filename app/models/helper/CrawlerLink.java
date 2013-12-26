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
	
	private String icon;

	public CrawlerLink(){
		
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