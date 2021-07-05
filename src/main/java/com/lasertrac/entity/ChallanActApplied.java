package com.lasertrac.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="challan_act_applied")
public class ChallanActApplied {
	
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
	private int challan_db_id;

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

	public int getChallan_db_id() {
		return challan_db_id;
	}

	public void setChallan_db_id(int challan_db_id) {
		this.challan_db_id = challan_db_id;
	}
	
	
	
	
}
