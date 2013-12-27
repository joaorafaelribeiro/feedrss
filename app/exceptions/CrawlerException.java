package exceptions;

import play.Logger;

public class CrawlerException extends Exception {


	public CrawlerException(String message) {
		super(message);
		Logger.error(this, message);
	}

	public CrawlerException(Throwable cause) {
		super(cause);
		Logger.error(cause.getMessage());
	}

	public CrawlerException(String message, Throwable cause) {
		super(message, cause);
		Logger.error(cause, message);
	}

}
