package com.lasertrac.datamodels;

import org.dom4j.util.StringUtils;

import com.lasertrac.common.CommonFunctions;

public class CurrentSessionClass {

	public static String departmentName="";
	public static String operatorName="";
	
	public static String selectedSpeedLimitEL="";
	public static String selectedSpeedLimitVL="";
	
	public static String location="";
	public static String device_sr="";

	static int  record_nr=1;
	public static String getRecordNr() {
//		int leftPad = "record_nr".length() - 6;
//		String leftPadding="";
//		for(int i=0; i<leftPad;i++) {
//			leftPadding+="0";	
//		}
//		leftPadding += record_nr+"";	
		String leftPadding = CommonFunctions.padLeft(record_nr+"", 6, "0"); 
		return leftPadding;
	}
	public static  int getRecordNrInt() {
		return record_nr;
	}
	public static void setRecordNr(int rec_nr) {
		record_nr=rec_nr;
	}
	//public static String record_nr="";
	
	public static int soundLimit=48;
	
	
}
