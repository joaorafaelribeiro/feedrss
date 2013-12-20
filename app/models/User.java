package models;

import javax.persistence.Entity;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class User extends Model{
	@Required(message="informe um nome")
	public String nome;
	@Required(message="informe um email")
	public String email;
	@Required(message="informe uma senha")
	public String senha;
	
	public boolean authenticate() {
		long x = User.count("email = ? and senha = ?", email, senha);
		return x > 0;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String email, String senha) {
		super();
		this.email = email;
		this.senha = senha;
	}
	
	
}
