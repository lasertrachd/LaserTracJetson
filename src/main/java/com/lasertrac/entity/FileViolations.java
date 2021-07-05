package com.lasertrac.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="file_violations")
public class FileViolations {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(nullable=true, length=50)
	private String act_section_id;
	
	@Column(nullable=true, length=200)
	private String act_section_no;
	
	
	@Column(nullable=true, length=200)
	private String act_name;
	
	@Column(nullable=false)
	private int file_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAct_section_id() {
		return act_section_id;
	}

	public void setAct_section_id(String act_section_id) {
		this.act_section_id = act_section_id;
	}

	public String getAct_section_no() {
		return act_section_no;
	}

	public void setAct_section_no(String act_section_no) {
		this.act_section_no = act_section_no;
	}

	public String getAct_name() {
		return act_name;
	}

	public void setAct_name(String act_name) {
		this.act_name = act_name;
	}

	public int getFile_id() {
		return file_id;
	}

	public void setFile_id(int file_id) {
		this.file_id = file_id;
	}
	
	
}
