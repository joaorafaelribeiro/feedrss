package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import play.db.jpa.Model;
@Entity
public class Category extends Model {

	public String nome;
	@OneToMany
	@JoinColumn(name="category_id")
	@OrderBy("title")
	public List<Feed> feeds;
	
	public static List<Category> findAllOrderByName() {
		return Category.find("select c from Category c where c.id in (select distinct(f.category.id) from Feed f) order by c.nome").fetch();
	}
}
