package exceptions;

import play.Logger;

public class ReaderFeedRSSException extends Exception {

	public ReaderFeedRSSException(String message) {
		super(message);
		Logger.error(this, message);
	}

	public ReaderFeedRSSException(Throwable cause) {
		super(cause);
		Logger.error(cause.getMessage());
	}

	public ReaderFeedRSSException(String message, Throwable cause) {
		super(message, cause);
		Logger.error(cause, message);
	}

}
