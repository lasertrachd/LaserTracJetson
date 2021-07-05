package com.lasertrac.settings;

public class CameraProperties {

	public static class DigitalZoom {
		public String xValue = "";
		public String hexValue = "";
		
	}
	
	public static DigitalZoom[] dZoomArr=null;
	public static void reloadCameraProperties() {
		dZoomArr=new DigitalZoom[4];
		
		DigitalZoom d=new DigitalZoom();
		d.xValue = "1x";
		d.hexValue = "00";
		dZoomArr[0] =d;
		
		d=new DigitalZoom();
		d.xValue = "2x";
		d.hexValue = "80";
		dZoomArr[0] =d;
		
		d=new DigitalZoom();
		d.xValue = "3x";
		d.hexValue = "AA";
		dZoomArr[0] =d;
		
		d=new DigitalZoom();
		d.xValue = "4x";
		d.hexValue = "C0";
		dZoomArr[0] =d;
		
		
		
	}
}
