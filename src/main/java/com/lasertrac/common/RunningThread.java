package com.lasertrac.common;

public class RunningThread extends Thread {
    public boolean isRunning;
    public boolean finishedRecording = false;

    public boolean isRecordingFinished(){
    	return finishedRecording;
    }
    public boolean isRunning() {
        return isRunning;
    }

    public void stopRunning() {
        this.isRunning = false;
    }
    
}
