package com.lasertrac.common;

import org.bytedeco.javacv.Frame;

public class FrameToRecord {
	private long timestamp;
    private Frame frame;
    int frameCounter;
    boolean isLastFrame = false;
    public boolean isLastFrame() {
        return isLastFrame;
    }
    
    public FrameToRecord(long timestamp, Frame frame, boolean isLastFrame, int frameCOunter) {
        this.timestamp = timestamp;
        this.frame = frame;
        this.isLastFrame = isLastFrame;
        this.frameCounter = frameCOunter;
    }

    public int getFrameCounter(){
    	return frameCounter;	
    }
    
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Frame getFrame() {
        return frame;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }
}
