package com.lasertrac.settings;

public class CameraPresets {

	public static int SunnyMode_opticalZoomIndex ;
    
	public static int SunnyMode_shutterSpeedIndex ;
    
	public static int SunnyMode_AEIndex;
    
    public static int SunnyMode_defogIndex;
    
    public static int SunnyMode_gainIndex;

    public static int SunnyMode_irisIndex;

    public static int SunnyMode_gainLimitIndex;
    
    public static int SunnyMode_hlcIndex;
    
    public static int SunnyMode_hlcMaskIndex;
    
    public static int SunnyMode_focusIndex;
    
    public static int CloudMode_opticalZoomIndex ;
    
    public static int CloudMode_shutterSpeedIndex;

    //public static readonly string ConfigFile = Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData) + "\\LaserTrac\\CameraPresets.ini";
    //public static final String ConfigFile = "c:\\LaserTrac\\CameraPresets.ini";

    public static final String sunnyModeSection = "SUNNYMODE";
        
    public static final String cloudModeSection = "CLOUDMODE";

    public static final String nightColorModeSection = "NIGHTCOLORMODE";

    public static final String nightIRModeSection = "NIGHTIRMODE";

    public static final String duskModeSection = "DUSTMODE";


    public static final String OpticalZoomKey = "opticalZoom";
    public static final String DigitalZoomKey = "digitalZoom";
    public static final String ShutterSpeedKey = "shutterSpeed";
    public static final String gainKey = "gain";
    public static final String gainLimitKey = "gainLimit";
    public static final String irisKey = "iris";
    public static final String AEKey = "ae";
    public static final String defogKey = "defog";
    public static final String hlcKey = "hlc";
    public static final String hlcMaskKey = "hlcMask";
    public static final String manualFocusKey = "manualFocusKey";


    public static final String blcKey  = "blc";
    public static final String focusKey = "focus";
    public static final String icrKey = "icr";
}
