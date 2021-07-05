package com.lasertrac.usb;

import java.nio.ByteBuffer;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.lasertrac.common.CommonFunctions;
import com.lasertrac.common.LogWriter;



public class ComLaser1 implements LaserInterface{

	private SerialPort comPort = null;
	private int boudrate = 9600;
	private boolean laserOn = false;
	
	private int laserSpeed=0;
    //public string laserSpeedNull = null;
    private String laserSpeedUnit;
    private double laserRange=0;
    private String laserRangeUnit;
    
	@Override
	public boolean connectLaser(SerialPort port) {
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
			      SP_DataRecieved(newData);
			      //System.out.println(DatatypeConverter.printHexBinary(newData));
			   }
			});
			return true;
		}catch(Exception ex) {
			return false;
		}
		
	}

	@Override
	public void setBoudRate(int boudrate) {
		this.boudrate = boudrate;
	}

	@Override
	public boolean startLaser() {
		if(comPort!=null) {
			System.out.println("laser com port null");
			return false;
		}
		if(comPort.isOpen()) {
			byte[] arr = new byte[5];
	        arr[0] = CommonFunctions.hexToByte("02");
	        arr[1] = CommonFunctions.hexToByte("01");
	        arr[2] = CommonFunctions.hexToByte("99");
	        arr[3] = CommonFunctions.hexToByte("9A");
	        arr[4] = CommonFunctions.hexToByte("03");
	        
			comPort.writeBytes(arr, 5);
			laserOn = true;	
		}else {
			laserOn = false;
		}
		return laserOn;
	}

	@Override
	public boolean stopLaser() {
		if(comPort!=null) {
			System.out.println("laser com port null");
			return false;
		}
		if(comPort.isOpen()) {
			byte[] arr = new byte[5];
	        arr[0] = CommonFunctions.hexToByte("02");
	        arr[1] = CommonFunctions.hexToByte("01");
	        arr[2] = CommonFunctions.hexToByte("9A");
	        arr[3] = CommonFunctions.hexToByte("9B");
	        arr[4] = CommonFunctions.hexToByte("03");
            
			comPort.writeBytes(arr, 5);
			laserOn = false;	
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean isLaserOn() {
		return laserOn;
	}

	@Override
	public int getLaserSpeed() {
		return laserSpeed;
	}

	@Override
	public String getLaserSpeedUnit() {
		return laserSpeedUnit;
	}

	@Override
	public double getDistance() {
		return laserRange;
	}

	@Override
	public String getDistanceUnit() {
		return laserRangeUnit;
	}
	byte[] bytesReceived;
    private  void SP_DataRecieved(byte[] newData)
    {
       //bytesReceived = newData;
       
//        SerialPort portLaserHandle = (SerialPort)sender;
//        int bytesToRead = portLaserHandle.BytesToRead;
//        byte[] data = new byte[bytesToRead];
//        portLaserHandle.Read(data, 0, bytesToRead);
    	byte[] data = newData;
        if (newData.length > 0)
        {
            if (bytesReceived == null)
            {
                //if (data[0].ToString("X2") == "02")
                if (CommonFunctions.byteToString(data[0]) .equals("02") )
                {
                    bytesReceived = new byte[newData.length];
                    bytesReceived = data;
                }
                else
                {
                    int index = 0;
                    int dataIndex = 0;
                    while (true)
                    {
                        if (dataIndex <= 0)
                        {
                            //if (data[index].ToString("X2") == "02")
                            if (CommonFunctions.byteToString(data[index]).equals("02"))
                            {
                                bytesReceived = new byte[data.length - index];
                                bytesReceived[dataIndex] = data[index];
                                dataIndex += 1;
                            }
                        }
                        else if (dataIndex > 0)
                        {
                            bytesReceived[dataIndex] = data[index];
                            dataIndex += 1;
                        }
                        index += 1;
                        if (index == data.length)
                        {
                            break;
                        }
                    }
                }

                //bytesReceived = null;
            }
            else
            {
                //byte[] t1 = new Byte[bytesToRead + bytesReceived.Length];
//                IEnumerable<byte> rv = bytesReceived.Concat(data);
//                List<byte> list1 = new List<byte>(bytesReceived);
//                List<byte> list2 = new List<byte>(data);
//                list1.AddRange(list2);
//                bytesReceived = list1.ToArray();
                //ArrayList<Byte> myList = new ArrayList<Byte>(Arrays.asList(bytesReceived));
                ByteBuffer final_array = ByteBuffer.wrap(bytesReceived);
                final_array.put(newData);
                bytesReceived=final_array.array();
            }
        }

        int count = 0;
        if (bytesReceived != null)
        {
            boolean dataComplete = false;
            //Debug.WriteLine("raw="+ByteArrayToString(bytesReceived));
            //richTextBox2.AppendText("raw=" + ByteArrayToString(bytesReceived));

            LogWriter.appendLogLaser(" raw data=" + CommonFunctions.byteArrayToString(bytesReceived));
            while (dataComplete == false)
            {

//                if (count != 0)
//                {
//                    bytesToRead = portLaserHandle.BytesToRead;
//                    data = new byte[bytesToRead];
//                    portLaserHandle.Read(data, 0, bytesToRead);
//                    if (bytesToRead > 0)
//                    {
//                        if (bytesReceived == null)
//                        {
//                            bytesReceived = new Byte[bytesToRead];
//                            bytesReceived = data;
//                            //bytesReceived = null;
//                        }
//                        else
//                        {
//                            byte[] t1 = new Byte[bytesToRead + bytesReceived.Length];
//                            IEnumerable<byte> rv = bytesReceived.Concat(data);
//                            List<byte> list1 = new List<byte>(bytesReceived);
//                            List<byte> list2 = new List<byte>(data);
//                            list1.AddRange(list2);
//                            //rv.ToArray();
//                            bytesReceived = list1.ToArray();
//                        }
//                    }
//
//                }
                //count += 1;
                int dataLen = 0;
                int increment = 0;
                int datalen_pos = 1;
                int extraLen = 1;
                for (int i = 0; i < bytesReceived.length; i++)
                {
                    if (i == datalen_pos)
                    {
                        String hexTemp = CommonFunctions.byteToString(bytesReceived[i]); //bytesReceived[i].ToString("X2");
                        if (hexTemp == "10")
                        {
                            datalen_pos += 1;
                        }
                        else
                        {
                            dataLen = 0;
                            increment = 0;
                            extraLen = 1;
                            //Debug.WriteLine("my hex for data len=" + bytesReceived[i].ToString("X2"));

                            dataLen = Integer.parseInt(hexTemp, 16);
                            //Debug.WriteLine("data len=" + dataLen);
                            if (bytesReceived.length >= datalen_pos + dataLen + 1)
                            {
                                for (int j = datalen_pos + 2; j <= datalen_pos + dataLen + (increment); j++)
                                {
                                    String t1 = CommonFunctions.byteToString(bytesReceived[j]);// bytesReceived[j].ToString("X2");
                                    if (t1 == "10")
                                    {
                                        if (increment > 0)
                                        {
                                            //if (bytesReceived[j - 1].ToString("X2") != "10")
                                            if(CommonFunctions.byteToString(bytesReceived[j-1]).equals("10")==false)	
                                            {
                                                increment += 1;
                                            }
                                        }
                                        else
                                        {
                                            increment += 1;
                                        }
                                    }
                                    if ((datalen_pos + dataLen + increment) >= bytesReceived.length)
                                    {
                                        dataComplete = true;
                                        break;
                                    }
                                }
                            }
                            else
                            {
                                dataComplete = true;
                                break;
                            }
                        }

                    }
                    else
                    if (i >= dataLen + increment + datalen_pos + 1 && dataLen > 0)
                    {
                        //if (bytesReceived[i].ToString("X2") == "10")
                        if(CommonFunctions.byteToString(bytesReceived[i]).equals("10"))	
                        {
                            if (extraLen > 1)
                            {
                                //if (bytesReceived[i - 1].ToString("X2") != "10")
                                if(CommonFunctions.byteToString(bytesReceived[i-1]).equals("10")==false)
                                {
                                    extraLen += 1;
                                }
                            }
                            else
                            {
                                extraLen += 1;
                            }
                        }
                    }

                    //if (bytesReceived[i].ToString("X2") == "03")
                    if(CommonFunctions.byteToString(bytesReceived[i]).equals("03"))	
                    {
                        if (bytesReceived.length > i + 1)
                        {
                            //if (bytesReceived[i + 1].ToString("X2") == "02")
                            if(CommonFunctions.byteToString(bytesReceived[i+1]).equals("02") )
                            {
                                byte[] t2 = new byte[bytesReceived.length - (i + 1)];
                                int myCount = i + 1;
                                for (int j = 0; j < t2.length; j++)
                                {
                                    t2[j] = bytesReceived[myCount];
                                    myCount += 1;
                                }
                                //call extract method
                                //Debug.WriteLine("unrecognized data =" + ByteArrayToString(bytesReceived.Take(i + 1).ToArray()));
                                System.out.println("unrecognized data =" + CommonFunctions.byteArrayToString( CommonFunctions.splitArray(bytesReceived, 0, i+1)) );
                                
                                //decodeData(bytesReceived.Take(i + 1).ToArray());
                                decodeData(CommonFunctions.splitArray(bytesReceived, 0, i+1));
                                if (t2.length > 0)
                                {
                                    bytesReceived = t2;
                                }
                                else
                                {
                                    bytesReceived = null;
                                    dataComplete = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (i == dataLen + increment + extraLen + datalen_pos + 1)
                    {
                        String t1 = CommonFunctions.byteToString(bytesReceived[i]);// bytesReceived[i].ToString("X2");
                        //Debug.WriteLine("t1=" + t1 + ", " + ByteArrayToString(bytesReceived));
                        if (t1 == "03")
                        {
                            int checkSum = bytesReceived[1] + bytesReceived[2];
                            byte checkSumReceived;
                            for (int j = 3; j < dataLen + increment + 2; j++)
                            {
                                //Debug.WriteLine("--" + bytesReceived[j].ToString("X2"));
                                checkSum += bytesReceived[j];
                                //if (bytesReceived[j].ToString("X2") == "02" || bytesReceived[j].ToString("X2") == "03")
                                if (CommonFunctions.byteToString(bytesReceived[j]).equals("02")  || CommonFunctions.byteToString(bytesReceived[j]).equals("03"))
                                {
                                    j += 1;
                                }
                            }

                            checkSumReceived = bytesReceived[2 + dataLen + increment];
                            checkSum = checkSum & 0xff;
                            String hexTemp = CommonFunctions.byteToString(checkSumReceived); //checkSumReceived.ToString("X2");
                            int checkSumInt = Integer.parseInt(hexTemp, 16);
                            //Debug.WriteLine(checkSum + "  ," + checkSumInt);

                            if (bytesReceived.length > i + 1)
                            {

                                byte[] t2 = new byte[bytesReceived.length - (i + 1)];
                                int myCount = i + 1;
                                for (int j = 0; j < t2.length; j++)
                                {
                                    t2[j] = bytesReceived[myCount];
                                    myCount += 1;
                                }
                                //call extract method
                                System.out.println(CommonFunctions.byteArrayToString(CommonFunctions.splitArray(bytesReceived, 0,i+1)));
                                //decodeData(bytesReceived.Take(i + 1).ToArray());
                                decodeData(CommonFunctions.splitArray(bytesReceived, 0,i+1));
                                bytesReceived = t2;
                            }
                            else
                            {
                                //call extract method
                                System.out.println(CommonFunctions.byteArrayToString(bytesReceived));
                                
                                decodeData(bytesReceived);
                                bytesReceived = null;
                                dataComplete = true;
                                break;
                            }
                            //break;
                        }
                    }
                    else if (i == bytesReceived.length - 1)
                    {
                        dataComplete = true;
                    }

                }
                //if (bytesReceived.Length == 0) {
                //    dataComplete = true;
                //    bytesReceived = null;
                //}

            }

        }
        //Debug.WriteLine(ByteArrayToString(bytesReceived));

        System.out.println("exit");

    }

    private void decodeData(byte[] dataArr)
    {
        int dataLen = 0;
        int increment = 0;
        //richTextBox1.AppendText("sample= " + ByteArrayToString(bytesReceived) + Environment.NewLine);
        LogWriter.appendLogLaser(" decode data=" + CommonFunctions.byteArrayToString(dataArr));
        
        int datalen_pos = 1;
        for (int i = 0; i < dataArr.length; i++)
        {
            if (i == datalen_pos)
            {
                //Debug.WriteLine("my hex for data len=" + bytesReceived[i].ToString("X2"));
                String hexTemp = CommonFunctions.byteToString(bytesReceived[i]); //bytesReceived[i].ToString("X2");
                if (hexTemp == "10")
                {
                    datalen_pos += 1;
                }
                else
                {
                    dataLen = Integer.parseInt(hexTemp, 16);
                    //Debug.WriteLine("data len=" + dataLen);

                    //for (int j = 0; j < dataLen + 1; j++)
                    //{
                    //    string t1 = bytesReceived[i].ToString("X2");
                    //    if (t1 == "02" || t1 == "03")
                    //    {
                    //        increment += 1;
                    //    }
                    //}
                    for (int j = 1; j <= dataLen + (increment); j++)
                    {
                        String t1 = CommonFunctions.byteToString(bytesReceived[j]); // bytesReceived[j].ToString("X2");
                        if (t1 == "10")
                        {
                            if (increment > 0)
                            {
                                //if (bytesReceived[j - 1].ToString("X2") != "10")
                                if ( CommonFunctions.byteToString(bytesReceived[j - 1]).equals("10")==false)
                                {
                                    increment += 1;
                                }
                            }
                            else
                            {
                                increment += 1;
                            }

                        }
                        if ((dataLen + increment) >= bytesReceived.length)
                        {
                            break;
                        }
                    }
                    break;
                }
            }

        }
        //if (dataArr[2] == 0x80) {

        //        Debug.WriteLine("80 data test=" + ByteArrayToString(dataArr));

        //        int index = 5;
        //        index = getIndex(dataArr, index);
        //        int v1 = int.Parse(dataArr[index].ToString("X2"), System.Globalization.NumberStyles.HexNumber);

        //        index += 1;
        //        index = getIndex(dataArr, index);

        //        int v2 = int.Parse(dataArr[index].ToString("X2"), System.Globalization.NumberStyles.HexNumber);
        //        double speed = ((v1 * 256) + (v2)) * 0.01;

        //        index += 1;
        //        index = getIndex(dataArr, index);

        //        int d1 = int.Parse(dataArr[index].ToString("X2"), System.Globalization.NumberStyles.HexNumber);


        //        index += 1;
        //        index = getIndex(dataArr, index);

        //        int d2 = int.Parse(dataArr[index].ToString("X2"), System.Globalization.NumberStyles.HexNumber);

        //        double range = ((d1 * 256) + (d2)) * 0.1;

        //        index += 1;
        //        index = getIndex(dataArr, index);


        //        if (dataArr[index].ToString("X2") == "00")
        //        {
        //            // leaving vehicle
        //            //richTextBox1.AppendText(ByteArrayToString(bytesReceived));
        //            //richTextBox1.AppendText("   v1=" + v1 + "  v2=" + v2 + Environment.NewLine);
        //            //richTextBox1.AppendText("-" + speed + " km/h, " + range + " m" + Environment.NewLine);
        //            laserSpeed = -((int)speed);
        //            laserSpeedUnit = "km/h";
        //            laserRange = range;
        //            laserRangeUnit = "m";
        //        }
        //        else
        //        {
        //            // closing vehicle
        //            //richTextBox1.AppendText(ByteArrayToString(bytesReceived));
        //            //richTextBox1.AppendText("   v1="+v1+"  v2="+v2 + Environment.NewLine);
        //            //richTextBox1.AppendText("+" + speed + " km/h, " + range + " m" + Environment.NewLine);
        //            laserSpeed = (int)speed;
        //            laserSpeedUnit = "km/h";
        //            laserRange = range;
        //            laserRangeUnit = "m";
        //        }
        //}

        if (dataArr[2] == 0xDC || dataArr[2] == 0xDB)
        {
            System.out.println("DC and DB" + CommonFunctions.byteArrayToString(dataArr));
            
            int index = 5;
            index = getIndex(dataArr, index);
            //int v1 = int.Parse(dataArr[index].ToString("X2"), System.Globalization.NumberStyles.HexNumber);
            int v1 = Integer.parseInt(CommonFunctions.byteToString(dataArr[index]) , 16);
            
            index += 1;
            index = getIndex(dataArr, index);

            //int v2 = int.Parse(dataArr[index].ToString("X2"), System.Globalization.NumberStyles.HexNumber);
            int v2 = Integer.parseInt(CommonFunctions.byteToString(dataArr[index]) , 16);
            
            double speed = ((v1 * 256) + (v2)) * 0.01;

            index += 1;
            index = getIndex(dataArr, index);

            //int d1 = int.Parse(dataArr[index].ToString("X2"), System.Globalization.NumberStyles.HexNumber);
            int d1 = Integer.parseInt(CommonFunctions.byteToString(dataArr[index]) , 16);

            index += 1;
            index = getIndex(dataArr, index);

            //int d2 = int.Parse(dataArr[index].ToString("X2"), System.Globalization.NumberStyles.HexNumber);
            int d2 = Integer.parseInt(CommonFunctions.byteToString(dataArr[index]) , 16);

            double range = ((d1 * 256) + (d2)) * 0.1;

            index += 1;
            index = getIndex(dataArr, index);


            //if (dataArr[index].ToString("X2") == "00")
            if (CommonFunctions.byteToString(dataArr[index]) .equals("00") )
            {
                // leaving vehicle
                //richTextBox1.AppendText(ByteArrayToString(bytesReceived));
                //richTextBox1.AppendText("   v1=" + v1 + "  v2=" + v2 + Environment.NewLine);
                //richTextBox1.AppendText("-" + speed + " km/h, " + range + " m" + Environment.NewLine);
                laserSpeed = -((int)speed);
                laserSpeedUnit = "km/h";
                laserRange = range;
                laserRangeUnit = "m";
            }
            else
            {
                // closing vehicle
                //richTextBox1.AppendText(ByteArrayToString(bytesReceived));
                //richTextBox1.AppendText("   v1="+v1+"  v2="+v2 + Environment.NewLine);
                //richTextBox1.AppendText("+" + speed + " km/h, " + range + " m" + Environment.NewLine);
                laserSpeed = (int)speed;
                laserSpeedUnit = "km/h";
                laserRange = range;
                laserRangeUnit = "m";
            }

        }
        else
        if (dataArr[2] == 0x51)
        {
            System.out.println("Speed Data Km/h=" + CommonFunctions.byteArrayToString(dataArr));


            int index = 3;
            index = getIndex(dataArr, index);
            //int v1 = int.Parse(dataArr[index].ToString("X2"), System.Globalization.NumberStyles.HexNumber);
            int v1 = Integer.parseInt(CommonFunctions.byteToString(dataArr[index]), 16);

            index += 1;
            index = getIndex(dataArr, index);

            //int v2 = int.Parse(dataArr[index].ToString("X2"), System.Globalization.NumberStyles.HexNumber);
            int v2 = Integer.parseInt(CommonFunctions.byteToString(dataArr[index]) , 16);
            double speed = ((v1 * 256) + (v2)) * 0.01;

            index += 1;
            index = getIndex(dataArr, index);

            //int d1 = int.Parse(dataArr[index].ToString("X2"), System.Globalization.NumberStyles.HexNumber);
            int d1 = Integer.parseInt(CommonFunctions.byteToString(dataArr[index]) , 16);

            index += 1;
            index = getIndex(dataArr, index);

            //int d2 = int.Parse(dataArr[index].ToString("X2"), System.Globalization.NumberStyles.HexNumber);
            int d2 = Integer.parseInt(CommonFunctions.byteToString(dataArr[index]), 16);

            double range = ((d1 * 256) + (d2)) * 0.1;

            index += 1;
            index = getIndex(dataArr, index);


            //if (dataArr[index].ToString("X2") == "00")
            if (CommonFunctions.byteToString(dataArr[index]).equals("00"))
            {
                // leaving vehicle
                //richTextBox1.AppendText(ByteArrayToString(bytesReceived));
                //richTextBox1.AppendText("-" + speed + " km/h, " + range + " m" + Environment.NewLine);
                laserSpeed = -((int)speed);
                laserSpeedUnit = "km/h";
                laserRange = range;
                laserRangeUnit = "m";
            }
            else
            {
                // closing vehicle
                //richTextBox1.AppendText(ByteArrayToString(bytesReceived));
                //richTextBox1.AppendText("+" + speed + " km/h, " + range + " m" + Environment.NewLine);
                laserSpeed = (int)speed;
                laserSpeedUnit = "km/h";
                laserRange = range;
                laserRangeUnit = "m";
            }
        }
        //else if (dataArr[2].ToString("X2") == "7C")
        else if (CommonFunctions.byteToString(dataArr[2]).toUpperCase().equals("7C"))
        {
            System.out.println("Distance Data=" + CommonFunctions.byteArrayToString(dataArr));

            int index = 3;
            index = getIndex(dataArr, index);
            int v1 = Integer.parseInt(CommonFunctions.byteToString(dataArr[index]) , 16);

            index += 1;
            index = getIndex(dataArr, index);

            int v2 = Integer.parseInt(CommonFunctions.byteToString(dataArr[index]), 16);

            index += 1;
            index = getIndex(dataArr, index);

            int v3 = Integer.parseInt(CommonFunctions.byteToString(dataArr[index]), 16);

            double distance = ((v1 * 65536) + (v2 * 256) + v3) * 0.01;

            //richTextBox1.AppendText(ByteArrayToString(bytesReceived));
            //richTextBox1.AppendText(distance + " m" + Environment.NewLine);
            laserSpeed = 0;
            laserSpeedUnit = "km/h";
            laserRange = distance;
            laserRangeUnit = "m";

        }
        //else if (dataArr[2].ToString("X2") == "55")
        else if ( CommonFunctions.byteToString(dataArr[2]).equals("55"))
        {
            System.out.println("Speed Data Miles/h=" + CommonFunctions.byteArrayToString(dataArr));

            int index = 3;
            if (CommonFunctions.byteToString(dataArr[index]) .equals("10"))
            {
                index += 1;
            }
            int v1 = Integer.parseInt(CommonFunctions.byteToString(dataArr[index]), 16);
            index += 1;

            index = getIndex(dataArr, index);
            int v2 = Integer.parseInt(CommonFunctions.byteToString(dataArr[index]), 16);
            double speed = ((v1 * 256) + (v2)) * 0.01;

            index += 1;
            index = getIndex(dataArr, index);

            int d1 = Integer.parseInt(CommonFunctions.byteToString(dataArr[index]) , 16);

            index += 1;
            index = getIndex(dataArr, index);
            int d2 = Integer.parseInt(CommonFunctions.byteToString(dataArr[index]), 16);

            double range = ((d1 * 256) + (d2));

            index += 1;
            index = getIndex(dataArr, index);


            //if (dataArr[index].ToString("X2") == "00")
            if (CommonFunctions.byteToString(dataArr[index]).equals("00"))
            {
                // leaving vehicle
                //richTextBox1.AppendText(ByteArrayToString(bytesReceived));
                //richTextBox1.AppendText("-" + speed + " miles/h, " + range + " ft" + Environment.NewLine);
                laserSpeed = -((int)speed);
                laserSpeedUnit = "km/h";
                laserRange = range;
                laserRangeUnit = "m";
            }
            else
            {
                // closing vehicle
                //richTextBox1.AppendText(ByteArrayToString(bytesReceived));
                //richTextBox1.AppendText("+" + speed + " miles/h, " + range + " ft" + Environment.NewLine);
                laserSpeed = (int)speed;
                laserSpeedUnit = "miles/h";
                laserRange = range;
                laserRangeUnit = "ft";
            }

        }
        else
        {

        }

    }
    private int getIndex(byte[] dataArr, int index)
    {
        //if (dataArr[index].ToString("X2") == "02" || dataArr[index].ToString("X2") == "03")
        //if (dataArr[index].ToString("X2") == "10")
        if ( CommonFunctions.byteToString(dataArr[index]).equals("10"))
        {
            index += 1;
        }
        return index;
    }

}
