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
@Table(name="challan")
public class Challan {
	
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
	
	@Column(nullable=false, length=500)
	private String image_path;
	
	@Column(nullable=true, length=100)
	private String vehicle_owner_name;
	
	@Column(nullable=true, length=100)
	private String vehicle_owner_address;
	
	@Column(nullable=true, length=100)
	private String vehicle_type;
	
	@Column(nullable=true, length=50)
	private String location_code;
	
	@Column(nullable=true, length=20)
	private String vehicle_owner_mobile;
	
	@Column(nullable=true, length=20)
	private String district_code;
	
	@Column(nullable=true, length=20)
	private String police_station_code;
	
	@Column(nullable=false, length=100)
	private String record_nr;
	
	@Column(nullable=false)
	private int upload_attempt;
	
	@Column(nullable=true, length=50)
	private String number_plate_area;
	
	@Column(nullable=true)
	private int speed;
	
	@Column(nullable=true)
	private int speed_limit;
	
	@Column(nullable=true, length=50)
	private String status_upload;
	
	@Column(nullable=true)
	private int sound_limit;

	public Date getDate_time() {
		return date_time;
	}

	public void setDate_time(Date date_time) {
		this.date_time = date_time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getVehicle_nr() {
		return vehicle_nr;
	}

	public void setVehicle_nr(String vehicle_nr) {
		this.vehicle_nr = vehicle_nr;
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

	public String getImage_path() {
		return image_path;
	}

	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}

	public String getVehicle_owner_name() {
		return vehicle_owner_name;
	}

	public void setVehicle_owner_name(String vehicle_owner_name) {
		this.vehicle_owner_name = vehicle_owner_name;
	}

	public String getVehicle_owner_address() {
		return vehicle_owner_address;
	}

	public void setVehicle_owner_address(String vehicle_owner_address) {
		this.vehicle_owner_address = vehicle_owner_address;
	}

	public String getVehicle_type() {
		return vehicle_type;
	}

	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}

	public String getLocation_code() {
		return location_code;
	}

	public void setLocation_code(String location_code) {
		this.location_code = location_code;
	}

	public String getVehicle_owner_mobile() {
		return vehicle_owner_mobile;
	}

	public void setVehicle_owner_mobile(String vehicle_owner_mobile) {
		this.vehicle_owner_mobile = vehicle_owner_mobile;
	}

	public String getDistrict_code() {
		return district_code;
	}

	public void setDistrict_code(String district_code) {
		this.district_code = district_code;
	}

	public String getPolice_station_code() {
		return police_station_code;
	}

	public void setPolice_station_code(String police_station_code) {
		this.police_station_code = police_station_code;
	}

	public String getRecord_nr() {
		return record_nr;
	}

	public void setRecord_nr(String record_nr) {
		this.record_nr = record_nr;
	}

	public int getUpload_attempt() {
		return upload_attempt;
	}

	public void setUpload_attempt(int upload_attempt) {
		this.upload_attempt = upload_attempt;
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

	public int getSpeed_limit() {
		return speed_limit;
	}

	public void setSpeed_limit(int speed_limit) {
		this.speed_limit = speed_limit;
	}

	public String getStatus_upload() {
		return status_upload;
	}

	public void setStatus_upload(String status_upload) {
		this.status_upload = status_upload;
	}

	public int getSound_limit() {
		return sound_limit;
	}

	public void setSound_limit(int sound_limit) {
		this.sound_limit = sound_limit;
	}
	
	
	
}
