package com.lasertrac.settings;

import com.lasertrac.common.ConfigurationProps;
import com.lasertrac.common.IniFile;

public class FtpHelper {

	public static String url = "";
    public static String username = "";
    public static String password = "";
    //public static string url = "";
    public static boolean urlLoaded=false;

    static String  pingUrl;
    
    public static void loadFtpUrls() {
        url = "";
        try {
        	IniFile ini = new IniFile(ConfigurationProps.config_file);
            if (ini.readStringValue(ConfigurationProps.IniSectionAndKeys.ftpSection, ConfigurationProps.IniSectionAndKeys.ftpIpKey).length() <= 0 &&
                ini.readStringValue(ConfigurationProps.IniSectionAndKeys.ftpSection, ConfigurationProps.IniSectionAndKeys.ftpPortKey).length() <= 0) {
                urlLoaded = false;
                return;
            }
            urlLoaded = true;
            pingUrl = ini.readStringValue(ConfigurationProps.IniSectionAndKeys.ftpSection, ConfigurationProps.IniSectionAndKeys.ftpIpKey);
            url = "ftp://" + ini.readStringValue(ConfigurationProps.IniSectionAndKeys.ftpSection, ConfigurationProps.IniSectionAndKeys.ftpIpKey) +
                ":" + ini.readStringValue(ConfigurationProps.IniSectionAndKeys.ftpSection,ConfigurationProps.IniSectionAndKeys.ftpPortKey);

            username = ini.readStringValue(ConfigurationProps.IniSectionAndKeys.ftpSection,ConfigurationProps.IniSectionAndKeys.ftpUserKey);
            password = ini.readStringValue(ConfigurationProps.IniSectionAndKeys.ftpSection, ConfigurationProps.IniSectionAndKeys.ftpPasswordKey);
	
        }catch(Exception ex) {
        	
        }
        
    }
    
}
