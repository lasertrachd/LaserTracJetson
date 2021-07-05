package com.lasertrac.settings;

import com.lasertrac.common.ConfigurationProps;

public class UsbRelay {
	public static String RelayOne="01";
    public static String RelayTwo = "02";
    static boolean relayOneOn = true;
    static boolean relayTwoOn = false;
    public static boolean isRelayOneOn() {
        return relayOneOn; 
    }
    public static boolean isRelayTwoOn()
    {
        return relayTwoOn; 
    }
    
    public static void openRelay( String relayName) {
    	try {
    		String path = ConfigurationProps.relay_exe_file;

            //Process p = Runtime.getRuntime().exec("cmd /c start /wait " + path + "\\RunFromCode.bat");
            Process p = Runtime.getRuntime().exec(path+" BITFT"+" open "+relayName);

            System.out.println("Waiting for batch file ...");
            p.waitFor();
            System.out.println("Batch file done.");
            relayTwoOn=true;
    	}catch(Exception ex) {
    		
    	}
    }
    
    public static void closeRelay( String relayName) {
    	try {
    		String path = ConfigurationProps.relay_exe_file;

            //Process p = Runtime.getRuntime().exec("cmd /c start /wait " + path + "\\RunFromCode.bat");
            Process p = Runtime.getRuntime().exec(path+" BITFT"+" close "+relayName);

            System.out.println("Waiting for batch file ...");
            p.waitFor();
            System.out.println("Batch file done.");
            relayTwoOn=false;
	
    	}catch(Exception ex) {
    		
    	}
    }
    
}
