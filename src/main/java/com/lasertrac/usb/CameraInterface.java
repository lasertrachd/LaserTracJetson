package com.lasertrac.usb;

import com.fazecast.jSerialComm.SerialPort;

public interface CameraInterface {

	public boolean connectCamera(SerialPort port);
	
	public void setBoudrate(int boudrate);
	
	public void startZoomIn();
	
	public void stopZoom();
	
	public void startZoomOut();
	
	public boolean zoomSeperateMode();
	
	public boolean setDizitalZoomOn();
	
	public boolean callPresetValue(byte hexMemoryNumber);
	
	public boolean setCamWB(byte value);
	
	public boolean setDisplayOn();
	
	public boolean setZoomDisplayOn();
	
	public boolean setAutoFocus(boolean value);
	
	public boolean setManualFocusByValue(String hexValue);
	
	public boolean setDizitalZoomByValue(String hexValue);
    
}
