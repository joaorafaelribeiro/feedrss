package models;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class SummaryId implements Serializable{
	private long id;
	private int month;
	private int year;
	public long getId() {
		return id;
	}
	public void setId(long day) {
		this.id = day;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + id);
		result = prime * result + month;
		result = prime * result + year;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SummaryId other = (SummaryId) obj;
		if (id != other.id)
			return false;
		if (month != other.month)
			return false;
		if (year != other.year)
			return false;
		return true;
	}
	
}
