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
@Table(name="messages")
public class Messages {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(nullable=true, length=200)
	private String subject;
	
	@Column(nullable=true)
	private String message;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=true)
	private Date submit_date;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=true)
	private Date delivered_date;
	
	@Column(nullable=true, length=200)
	private String attachment;
	
	@Column(nullable=true)
	private int challan_id;
	
	@Column(nullable=true)
	private int mail_sent;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getSubmit_date() {
		return submit_date;
	}

	public void setSubmit_date(Date submit_date) {
		this.submit_date = submit_date;
	}

	public Date getDelivered_date() {
		return delivered_date;
	}

	public void setDelivered_date(Date delivered_date) {
		this.delivered_date = delivered_date;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public int getChallan_id() {
		return challan_id;
	}

	public void setChallan_id(int challan_id) {
		this.challan_id = challan_id;
	}

	public int getMail_sent() {
		return mail_sent;
	}

	public void setMail_sent(int mail_sent) {
		this.mail_sent = mail_sent;
	}
		
}
