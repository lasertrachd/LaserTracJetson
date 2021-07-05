package com.lasertrac.common;

import net.bjoernpetersen.volctl.VolumeControl;

public class SoundAndMic {

	static VolumeControl volumeControl = null;
	public static void setVolume(int value) {
		try {
			if(volumeControl==null) {
				volumeControl = new VolumeControl();	
			}
			
			
			// Gets the current master audio volume
			//int value = volumeControl.getVolume();
			System.out.println("val="+value);
			// Sets the current master audio volume to 82%
			volumeControl.setVolume(value);
			
			
		}catch(Exception ex) {
			System.out.println("ji"+ex.toString());
		}
		
	}
	public static int getVolume(){
		try {
			
			if(volumeControl==null) {
				volumeControl = new VolumeControl();	
			}
			// Gets the current master audio volume
			int value = volumeControl.getVolume();
			return value;
			//System.out.println("val="+value);
			// Sets the current master audio volume to 82%
			//volumeControl.setVolume(value);
			
			
		}catch(Exception ex) {
			System.out.println("ji"+ex.toString());
			return 0;
		}
	}
	
}
