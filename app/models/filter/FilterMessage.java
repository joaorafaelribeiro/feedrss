package models.filter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FilterMessage {

	private int page;
	private String title;
	private Boolean isLike;
	private Boolean isRead;
	private Date date;
	
	public FilterMessage(int page, String title, Boolean isLike,
			Boolean isRead, Date date) {
		super();
		this.page = page;
		this.title = title;
		this.isLike = isLike;
		this.isRead = isRead;
		this.date = date;
	}

	public int getPage() {
		return page;
	}

	public String getTitle() {
		return title;
	}

	public Boolean getIsLike() {
		return isLike;
	}

	public Boolean getIsRead() {
		return isRead;
	}
	public Date getDate(){
		return date;
	}
	public String getJQL() {
		StringBuilder sql = new StringBuilder();
		sql.append(" from FeedMessage f where ");
		sql.append(" 1=1 ");
		if(this.getTitle() != null)
			sql.append("and f.title like '%"+this.getTitle()+"%' ");
		if(this.getIsLike() != null)
			sql.append(" and f.isLike = "+this.getIsLike());
		if(this.getIsRead() != null)
			sql.append(" and f.isRead = "+this.getIsRead());
		if(this.getDate() != null) { 
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			sql.append(" and year(f.pubDate) = "+c.get(Calendar.YEAR)+" and month(f.pubDate) = "+(c.get(Calendar.MONTH)+1)+" and day(f.pubDate) = "+c.get(Calendar.DAY_OF_MONTH));
		}
		sql.append(" order by f.pubDate desc");
		return sql.toString();
	}
}
