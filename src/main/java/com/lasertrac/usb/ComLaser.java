package com.lasertrac.usb;

//import javax.xml.bind.DatatypeConverter;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class ComLaser {

	public static int boudrate = 9600;
	//public static String comPortCamera="";
	static SerialPort comPort;
	public static boolean connectLaser(SerialPort port) {
		
		try {
			comPort = port;
			comPort.setParity(SerialPort.NO_PARITY);
			comPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
			comPort.setNumDataBits(8);
			comPort.setBaudRate(boudrate); 
			comPort.openPort();
			
			//System.out.println();
			//System.out.println("port desc = "+comPort.getDescriptivePortName());
			//System.out.println("port desc = "+comPort.getPortDescription());
			
			comPort.addDataListener(new SerialPortDataListener() {
			   @Override
			   public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
			   @Override
			   public void serialEvent(SerialPortEvent event)
			   {
			      if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
			         return;
			      byte[] newData = new byte[comPort.bytesAvailable()];
			      int numRead = comPort.readBytes(newData, newData.length);
			      System.out.println("Read " + numRead + " bytes.");
			      //System.out.println(DatatypeConverter.printHexBinary(newData));
			   }
			});
			return true;
		}catch(Exception ex) {
			return false;
		}
		
	}
}
