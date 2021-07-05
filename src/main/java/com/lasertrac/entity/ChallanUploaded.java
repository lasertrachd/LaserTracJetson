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
@Table(name="challan_uploaded")
public class ChallanUploaded {
	@Id									// primary key
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date date_time;
	
	@Column(nullable=true, length=20)
	private String latitude;
	
	@Column(nullable=true, length=20)
	private String longitude;
	
	@Column(nullable=false, length=20)
	private String device_sr;
	
	@Column(nullable=false, length=100)
	private String location;
	
	@Column(nullable=true, length=20)
	private String vehicle_nr;
	
	@Column(nullable=true, length=100)
	private String operator;
	
	@Column(nullable=true, length=100)
	private String department_name;
	
	@Column(nullable=false, length=200)
	private String image_path;
	
	@Column(nullable=true, length=200)
	private String vehicle_owner_name;
	
	@Column(nullable=true, length=200)
	private String vehicle_owner_address;
	
	@Column(nullable=true, length=200)
	private String vehicle_type;
	

	@Column(nullable=true, length=20)
	private String vehicle_owner_mobile;
	
	@Column(nullable=true, length=50)
	private String location_code;
	
	@Column(nullable=true, length=20)
	private String district_code;
	
	@Column(nullable=true, length=20)
	private String police_station_code;
	
	@Column(nullable=false, length=100)
	private String record_nr;
	
	@Column(nullable=true)
	private int challan_db_id;

	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date upload_datetime;
	
	@Column(nullable=false, length=100)
	private String challan_no;
	
	@Column(nullable=true, length=40)
	private String number_plate_area;
	
	@Column(nullable=true)
	private int speed;
	
	@Column(nullable=true)
	private int speed_limit;
	
	@Column(nullable=true)
	private int sound_limit;

	
}
