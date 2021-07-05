package com.lasertrac.controller;

import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.FrameRecorder.Exception;

import com.lasertrac.common.AudioData;
import com.lasertrac.common.ConfigurationProps;
import com.lasertrac.common.FrameToRecord;
import com.lasertrac.common.JavaCVUtils;
import com.lasertrac.common.MyUtils;
import com.lasertrac.common.NavigationContstants;
import com.lasertrac.common.RecordFragment;
import com.lasertrac.common.RunningThread;
import com.lasertrac.main.StartScreen;
import com.lasertrac.usb.CameraInterface;

//import application.Main;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
//import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;

//import javafx.scene.control.ContextMenu;
//import javafx.scene.control.Label;
//import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.labs.scene.control.window.Window;

//import jfxtras.labs.scene.control.window.Window;
//import jfxtras.labs.scene.control.window.CloseIcon;
//import jfxtras.labs.scene.control.window.MinimizeIcon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;

//opencv
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
//import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameFilter;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

public class LiveViewController {

	StartScreen startSreenObj;
	
	public void setStartScreenObject(StartScreen obj) {
		this.startSreenObj = obj;
	}
	
	final private static int WEBCAM_DEVICE_INDEX = 0;
    final private static int AUDIO_DEVICE_INDEX = 4;

    final private static int FRAME_RATE = 25;
    final private static int GOP_LENGTH_IN_FRAMES = 50;

    private static long startTime = 0;
    private static long videoTS = 0;
    private static long startTimeAudio = 0;
    
    @FXML
	private Pane rootPane1;
	
	@FXML
	private GridPane gridPane1;
	
    @FXML
    private BorderPane currentFrameContainerPane;
    
    @FXML
	private ImageView currentFrame;

	@FXML 
	private Button zoomOutBtn;
	
	@FXML 
	private Button zoomInBtn;
	
	@FXML 
	private Button menuBtn;
	
	@FXML 
	private Button snapBtn;
	
	@FXML
	private Button autoManualRecBtn;

	@FXML
	private Button recBtn;
	
    OpenCVFrameGrabber grabber;
	FFmpegFrameRecorder recorder;
	
	boolean isRecording = false;
	LinkedBlockingQueue<AudioData> audioRecordToQueue = new LinkedBlockingQueue<>(125);
	LinkedBlockingQueue<FrameToRecord> frameRecordToQueue = new LinkedBlockingQueue<>(125);
	

	//LinkedBlockingQueue<FrameToRecord> mRecycledFrameQueue = new LinkedBlockingQueue<>(2);
	AudioMonitorThread audioMonitorThread;
	//Stack<RecordFragment> recordFragments;
	CameraInterface cameraObj=null;
	public void setCameraObject(CameraInterface camObj){
		this.cameraObj = camObj;
	}
	/**
	 * Initialize method, automatically called by @{link FXMLLoader}
	 */
	public void initialize()
	{
		gridPane1.prefWidthProperty().bind(rootPane1.widthProperty());
		gridPane1.prefHeightProperty().bind(rootPane1.heightProperty());
		
		currentFrame.fitWidthProperty().bind(currentFrameContainerPane.widthProperty());
		currentFrame.fitHeightProperty().bind(currentFrameContainerPane.heightProperty());
		
		//currentFrameContainerPane.setCenter(currentFrame);
		
		initActions();
		
		Date date1 = new Date();
		String tempFileName = ConfigurationProps.video_dir+"\\"+ MyUtils.directoryDTFormatter.format(date1);
        File fileDir= new File(tempFileName); 
		if(fileDir.exists() ==false) {
			fileDir.mkdir();
		}
		tempFileName+="\\"+ MyUtils.recFileNameDTFormatter.format(date1)+".mp4";
		
		recorder =createRecorderObj(tempFileName, 1280, 720);
		try {
			recorder.start();
	        //grabber.stop();	
		}catch(Exception ex) {
			System.out.println("start rec Exception="+ex.toString());
		}
		
		Platform.runLater(new Runnable() {
//            @Override
            public void run() {
            	//System.out.println(controller.labelLog.getText());
                //label.setText("Input was : " + userInput);
            	try {
            		Thread.sleep(500);	
            	}catch(java.lang.Exception ex) {
            		
            	}
            	startCamera2();
            	audioMonitorThread = new AudioMonitorThread();
            	audioMonitorThread .start();
            	activateLive();
            	//camera_started = true;
            }
        });
		
		
	}
	
	void startStopRecordingManual() {
		if(isRecording==false) {
			startRecording(true);
		}else {
			stopRecording();
			setLastFrame = true;
		}
	}
	//boolean camera_started=false;
	void activateLive() {
		//while(camera_started==false) {
			StartScreen.screenController.activate(NavigationContstants.liveScreen);	
		//}
	}
	
	static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	
	Window snapShotWindow = null;
	BufferedImage currentSnapShot=null;
	boolean takeSnapShot = false;
	SnapshotShowController controllerSnapShot;
	
	//SnapshotShowController snashotController
	synchronized void takeSnapShot() {
		
		//Image snapShot = currentFrame.getImage();
		currentSnapShot = null;
		takeSnapShot = true;
		System.out.println("snap start time:"+MyUtils.recordingDTFormatter.format(new Date())); 
		while(takeSnapShot) {
			if(currentSnapShot!=null) {
				break;
			}
			try {
				Thread.sleep(5);
			}catch(java.lang.Exception ex) {
				
			}
		}
		System.out.println("snap end time:"+MyUtils.recordingDTFormatter.format(new Date())); 
		
		Date date1 = new Date();
		String tempStr = ConfigurationProps.snap_dir+"\\"+ MyUtils.directoryDTFormatter.format(date1);
        File fileDir= new File(tempStr); 
		if(fileDir.exists() ==false) {
			fileDir.mkdir();
		}
		String tempStr2 =tempStr+"\\"+ MyUtils.recFileNameDTFormatter.format(date1);
		int count=1;
		while(true) {
			if(new File(tempStr2+".png").exists()==false) {
				tempStr2 = tempStr+"\\"+ MyUtils.recFileNameDTFormatter.format(date1)+".s"+count;
				break;
			}
			count+=1;
		}
		
		final String tempFileName= tempStr2+".png";
		
		Runnable r = new Runnable() {
			public void run() {
				saveSnapshot(currentSnapShot, tempFileName);
			}
		};
 
		Thread t = new Thread(r);
		t.start();
		
//		Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		 
		int width =(int) (screenBounds.getWidth()*37/100);
		int height =(int) (screenBounds.getHeight() *77/100);
		
		if(snapShotWindow!=null) {
			controllerSnapShot.updateImageView(currentSnapShot, tempFileName, width);
			controllerSnapShot.handOverWindowHandle(snapShotWindow);
			snapShotWindow.setVisible(true);
		}else {
			try{
        		//saveSnapshot(currentSnapShot);
    			System.out.println("save end time:"+MyUtils.recordingDTFormatter.format(new Date())); 
    			//Node node = FXMLLoader.load(getClass().getResource("/com/lasertrac/view/SnapshotShow.fxml"));
    			
    			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lasertrac/view/SnapshotShow.fxml"));
    			
    			Node node = loader.load();
    			
    			// create a window with title "My Window"
    			snapShotWindow = new Window();
    	        
    			snapShotWindow.setMovable(false);
    			snapShotWindow.setResizableWindow(false);
    			snapShotWindow.setTitleBarStyleClass("windowTitleBar");
    			snapShotWindow.setMinimized(false);
    			
    			// set the window position to 10,10 (coordinates inside canvas)
    			snapShotWindow.setLayoutX(screenBounds.getWidth()-width+5);
    			//snapShotWindow.setLayoutX(200);
    			
    			//System.out.println("screen height="+screenBounds.getHeight()+"  , height="+height+" , grid height="+currentFrame.getFitHeight());
    			//snapShotWindow.setLayoutY(screenBounds.getHeight()-(height+snapBtn.getHeight()+5));
    			snapShotWindow.setLayoutY(currentFrame.getFitHeight() - (height+5));
    				
    	        // define the initial window size
    			snapShotWindow.setPrefSize(width, height);
    	        // either to the left
    			//snapShotWindow.getLeftIcons().add(new CloseIcon(snapShotWindow));
    	        // .. or to the right
    			//snapShotWindow.getRightIcons().add(new MinimizeIcon(snapShotWindow));
    	        // add some content
    			//snapShotWindow.getContentPane().getChildren().add(new Label("Content... \nof the window#"));
    			
    			controllerSnapShot = loader.getController();
    			controllerSnapShot.updateImageView(currentSnapShot, tempFileName, width);
    			controllerSnapShot.handOverWindowHandle(snapShotWindow);
    			//controller.
    			snapShotWindow.getContentPane().getChildren().add(node);
    			
    	        // add the window to the canvas
    	        rootPane1.getChildren().add(snapShotWindow); 
    	        
    		}catch(java.lang.Exception ex) {
    		    	System.out.println("snap shot error "+ex.toString());
    		    	ex.printStackTrace();
    		}
			
		}
            	
//            }});
		 
//		if(snapShotWindow1==null) {
//			
//		}

	}
	
	public static void saveSnapshot(BufferedImage image, String tempFileName) {
	    //File outputFile = new File("C:/JavaFX/");
//	    Date date1 = new Date();
//		String tempFileName = ConfigurationProps.snap_dir+"\\"+ MyUtils.directoryDTFormatter.format(date1);
//        File fileDir= new File(tempFileName); 
//		if(fileDir.exists() ==false) {
//			fileDir.mkdir();
//		}
//		
//		tempFileName+="\\"+ MyUtils.recFileNameDTFormatter.format(date1)+".png";
		//tempFileName =  MyUtils.recFileNameDTFormatter.format(date1)+".mp4";
		
		System.out.println(tempFileName);
		File outputFile = new File(tempFileName);
		
	    //BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
	    try {
	      //ImageIO.write(bImage, "jpg", outputFile);
	      ImageIO.write(image, "png", outputFile);
	      //(SwingFXUtils.fromFXImage(image, null)
	    		  
	    } catch (IOException e) {
	    	System.out.println("save snapshot "+e.toString());
	      throw new RuntimeException(e);
	    }
	}
	int frameCOunter=0;
	protected void startCamera2() {
		// set a fixed width for the frame
		//this.currentFrame.setFitWidth(currentFrameContainerPane.getFitWidth());
		//this.currentFrame.setFitWidth(600);
		// preserve image ratio
		this.currentFrame.setPreserveRatio(true);
		try {
			
			final int captureWidth = 1280;
	        final int captureHeight = 720;

	        // The available FrameGrabber classes include OpenCVFrameGrabber (opencv_videoio),
	        // DC1394FrameGrabber, FlyCapture2FrameGrabber, OpenKinectFrameGrabber,
	        // PS3EyeFrameGrabber, VideoInputFrameGrabber, and FFmpegFrameGrabber.
	        grabber = new OpenCVFrameGrabber(WEBCAM_DEVICE_INDEX);
	        grabber.setImageWidth(captureWidth);
	        grabber.setImageHeight(captureHeight);
	        grabber.start();
	        
	        new Thread(new Runnable() {
//	            @Override
	            public void run()
	            {
	            	try {
	            		final Java2DFrameConverter converter = new Java2DFrameConverter();
		    	        Frame capturedFrame = null;

		    	        String watermark = "movie=frame1_2b.png[watermark];[in][watermark]overlay=W-w-0:0:format=rgb[out]";
		                FFmpegFrameFilter frameFilter = new FFmpegFrameFilter(watermark, 1280, 720);
		                frameFilter.setPixelFormat(avutil.AV_PIX_FMT_BGR24);
		                frameFilter.start();
		                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS");
		    	        // While we are capturing...
		    	        while ((capturedFrame = grabber.grab()) != null)
		    	        {
		    	        	// Adding the WaterMark
	                        frameFilter.push(capturedFrame);
	                        Frame filteredFrame = frameFilter.pull();
	                        
	                        BufferedImage imgB=converter.convert(filteredFrame);
	                        Graphics2D w = (Graphics2D) imgB.getGraphics();
	                        w.setColor(Color.WHITE);
	                        w.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
	                        w.drawString(dateFormatter.format(new Date()), 2, 25);
//	                        Image img=SwingFXUtils.toFXImage(converter.convert(filteredFrame),null);
//	                        //Bitmap newImage = Bitmap.createBitmap(width, height, Config.ARGB_8888);
//	                        opencv_core.CvFont font = new opencv_core.CvFont(); 
//	                        opencv_core.cvInitFont(font, opencv_core.CV_FONT_HERSHEY_SCRIPT_SIMPLEX, 2.0, 2.0, 2, 2, opencv_core.CV_AA);
//	                        opencv_core.IplImage bgrImage = opencv_core.IplImage.create(1280, 720, IPL_DEPTH_8U, 3);
//	                        //...
//	                        //init something(eg init recorder,init iplimage...)
//
//	                        opencv_core.cvPutText(bgrImage/*IplImage*/, "Text here", cvPoint(10,10), font, opencv_core.CvScalar.RED);
//	                        recorder.record(bgrImage);
	                        //org.bytedeco.javacv.AndroidFrameConverter converter2 = new AndroidFrameConverter();
	                        if (currentFrame.isVisible())
//		    	            {
//		    	                // Show our frame in the preview
//		    	            	currentFrame.showImage(capturedFrame);
//		    	            }
		    	        	//converterToMat.convert(capturedFrame);
		    	        	if(currentFrame!=null) {
		    	        		//updateImageView(currentFrame,SwingFXUtils.toFXImage(converter.convert(filteredFrame), null));
		    	        		if(takeSnapShot && currentSnapShot==null) {
		    	        			System.out.println("copy start time:"+MyUtils.recordingDTFormatter.format(new Date())); 
		    	        			currentSnapShot = imgB;
		    	        			//currentSnapShot = deepCopy(imgB);
		    	        			takeSnapShot = false;
		    	        			System.out.println("copy end time:"+MyUtils.recordingDTFormatter.format(new Date())); 
		    	        			
		    	        		}
		    	        		updateImageView(currentFrame,SwingFXUtils.toFXImage(imgB, null));
		    	        	}
		    	        		// Let's define our start time...
			    	            // This needs to be initialized as close to when we'll use it as
			    	            // possible,
			    	            // as the delta from assignment to computed time could be too high
//			    	            if (startTime == 0)
//			    	                startTime = System.currentTimeMillis();
//
//			    	            // Create timestamp for this frame
//			    	            videoTS = 1000 * (System.currentTimeMillis() - startTime);
			    	            
			    	            videoTS = System.currentTimeMillis();
			    	            FrameToRecord frame;
			    	            if(setLastFrame) {
			    	            	setLastFrame = false;
			    	            	frame = new FrameToRecord(videoTS,  converter.convert(imgB),true , frameCOunter++);
			    	            	startTime = 0;
			    	            }else{
			    	            	frame = new FrameToRecord(videoTS,  converter.convert(imgB), false, frameCOunter++);
			    	            }
			    	            
			    	            //System.out.println("setting timestamp for frame="+videoTS);
			    	            try {
		    		            	if(frameRecordToQueue.remainingCapacity()<=0) {
		    		            		frameRecordToQueue.poll();
		    		            	}
		    		            	//System.out.println("capacity="+frameRecordToQueue.remainingCapacity());
		    		            	frameRecordToQueue.put(frame);	
		    		            }catch(java.lang.Exception ex) {
		    		            	
		    		            }
	                        
		    	        }
		    	    }
	            	catch(org.bytedeco.javacv.FrameGrabber.Exception ex) {
	        			System.out.println(" initialize FrameGrabber.Exception="+ex.toString());
	        		}catch(org.bytedeco.javacv.FrameFilter.Exception ex) {
	        			System.out.println(" initialize FrameFilter.Exception="+ex.toString());
	        		}
	           		    	        
	          }
	        }).start();
	        
		}catch(java.lang.Exception ex) {
			ex.printStackTrace();
		}
		
	}
	private long calculateTotalRecordedTime(Stack<RecordFragment> recordFragments) {
        long recordedTime = 0;
        for (RecordFragment recordFragment : recordFragments) {
            recordedTime += recordFragment.getDuration();
        }
        return recordedTime;
    }
	
	protected void startCamera()
	{
		// set a fixed width for the frame
		//this.currentFrame.setFitWidth(currentFrameContainerPane.getFitWidth());
		//this.currentFrame.setFitWidth(600);
		// preserve image ratio
		this.currentFrame.setPreserveRatio(true);
		try {
			
			final int captureWidth = 1280;
	        final int captureHeight = 720;

	        // The available FrameGrabber classes include OpenCVFrameGrabber (opencv_videoio),
	        // DC1394FrameGrabber, FlyCapture2FrameGrabber, OpenKinectFrameGrabber,
	        // PS3EyeFrameGrabber, VideoInputFrameGrabber, and FFmpegFrameGrabber.
	        grabber = new OpenCVFrameGrabber(WEBCAM_DEVICE_INDEX);
	        grabber.setImageWidth(captureWidth);
	        grabber.setImageHeight(captureHeight);
	        grabber.start();
	        
	     // org.bytedeco.javacv.FFmpegFrameRecorder.FFmpegFrameRecorder(String
	        // filename, int imageWidth, int imageHeight, int audioChannels)
	        // For each param, we're passing in...
	        // filename = either a path to a local file we wish to create, or an
	        // RTMP url to an FMS / Wowza server
	        // imageWidth = width we specified for the grabber
	        // imageHeight = height we specified for the grabber
	        // audioChannels = 2, because we like stereo
	        recorder =createRecorderObj("rama.mp4", captureWidth, captureHeight);

	        // Jack 'n coke... do it...
	        //recorder.start();
	         
	     // Thread for audio capture, this could be in a nested private class if you prefer...
	        new Thread(new Runnable() {
	            @Override
	            public void run()
	            {
	                // Pick a format...
	                // NOTE: It is better to enumerate the formats that the system supports,
	                // because getLine() can error out with any particular format...
	                // For us: 44.1 sample rate, 16 bits, stereo, signed, little endian
	                AudioFormat audioFormat = new AudioFormat(44100.0F, 16, 2, true, false);

	                // Get TargetDataLine with that format
	                Mixer.Info[] minfoSet = AudioSystem.getMixerInfo();
	                Mixer mixer = AudioSystem.getMixer(minfoSet[AUDIO_DEVICE_INDEX]);
	                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

	                try
	                {
	                    // Open and start capturing audio
	                    // It's possible to have more control over the chosen audio device with this line:
	                    // TargetDataLine line = (TargetDataLine)mixer.getLine(dataLineInfo);
	                    final TargetDataLine line = (TargetDataLine)AudioSystem.getLine(dataLineInfo);
	                    line.open(audioFormat);
	                    line.start();

	                    final int sampleRate = (int) audioFormat.getSampleRate();
	                    final int numChannels = audioFormat.getChannels();

	                    // Let's initialize our audio buffer...
	                    final int audioBufferSize = sampleRate * numChannels;
	                    final byte[] audioBytes = new byte[audioBufferSize];

	                    // Using a ScheduledThreadPoolExecutor vs a while loop with
	                    // a Thread.sleep will allow
	                    // us to get around some OS specific timing issues, and keep
	                    // to a more precise
	                    // clock as the fixed rate accounts for garbage collection
	                    // time, etc
	                    // a similar approach could be used for the webcam capture
	                    // as well, if you wish
	                    ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
	                    exec.scheduleAtFixedRate(new Runnable() {
	                        @Override
	                        public void run()
	                        {
	                            try
	                            {
	                                
	                                if(isRecording) {
	                                	// Read from the line... non-blocking
		                                int nBytesRead = 0;
		                                while (nBytesRead == 0) {
		                                    nBytesRead = line.read(audioBytes, 0, line.available());
		                                }

		                                // Since we specified 16 bits in the AudioFormat,
		                                // we need to convert our read byte[] to short[]
		                                // (see source from FFmpegFrameRecorder.recordSamples for AV_SAMPLE_FMT_S16)
		                                // Let's initialize our short[] array
		                                int nSamplesRead = nBytesRead / 2;
		                                short[] samples = new short[nSamplesRead];

		                                // Let's wrap our short[] into a ShortBuffer and
		                                // pass it to recordSamples
		                                ByteBuffer.wrap(audioBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(samples);
		                                ShortBuffer sBuff = ShortBuffer.wrap(samples, 0, nSamplesRead);
		                                
	                                	// recorder is instance of
		                                // org.bytedeco.javacv.FFmpegFrameRecorder
		                                recorder.recordSamples(sampleRate, numChannels, sBuff);	
	                                }
	                                
	                            } 
	                            catch (org.bytedeco.javacv.FrameRecorder.Exception e)
	                            {
	                                e.printStackTrace();
	                            }
	                        }
	                    }, 0, (long) 1000 / FRAME_RATE, TimeUnit.MILLISECONDS);
	                } 
	                catch (LineUnavailableException e1)
	                {
	                    e1.printStackTrace();
	                }
	            }
	        }).start();
	        
	        new Thread(new Runnable() {
//	            @Override
	            public void run()
	            {
	            	try {
	            		final Java2DFrameConverter converter = new Java2DFrameConverter();
		    	        Frame capturedFrame = null;

		    	        String watermark = "movie=frame1_2b.png[watermark];[in][watermark]overlay=W-w-0:0:format=rgb[out]";
		                FFmpegFrameFilter frameFilter = new FFmpegFrameFilter(watermark, 1280, 720);
		                frameFilter.setPixelFormat(avutil.AV_PIX_FMT_BGR24);
		                frameFilter.start();
		                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS");
		    	        // While we are capturing...
		    	        while ((capturedFrame = grabber.grab()) != null)
		    	        {
		    	        	// Adding the WaterMark
	                        frameFilter.push(capturedFrame);
	                        Frame filteredFrame = frameFilter.pull();
	                        
	                        BufferedImage imgB=converter.convert(filteredFrame);
	                        Graphics2D w = (Graphics2D) imgB.getGraphics();
	                        w.setColor(Color.WHITE);
	                        w.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
	                        w.drawString(dateFormatter.format(new Date()), 2, 25);
//	                        Image img=SwingFXUtils.toFXImage(converter.convert(filteredFrame),null);
//	                        //Bitmap newImage = Bitmap.createBitmap(width, height, Config.ARGB_8888);
//	                        opencv_core.CvFont font = new opencv_core.CvFont(); 
//	                        opencv_core.cvInitFont(font, opencv_core.CV_FONT_HERSHEY_SCRIPT_SIMPLEX, 2.0, 2.0, 2, 2, opencv_core.CV_AA);
//	                        opencv_core.IplImage bgrImage = opencv_core.IplImage.create(1280, 720, IPL_DEPTH_8U, 3);
//	                        //...
//	                        //init something(eg init recorder,init iplimage...)
//
//	                        opencv_core.cvPutText(bgrImage/*IplImage*/, "Text here", cvPoint(10,10), font, opencv_core.CvScalar.RED);
//	                        recorder.record(bgrImage);
	                        //org.bytedeco.javacv.AndroidFrameConverter converter2 = new AndroidFrameConverter();
	                        if (currentFrame.isVisible())
//		    	            {
//		    	                // Show our frame in the preview
//		    	            	currentFrame.showImage(capturedFrame);
//		    	            }
		    	        	//converterToMat.convert(capturedFrame);
		    	        	if(currentFrame!=null) {
		    	        		//updateImageView(currentFrame,SwingFXUtils.toFXImage(converter.convert(filteredFrame), null));
		    	        		if(takeSnapShot && currentSnapShot==null) {
		    	        			System.out.println("copy start time:"+MyUtils.recordingDTFormatter.format(new Date())); 
		    	        			currentSnapShot = imgB;
		    	        			//currentSnapShot = deepCopy(imgB);
		    	        			takeSnapShot = false;
		    	        			System.out.println("copy end time:"+MyUtils.recordingDTFormatter.format(new Date())); 
		    	        			
		    	        		}
		    	        		updateImageView(currentFrame,SwingFXUtils.toFXImage(imgB, null));
		    	        	}
		    	        	
	                        {
	                        	// Let's define our start time...
			    	            // This needs to be initialized as close to when we'll use it as
			    	            // possible,
			    	            // as the delta from assignment to computed time could be too high
			    	            if (startTime == 0)
			    	                startTime = System.currentTimeMillis();

			    	            // Create timestamp for this frame
			    	            videoTS = 1000 * (System.currentTimeMillis() - startTime);
			    	            
			    	            FrameToRecord frame;
			    	            if(setLastFrame) {
			    	            	setLastFrame = false;
			    	            	frame = new FrameToRecord(videoTS,  converter.convert(deepCopy(imgB)),true , frameCOunter++);
			    	            }else{
			    	            	frame = new FrameToRecord(videoTS,  converter.convert(deepCopy(imgB)), false, frameCOunter++ );
			    	            }
			    	            try {
		    		            	if(frameRecordToQueue.remainingCapacity()<=0) {
		    		            		frameRecordToQueue.poll();
		    		            	}
		    		            	frameRecordToQueue.offer(frame);	
		    		            }catch(java.lang.Exception ex) {
		    		            	
		    		            }
	                        }
	                        
//		    	            if(isRecording) {
//		    	            	// Let's define our start time...
//			    	            // This needs to be initialized as close to when we'll use it as
//			    	            // possible,
//			    	            // as the delta from assignment to computed time could be too high
//			    	            if (startTime == 0)
//			    	                startTime = System.currentTimeMillis();
//
//			    	            // Create timestamp for this frame
//			    	            videoTS = 1000 * (System.currentTimeMillis() - startTime);
//			    	            
//			    	            FrameToRecord frame =new FrameToRecord(videoTS,  converter.convert(imgB));
//		    		            
//		    	            	// Check for AV drift
//		    		            if (videoTS > recorder.getTimestamp())
//		    		            {
//		    		                System.out.println(
//		    		                        "Lip-flap correction: " 
//		    		                        + videoTS + " : "
//		    		                        + recorder.getTimestamp() + " -> "
//		    		                        + (videoTS - recorder.getTimestamp()));
//
//		    		                // We tell the recorder to write this frame at this timestamp
//		    		                recorder.setTimestamp(videoTS);
//		    		                //frame.timestamp=videoTS;
//		    		            }
//
//		    		            // Send the frame to the org.bytedeco.javacv.FFmpegFrameRecorder
//		    		            recorder.record(converter.convert(imgB));
//		    		            try {
//		    		            	if(frameRecordToQueue.remainingCapacity()<=0) {
//		    		            		frameRecordToQueue.poll();
//		    		            	}
//		    		            	frameRecordToQueue.put(frame);	
//		    		            }catch(java.lang.Exception ex) {
//		    		            	
//		    		            }
//		    		            
//		    	            }
		    	            
		    	        }
	            	}
//	            	catch(Exception ex) {
//	            		
//	            		System.out.println("initialize Exception="+ex.toString());
//	            	}
	            	catch(org.bytedeco.javacv.FrameGrabber.Exception ex) {
	        			System.out.println(" initialize FrameGrabber.Exception="+ex.toString());
	        		}catch(org.bytedeco.javacv.FrameFilter.Exception ex) {
	        			System.out.println(" initialize FrameFilter.Exception="+ex.toString());
	        		}
	            	
	            }
	        }).start();
	        
	        
		}catch(org.bytedeco.javacv.FrameGrabber.Exception ex) {
			System.out.println(" initialize FrameGrabber.Exception="+ex.toString());
		}
		
	}
	
	
	VideoRecordThread videoRecordTh;
	AudioRecordThread audioRecordTh;
	
	protected void startRecording(boolean manual) {
		
		Date date1 = new Date();
		String tempFileName = ConfigurationProps.video_dir+"\\"+ MyUtils.directoryDTFormatter.format(date1);
        File fileDir= new File(tempFileName); 
		if(fileDir.exists() ==false) {
			fileDir.mkdir();
		}
		tempFileName+="\\"+ MyUtils.recFileNameDTFormatter.format(date1)+".mp4";
		//tempFileName =  MyUtils.recFileNameDTFormatter.format(date1)+".mp4";
		
//		if(recordFragments==null) {
//			recordFragments=new Stack<>();
//		}
//		
//		RecordFragment recordFragment = new RecordFragment();
//        recordFragment.setStartTimestamp(frameRecordToQueue.peek().getTimestamp());
//		recordFragments.push(recordFragment);
		
//		System.out.println(tempFileName);
//		recorder =createRecorderObj(tempFileName, 1280, 720);
//		try {
//			recorder.start();
//	        //grabber.stop();	
//		}catch(Exception ex) {
//			System.out.println("start rec Exception="+ex.toString());
//		}
		
//		audioRecordTh= new AudioRecordThread();
//		audioRecordTh.start();
		
		videoRecordTh = new VideoRecordThread();
		videoRecordTh.start();
		
		//count+=1;
        // Jack 'n coke... do it...
        
		isRecording=true;
		recBtn.setText("Stop Rec");
		
	}
	
	boolean setLastFrame = false;
	protected void stopRecording() {
		
		isRecording=false;
		setLastFrame = true;
		try {
			
			
			//audioRecordTh.finishedRecording=false;
//			while(audioRecordTh.isRunning) {
//				Thread.sleep(1);
//			}
//			while(videoRecordTh.isRunning) {
//				Thread.sleep(1);
//			}
//			System.out.println(b);
//			videoRecordTh.join();
			stopRecordingThreads();
			
			Thread.sleep(100);
			recorder.stop();
			recorder.release();
			//startTime=0;
			startTimeAudio = 0;
		}catch(InterruptedException ex1) {
			System.out.println("stop rec Exception="+ex1.toString());
		}
		catch(Exception ex) {
			System.out.println("stop rec Exception="+ex.toString());
		}
		recBtn.setText("Rec");
		
	}
	private void stopRecordingThreads() {
        if (audioRecordTh != null) {
            if (audioRecordTh .isRunning()) {
            	audioRecordTh.stopRunning();
            }
        }
        while(videoRecordTh.finishedRecording) {
        	
        }
        if (videoRecordTh != null) {
            if (videoRecordTh.isRunning()) {
            	videoRecordTh.stopRunning();
            }
        }

        try {
            if (audioRecordTh != null) {
                audioRecordTh.join();
            }
            if (videoRecordTh != null) {
                videoRecordTh.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        audioRecordTh = null;
        videoRecordTh = null;
      
	}
	/**
	 * On application close, stop the acquisition from the camera
	 */
	protected void setClosed()
	{
		//this.stopAcquisition();
	}
	
	/**
	 * Update the {@link ImageView} in the JavaFX main thread
	 * 
	 * @param view
	 *            the {@link ImageView} to update
	 * @param image
	 *            the {@link Image} to show
	 */
	private void updateImageView(ImageView view, Image image)
	{
		JavaCVUtils.onFXThread(view.imageProperty(), image);
	}
	FFmpegFrameRecorder createRecorderObj(String filename,int captureWidth, int captureHeight) {
    	FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(
                filename,
                captureWidth, captureHeight, 2);
        recorder.setInterleaved(true);
        
        // decrease "startup" latency in FFMPEG (see:
        // https://trac.ffmpeg.org/wiki/StreamingGuide)
        recorder.setVideoOption("tune", "zerolatency");
        // tradeoff between quality and encode speed
        // possible values are ultrafast,superfast, veryfast, faster, fast,
        // medium, slow, slower, veryslow
        // ultrafast offers us the least amount of compression (lower encoder
        // CPU) at the cost of a larger stream size
        // at the other end, veryslow provides the best compression (high
        // encoder CPU) while lowering the stream size
        // (see: https://trac.ffmpeg.org/wiki/Encode/H.264)
        recorder.setVideoOption("preset", "ultrafast");
        // Constant Rate Factor (see: https://trac.ffmpeg.org/wiki/Encode/H.264)
        recorder.setVideoOption("crf", "28");
        // 2000 kb/s, reasonable "sane" area for 720
        recorder.setVideoBitrate(2000000);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H265);
        recorder.setFormat("mp4");
        // FPS (frames per second)
        recorder.setFrameRate(FRAME_RATE);
        // Key frame interval, in our case every 2 seconds -> 30 (fps) * 2 = 60
        // (gop length)
        //recorder.setGopSize(GOP_LENGTH_IN_FRAMES);

        // We don't want variable bitrate audio
        recorder.setAudioOption("crf", "0");
        // Highest quality
        recorder.setAudioQuality(0);
        // 192 Kbps
        recorder.setAudioBitrate(192000);
        recorder.setSampleRate(44100);
        recorder.setAudioChannels(1);
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);

        // Jack 'n coke... do it...
        //recorder.start();
        return recorder;
    	
    }
	
	private void openMenuWindow() {
		try {
			StartScreen.contextMenu.show(gridPane1, 100, 100);
		    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/lasertrac/view/Menu.fxml"));
		    Pane root1 = (Pane) fxmlLoader.load();
		    Stage stage = new Stage();
		    stage.initModality(Modality.WINDOW_MODAL);
		    
		    stage.initStyle(StageStyle.UNDECORATED);
		    stage.setTitle("Menu");
		    stage.setScene(new Scene(root1,200,400));
		    stage.initOwner(rootPane1.getScene().getWindow());
		    
		    stage.show();
		    stage.centerOnScreen();
		    stage.setAlwaysOnTop(true);
//		    ((Node)(event.getSource())).getScene().getWindow().getScene().get;
		
		}catch(Exception ex) {
			System.out.println("error open menu from live="+ex.toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void initActions(){
		zoomInBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if(cameraObj != null) {
					cameraObj.startZoomIn();	
				}
				
			}
		}); 
		zoomInBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
//			@Override
			public void handle(MouseEvent arg0) {
				if(cameraObj != null) {
					cameraObj.stopZoom();
				}
			}
		}); 
		
		zoomInBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				if(cameraObj != null) {
					cameraObj.startZoomOut();
				}
			}
		});
		zoomInBtn.setOnTouchReleased(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				if(cameraObj != null) {
					cameraObj.stopZoom();
				}
			}
		});

		zoomOutBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if(cameraObj != null) {
					cameraObj.startZoomOut();
				}
			}
		});
		zoomOutBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if(cameraObj != null) {
					cameraObj.stopZoom();
				}
			}
		});
		
		zoomOutBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
//			@Override
			public void handle(TouchEvent event) {
				if(cameraObj != null) {
					cameraObj.startZoomOut();
				}
			}
		});
		zoomOutBtn.setOnTouchReleased(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				if(cameraObj != null) {
					cameraObj.stopZoom();
				}
			}
		});
		
		menuBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				openMenuWindow();
			}
		});
		menuBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				openMenuWindow();
			}
		});
		
		recBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent arg0) {
				startStopRecordingManual();
				
			}
		});
		recBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				startStopRecordingManual();
			}
		});
		
		snapBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				takeSnapShot();
			}
		});
		snapBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				takeSnapShot();
			}
		});
		
	}
	
	class VideoRecordThread extends RunningThread{
		@Override
		public void run() {
			isRunning=true;
			System.out.println(" clicked fc="+frameCOunter);
			FrameToRecord recordedFrame;
			while((isRunning || frameRecordToQueue.isEmpty()) && finishedRecording==false) {
					try {
						recordedFrame = frameRecordToQueue.poll();	
					}catch(java.lang.Exception ex) {
						System.out.println("pull error="+ex.toString());
                    	ex.printStackTrace();
						break;
					}
					if(isRecording) {
						System.out.println("khali choriya hi pta ta h"); 
					}
					
					// Let's define our start time...
    	            // This needs to be initialized as close to when we'll use it as
    	            // possible,
    	            // as the delta from assignment to computed time could be too high
    	            if (startTime == 0) {
    	            	startTime = recordedFrame.getTimestamp();
    	            }
    	                

    	            // Create timestamp for this frame
    	           long timestamp = 1000 * (recordedFrame.getTimestamp() - startTime);
    	            
    	            //long timestamp = recordedFrame.getTimestamp();
    	            
//					long timestamp1 = recordedFrame.getTimestamp();
//					// pop the current record fragment when calculate total recorded time
//                    RecordFragment curFragment = recordFragments.pop();
//                    long recordedTime = calculateTotalRecordedTime(recordFragments);
//                    // push it back after calculation
//                    recordFragments.push(curFragment);
//                    long curRecordedTime = timestamp1 - curFragment.getStartTimestamp() + recordedTime;
//                    
//                    long timestamp = 1000 * curRecordedTime;
                    
                    System.out.println("setting timestamp="+timestamp+"  isrunninng="+isRunning+" ,queue="+frameRecordToQueue.isEmpty()+"  ,finished="+finishedRecording +" , fc="+recordedFrame.getFrameCounter());
                    
                    if (timestamp > recorder.getTimestamp()) {
//                    	System.out.println(
//		                        "Lip-flap correction: " 
//		                        + timestamp + " : "
//		                        + recorder.getTimestamp() + " -> "
//		                        + (timestamp - recorder.getTimestamp()));
                        recorder.setTimestamp(timestamp);
                    }
                    //recordedFrame.getFrame().timestamp=timestamp;
                    try {
                    	recorder.record(recordedFrame.getFrame());	
                    }catch(java.lang.Exception ex) {
                    	System.out.println(ex.toString());
                    	ex.printStackTrace();
                    }
                    if(recordedFrame.isLastFrame()) {
                    	finishedRecording = true;
                    }
			}
		}
	}
	
	class AudioRecordThread extends RunningThread {
		@Override
		public void run() {
			isRunning = true;
			while((isRunning || audioRecordToQueue.isEmpty()) && isRecording) {
				AudioData audioData;
				try {
					audioData = audioRecordToQueue.take();
				}catch(java.lang.Exception ex) {
					ex.printStackTrace();
					break;
				}
				long timestamp = audioData.getTimestamp();
                if (timestamp > recorder.getTimestamp()) {
                    recorder.setTimestamp(timestamp);
                }
                try	{
                    recorder.recordSamples(audioData.getSampleRate(), audioData.getNumChannels(), audioData.getbBufferData());
                }catch(java.lang.Exception ex) {
                	ex.printStackTrace();
                }
				
			}
		}
    }
	
	class AudioMonitorThread extends RunningThread{
		
		AudioFormat audioFormat ;
		DataLine.Info dataLineInfo ;
		AudioMonitorThread() {
            // Pick a format...
            // NOTE: It is better to enumerate the formats that the system supports,
            // because getLine() can error out with any particular format...
            // For us: 44.1 sample rate, 16 bits, stereo, signed, little endian
			audioFormat = new AudioFormat(44100.0F, 16, 1, true, false);
		
			// Get TargetDataLine with that format
            Mixer.Info[] minfoSet = AudioSystem.getMixerInfo();
            Mixer mixer = AudioSystem.getMixer(minfoSet[AUDIO_DEVICE_INDEX]);
            dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
		}
		
		
		@Override
        public void run()
        {

            try
            {
            	isRunning=true;
                
            	// Open and start capturing audio
                // It's possible to have more control over the chosen audio device with this line:
                // TargetDataLine line = (TargetDataLine)mixer.getLine(dataLineInfo);
                TargetDataLine line = (TargetDataLine)AudioSystem.getLine(dataLineInfo);
                line.open(audioFormat);
                line.start();

                final int sampleRate = (int) audioFormat.getSampleRate();
                final int numChannels = audioFormat.getChannels();

                // Let's initialize our audio buffer...
                final int audioBufferSize = sampleRate * numChannels;
                final byte[] audioBytes = new byte[audioBufferSize];

                // Using a ScheduledThreadPoolExecutor vs a while loop with
                // a Thread.sleep will allow
                // us to get around some OS specific timing issues, and keep
                // to a more precise
                // clock as the fixed rate accounts for garbage collection
                // time, etc
                // a similar approach could be used for the webcam capture
                // as well, if you wish
                while(isRunning) {
                	try {
                		// Read from the line... non-blocking
                        int nBytesRead = 0;
                        while (nBytesRead == 0) {
                            nBytesRead = line.read(audioBytes, 0, line.available());
                        }

                        // Since we specified 16 bits in the AudioFormat,
                        // we need to convert our read byte[] to short[]
                        // (see source from FFmpegFrameRecorder.recordSamples for AV_SAMPLE_FMT_S16)
                        // Let's initialize our short[] array
                        int nSamplesRead = nBytesRead / 2;
                        short[] samples = new short[nSamplesRead];

                        // Let's wrap our short[] into a ShortBuffer and
                        // pass it to recordSamples
                        ByteBuffer.wrap(audioBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(samples);
                        ShortBuffer sBuff = ShortBuffer.wrap(samples, 0, nSamplesRead);
                        
                    	// recorder is instance of
                        // org.bytedeco.javacv.FFmpegFrameRecorder
                        //recorder.recordSamples(sampleRate, numChannels, sBuff);
                        
                        // Let's define our start time...
	    	            // This needs to be initialized as close to when we'll use it as
	    	            // possible,
	    	            // as the delta from assignment to computed time could be too high
	    	            if (startTimeAudio == 0)
	    	            	startTimeAudio = System.currentTimeMillis();

	    	            // Create timestamp for this frame
	    	            long audioTS = 1000 * (System.currentTimeMillis() - startTimeAudio);
	    	            
//                        long curRecordedTime=0;
//                        if(audioRecordToQueue!=null && audioRecordToQueue.isEmpty()==false) {
//                        	curRecordedTime = 100
//                        }
                        
                        if(audioRecordToQueue.remainingCapacity()<=0) {
                        	audioRecordToQueue.poll();
                        }
//                        long curRecordedTime = System.currentTimeMillis()
//                                - curFragment.getStartTimestamp() + recordedTime;
//                        // check if exceeds time limit
//                        if (curRecordedTime > MAX_VIDEO_LENGTH) {
//                            pauseRecording();
//                            new FinishRecordingTask().execute();
//                            return;
//                        }
//
//                        long timestamp = 1000 * curRecordedTime;
                        
                        audioRecordToQueue.put(new AudioData(sampleRate, numChannels, sBuff, audioTS));
                        
                        Thread.sleep(1000 / FRAME_RATE);
                	}catch(java.lang.Exception ex) {
                		ex.printStackTrace();
                	}
                	
                }
                line.close();
                line = null;
//                ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
//                exec.scheduleAtFixedRate(new Runnable() {
//                    @Override
//                    public void run()
//                    {
//                        try
//                        {
//                            
//                            if(startRecording) {
//                            	// Read from the line... non-blocking
//                                int nBytesRead = 0;
//                                while (nBytesRead == 0) {
//                                    nBytesRead = line.read(audioBytes, 0, line.available());
//                                }
//
//                                // Since we specified 16 bits in the AudioFormat,
//                                // we need to convert our read byte[] to short[]
//                                // (see source from FFmpegFrameRecorder.recordSamples for AV_SAMPLE_FMT_S16)
//                                // Let's initialize our short[] array
//                                int nSamplesRead = nBytesRead / 2;
//                                short[] samples = new short[nSamplesRead];
//
//                                // Let's wrap our short[] into a ShortBuffer and
//                                // pass it to recordSamples
//                                ByteBuffer.wrap(audioBytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(samples);
//                                ShortBuffer sBuff = ShortBuffer.wrap(samples, 0, nSamplesRead);
//                                
//                            	// recorder is instance of
//                                // org.bytedeco.javacv.FFmpegFrameRecorder
//                                recorder.recordSamples(sampleRate, numChannels, sBuff);	
//                            }
//                            
//                        } 
//                        catch (org.bytedeco.javacv.FrameRecorder.Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                    }
//                }, 0, (long) 1000 / FRAME_RATE, TimeUnit.MILLISECONDS);
            } 
            catch (LineUnavailableException e1)
            {
                e1.printStackTrace();
            }
        }
		
	}
	
	
	
	
}
