package com.lasertrac.settings;

import com.lasertrac.common.ConfigurationProps;
import com.lasertrac.common.IniFile;

public class ZoomMap {

	public static void setZoomValue(double value) {
		try {
			zoomValue = value;
	        IniFile ini = new IniFile(ConfigurationProps.config_file);
	        ini.putValue(ConfigurationProps.IniSectionAndKeys.zoomValueSection,ConfigurationProps.IniSectionAndKeys.zoomValueKey,value+"");	
		}catch(Exception ex) {
			
		}
        
	}
	
	public static double readZoomValue() {
	    try
	    {
	        IniFile ini = new IniFile(ConfigurationProps.config_file);
	        String strValue = ini.readStringValue(ConfigurationProps.IniSectionAndKeys.zoomValueSection, ConfigurationProps.IniSectionAndKeys.zoomValueKey);
	
	        double value = Double.parseDouble(strValue);
	        zoomValue = value;
	    }
	    catch (Exception ex)
	    {
	        zoomValue = 0.01;
	        setZoomValue(zoomValue);
	    }
	    
	    return zoomValue;
	}
	
	public static double getMapZoomValue() {
	        return zoomValue;
	    
	}
	private static double zoomValue;
	
}
