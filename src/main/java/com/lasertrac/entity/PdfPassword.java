package com.lasertrac.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="pdf_password")
public class PdfPassword {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(nullable=true, length=30)
	private String user_pwd;
	
	@Column(nullable=true, length=30)
	private String logged_user;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=true)
	private Date date_time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUser_pwd() {
		return user_pwd;
	}

	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}

	public String getLogged_user() {
		return logged_user;
	}

	public void setLogged_user(String logged_user) {
		this.logged_user = logged_user;
	}

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}
	
	
	
	
}
