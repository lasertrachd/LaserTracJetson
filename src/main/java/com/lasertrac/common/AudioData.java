package com.lasertrac.common;

import java.nio.ShortBuffer;

public class AudioData {
	
		int sampleRate, numChannels;
		ShortBuffer   sBuff;
		long timestamp;
		public AudioData(int sampleRate,int numChannels,ShortBuffer sBuf, long timestamp) {
			this.sampleRate = sampleRate;
			this.numChannels = numChannels;
			this.sBuff = sBuf;
			this.timestamp = timestamp;
		}
		public int getSampleRate() {
			return sampleRate;
		}
		
		public int getNumChannels() {
			return numChannels;
		}
		public ShortBuffer getbBufferData() {
			return sBuff;
		}
		public long getTimestamp() {
			return timestamp;
		}
	
}
