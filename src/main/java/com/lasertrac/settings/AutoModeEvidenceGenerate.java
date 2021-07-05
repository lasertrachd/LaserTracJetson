package com.lasertrac.settings;

import java.util.Date;
import java.util.List;

import com.lasertrac.common.ConfigurationProps;
import com.lasertrac.common.IniFile;
import com.lasertrac.common.LogWriter;
import com.lasertrac.entity.Challan;
import com.lasertrac.entity.Violations;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class AutoModeEvidenceGenerate {

	 public class EvidenceClass {
         //public Image image;
         public Image numberPlate;
         public String filename;
         public String numberText;
         public Date dt;
         public int speed;
         public int speed_limit;
         public Rectangle number_plate_area;
         public Challan challan_entity;
         public int sound_limit ;
         public List<Violations> actLists;
     }
	 public static int distanceDay=180;
     public static int distanceNight=70;
     public static int timerInterval=500;
     
     public static void loadParams() {
    	 try {
    		 IniFile ini=new IniFile(ConfigurationProps.config_file);
        	 
        	 String str = ini.readStringValue(ConfigurationProps.IniSectionAndKeys.autoModeEvidenceGenerateSection,ConfigurationProps.IniSectionAndKeys.dayDistanceKey);
             try {
            	 distanceDay = Integer.parseInt(str);	 
             }catch(Exception ex) {
            	 distanceDay = 180;
             }
        	 
             str = ini.readStringValue(ConfigurationProps.IniSectionAndKeys.autoModeEvidenceGenerateSection, ConfigurationProps.IniSectionAndKeys.nightDistanceKey);
             try {
            	 distanceNight = Integer.parseInt(str);	 
             }catch(Exception ex) {
            	 distanceNight = 70;
             }
        	 
             str = ini.readStringValue(ConfigurationProps.IniSectionAndKeys.autoModeEvidenceGenerateSection, ConfigurationProps.IniSectionAndKeys.timerDistanceKey);
             try {
            	 timerInterval = Integer.parseInt(str);	 
             }catch(Exception ex) {
            	 timerInterval = 500;
             }
             
    	 }catch(Exception ex) {
    		 System.out.println(ex.toString());
    		 ex.printStackTrace();
    		 LogWriter.appendLog("load params auto evidence="+ex.getLocalizedMessage());
    	 }
         
     }
	 
}
