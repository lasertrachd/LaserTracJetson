package com.lasertrac.settings;

/*
 * Copyright 2011 Witoslaw Koczewsi <wi@koczewski.de>
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.Control.Type;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import javax.sound.sampled.TargetDataLine;

public class MyAudioManager {
	public static void main(String[] args) throws
		LineUnavailableException, InterruptedException {
		
		//System.out.println(getHierarchyInfo());
		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
		for (int i = 0; i < mixerInfos.length; i++) {
			System.out.println(mixerInfos[i].getName()); 
			Mixer mixer = AudioSystem.getMixer(mixerInfos[i]);
		
			Line.Info[] targetLineInfos = mixer.getTargetLineInfo();
				for (int j = 0; j < targetLineInfos.length; j++) {
					setVolume(targetLineInfos[j],true);
				}
		}
		
	}
	public static void setMicMute(boolean mute) {
			//System.out.println(getHierarchyInfo());
			Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
			for (int i = 0; i < mixerInfos.length; i++) {
				System.out.println(mixerInfos[i].getName()); 
				Mixer mixer = AudioSystem.getMixer(mixerInfos[i]);
			
				Line.Info[] targetLineInfos = mixer.getTargetLineInfo();
					for (int j = 0; j < targetLineInfos.length; j++) {
						setVolume(targetLineInfos[j], mute);
					}
			}

	}
	
//	public static int getVolume() {
//		try {
//			Line line = AudioSystem.getLine(lineInfo);
//			
//			//if(line.getLineInfo().toString().toUpperCase().contains("HEADPHONE")) {
//				System.out.println(line.getLineInfo());
//				line.open();
//				FloatControl control = (FloatControl)line.getControl(FloatControl.Type.VOLUME);
//				System.out.println("mic value="+control.getValue());
//				control.setValue(control.getMinimum());
//				line.close();
//			//}
//			
//		} catch (Exception e) {
//			System.out.println(e);
//			return 0;
//		}
//			return 0;
//		}
//	}

	static boolean setVolume(Line.Info lineInfo, boolean mute) {
	try {
		Line line = AudioSystem.getLine(lineInfo);
		
		//if(line.getLineInfo().toString().toUpperCase().contains("HEADPHONE")) {
			System.out.println(line.getLineInfo());
			line.open();
			FloatControl control = (FloatControl)line.getControl(FloatControl.Type.VOLUME);
			System.out.println("mic value="+control.getValue());
			if(mute) {
				control.setValue(control.getMinimum());
			}else {
				control.setValue(control.getMaximum());	
			}
			
			line.close();
		//}
		
	} catch (Exception e) {
		System.out.println(e);
		return false;
	}
		return true;
	}
	
//	public static void main(String[] args) throws Exception {
////		System.out.println(getHierarchyInfo());
////		System.out.println(getMasterOutputVolume());
//		//setMasterOutputVolume(0.8f);
//		//setMasterOutputMute(true);
//		AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
//		TargetDataLine microphone = AudioSystem.getTargetDataLine(format);
//		FloatControl c=(FloatControl) microphone.getControl(FloatControl.Type.);
//		c.setValue(0);
//	}

	public static void setMasterOutputVolume(float value) {
		if (value < 0 || value > 1)
			throw new IllegalArgumentException(
					"Volume can only be set to a value from 0 to 1. Given value is illegal: " + value);
		Line line = getMasterOutputLine();
		if (line == null) throw new RuntimeException("Master output port not found");
		boolean opened = open(line);
		if(opened) {
			System.out.println("yes line open");
		}
		try {
			FloatControl control = getVolumeControl(line);
			if (control == null)
				throw new RuntimeException("Volume control not found in master port: " + toString(line));
			control.setValue(value);
		} finally {
			if (opened) line.close();
		}
	}

	public static Float getMasterOutputVolume() {
		Line line = getMasterOutputLine();
		if (line == null) return null;
		boolean opened = open(line);
		try {
			FloatControl control = getVolumeControl(line);
			if (control == null) return null;
			return control.getValue();
		} finally {
			if (opened) line.close();
		}
	}

	public static void setMasterOutputMute(boolean value) {
		Line line = getMasterOutputLine();
		if (line == null) throw new RuntimeException("Master output port not found");
		boolean opened = open(line);
		try {
			BooleanControl control = getMuteControl(line);
			if (control == null)
				throw new RuntimeException("Mute control not found in master port: " + toString(line));
			control.setValue(value);
		} finally {
			if (opened) line.close();
		}
	}

	public static Boolean getMasterOutputMute() {
		Line line = getMasterOutputLine();
		if (line == null) return null;
		boolean opened = open(line);
		try {
			BooleanControl control = getMuteControl(line);
			if (control == null) return null;
			return control.getValue();
		} finally {
			if (opened) line.close();
		}
	}

	public static Line getMasterOutputLine() {
		for (Mixer mixer : getMixers()) {
			for (Line line : getAvailableOutputLines(mixer)) {
				
				if (line.getLineInfo().toString().contains("Master")) return line;
			}
		}
		return null;
	}

	public static FloatControl getVolumeControl(Line line) {
		if (!line.isOpen()) throw new RuntimeException("Line is closed: " + toString(line));
		return (FloatControl) findControl(FloatControl.Type.VOLUME, line.getControls());
	}

	public static BooleanControl getMuteControl(Line line) {
		if (!line.isOpen()) throw new RuntimeException("Line is closed: " + toString(line));
		return (BooleanControl) findControl(BooleanControl.Type.MUTE, line.getControls());
	}

	private static Control findControl(Type type, Control... controls) {
		if (controls == null || controls.length == 0) return null;
		for (Control control : controls) {
			if (control.getType().equals(type)) return control;
			if (control instanceof CompoundControl) {
				CompoundControl compoundControl = (CompoundControl) control;
				Control member = findControl(type, compoundControl.getMemberControls());
				if (member != null) return member;
			}
		}
		return null;
	}

	public static List<Mixer> getMixers() {
		Info[] infos = AudioSystem.getMixerInfo();
		List<Mixer> mixers = new ArrayList<Mixer>(infos.length);
		for (Info info : infos) {
			Mixer mixer = AudioSystem.getMixer(info);
			mixers.add(mixer);
		}
		return mixers;
	}

	public static List<Line> getAvailableOutputLines(Mixer mixer) {
		return getAvailableLines(mixer, mixer.getTargetLineInfo());
	}

	public static List<Line> getAvailableInputLines(Mixer mixer) {
		return getAvailableLines(mixer, mixer.getSourceLineInfo());
	}

	private static List<Line> getAvailableLines(Mixer mixer, Line.Info[] lineInfos) {
		List<Line> lines = new ArrayList<Line>(lineInfos.length);
		for (Line.Info lineInfo : lineInfos) {
			Line line;
			line = getLineIfAvailable(mixer, lineInfo);
			if (line != null) lines.add(line);
		}
		return lines;
	}

	public static Line getLineIfAvailable(Mixer mixer, Line.Info lineInfo) {
		try {
			return mixer.getLine(lineInfo);
		} catch (LineUnavailableException ex) {
			return null;
		}
	}

	public static String getHierarchyInfo() {
		StringBuilder sb = new StringBuilder();
		for (Mixer mixer : getMixers()) {
			sb.append("Mixer: ").append(toString(mixer)).append("\n");

			for (Line line : getAvailableOutputLines(mixer)) {
				sb.append("  OUT: ").append(toString(line)).append("\n");
				boolean opened = open(line);
				for (Control control : line.getControls()) {
					sb.append("    Control: ").append(toString(control)).append("\n");
					if (control instanceof CompoundControl) {
						CompoundControl compoundControl = (CompoundControl) control;
						for (Control subControl : compoundControl.getMemberControls()) {
							sb.append("      Sub-Control: ").append(toString(subControl)).append("\n");
						}
					}
				}
				if (opened) line.close();
			}

			for (Line line : getAvailableOutputLines(mixer)) {
				sb.append("  IN: ").append(toString(line)).append("\n");
				boolean opened = open(line);
				for (Control control : line.getControls()) {
					sb.append("    Control: ").append(toString(control)).append("\n");
					if (control instanceof CompoundControl) {
						CompoundControl compoundControl = (CompoundControl) control;
						for (Control subControl : compoundControl.getMemberControls()) {
							sb.append("      Sub-Control: ").append(toString(subControl)).append("\n");
						}
					}
				}
				if (opened) line.close();
			}

			sb.append("\n");
		}
		return sb.toString();
	}

	public static boolean open(Line line) {
		if (line.isOpen()) return false;
		try {
			line.open();
		} catch (LineUnavailableException ex) {
			return false;
		}
		return true;
	}

	public static String toString(Control control) {
		if (control == null) return null;
		return control.toString() + " (" + control.getType().toString() + ")";
	}

	public static String toString(Line line) {
		if (line == null) return null;
		Line.Info info = line.getLineInfo();
		return info.toString();// + " (" + line.getClass().getSimpleName() + ")";
	}

	public static String toString(Mixer mixer) {
		if (mixer == null) return null;
		StringBuilder sb = new StringBuilder();
		Info info = mixer.getMixerInfo();
		sb.append(info.getName());
		sb.append(" (").append(info.getDescription()).append(")");
		sb.append(mixer.isOpen() ? " [open]" : " [closed]");
		return sb.toString();
	}

}
