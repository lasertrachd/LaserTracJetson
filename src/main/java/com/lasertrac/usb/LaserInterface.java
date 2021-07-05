package com.lasertrac.usb;

import com.fazecast.jSerialComm.SerialPort;

public interface LaserInterface {

	public boolean connectLaser(SerialPort port);
	public void setBoudRate(int boudrate);
	
	public boolean startLaser();
	public boolean stopLaser();
	
	public boolean isLaserOn();
	
	public int getLaserSpeed();
	public String getLaserSpeedUnit();
	
	public double getDistance();
	public String getDistanceUnit();
	
	
	
}
