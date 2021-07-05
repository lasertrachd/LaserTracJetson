package com.lasertrac.usb;

import com.lasertrac.common.*;

//import javax.xml.bind.DatatypeConverter;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class CameraWonwoo implements CameraInterface{

	int boudrate = 9600;
	//public static String comPortCamera="";
	SerialPort comPort=null;
	
	
	public static final  byte Cam_WB_auto = CommonFunctions.hexToByte("00");
    public static final byte Cam_WB_indoor = CommonFunctions.hexToByte("01");
    public static final byte Cam_WB_outdoor = CommonFunctions.hexToByte("02");
    public static final byte Cam_WB_onePushWB = CommonFunctions.hexToByte("03");
    public static final byte Cam_WB_ATW = CommonFunctions.hexToByte("04");
    public static final byte Cam_WB_manual = CommonFunctions.hexToByte("05");
    public static final byte Cam_WB_onePushTrigger = CommonFunctions.hexToByte("05");
    public static final byte Cam_WB_outdoorAuto = CommonFunctions.hexToByte("06");
    public static final byte Cam_WB_sodiumLampAuto = CommonFunctions.hexToByte("07");
    public static final byte Cam_WB_sodiumLamp = CommonFunctions.hexToByte("08");
    public static final byte Cam_WB_sodiumLampOutdoorAuto = CommonFunctions.hexToByte("09");
    
    
    public static class CameraModes {
    	public static final byte presetSunnyModeAddress = 0x00;
        public static final byte presetCloudModeAddress = 0x01;
        public static final byte presetNightColorModeAddress = 0x02;
        public static final byte presetNightIRModeAddress = 0x03;
        public static final byte presetDuskModeAddress = 0x04;
    }
    
	@Override
	public void setBoudrate(int boud_rate) {
		this.boudrate = boud_rate;
	}
	
	@Override
	public  boolean connectCamera(SerialPort port) {
		
		try {
			System.out.println("aaye t = "+port.getDescriptivePortName());
			comPort = port;
			comPort.setParity(SerialPort.NO_PARITY);
			comPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
			comPort.setNumDataBits(8);
			comPort.setBaudRate(boudrate); 
			return comPort.openPort();
			
			//System.out.println();
			//System.out.println("port desc = "+comPort.getDescriptivePortName());
			//System.out.println("port desc = "+comPort.getPortDescription());
 				
//			comPort.addDataListener(new SerialPortDataListener() {
//			   @Override
//			   public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
//			   @Override
//			   public void serialEvent(SerialPortEvent event)
//			   {
//			      if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
//			         return;
//			      byte[] newData = new byte[comPort.bytesAvailable()];
//			      int numRead = comPort.readBytes(newData, newData.length);
//			      //System.out.println("Read " + numRead + " bytes.");
//			      //System.out.println(DatatypeConverter.printHexBinary(newData));
//			   }
//			});
			//return true;
		}catch(Exception ex) {
			return false;
		}
	}
	
	@Override
	public void startZoomIn() {
		byte[] arr = new byte[6];
        arr[0] = CommonFunctions.hexToByte("81");
        arr[1] = CommonFunctions.hexToByte("01");
        arr[2] = CommonFunctions.hexToByte("04");
        arr[3] = CommonFunctions.hexToByte("07");
        arr[4] = CommonFunctions.hexToByte("27");
        arr[5] = CommonFunctions.hexToByte("FF");
		
		comPort.writeBytes(arr, 6);
	}
	
	@Override
	public  void stopZoom() {
		byte[] arr = new byte[6];
        arr[0] = CommonFunctions.hexToByte("81");
        arr[1] = CommonFunctions.hexToByte("01");
        arr[2] = CommonFunctions.hexToByte("04");
        arr[3] = CommonFunctions.hexToByte("07");
        arr[4] = CommonFunctions.hexToByte("00");
        arr[5] = CommonFunctions.hexToByte("FF");
		
		comPort.writeBytes(arr, 6);
	}
	
	@Override
	public  void startZoomOut() {
		byte[] arr = new byte[6];
        arr[0] = CommonFunctions.hexToByte("81");
        arr[1] = CommonFunctions.hexToByte("01");
        arr[2] = CommonFunctions.hexToByte("04");
        arr[3] = CommonFunctions.hexToByte("07");
        arr[4] = CommonFunctions.hexToByte("37");
        arr[5] = CommonFunctions.hexToByte("FF");
        comPort.writeBytes(arr, 6);
	}
	
	@Override
	public boolean zoomSeperateMode() {
        if (comPort == null )
        {
            return false;
        }
        //comPort.DiscardInBuffer();

        byte[] arr = new byte[6];
        arr[0] = CommonFunctions.hexToByte("81");
        arr[1] = CommonFunctions.hexToByte("01");
        arr[2] = CommonFunctions.hexToByte("04");
        arr[3] = CommonFunctions.hexToByte("36");
        arr[4] = CommonFunctions.hexToByte("01");
        arr[5] = CommonFunctions.hexToByte("FF");
        
        comPort.writeBytes(arr, 6);

        if (getCommandresponse()==false)
        {
            return false;
        }
         
        return true;
    }
	
	@Override
	public boolean setDizitalZoomOn()
    {
        if (comPort == null)
        {
            //errorMessage = "Camera Not connected";
            return false;
        }
        //comPort.DiscardInBuffer( );
        
        byte[] arr = new byte[6];
        arr[0] = CommonFunctions.hexToByte("81");
        arr[1] = CommonFunctions.hexToByte("01");
        arr[2] = CommonFunctions.hexToByte("04");
        arr[3] = CommonFunctions.hexToByte("06");
        arr[4] = CommonFunctions.hexToByte("02");
        arr[5] = CommonFunctions.hexToByte("FF");
        //Console.WriteLine("commndzoom=" + BitConverter.ToString(arr));
        comPort.writeBytes(arr, 6);

        if (getCommandresponse())
        {
            return true;
        }
        else {
            return false;
        }
            
    }
	
	@Override
	public boolean setAutoFocus(boolean value)
    {
        if (comPort == null)
        {
            return false;
        }

        //comPort.DiscardInBuffer();

        byte[] arr = new byte[6];
        arr[0] = CommonFunctions.hexToByte("81") ;
        arr[1] = CommonFunctions.hexToByte("01");
        arr[2] = CommonFunctions.hexToByte("04");
        arr[3] = CommonFunctions.hexToByte("38");
        if (value)
        {
            arr[4] = CommonFunctions.hexToByte("02");
        }
        else
        {
            arr[4] = CommonFunctions.hexToByte("03");
        }
        arr[5] = CommonFunctions.hexToByte("FF");

        comPort.writeBytes(arr,  6);
        
            if (getCommandresponse())
            {
//                if (value)
//                {
//                    isFocusManual = false;
//                }
//                else
//                {
//                    isFocusManual = true;
//                }
                return true;
            }
            else
            {
                return false;

            }
        
    }
	
	@Override
	public boolean setManualFocusByValue(String hexValue)
    {
        //8x 01 04 48 0p 0q 0r 0s FF

        if (comPort == null)
        {
            return false;
        }
        int discarded;
        //byte[] byteArray = HexEncoding.GetBytes(hexValue, out discarded);

        //Console.WriteLine("discarded chars:" + discarded);
        byte[] arr = new byte[9];
        arr[0] = CommonFunctions.hexToByte("81");
        arr[1] = CommonFunctions.hexToByte("01");
        arr[2] = CommonFunctions.hexToByte("04");
        arr[3] = CommonFunctions.hexToByte("48");
        
        
        arr[4] = CommonFunctions.hexToByte(hexValue.substring(0, 2));//byteArray[0];//hexValue.Substring(0, 1).ToCharArray()[0];//Convert.ToByte(hexValue.Substring(0,1));
        arr[5] = CommonFunctions.hexToByte(hexValue.substring(2, 4));//byteArray[1]; //Convert.ToByte(hexValue.Substring(1, 1));
        arr[6] = CommonFunctions.hexToByte(hexValue.substring(4, 6));//byteArray[2]; //Convert.ToByte(hexValue.Substring(2, 1));
        arr[7] = CommonFunctions.hexToByte(hexValue.substring(6, 8));//byteArray[3]; //Convert.ToByte(hexValue.Substring(3, 1));
        arr[8] = CommonFunctions.hexToByte("FF");

        //8101040724FF
//        String str = "";
//        for (int i = 0; i < arr.length; i++)
//        {
//            str = str + arr[i].ToString("X");
//        }
//        Debug.WriteLine("cmd:" + str);

        comPort.writeBytes(arr, 9);
        if (getCommandresponsePresets())
        {
            return true;
        }
        else
        {
            return false;
        }
//        if (readGeneralCommandResponse(BitConverter.ToString(arr).Replace("-", string.Empty)))
//        {
//            return true;
//        }
//        else
//        {
//            return false;
//        }

    }
	
	@Override
	public boolean setDizitalZoomByValue(String hexValue)
    {
        //8x 01 04 46 00 00 0p 0q FF
        //81 01 04 46 00 00 00 01 FF

        if (comPort == null)
        {
            return false;
        }
        //byte[] value = StrToByteArray(hexValue);// StringToByteArray(hexValue);
        int discarded;
//        hexValue = "0" + hexValue.ToCharArray()[0]+"0"+ hexValue.ToCharArray()[1] ;
//        byte[] byteArray = HexEncoding.GetBytes(hexValue, out discarded);
//        Debug.WriteLine("dzooom by value="+BitConverter.ToString(byteArray));
        //Console.WriteLine("discarded chars:" + discarded);
        byte[] arr = new byte[9];
        arr[0] = CommonFunctions.hexToByte("81");
        arr[1] = CommonFunctions.hexToByte("01");
        arr[2] = CommonFunctions.hexToByte("04");
        arr[3] = CommonFunctions.hexToByte("46");
        arr[4] = CommonFunctions.hexToByte("00");//hexValue.Substring(0, 1).ToCharArray()[0];//Convert.ToByte(hexValue.Substring(0,1));
        arr[5] = CommonFunctions.hexToByte("00");// byteArray[1]; //Convert.ToByte(hexValue.Substring(1, 1));
        arr[6] = CommonFunctions.hexToByte(hexValue.substring(0,2));// byteArray[0]; //Convert.ToByte(hexValue.Substring(2, 1));
        arr[7] = CommonFunctions.hexToByte(hexValue.substring(2,4));//byteArray[1]; //Convert.ToByte(hexValue.Substring(3, 1));
        arr[8] = CommonFunctions.hexToByte("FF");

        //8101040724FF
//        string str = "";
//        for (int i = 0; i < arr.Length; i++)
//        {
//            str = str + arr[i].ToString("X");
//        }
//        Console.WriteLine("cmd:" + str);

        comPort.writeBytes(arr,  9);

        //if (readGeneralCommandResponse(BitConverter.ToString(arr).Replace("-", string.Empty)))
        //{
            return true;
        //}
        //else
        //{
        //    return false;
        //}
    }

	@Override
	public boolean callPresetValue(byte hexMemoryNumber)
    {
        if (comPort == null)
        {
            return false;
        }
        //comPort.removeDataListener();

        //comPort.DiscardInBuffer();
        //8x 01 04 3F 00 0p FF
        byte[] arr = new byte[7];

        arr[0] = CommonFunctions.hexToByte("81");
        arr[1] = CommonFunctions.hexToByte("01");
        arr[2] = CommonFunctions.hexToByte("04");
        arr[3] = CommonFunctions.hexToByte("3F");
        arr[4] = CommonFunctions.hexToByte("02");
        arr[5] = hexMemoryNumber;
        arr[6] = CommonFunctions.hexToByte("FF");
        comPort.writeBytes(arr, 7);
        
        if (getCommandresponsePresets())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
	
	@Override
	public boolean setCamWB(byte value) {
        if (comPort == null)
        {
            return false;
        }
        //portCamHandle.DiscardInBuffer();
        
        byte[] arr = new byte[6];
        arr[0] = CommonFunctions.hexToByte("81");
        arr[1] = CommonFunctions.hexToByte("01");
        arr[2] = CommonFunctions.hexToByte("04");
        if (value != Cam_WB_onePushTrigger)
        {
            arr[3] = 0x35;
        }
        else {
            arr[3] = 0x10;
        }
        arr[4] = value;
        arr[5] = CommonFunctions.hexToByte("FF");

        comPort.writeBytes(arr, 6);
        //Console.WriteLine("WB="+BitConverter.ToString(arr));
        if (getCommandresponse())
        {
            return true;
        }
        else { return false; }

    }
	
	@Override
	public boolean setDisplayOn() {
        if (comPort == null)
        {
            return true;
        }
        //portCamHandle.DiscardInBuffer();
        
        byte[] arr = new byte[6];
        arr[0] = CommonFunctions.hexToByte("81");
        arr[1] = CommonFunctions.hexToByte("01");
        arr[2] = CommonFunctions.hexToByte("04");
        arr[3] = CommonFunctions.hexToByte("15");
        arr[4] = CommonFunctions.hexToByte("02");
        arr[5] = CommonFunctions.hexToByte("FF");

        comPort.writeBytes(arr,  6);
        if (getCommandresponse())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
	
	@Override
	public boolean setZoomDisplayOn()
    {
        if (comPort == null)
        {
            //error = "Camera not connected";
            return true;
        }
        //portCamHandle.DiscardInBuffer();
        
        byte[] arr = new byte[7];
        arr[0] = CommonFunctions.hexToByte("81");
        arr[1] = CommonFunctions.hexToByte("01");
        arr[2] = CommonFunctions.hexToByte("04");
        arr[3] = CommonFunctions.hexToByte("14");
        arr[4] = CommonFunctions.hexToByte("00");

        arr[5] = CommonFunctions.hexToByte("04");
        arr[6] = CommonFunctions.hexToByte("FF");

        comPort.writeBytes(arr, 7);
        if (getCommandresponse())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
	
	
	//inner helper functions
	boolean getCommandresponsePresets()
    {

        //errorMessage = null;

        int retryCounter = 0;
//        byte[] byteArr = new byte[12];
//        int offset = 0;
        long startTime = System.currentTimeMillis();
        while (true) {
            
            int length = comPort.bytesAvailable();
            byte[] byteArr2;
            if (length > 0)
            {
                byteArr2 = new byte[length];
                try
                {
                    comPort.readBytes(byteArr2, length,0);
                }
                catch (Exception ex) { System.out.println("ErrorResponse Comamnd :" + ex.toString()); }

                //offset = offset + length;
                //if (offset >= 6)
                //{
                //    break;
                //}
                System.out.println("yo presets=" + CommonFunctions.byteArrayToString(byteArr2));
                if (CommonFunctions.byteArrayToString(byteArr2).contains("51") )
                {
                    return true;
                }
            }
            if ((System.currentTimeMillis() - startTime) > 8000) {
                break;
            }
        }
 
        return false;
    }
	boolean getCommandresponsePresets2() {
		
		return false;
	}

	public boolean getCommandresponse() {
        
        //errorMessage = null;

        int retryCounter = 0;
        byte[] byteArr = new byte[6];
        int offset = 0;

        while(retryCounter<10)
        {
        	try {
        		Thread.sleep(100);	
        	}catch(Exception ex) {
        		
        	}
            
            int length = comPort.bytesAvailable();
            
            if (length > 0)
            {
                try { 
                    comPort.readBytes(byteArr, length, offset);
                }
                catch (Exception ex) { System.out.println("ErrorResponse Comamnd :" + ex.toString()); }

                offset = offset + length;
                if (offset >= 6)
                {
                    break;
                }
                System.out.println("yo "+CommonFunctions.byteArrayToString(byteArr));
                if (CommonFunctions.byteArrayToString(byteArr).toUpperCase().contains("FF") && offset == 3)
                {
                    //errorMessage = null;
                    return true;
                }
            }

            retryCounter = retryCounter + 1;
            

        } 
        //Console.WriteLine("zoom seperate ,response:" + BitConverter.ToString(byteArr));
        String hexResponse = CommonFunctions.byteArrayToString(byteArr).replace("-", "");
        if (hexResponse.contains("904") || hexResponse.contains("905")) {
            //errorMessage = null;
            //Console.WriteLine("returning true good");
            return true;
        }
        String errorMessage = parseCommandReturnError(hexResponse);
        //System.out.println("yo error=" + errorMessage);
        LogWriter.appendLog(errorMessage);
        return false;
    }
    public String parseCommandReturnError(String hexResponse) {
        if (hexResponse.contains("906002"))
        {
            return "Accepted a Command which is not supported or a command lacking parameters.";
        }
        else if (hexResponse.contains("906003"))
        {
            return "There are two command currently being executed, and the command could not be accepted.";
        }
        else if (hexResponse.contains("9061") || hexResponse.contains("9062")) {
            return "could not execute the command in current mode.";
        }
        return "unknown error.";
    }
}
