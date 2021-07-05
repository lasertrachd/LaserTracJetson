package com.lasertrac.settings;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;

import net.bjoernpetersen.volctl.VolumeControl;

public class MySoundManager {

	public static boolean setMic(String inputDeviceName,boolean mute) {
		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
		boolean result=false;
		for (int i = 0; i < mixerInfos.length; i++) {
			System.out.println(mixerInfos[i].getName()); 
			if(mixerInfos[i].getName().toLowerCase().contains(inputDeviceName.toLowerCase())) {
				Mixer mixer = AudioSystem.getMixer(mixerInfos[i]);
				
				Line.Info[] targetLineInfos = mixer.getTargetLineInfo();
					for (int j = 0; j < targetLineInfos.length; j++) {
						if(mute) {
							result=	setVolume(targetLineInfos[j], 0);
						}else {
							result = setVolume(targetLineInfos[j], 1);	
						}
						
					}		
			}
		}
		return result;
	}
	
	static boolean setVolume(Line.Info lineInfo,float value) {
		try {
			Line line = AudioSystem.getLine(lineInfo);
			System.out.println(line.getLineInfo());
			line.open();
			FloatControl control = (FloatControl)line.getControl(FloatControl.Type.VOLUME);
			System.out.println("mic value="+control.getValue());
			control.setValue(control.getMinimum());
			line.close();
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
			return true;
	}
	
	public static boolean isMicMute(String inputDeviceName,boolean mute) {
		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
		
		for (int i = 0; i < mixerInfos.length; i++) {
			System.out.println(mixerInfos[i].getName()); 
			if(mixerInfos[i].getName().toLowerCase().contains(inputDeviceName.toLowerCase())) {
				Mixer mixer = AudioSystem.getMixer(mixerInfos[i]);
				
				Line.Info[] targetLineInfos = mixer.getTargetLineInfo();
					for (int j = 0; j < targetLineInfos.length; j++) {
						float d;
						if(mute) {
							 d = 	getVolume(targetLineInfos[j]);
						}else {
							d = getVolume(targetLineInfos[j]);	
						}
						if(d==1) {
							return false;
						}else if(d==0){
							return true;
						}else {
							setVolume(targetLineInfos[i], 0);
							return true;
						}
					}		
			}
		}
		return false;
	}
	static float getVolume(Line.Info lineInfo) {
		try {
			Line line = AudioSystem.getLine(lineInfo);
			
			//if(line.getLineInfo().toString().toUpperCase().contains("HEADPHONE")) {
				System.out.println(line.getLineInfo());
				line.open();
				FloatControl control = (FloatControl)line.getControl(FloatControl.Type.VOLUME);
				float volume = control.getValue();
				System.out.println("mic value="+control.getValue());
				line.close();
				return volume;
				//control.setValue(control.getMinimum());
				
			//}
		} catch (Exception e) {
			System.out.println(e);
			return -1;
		}
	}
	
	//value between 0 to 100
	public static void setOutputVolume(int value) {
		try {
			
			VolumeControl volumeControl = new VolumeControl();
			// Gets the current master audio volume
			//value = volumeControl.getVolume();
			// Sets the current master audio volume to 82%
			volumeControl.setVolume(value);
			
			
		}catch(Exception ex) {
			System.out.println("ji"+ex.toString());
		}
	}
	//value between 0 to 100
	public static int getOutputVolume() {
			try {
				
				VolumeControl volumeControl = new VolumeControl();
				// Gets the current master audio volume
				//value = volumeControl.getVolume();
				// Sets the current master audio volume to 82%
				return volumeControl.getVolume();
				
				
			}catch(Exception ex) {
				System.out.println("ji"+ex.toString());
			}
			return -1;
		}
	
	
}
