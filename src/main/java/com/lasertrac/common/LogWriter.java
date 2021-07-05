package com.lasertrac.common;

import java.util.Date;

import org.ini4j.Ini;

public class LogWriter {

	public static void appendLogLaser(String msg) {
		try {
			Date d=new Date();
			IniFile ini = new IniFile(ConfigurationProps.laser_log);
				ini.putValue(MyUtils.directoryDTFormatter.format(d),
							MyUtils.logTimeFormatter.format(d), 
							msg);
		}catch(Exception ex) {
			
		}
	}
	
	public static void appendLog(String msg) {
		try {
			Date d=new Date();
			IniFile ini = new IniFile(ConfigurationProps.common_log);
				ini.putValue(MyUtils.directoryDTFormatter.format(d),
							MyUtils.logTimeFormatter.format(d), 
							msg);
		}catch(Exception ex) {
			
		}
	}
}
