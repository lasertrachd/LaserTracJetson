package com.lasertrac.common;

public class ConfigurationProps {

	public static final String config_dir = "/home/LaserConfig";
	public  static  final String usb_config_file = config_dir+"/usb_config.ini";
	public  static  final String config_file = config_dir+"/config.ini";
	public  static  final String config_file_camera_presets = config_dir+"/CameraPresets.ini";
	
	public  static  final String video_dir = config_dir+"/videos";
	public  static  final String snap_dir = config_dir+"/snapshots";
	public  static  final String pdf_dir = config_dir+"/pdfs";
	
	public  static  final String laser_log = config_dir+"/laser_log.ini";
	public  static  final String common_log = config_dir+"/log.ini";
	
	public static String relay_exe_file = config_dir+"/relay/CommandApp_USBRelay.exe";
	
	public static class IniSectionAndKeys{
		public static final String keyValue="value";
		
		public static final String departmentNameSection="Department";
		public static final String operatorSection="Operator";
		public static final String locationSection="Location";
		
		public static String deviceSrSection = "DeviceSr";
		public static String recordNrSection = "RecordNr";
		
		
		
		public static final String logoImageSection="logoImageSection";
		
		public static final String sound_data_script_section = "sound_script";
	
		public static final String autoModeEvidenceGenerateSection = "AutoModeEvidenceGenerateSection";
		public static final String dayDistanceKey = "day_disatnce";
		public static final String nightDistanceKey = "night_disatnce";
		public static final String timerDistanceKey = "timer_value";
		
		
		public static String printerDefaultSection = "priter_default";
		public static String printerNameKey = "printer_name";
		public static String pageNameKey = "page_name";
		public static String removePrintersNameKey = "remove_printers";
		
		public static String databaseSection = "DatabaseSection";
        public static String dbServerKey = "ServerKey";
        public static String dbPortKey = "PortKey";
        public static String dbUserKey = "UserKey";
        public static String dbPasswordKey = "PasswordKey";
        public static String dbDatabaseKey = "DatabaseKey";
		
        
        //FTP
        public static String ftpSection = "FTP_Settings";
        public static String ftpIpKey = "FTP_IP";
        public static String ftpUserKey = "FTP_User";
        public static String ftpPasswordKey = "FTP_Pass";
        public static String ftpPortKey = "FTP_Port";
        
        
        public static String encryptPdfOptionSection = "encryptPdfOptionSection";
        
        
        //upload config variables
        public static String uploadUrlKey = "url";
        public static String uploadBackUpInterval = "backup";
        public static String UploadServiceSection = "UploadSection";
        public static String uploadServiceUtility = "uploadServiceUtility";
        public static String uploadChoice = "upload";
        

        //offline map
        public static String zoomValueSection = "zoom_map";
        public static String zoomValueKey= "zoom";
        
        public static String wrongSideSection = "wrong_side";
        public static String wrongSideDirectionKey = "direction";
        // 0 disable, value>0 plus , value<0
        public static int wrongSideDirection = 0;
        
        
        public static String speedLimitSection = "SpeedLimitSection";
        public static String eL1Speed = "eL1value";
        public static String vL1Speed = "vL1value";
        
        public static String eL2Speed = "eL2value";
        public static String vL2Speed = "vL2value";
        
        public static String eL3Speed = "eL3value";
        public static String vL3Speed = "vL3value";
        
        public static String selectedSpeedIndex = "selectedSpeedIndex";
        
        public static String Mic = "MIC";
        
        
		public class USB_PROPS{
			public final static String laserSection="laser";
			public final static String cameraSection="camera";
			public final static String gpsSection="gps";
			
			public final static String portKey="port";
			
			public final static String portDescKey="port_desc";
			
			public final static String detectKey="detect";
			
			public final static String boudrateKey="boudrate";
			
		}
	}
	
	
	
}
