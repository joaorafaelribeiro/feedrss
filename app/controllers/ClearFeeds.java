package controllers;

import java.util.List;

import models.Feed;
import models.FeedMessage;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
@Every("1mn")
@OnApplicationStart
public class ClearFeeds extends Job{
	public void doJob() {
		FeedMessage.clear();
	}
}

