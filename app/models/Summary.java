package models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;
@Entity
@Table(name="Summary")
public class Summary{

	private int count;
	@EmbeddedId
	private SummaryId id;
	
	private String title;
	
	

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public SummaryId getId() {
		return id;
	}
	public void setId(SummaryId id) {
		this.id = id;
	}
	
	/*public String getDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.YEAR, id.getYear());
		c.set(Calendar.MONTH, id.getMonth()-1);
		c.set(Calendar.DAY_OF_MONTH, id.getId());
		SimpleDateFormat s = (SimpleDateFormat) SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT);
		return s.format(c.getTime());
	}*/
}
