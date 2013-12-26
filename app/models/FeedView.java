package models;

import java.util.Date;

import javax.persistence.Entity;

import play.db.jpa.Model;
@Entity
public class FeedView extends Model {
	public String title;
	public String image;
	public Date dateExpired;
	public int size;
}
