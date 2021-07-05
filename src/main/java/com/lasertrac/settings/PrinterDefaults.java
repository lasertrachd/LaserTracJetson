package com.lasertrac.settings;

import com.lasertrac.common.ConfigurationProps;
import com.lasertrac.common.IniFile;

public class PrinterDefaults {

	
	private static int selectedPrinterIndex = 0;
    //public static int selectedPrinter
    //{
    //    get
    //    {
    //        return selectedPrinterIndex;
    //    }
    //    set {
    //        selectedPrinterIndex = value;
    //    }
    //}

    private static int selectedPaperSizeIndex = 0;
    //public static int selectedPaperSize {
    //    get { return selectedPaperSizeIndex; }
    //    set { selectedPaperSizeIndex = value; }
    //}
    public static String defaultPrinterStr = "";
    public static String defaultpageSizeStr = "";

    public static void updateDefaultPrinter(String printerName)
    {
        try{
        	IniFile ini = new IniFile(ConfigurationProps.config_file);
            ini.putValue(ConfigurationProps.IniSectionAndKeys.printerDefaultSection,ConfigurationProps.IniSectionAndKeys.printerNameKey, printerName);
            }catch(Exception ex) {
        }
    }
    public static void updateDefaultPrinterPage(String page)
    {
        try {
        	IniFile ini = new IniFile(ConfigurationProps.config_file);
            ini.putValue(ConfigurationProps.IniSectionAndKeys.printerDefaultSection, ConfigurationProps.IniSectionAndKeys.pageNameKey, page);	
        }catch(Exception ex) {
        	ex.printStackTrace();
        }
    }
    public static void readDefaultPrinterAndPaper() {
    	try {
    		IniFile ini = new IniFile(ConfigurationProps.config_file);
            defaultPrinterStr= ini.readStringValue(ConfigurationProps.IniSectionAndKeys.printerDefaultSection, ConfigurationProps.IniSectionAndKeys.printerNameKey);
            defaultpageSizeStr = ini.readStringValue(ConfigurationProps.IniSectionAndKeys.printerDefaultSection, ConfigurationProps.IniSectionAndKeys.pageNameKey);	
    	}catch(Exception ex) {
    		
    	}
    }
    
}
