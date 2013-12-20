package models;

public class Chats {
	private long id;
	private String title;
	private long count;
	
	public Chats(long id, String title, long count) {
		super();
		this.id = id;
		this.title = title;
		this.count = count;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	
	
}
