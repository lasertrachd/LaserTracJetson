package com.lasertrac.settings;

import java.io.File;

import com.lasertrac.common.ConfigurationProps;
import com.lasertrac.common.IniFile;

public class MyUploadConfig {
	public static class UploadChoice {
        public static String Manual = "manual";
        public static String Auto = "auto";
        public static String Off = "off";
    }

    public static class UploadStatusDB
    {
    	 public static String pending = "pending";
    	 public static String confirm = "confirm";
    	 public static String uploaded="uploaded";
    	 public static String Off = "off";
    }

    static String uploadChoice = UploadChoice.Off;

    public static String getUploadChoiceSelected() {
        return uploadChoice; 
    }

    public static void updateUploadSetting(String uc) {
    	try {
    		IniFile ini = new IniFile(ConfigurationProps.config_file);
            ini.putValue(ConfigurationProps.IniSectionAndKeys.UploadServiceSection, ConfigurationProps.IniSectionAndKeys.uploadChoice, uc + "");
            uploadChoice = uc;	
    	}catch(Exception ex) {
    		//return UploadChoice.Off;
    	}
        
    }

    public static void loadUploadSetting()
    {
    	try {
    		IniFile ini = new IniFile(ConfigurationProps.config_file);
            String value = ini.readStringValue(ConfigurationProps.IniSectionAndKeys.UploadServiceSection, ConfigurationProps.IniSectionAndKeys.uploadChoice);
            if (value == UploadChoice.Auto + "")
            {
                uploadChoice = UploadChoice.Auto;
            }
            else if (value == UploadChoice.Manual + "")
            {
                uploadChoice = UploadChoice.Manual;
            }
            else {
                uploadChoice = UploadChoice.Off;
            }

    	}catch(Exception ex) {
    		
    	}
    }

    public static String getUploadService() {
        try {
        	IniFile ini = new IniFile(ConfigurationProps.config_file);

            String strServiceName = ini.readStringValue(ConfigurationProps.IniSectionAndKeys.UploadServiceSection, ConfigurationProps.IniSectionAndKeys.uploadServiceUtility);
            File f=new File(strServiceName);
            if (f.exists()==false)
            {
                //MyMessageBox.ShowBox("Upload Service .exe file not found.", "Upload Service not found");
                return null;
            }
            else {
                return strServiceName;
            }
        }catch(Exception ex) {
        	return null;
        }
    }
    
}
