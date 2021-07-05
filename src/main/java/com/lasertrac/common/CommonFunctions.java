package com.lasertrac.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class CommonFunctions {

	public static byte hexToByte(String hexString) {
	    int firstDigit = toDigit(hexString.charAt(0));
	    int secondDigit = toDigit(hexString.charAt(1));
	    return (byte) ((firstDigit << 4) + secondDigit);
	}
	private static  int toDigit(char hexChar) {
	    int digit = Character.digit(hexChar, 16);
	    if(digit == -1) {
	        throw new IllegalArgumentException(
	          "Invalid Hexadecimal Character: "+ hexChar);
	    }
	    return digit;
	}
	public static String byteArrayToString(byte[] a) {
	  	   StringBuilder sb = new StringBuilder(a.length * 2);
	  	   for(byte b: a)
	  	      sb.append(String.format("%02x", b));
	  	   return sb.toString();
	  	}
		
		public static byte[] splitArray(byte[] arr,int indexStart, int length) {
			return Arrays.copyOfRange(arr, indexStart, length);
		}
		
		public static String byteToString(byte b) {
			return String.format("%02x", b);
		}
		
		public static  void copy(File src, File dest) throws IOException { 
			InputStream is = null; 
			OutputStream os = null; 
			try { 
				is = new FileInputStream(src); 
				os = new FileOutputStream(dest); 
				// buffer size 1K 
				byte[] buf = new byte[1024]; 
				int bytesRead; 
				while ((bytesRead = is.read(buf)) > 0) { 
					os.write(buf, 0, bytesRead); 
					} 
			} finally { is.close(); os.close(); } 
		}
		
		public static String padLeft(String inputString, int maxLength, String padChar) {
			int length = maxLength - inputString.length();
			String str="";
			for(int i=0;i<=0;i++) {
				str+="0";
			}
			str+=inputString;
			return str;
		}
}
