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
@Table(name="file_info")
public class FileInfo {
	
	@Id									// primary key
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(nullable=true, length=200)
	private String file_name;
	
	@Column(nullable=true)
	private int speed_limit;
	
	@Column(nullable=true, length=20)
	private String vehicle_nr;
	
	@Column(nullable=true, length=20)
	private String latitude;
	
	@Column(nullable=true, length=20)
	private String longitude;
	
	@Column(nullable=true, length=20)
	private String device_sr;
	
	@Column(nullable=true, length=20)
	private String location;
	
	@Column(nullable=true, length=20)
	private String operator;
	
	@Column(nullable=true, length=100)
	private String department_name;
	
	@Column(nullable=true, length=20)
	private String record_nr;
	
	@Column(nullable=true, length=20)
	private String number_plate_area;
	
	@Column(nullable=true)
	private int speed;
	
	@Column(nullable=true)
	private int sound_limit;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date date_evidence;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date date_created;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public int getSpeed_limit() {
		return speed_limit;
	}

	public void setSpeed_limit(int speed_limit) {
		this.speed_limit = speed_limit;
	}

	public String getVehicle_nr() {
		return vehicle_nr;
	}

	public void setVehicle_nr(String vehicle_nr) {
		this.vehicle_nr = vehicle_nr;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getDevice_sr() {
		return device_sr;
	}

	public void setDevice_sr(String device_sr) {
		this.device_sr = device_sr;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

	public String getRecord_nr() {
		return record_nr;
	}

	public void setRecord_nr(String record_nr) {
		this.record_nr = record_nr;
	}

	public String getNumber_plate_area() {
		return number_plate_area;
	}

	public void setNumber_plate_area(String number_plate_area) {
		this.number_plate_area = number_plate_area;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSound_limit() {
		return sound_limit;
	}

	public void setSound_limit(int sound_limit) {
		this.sound_limit = sound_limit;
	}

	public Date getDate_evidence() {
		return date_evidence;
	}

	public void setDate_evidence(Date date_evidence) {
		this.date_evidence = date_evidence;
	}

	public Date getDate_created() {
		return date_created;
	}

	public void setDate_created(Date date_created) {
		this.date_created = date_created;
	}
	
	
}
