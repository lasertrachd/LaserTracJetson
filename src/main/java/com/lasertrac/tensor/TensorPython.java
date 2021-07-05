package com.lasertrac.tensor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

//import com.lasertrac.tensor.TensorOcr.StreamGobbler;

public class TensorPython {

	ProcessBuilder pb;
	Process process;
	
	BufferedReader stdInput, stdError;
	OutputStream out;
	BufferedWriter writer ;

	public TensorPython() {
		
		pb=new ProcessBuilder("/home/tensor/anaconda3/envs/tensor_gpu_3/bin/python", "/home/LaserConfig/ocr/test_ob3.py");
		pb.redirectErrorStream(true);
		pb.directory(new File("/home/LaserConfig/ocr/"));
		new Thread(new Runnable() {
		
		    @Override
		    public void run() {
		        try {
		        	
		            process = pb.start();
		            stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		            stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		
		            //OutputStream out;
		            out = process.getOutputStream();
		
		            writer = new BufferedWriter(new OutputStreamWriter(out));
		            StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), "ERROR");
		
		            // any output?
		            StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), "OUTPUT");
		
		            // start gobblers
		            outputGobbler.start();
		            errorGobbler.start();
		
		            //process.waitFor();
		
		        } catch (IOException ex) {
		//            Logger.getLogger(JavaFXConsole.class.getName()).log(Level.SEVERE, null, ex);
		            System.err.print("error 1 ="+ex.getMessage());
		        } 
//		        catch (InterruptedException ex) {
//		//            Logger.getLogger(JavaFXConsole.class.getName()).log(Level.SEVERE, null, ex);
//		        	System.err.print("error 2 ="+ex.getMessage());
//		        }
		    }
		}).start();
		
		while(is_ready==false) {
			try {
				Thread.sleep(100);	
			}catch(Exception ex) {
				
			}
		}
	}
	
	boolean is_working = false;
	String number_plate = "";
	public boolean is_ready = false;
	
	public String getOcr(String imgBase64) {
		try {
			if(is_working) {
				return "wroking";
			}
			writer.write(imgBase64+"\n");
			writer.flush();
			is_working = true;
			while(is_working) {
				Thread.sleep(50);
			}
			return number_plate; 
		}catch(Exception ex) {
			
		}
		return "";
	}
	
	private  class StreamGobbler extends Thread {
		
		InputStream is;
		String type;
		
		private StreamGobbler(InputStream is, String type) {
		    this.is = is;
		    this.type = type;
		}
		
		@Override
		public void run() {
		    try {
		        InputStreamReader isr = new InputStreamReader(is);
		        BufferedReader br = new BufferedReader(isr);
		        String line = null;
		        while ((line = br.readLine()) != null) {
		            //System.out.println(type + "> " + line);
		            String str =line;
		            System.out.println(str);
		            if(str.contains("number_output")) {
		            	str=str.replace("EOF", "");
		            	String[] strArr=str.split("=");
		            	if(strArr.length>1) {
		            		number_plate = strArr[1].toUpperCase();
		            	}else {
		            		number_plate=str;	
		            	}
		            	
		            	is_working=false;
		            }
		            if(str.contains("send base64")) {
		            	is_ready = true;
		            }
//		             Platform.runLater(new Runnable() {
//		
//		                    @Override
//		                    public void run() {
//		                        //ta.appendText(">" + str + "\n");
//		                    	System.out.println();
//		                    }
//		                });
		        }
		    } catch (IOException ioe) {
		        ioe.printStackTrace();
		    }
		}
	}
	
}
