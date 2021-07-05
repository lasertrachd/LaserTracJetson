package com.lasertrac.common;

import java.io.File;

import org.ini4j.Wini;

public class IniFile {

	String configFileName = "C:\\LaserConfig\\usb_config.ini";
	Wini iniFile;
	public IniFile(String iniFileName) throws Exception {
		configFileName = iniFileName;
		iniFile = new Wini(new File(configFileName));
		//iniFile.
	}
	
	public int readIntValue(String section, String key) {
		try {
			//Wini ini = new Wini(new File("C:\\Users\\sdkca\\Desktop\\myinifile.ini"));
		    int age = iniFile.get(section, key, int.class);	
		    return age;	
		}catch(Exception ex) {
			return -1;
		}
	}
	
	public double readDoubleVlaue(String section, String key) {
		try {
			//Wini ini = new Wini(new File("C:\\Users\\sdkca\\Desktop\\myinifile.ini"));
		    double value = iniFile.get(section, key, double.class);	
		    return value;
		}catch(Exception ex) {
			return -1;
		}
	}
	
	public String readStringValue(String section, String key) {
		try{
			String value = iniFile.get(section, key, String.class);
			//System.out.println("value to="+value);
			if(value==null) {
				return "";
			}
			return value;
		}catch(Exception ex) {
			return "";
		}
	}
	public void putValue(String section ,String key, String value ) {
		try {
			iniFile.put(section, key, value);
			iniFile.store();
		}catch(Exception ex) {
			
		}
	}
	
	
}
