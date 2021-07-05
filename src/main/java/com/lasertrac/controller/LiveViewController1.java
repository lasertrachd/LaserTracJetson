package com.lasertrac.controller;


import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.ini4j.Ini;
import org.bytedeco.javacv.FrameRecorder.Exception;
import org.bytedeco.librealsense.intrinsics;

import com.lasertrac.common.ConfigurationProps;
import com.lasertrac.common.ControlsHelper;
import com.lasertrac.common.IniFile;
import com.lasertrac.common.JavaCVUtils;
import com.lasertrac.common.ListViewImageItem;
import com.lasertrac.common.LogWriter;
import com.lasertrac.common.MyUtils;
import com.lasertrac.common.NavigationContstants;
import com.lasertrac.common.ResponsiveConst;
import com.lasertrac.common.SoundAndMic;
import com.lasertrac.dao.UserDao;
import com.lasertrac.dao.impl.UserDaoImpl;
import com.lasertrac.datamodels.CurrentSessionClass;
import com.lasertrac.entity.FileInfo;
import com.lasertrac.main.StartScreen;
import com.lasertrac.main.TensorResult;
import com.lasertrac.settings.CameraPresets;
import com.lasertrac.settings.CameraProperties;
import com.lasertrac.settings.MyAudioManager;
import com.lasertrac.settings.UsbRelay;
import com.lasertrac.tensor.TensorLibrary;
import com.lasertrac.tensor.TensorOcr;
import com.lasertrac.tensor.TensorUtil;
import com.lasertrac.usb.CameraInterface;
import com.lasertrac.usb.CameraWonwoo;
import com.lasertrac.usb.LaserInterface;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
//import application.Main;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
//import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
//import javafx.scene.control.ContextMenu;
//import javafx.scene.control.Label;
//import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import jfxtras.labs.scene.control.window.CloseIcon;
import jfxtras.labs.scene.control.window.MinimizeIcon;
import jfxtras.labs.scene.control.window.Window;

import java.awt.Color;
//import jfxtras.labs.scene.control.window.Window;
//import jfxtras.labs.scene.control.window.CloseIcon;
//import jfxtras.labs.scene.control.window.MinimizeIcon;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LiveViewController1 {

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
    
    @FXML
	private Pane rootPane1;
	
	@FXML
	private GridPane gridPane1;
	
	@FXML
	private GridPane bottomSideControlGrid;
	
	@FXML
	private GridPane leftSideControlGrid;
	
	
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
	
	@FXML
	private Button dZoomBtn;

	@FXML
	private Button focusAutoManualBtn;
	
	@FXML
	private Button focusPlusBtn;
	
	@FXML
	private Button focusMinusBtn;
	
	@FXML
	private Button infraredBtn;
	
	@FXML
	private Button laserBtn;
	
	@FXML
	private Button bikeSpeedBtn;
	
	@FXML
	private Button carSpeedBtn;
	
	@FXML
	private Button truckSpeedBtn;
	
	@FXML 
	private Button defineSpeedBtn;
	
	@FXML 
	private Button camModeBtn;
	
	@FXML 
	private Button soundBtn;
	
	@FXML 
	private Button snapNoHelmetBtn;
	
	
    OpenCVFrameGrabber grabber;
	FFmpegFrameRecorder recorder;
	
	boolean startRecording = false;
	
	int dZoomLevel = 0;
	
	int selectedSpeedLimitEL = 0; 
	int selectedSpeedLimitVL = 0; 
	
	CameraInterface cameraObj = null;
	LaserInterface laserObj = null;
	//GpsInterface laserObj = null;
	
	ObservableList<ListViewImageItem> autoEvidenceMag=FXCollections.<ListViewImageItem>observableArrayList();
	
	UserDao udao;
	
	
	Rectangle2D screenBounds ;
	
	TensorLibrary tensorlib;
	
	TensorOcr tensorOcr;
	
	
	public void setCameraObject(CameraInterface camObj){
		this.cameraObj = camObj;
	}
	
	public void setLaserObject(LaserInterface laserObj){
		this.laserObj = laserObj;
	}
	
	/**
	 * Initialize method, automatically called by @{link FXMLLoader}
	 */
	public void initialize()
	{
		//udao=new UserDaoImpl();
		
		screenBounds = Screen.getPrimary().getBounds();
		gridPane1.prefWidthProperty().bind(rootPane1.widthProperty());
		gridPane1.prefHeightProperty().bind(rootPane1.heightProperty());
		
		currentFrame.fitWidthProperty().bind(currentFrameContainerPane.widthProperty());
		currentFrame.fitHeightProperty().bind(currentFrameContainerPane.heightProperty());
		
		//currentFrameContainerPane.setCenter(currentFrame);
		if(cameraObj!=null) {
			try {
				IniFile iniCameraPresets=new IniFile(ConfigurationProps.config_file_camera_presets);
				String focus = iniCameraPresets.readStringValue(CameraPresets.sunnyModeSection, CameraPresets.focusKey);
				int focusIndex = 1;
				try {
					focusIndex =  Integer.parseInt(focus);
				}catch(java.lang.Exception ex){
					focusIndex = 1;
				}
				if (focusIndex == 1)
                {
                    cameraObj.setAutoFocus(true);
                    manageFoucsControl(true);
                }
                else
                {
                    cameraObj.setAutoFocus(false);
                    manageFoucsControl(false);
                }
				
				if(focusIndex!=1) {
					
					String manualFocus = iniCameraPresets.readStringValue(CameraPresets.sunnyModeSection, CameraPresets.manualFocusKey);
	                if (manualFocus.length() == 4)
	                {
	                	String str = "0"+manualFocus.charAt(0)+"0"+manualFocus.charAt(1)+"0"+manualFocus.charAt(2)+manualFocus.charAt(3);
	                	cameraObj.setManualFocusByValue(str);
	                }
				}
				
				int dzoom = 0;
				String str=iniCameraPresets.readStringValue(CameraPresets.sunnyModeSection, CameraPresets.DigitalZoomKey);
				if(str.length()>0) {
					try {
						dzoom = Integer.parseInt(str);
					}catch(java.lang.Exception ex) {
						LogWriter.appendLog("Error while reading d zoom value="+ex.toString());
						dzoom = 0;
					}
				}
				if(dzoom<CameraProperties.dZoomArr.length) {
					switchDigitalZoom();
				}
				
			}catch(java.lang.Exception ex) {
				System.err.println("error while setting focus="+ ex.toString());
			}
		}
		
		
		try {
			IniFile ini= new IniFile(ConfigurationProps.config_file);	
			int speedIndex = ini.readIntValue(ConfigurationProps.IniSectionAndKeys.speedLimitSection, ConfigurationProps.IniSectionAndKeys.selectedSpeedIndex);
			changeTargetSpeed(speedIndex);
			
			String deptName=ini.readStringValue(ConfigurationProps.IniSectionAndKeys.departmentNameSection, ConfigurationProps.IniSectionAndKeys.keyValue);
			if(deptName.length()<=0) {
				CurrentSessionClass.departmentName="Integra Design"; 
			}else {
				CurrentSessionClass.departmentName=deptName;
			}
			
			String operatorName=ini.readStringValue(ConfigurationProps.IniSectionAndKeys.operatorSection, ConfigurationProps.IniSectionAndKeys.keyValue);
			if(operatorName.length()<=0) {
				CurrentSessionClass.operatorName = "Integra Design"; 
			}else {
				CurrentSessionClass.operatorName = operatorName;
			}
			
			String location = ini.readStringValue(ConfigurationProps.IniSectionAndKeys.locationSection, ConfigurationProps.IniSectionAndKeys.keyValue);
			if(location.length()<=0) {
				CurrentSessionClass.location = "Delhi";
			}else {
				CurrentSessionClass.location = location;
			}
			
			String device_sr=ini.readStringValue(ConfigurationProps.IniSectionAndKeys.deviceSrSection, ConfigurationProps.IniSectionAndKeys.keyValue);
			if(device_sr.length()<=0) {
				CurrentSessionClass.device_sr = device_sr;
			}
			
			String record_nr=ini.readStringValue(ConfigurationProps.IniSectionAndKeys.recordNrSection, ConfigurationProps.IniSectionAndKeys.keyValue);
			if(record_nr.length()<=0) {
				int rec_nr = Integer.parseInt(record_nr);
				try {
					CurrentSessionClass.setRecordNr(rec_nr); 
				}catch(java.lang.Exception ex) {
					CurrentSessionClass.setRecordNr(1); 
				}
			}
			
		}catch(java.lang.Exception ex) {
			
		}
		System.out.println("stage 1");
		
		setupTextFont();
		
		//off Infared at startup
        UsbRelay.closeRelay(UsbRelay.RelayTwo);
		
        System.out.println("stage 2");
        
		initActions();
		
		//tensorlib= new TensorLibrary();
		System.out.println("stage 2.5");
		
		//tensorOcr = new TensorOcr();
		
		System.out.println("stage 3");
		
		initializeListViewImage();
//		if(snapShotWindow==null){
//			Platform.runLater(new Runnable() {
//	            @Override
//	            public void run() {
//	            	initializeSnapShotWindow();//camera_started = true;
//	            }
//	        });
//		}
		
		Platform.runLater(new Runnable() {
//            @Override
            public void run() {
            	//System.out.println(controller.labelLog.getText());
                //label.setText("Input was : " + userInput);
            	try {
            		Thread.sleep(500);	
            	}catch(java.lang.Exception ex) {
            		
            	}
            	startCamera();
            	activateLive();
            	
            	//camera_started = true;
            }
        });
		
		
    	
	}
	void startCheckAutoEvidence() {
		Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
    		//int count=0;
    	    @Override
    	    public void handle(ActionEvent event) {
    	    	checkAutoEvidence();
    	    }
    	    
    	}));
    	fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
    	fiveSecondsWonder.play();
	}
	
	void checkAutoEvidence() {
		if(dequeAsStack.size()>0) {
    		//System.out.println(count+" hello ocr = "+MyUtils.logTimeFormatter.format(new Date()).toString());
    		List<ListViewImageItem> list=new ArrayList<ListViewImageItem>();
    		for(int i=0;i<dequeAsStack.size();i++) {
    			EvidenceClass e=dequeAsStack.poll();
    	    	if(e==null) {
    	    		System.out.println("null = size="+dequeAsStack.size());
    	    		break;	
    	    	}
    	    	TensorResult selectedPlate = e.result;
    	    	BufferedImage np = TensorUtil.cropImage(e.img, selectedPlate.rect);
    	    	try {
//    	    		File f = new File("/home/LaserConfig/np"+count+".jpg");
//        	    	ImageIO.write(np, "JPEG", f);
        	    		
    	    	}catch(java.lang.Exception ex) {
    	    		System.out.println("file save error"+ex.toString());
    	    	}
    	    	String base64 = TensorUtil.encodeToString(np, "PNG");
    	    	//System.out.println(base64);
    	    	
    	    	String str= tensorOcr.getOcr(base64);
    	    	
    	    	e.fileInfo.setVehicle_nr(str);
    	    	//list.add(new ListViewImageItem(str, e.fileInfo.getFile_name()) );
    	    	autoEvidenceMag.add(new ListViewImageItem(str, e.fileInfo.getFile_name()));
    	    	
    		}
    		if(list.size()>0) {
//    			Platform.runLater(new Runnable() {
//	                @Override
//	                public void run() {
//	                	autoEvidenceMag.addAll(list);	
//	                }
//	            });
    			System.out.println("added ");
    		}
    		//System.out.println(count+" hello ocr ends = "+MyUtils.logTimeFormatter.format(new Date()).toString());
	
    	}
    	
	}
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	void startAutoEvidence() {
		Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.millis(700), new EventHandler<ActionEvent>() {
    		int count=0;
    	    @Override
    	    public void handle(ActionEvent event) {
    	    	System.out.println(count+" hello world = "+MyUtils.logTimeFormatter.format(new Date()).toString());
    	    	processImage();
    	    	System.out.println(count+" hello world ends = "+MyUtils.logTimeFormatter.format(new Date()).toString());
    	    		
    	    	count += 1;
    	    	if(laserObj!=null) 
    	    	{
     	    		if(laserObj.isLaserOn() && laserObj.getLaserSpeed()>selectedSpeedLimitEL ) {
        	    		//asas
        	    		
        	    	}	
    	    	}
    	    	
    	    }
    	}));
    	fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
    	fiveSecondsWonder.play();
    	
    	//startCheckAutoEvidence();
    	
    	final Runnable beeper = new Runnable() {
            public void run() { 
            	checkAutoEvidence();
            }
    	};
    	final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 100, 200, TimeUnit.MILLISECONDS);
	}
	
	class EvidenceClass{
		public BufferedImage img;
		public FileInfo fileInfo;
		public List<TensorResult> tensorResults;
		public TensorResult result;
	}
	
	Deque<EvidenceClass> dequeAsStack = new ArrayDeque<EvidenceClass>();
	void processImage() {
//		currentSnapShot = null;
//		takeSnapShot = true;
//		System.out.println("snap start time:"+MyUtils.recordingDTFormatter.format(new Date())); 
//		while(takeSnapShot) {
//			if(currentSnapShot!=null) {
//				break;
//			}
//			try {
//				Thread.sleep(1);
//			}catch(java.lang.Exception ex) {
//				
//			}
//		}
		//currentSnapShot = SwingFXUtils.fromFXImage(currentFrame.getImage(), null); 
		BufferedImage img=SwingFXUtils.fromFXImage(currentFrame.getImage(), null); 
		Date date1 = new Date();
		String tempStr = ConfigurationProps.snap_dir+"/"+ MyUtils.directoryDTFormatter.format(date1);
        File fileDir= new File(tempStr); 
		if(fileDir.exists() ==false) {
			fileDir.mkdir();
		}
		String tempStr2 =tempStr+"/"+ MyUtils.recFileNameDTFormatter.format(date1);
		int count=1;
		while(true) {
			if(new File(tempStr2+".png").exists()==false) {
				tempStr2 = tempStr+"/"+ MyUtils.recFileNameDTFormatter.format(date1)+".s"+count;
				break;
			}
			count+=1;
		}
		
		final String tempFileName= tempStr2+".png";
		
		Runnable r = new Runnable() {
			public void run() {
				saveSnapshot(img, tempFileName);
			}
		};
 
		Thread t = new Thread(r);
		t.start();
		
		FileInfo fileInfo =new FileInfo();
		fileInfo.setDate_created(date1);
		fileInfo.setDate_evidence(date1);
		fileInfo.setDepartment_name(CurrentSessionClass.departmentName);
		fileInfo.setDevice_sr(CurrentSessionClass.device_sr);
		fileInfo.setFile_name(tempFileName);
		fileInfo.setLatitude("0");
		fileInfo.setLongitude("0");
		fileInfo.setLocation(CurrentSessionClass.location);
		fileInfo.setNumber_plate_area("0,0,1279,719");
		fileInfo.setOperator(CurrentSessionClass.operatorName);
		fileInfo.setRecord_nr(CurrentSessionClass.getRecordNr());
		fileInfo.setSound_limit(85);
		
		fileInfo.setSpeed(0);
		fileInfo.setSpeed_limit(45);
		fileInfo.setVehicle_nr("");
		
		List<TensorResult> tensorResult=tensorlib.runTensor(img);
		TensorResult selectedPlate=TensorUtil.getCenterNumberPlate(tensorResult);
		
//		int width =(int) (screenBounds.getWidth()*37/100);
//		int height =(int) (screenBounds.getHeight() *77/100);
		EvidenceClass e = new EvidenceClass();
		e.img = img;
		e.fileInfo = fileInfo;
		e.tensorResults = tensorResult;
		e.result = selectedPlate;
		
		dequeAsStack.addLast(e);
		System.out.println("add new evidence");
	}
	
	void changeTargetSpeed(int selectedLimitIndex)
    {
		try {
			IniFile ini = new IniFile(ConfigurationProps.config_file);
	        ini.putValue(ConfigurationProps.IniSectionAndKeys.speedLimitSection, ConfigurationProps.IniSectionAndKeys.selectedSpeedIndex, selectedLimitIndex + "");

	        String elkey="";
	        String vlkey="";
	        //setSelectLimitIndex(selectedLimitIndex);
	        switch (selectedLimitIndex) {
	            case 1:
	                //active
	                //btnSetSpeedBike.Image = Properties.Resources.bike_icon_red_32;

	                //deactive
	                //btnSetSpeedCar.Image = Properties.Resources.car_icon_32;
	                //btnSetSpeedTruck.Image = Properties.Resources.truck_icon_32;
	            	elkey = ConfigurationProps.IniSectionAndKeys.eL1Speed;
	            	vlkey = ConfigurationProps.IniSectionAndKeys.vL1Speed;
	                break;
	            case 2:
	                //active
	                //btnSetSpeedCar.Image = Properties.Resources.car_icon_red_32;

	                //deactive
	                //btnSetSpeedBike.Image = Properties.Resources.bike_icon_32;
	                //btnSetSpeedTruck.Image = Properties.Resources.truck_icon_32;
	            	elkey = ConfigurationProps.IniSectionAndKeys.eL2Speed;
	            	vlkey = ConfigurationProps.IniSectionAndKeys.vL2Speed;
	            	break;
	            case 3:
	                //active
	                //btnSetSpeedTruck.Image = Properties.Resources.truck_icon_red_32;

	                //deactive
	                //btnSetSpeedCar.Image = Properties.Resources.car_icon_32;
	                //btnSetSpeedBike.Image = Properties.Resources.bike_icon_32;
	            	elkey = ConfigurationProps.IniSectionAndKeys.eL3Speed;
	            	vlkey = ConfigurationProps.IniSectionAndKeys.vL3Speed;
	            	break;
	        }
	        
	        if(elkey.length()>0) {
	        	selectedSpeedLimitEL = ini.readIntValue(ConfigurationProps.IniSectionAndKeys.speedLimitSection, elkey);
	        	selectedSpeedLimitVL = ini.readIntValue(ConfigurationProps.IniSectionAndKeys.speedLimitSection, vlkey);
	        }else {
	        	selectedLimitIndex = 0;
	        	selectedSpeedLimitEL = 0;
	        	selectedSpeedLimitVL = 0;
	        }
		}catch(java.lang.Exception ex) {
			System.out.println("changeTargetSpeed"+ex.toString());
			LogWriter.appendLog("changeTargetSpeed"+ex.toString());
		}
    }
	
	void startStopRecordingManual() {
		if(startRecording==false) {
			startRecording(true);
		}else {
			stopRecording();
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
	BufferedImage currentNumberPlate;
	String currentNumberPlateText="";
	
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
				Thread.sleep(1);
			}catch(java.lang.Exception ex) {
				
			}
		}
		int speed=0;
		if(laserObj!=null) {
			speed=laserObj.getLaserSpeed();
		}
		
		if(snapShotWindow==null){
			Platform.runLater(new Runnable() {
	            @Override
	            public void run() {
	            	initializeSnapShotWindow();//camera_started = true;
	            }
	        });
		}
		
		System.out.println("snap end time:"+MyUtils.recordingDTFormatter.format(new Date())); 
		
		Date date1 = new Date();
		String tempStr = ConfigurationProps.snap_dir+"/"+ MyUtils.directoryDTFormatter.format(date1);
        File fileDir= new File(tempStr); 
		if(fileDir.exists() ==false) {
			fileDir.mkdir();
		}
		String tempStr2 =tempStr+"/"+ MyUtils.recFileNameDTFormatter.format(date1);
		int count=1;
		while(true) {
			if(new File(tempStr2+".png").exists()==false) {
				tempStr2 = tempStr+"/"+ MyUtils.recFileNameDTFormatter.format(date1)+".s"+count;
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
		FileInfo fileInfo =new FileInfo();
		fileInfo.setDate_created(date1);
		fileInfo.setDate_evidence(date1);
		fileInfo.setDepartment_name(CurrentSessionClass.departmentName);
		fileInfo.setDevice_sr(CurrentSessionClass.device_sr);
		fileInfo.setFile_name(tempFileName);
		fileInfo.setLatitude("0");
		fileInfo.setLongitude("0");
		fileInfo.setLocation(CurrentSessionClass.location);
		fileInfo.setNumber_plate_area("0,0,1279,719");
		fileInfo.setOperator(CurrentSessionClass.operatorName);
		fileInfo.setRecord_nr(CurrentSessionClass.getRecordNr());
		fileInfo.setSound_limit(85);
		
		fileInfo.setSpeed(speed);
		fileInfo.setSpeed_limit(45);
		fileInfo.setVehicle_nr("");
		
		List<TensorResult> tensorResult=tensorlib.runTensor(currentSnapShot);
		TensorResult selectedPlate=TensorUtil.getCenterNumberPlate(tensorResult);
		
		int width =(int) (screenBounds.getWidth()*37/100);
		int height =(int) (screenBounds.getHeight() *77/100);
		
		
		currentNumberPlate = null;
		if(selectedPlate!=null) {
			
			currentNumberPlate = TensorUtil.cropImage(currentSnapShot, selectedPlate.rect);
			System.out.println("myocr start time="+MyUtils.logTimeFormatter.format(new Date()));
			currentNumberPlateText = tensorOcr.getOcr(TensorUtil.encodeToString(currentNumberPlate, "JPG"));
			System.out.println("myocr ends time="+MyUtils.logTimeFormatter.format(new Date()));
			
			String temp = selectedPlate.rect.x+","+selectedPlate.rect.y+","+selectedPlate.rect.width+","+selectedPlate.rect.height;
			fileInfo.setNumber_plate_area(temp);
			fileInfo.setVehicle_nr(currentNumberPlateText);
			
			System.out.println("---------------");
		}
		
		Platform.runLater(new Runnable() {
            @Override
            public void run() {
            	controllerSnapShot.updateImageView(currentSnapShot, tempFileName, width);
        		controllerSnapShot.handOverWindowHandle(snapShotWindow);
        		if(selectedPlate!=null) {
        			controllerSnapShot.updateNumberPlateView( currentNumberPlate, currentNumberPlateText, 0);
        			//controllerSnapShot.updateNumberPlateText( str);
        		}
        		snapShotWindow.setVisible(true);
            }
        });
		//udao.addFileInfo(fileInfo);    	
		
//		if(selectedPlate!=null) {
//			System.out.println("myocr start time="+MyUtils.logTimeFormatter.format(new Date()));
//			String str = tensorOcr.getOcr(TensorUtil.encodeToString(currentNumberPlate, "JPG"));
//			System.out.println("myocr end time="+MyUtils.logTimeFormatter.format(new Date()));
//			Platform.runLater(new Runnable() {
//	            @Override
//	            public void run() {
//	    			controllerSnapShot.updateNumberPlateText( str);
//	            }
//	        });
//
//		}

	}
	
	class SayHello extends TimerTask {
	    public void run() {
	       System.out.println("Hello World!"); 
	    }
	}
	
	Window listViewWindow; 
	ListView<ListViewImageItem> autoEvidenceList;
	void initializeListViewImage() {
		int width =(int) (screenBounds.getWidth()*13/100);
		int height =(int) (screenBounds.getHeight() *67/100);
		
		listViewWindow = new Window(); 
		listViewWindow.setMovable(false);
		listViewWindow.setResizableWindow(false);
		listViewWindow.setTitleBarStyleClass("windowTitleBar");
		listViewWindow.setMinimized(false);
		
		listViewWindow.setMinWidth((double)width);
		listViewWindow.setMinHeight((double)height);
//		listViewWindow.setBackground(Background.);
		
		listViewWindow.setLayoutX(screenBounds.getWidth()-(width+10));
		//System.out.println("list view x= "+(screenBounds.getWidth()-(width+5)) +"  ,y="+(screenBounds.getHeight() - (height+snapBtn.getHeight())));
		//listViewWindow.setLayoutY(currentFrame.getFitHeight() - (height+5));
		listViewWindow.setLayoutX(200);
		//listViewWindow.setLayoutY(100);
//		autoEvidenceList = new ListView<>(FXCollections.observableArrayList(
//		        new ListViewImageItem("Straight", "https://d30y9cdsu7xlg0.cloudfront.net/png/17383-200.png")
//		        ));
		//autoEvidenceMag.add(new ListViewImageItem("straight", "https://d30y9cdsu7xlg0.cloudfront.net/png/17383-200.png"));
		autoEvidenceList =new ListView<>(autoEvidenceMag);
		
		//autoautoEvidenceList.
		// Custom cell factory
		autoEvidenceList.setCellFactory(l -> new ListCell<ListViewImageItem>() {
		    @Override
		    public void updateItem(final ListViewImageItem item, final boolean empty) {
		        if (empty) {
		            setText("");
		            setGraphic(null);
		        } else {
		            Platform.runLater(new Runnable() {
		                @Override
		                public void run() {
		                	setText(item.getText());
				            setGraphic(item.getImage());    	
				            //this.setStyle("-fx-content-display: top;");
		                }
		            });
		            //getGraphic().maxWidth(width-10);
		            //this.setStyle("-fx-content-display: top;");
		        }
		    }
		});
		listViewWindow.getContentPane().getChildren().add(autoEvidenceList);
		rootPane1.getChildren().add(listViewWindow); 
		listViewWindow.setVisible(true);
		
	}
	ListViewEvidenceController listViewController;
	void initializeListViewImage2(){
		try{
			int width =(int) (screenBounds.getWidth()*13/100);
			int height =(int) (screenBounds.getHeight() *77/100);
    		//saveSnapshot(currentSnapShot);
			System.out.println("listview init:"+MyUtils.recordingDTFormatter.format(new Date())); 
			//Node node = FXMLLoader.load(getClass().getResource("/com/lasertrac/view/SnapshotShow.fxml"));
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lasertrac/view/ListViewEvidence.fxml"));
			
			Node node = loader.load();
			
			// create a window with title "My Window"
			listViewWindow = new Window();
	        
			listViewWindow.setMovable(false);
			listViewWindow.setResizableWindow(false);
			listViewWindow.setTitleBarStyleClass("windowTitleBar");
			listViewWindow.setMinimized(false);
			
			// set the window position to 10,10 (coordinates inside canvas)
			listViewWindow.setLayoutX(screenBounds.getWidth()-width+5);
			listViewWindow.setLayoutX(200);
			
			//System.out.println("screen height="+screenBounds.getHeight()+"  , height="+height+" , grid height="+currentFrame.getFitHeight());
			//snapShotWindow.setLayoutY(screenBounds.getHeight()-(height+snapBtn.getHeight()+5));
			listViewWindow.setLayoutY(currentFrame.getFitHeight() - (height+5));
			listViewWindow.setLayoutY(100);
			// define the initial window size
			listViewWindow.setPrefSize(width, height);
	        // either to the left
			//snapShotWindow.getLeftIcons().add(new CloseIcon(snapShotWindow));
	        // .. or to the right
			//snapShotWindow.getRightIcons().add(new MinimizeIcon(snapShotWindow));
	        // add some content
			//snapShotWindow.getContentPane().getChildren().add(new Label("Content... \nof the window#"));
			
			listViewController = loader.getController();
			//controllerSnapShot.updateImageView(currentSnapShot, tempFileName, width);
			
			//controllerSnapShot.handOverWindowHandle(snapShotWindow);
			//controller.
			listViewWindow.getContentPane().getChildren().add(node);
			
	        // add the window to the canvas
	        rootPane1.getChildren().add(listViewWindow); 
	        System.out.println("done init winndow ");
	        
	        listViewWindow.setVisible(true);
	        
		}catch(java.lang.Exception ex) {
		    	System.out.println("snap shot error "+ex.toString());
		    	ex.printStackTrace();
		}
	}
	
	void initializeSnapShotWindow(){
		try{
			int width =(int) (screenBounds.getWidth()*37/100);
			int height =(int) (screenBounds.getHeight() *77/100);
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
			//controllerSnapShot.updateImageView(currentSnapShot, tempFileName, width);
			
			//controllerSnapShot.handOverWindowHandle(snapShotWindow);
			//controller.
			snapShotWindow.getContentPane().getChildren().add(node);
			
	        // add the window to the canvas
	        rootPane1.getChildren().add(snapShotWindow); 
	        
		}catch(java.lang.Exception ex) {
		    	System.out.println("snap shot error "+ex.toString());
		    	ex.printStackTrace();
		}
	}
	
	public static void saveSnapshot(BufferedImage image, String tempFileName) {
	    //File outputFile = new File("C:/JavaFX/");
//	    Date date1 = new Date();
//		String tempFileName = ConfigurationProps.snap_dir+"/"+ MyUtils.directoryDTFormatter.format(date1);
//        File fileDir= new File(tempFileName); 
//		if(fileDir.exists() ==false) {
//			fileDir.mkdir();
//		}
//		
//		tempFileName+="/"+ MyUtils.recFileNameDTFormatter.format(date1)+".png";
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
	        boolean audio_record = false;
	        if(audio_record) {
	        	// Thread for audio capture, this could be in a nested private class if you prefer...
		        new Thread(new Runnable() {
//		            @Override
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
//		                        @Override
		                        public void run()
		                        {
		                            try
		                            {
		                                
		                                
		                                if(startRecording ) {
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
		                    }, 0, (long) 2000 / FRAME_RATE, TimeUnit.MILLISECONDS);
		                } 
		                catch (LineUnavailableException e1)
		                {
		                    e1.printStackTrace();
		                }
		            }
		        }).start();
	        }
	     
	        
	        new Thread(new Runnable() {
//	            @Override
	            public void run()
	            {
	            	try {
	            		final Java2DFrameConverter converter = new Java2DFrameConverter();
		    	        Frame capturedFrame = null;

		    	        String watermark = "movie="+ConfigurationProps.config_dir+"/frame1_2b.png[watermark];[in][watermark]overlay=W-w-0:0:format=rgb[out]";
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
	                        
	                        //top line
	                        w.setColor(Color.WHITE);
	                        w.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
	                        w.drawString(dateFormatter.format(new Date()), 2, 25);
	                        
	                        //speed and sound 
	                        String speed= "000 kph";
	                        if(laserObj!=null) {
	                        	speed=laserObj.getLaserSpeed()+" "+laserObj.getLaserSpeedUnit();
	                        }
	                        
	                        w.setColor(Color.YELLOW);
	                        w.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 70));
	                        int width = w.getFontMetrics().stringWidth(speed);
	                        
	                        w.drawString(speed, 1280-width, 95);
	                        
	                        String sound="65 db";
	                        width = w.getFontMetrics().stringWidth(sound);
	                        w.setColor(Color.YELLOW);
	                        w.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 70));
	                        w.drawString(sound, 1280-width, 175);
	                        
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
		    	        			//System.out.println("copy start time:"+MyUtils.recordingDTFormatter.format(new Date())); 
//		    	        			currentSnapShot =converter.convert(filteredFrame);
//			                        Graphics2D w1 = (Graphics2D) currentSnapShot.getGraphics();
//			                        w1.setColor(Color.WHITE);
//			                        w1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
//			                        w1.drawString(dateFormatter.format(new Date()), 2, 25);
		    	        			//currentSnapShot = deepCopy(imgB);
		    	        			currentSnapShot = imgB;
		    	        			takeSnapShot = false;
		    	        			//System.out.println("copy end time:"+MyUtils.recordingDTFormatter.format(new Date())); 
		    	        		}
		    	        		
		    	        		updateImageView(currentFrame,SwingFXUtils.toFXImage(imgB, null));
		    	        	}
		    	        	
		    	            if(startRecording) {
		    	            	// Let's define our start time...
			    	            // This needs to be initialized as close to when we'll use it as
			    	            // possible,
			    	            // as the delta from assignment to computed time could be too high
			    	            if (startTime == 0)
			    	                startTime = System.currentTimeMillis();

			    	            // Create timestamp for this frame
			    	            videoTS = 1000 * (System.currentTimeMillis() - startTime);
			    	            
		    	            	// Check for AV drift
		    		            if (videoTS > recorder.getTimestamp())
		    		            {
//		    		                System.out.println(
//		    		                        "Lip-flap correction: " 
//		    		                        + videoTS + " : "
//		    		                        + recorder.getTimestamp() + " -> "
//		    		                        + (videoTS - recorder.getTimestamp()));

		    		                // We tell the recorder to write this frame at this timestamp
		    		                recorder.setTimestamp(videoTS);
		    		            }

		    		            // Send the frame to the org.bytedeco.javacv.FFmpegFrameRecorder
		    		            recorder.record( converter.convert(imgB) );
		    	            }else if(countStopRec==1){
		    	            	Date date1 = new Date();
		    	            	System.out.println("recording goes off "+MyUtils.logTimeFormatter.format(date1));
		    	            	countStopRec = 0;
		    	            }
		    	            
		    	        }
	            	}catch(Exception ex) {
	            		
	            		System.out.println("initialize Exception="+ex.toString());
	            	}catch(org.bytedeco.javacv.FrameGrabber.Exception ex) {
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
	
	protected void startRecording(boolean manual) {
		Date date1 = new Date();
		String tempFileName = ConfigurationProps.video_dir+"/"+ MyUtils.directoryDTFormatter.format(date1);
        File fileDir= new File(tempFileName); 
		if(fileDir.exists() ==false) {
			fileDir.mkdir();
		}
		tempFileName+="/"+ MyUtils.recFileNameDTFormatter.format(date1)+".mp4";
		//tempFileName =  MyUtils.recFileNameDTFormatter.format(date1)+".mp4";
		
		System.out.println(tempFileName);
		recorder =createRecorderObj(tempFileName, 1280, 720);
		//date1 = new Date();
		System.out.println("before recorder start "+MyUtils.logTimeFormatter.format(date1));
		//count+=1;
        // Jack 'n coke... do it...
        try {
			recorder.start();
			date1 = new Date();
			System.out.println("after recorder start "+MyUtils.logTimeFormatter.format(date1));
	        //grabber.stop();	
		}catch(Exception ex) {
			System.out.println("start rec Exception="+ex.toString());
		}
		startRecording=true;
		recBtn.setText("Stop Rec");
		countStopRec = 1;
	}
	int countStopRec = 0;
	protected void stopRecording() {
		Date dt1= new Date();
		startRecording=false;
		try {
			Thread.sleep(1);
			
			System.out.println("before recorder stop "+MyUtils.logTimeFormatter.format(dt1));
			recorder.stop();
			//recorder.flush();
			dt1=new Date();
			System.out.println("after recorder stop "+MyUtils.logTimeFormatter.format(dt1));
			recorder.release();
			dt1=new Date();
			System.out.println("after recorder release "+MyUtils.logTimeFormatter.format(dt1));
			
		}catch(InterruptedException ex1) {
			System.out.println("stop rec Exception="+ex1.toString());
		}
		catch(Exception ex) {
			System.out.println("stop rec Exception="+ex.toString());
		}
		startTime=0;
		recBtn.setText("Rec");
		
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
        recorder.setVideoBitrate(25*1280*720);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setFormat("mp4");
        // FPS (frames per second)
        recorder.setFrameRate(FRAME_RATE);
        // Key frame interval, in our case every 2 seconds -> 30 (fps) * 2 = 60
        // (gop length)
        recorder.setGopSize(GOP_LENGTH_IN_FRAMES);

        // We don't want variable bitrate audio
        recorder.setAudioOption("crf", "0");
        // Highest quality
        recorder.setAudioQuality(0);
        // 192 Kbps
        recorder.setAudioBitrate(192000);
        recorder.setSampleRate(44100);
        recorder.setAudioChannels(2);
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        
        System.out.println("cmd  = "+recorder.getFormat());	
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
	
	
	/// <summary>manageFoucsControl.
    /// <para>Manage controls related to camera focus</para>
    /// </summary>
    public void manageFoucsControl(boolean isAutoFocus)
    {
        if (isAutoFocus)
        {
            focusMinusBtn.setDisable(true); ;
            focusPlusBtn.setDisable(true);
            focusAutoManualBtn.setText("Focus\nAuto  ");
        }
        else
        {
        	focusMinusBtn.setDisable(false); 
        	focusPlusBtn.setDisable(false);;
            focusAutoManualBtn.setText("Focus\nManual ");
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
		
		dZoomBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
			}
		});
		dZoomBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				
			}
		});
		
		focusAutoManualBtn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				
			}
		});
		focusAutoManualBtn.setOnTouchPressed(new EventHandler<TouchEvent>(){
			@Override
			public void handle(TouchEvent event) {
				
			}
		});
		
		focusPlusBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if(cameraObj != null) {
					
				}
			}
		});
		focusPlusBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if(cameraObj != null) {
					
				}
			}
		});
		focusPlusBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				
			}
		});
		focusPlusBtn.setOnTouchReleased(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				
			}
		});
		
		
		focusMinusBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if(cameraObj != null) {
					
				}
			}
		});
		focusMinusBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if(cameraObj != null) {
					
				}
			}
		});
		focusMinusBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				
			}
		});
		focusMinusBtn.setOnTouchReleased(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				
			}
		});
		
		
		infraredBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//listViewWindow.setVisible(true);
//				Platform.runLater(new Runnable() {
////		            @Override 
//		            public void run() {
////		            	autoEvidenceMag.add(new ListViewImageItem("straight", "https://d30y9cdsu7xlg0.cloudfront.net/png/17383-200.png"));
//		            }
//		        });
				
				operateInfrared();
			}
		});
		
		infraredBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				operateInfrared();
			}
		});
		
		
		
		bikeSpeedBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				startAutoEvidence();	
			}
		});
		bikeSpeedBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				
			}
		});
		
		carSpeedBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
			}
		});
		carSpeedBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				
			}
		});
		
		truckSpeedBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
			}
		});
		truckSpeedBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				
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
		
		
		soundBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent arg0) {
				openSoundPanel();
			}
		});
		soundBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				openSoundPanel();
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
//				Platform.runLater(new Runnable() {
////		            @Override
//		            public void run() {
//		            	takeSnapShot();
//		           }
//		        });
				Runnable r = new Runnable() {
					public void run() {
						takeSnapShot();
					}
				};
		 
				Thread t = new Thread(r);
				t.start();
			}
		});
		
	}
	HBox  soundPane = null;
	Button micBtn = null;
	Button audioBtn = null ;
	void openSoundPanel(){
		System.out.println(" aya re babu rao");
		if(soundPane == null) {
			int btnWidth = (int) soundBtn.getWidth()/2;
//			Label lbl=new Label("asas");
//			lbl.relocate(50, 10);
			
			micBtn=new Button();
			
			micBtn.setPrefSize(btnWidth, 80);
			micBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if(micMute) {
						setMicMute(false);	
					}else {
						setMicMute(true);
					}
				}
			});
			micBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
				@Override
				public void handle(TouchEvent event) {
					if(micMute) {
						setMicMute(false);	
					}else {
						setMicMute(true);
					}
				}
			});
			
			audioBtn=new Button();
			
			audioBtn.setPrefSize(btnWidth, 80);
			audioBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if(SoundAndMic.getVolume()>0) {
						setVolumeMute(true);	
					}else {
						setVolumeMute(false);
					}
					
				}
			});
			audioBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
				@Override
				public void handle(TouchEvent event) {
					if(SoundAndMic.getVolume()>0) {
						setVolumeMute(true);	
					}else {
						setVolumeMute(false);
					}
				}
			});
			
			
			List<ControlsHelper> controlBtns=new ArrayList<ControlsHelper>();
			
			
			if(micMute) {
				controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/mic_icon_mute_58.png"),"", micBtn, 40, 40) );	
			}else{
				controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/mic_icon_58.png"),"", micBtn, 40, 40) );
			}
			
			if(SoundAndMic.getVolume()>0) {
				controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/sound_on_58.png"),"", audioBtn, 40, 40) );	
			}else {
				controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/sound_mute_58.png"),"", audioBtn, 40, 40) );
			}
			
			
			ImageView iv1;
			//Image img;
			try {
				for(ControlsHelper ch : controlBtns) {
					iv1=new ImageView(new Image(ch.img.openStream()));
//					iv1.maxHeight(ch.iconHeight);
					iv1.setFitHeight(ch.iconHeight);
					iv1.setFitWidth(ch.iconWidth);
					iv1.setPreserveRatio(true);
					ch.btn.setStyle("-fx-background-color: transparent;");
					
//					Text text1 = new Text( ch.text);
//					text1.setFont(javafx.scene.text.Font.font ("Arial", FontWeight.BOLD, 14));
//					text1.setFill(javafx.scene.paint.Color.WHITE);
//					text1.setTextAlignment(TextAlignment.CENTER);
					
//					StackPane stackPane1 = new StackPane( text1,iv1);
//				    StackPane.setAlignment( text1, Pos.CENTER_RIGHT);
//				    StackPane.setAlignment( iv1, Pos.CENTER_LEFT );
//				    stackPane1.setMinHeight(ch.iconHeight);
//				    stackPane1.maxHeight(ch.iconHeight);
				    ch.btn.setText("");
				    ch.btn.setGraphic(iv1);
				}
			}catch(java.lang.Exception ex) {
				ex.printStackTrace();
			}
			
			soundPane= new HBox();
			
			soundPane.prefHeight(60);
			soundPane.prefWidth(180);
			soundPane.setStyle("-fx-background-color : #303030;");
			soundPane.setLayoutX(soundBtn.getLayoutX()+leftSideControlGrid.getWidth());
			soundPane.setLayoutY(currentFrameContainerPane.getHeight()-80);
			
			soundPane.getChildren().add(micBtn);
			soundPane.getChildren().add(audioBtn);
			
			//as
			
			rootPane1.getChildren().add(soundPane);
			//soundPane.relocate(200, 200);
			//soundPane.setLayoutX(soundBtn.getLayoutX());
			//soundPane.setLayoutY(soundBtn.getLayoutY());
			
			soundPane.toFront();
			//System.out.println(" aya re babu  121 rao x="+soundBtn.getLayoutX()+" , y="+soundBtn.getLayoutY ());
				
		}else {
			if(soundPane.isVisible()) {
				soundPane.setVisible(false);	
			}else {
				soundPane.setVisible(true);
			}
		}
	}
	
	boolean micMute=false;
	void setMicMute(boolean mute) {
		try {
			if(mute) {
				if(micBtn!=null) {
					ImageView iv1=new ImageView(new Image(getClass().getResource("/images/large/mic_icon_mute_58.png").openStream()));
//					iv1.maxHeight(ch.iconHeight);
					iv1.setFitHeight(40);
					iv1.setFitWidth(40);
					iv1.setPreserveRatio(true);
					//audioBtn.setStyle("-fx-background-color: transparent;");
					micBtn.setGraphic(iv1);
				}
				MyAudioManager.setMicMute(true);
				micMute=true; 
				IniFile ini =new IniFile(ConfigurationProps.config_file);
				ini.putValue(ConfigurationProps.IniSectionAndKeys.Mic, ConfigurationProps.IniSectionAndKeys.keyValue, 1+"");
				
			}else {
				if(micBtn!=null) {
					ImageView iv1=new ImageView(new Image(getClass().getResource("/images/large/mic_icon_58.png").openStream()));
//					iv1.maxHeight(ch.iconHeight);
					iv1.setFitHeight(40);
					iv1.setFitWidth(40);
					iv1.setPreserveRatio(true);
					//audioBtn.setStyle("-fx-background-color: transparent;");
					micBtn.setGraphic(iv1);
				}
				MyAudioManager.setMicMute(false);
				micMute=false;
				IniFile ini =new IniFile(ConfigurationProps.config_file);
				ini.putValue(ConfigurationProps.IniSectionAndKeys.Mic, ConfigurationProps.IniSectionAndKeys.keyValue, 0+"");
			}
		}catch(java.lang.Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	void setVolumeMute(boolean mute) {
		try {
			if(mute) {
				if(audioBtn!=null) {
					ImageView iv1=new ImageView(new Image(getClass().getResource("/images/large/sound_mute_58.png").openStream()));
//					iv1.maxHeight(ch.iconHeight);
					iv1.setFitHeight(40);
					iv1.setFitWidth(40);
					iv1.setPreserveRatio(true);
					//audioBtn.setStyle("-fx-background-color: transparent;");
					audioBtn.setGraphic(iv1);
				}
				SoundAndMic.setVolume(0);	
			}else {
				SoundAndMic.setVolume(90);	
				if(audioBtn!=null) {
					ImageView iv1=new ImageView(new Image(getClass().getResource("/images/large/sound_on_58.png").openStream()));
//					iv1.maxHeight(ch.iconHeight);
					iv1.setFitHeight(40);
					iv1.setFitWidth(40);
					iv1.setPreserveRatio(true);
					//audioBtn.setStyle("-fx-background-color: transparent;");
					audioBtn.setGraphic(iv1);
				}
			}
		}catch(java.lang.Exception ex) {
			ex.printStackTrace();
		}
	}
	
	void operateInfrared() {
		infraredBtn.setDisable(true);
		try {
			if(UsbRelay.isRelayTwoOn()) {
				UsbRelay.closeRelay(UsbRelay.RelayTwo);
			}else {
				UsbRelay.openRelay(UsbRelay.RelayTwo);
			}
			infraredBtn.setDisable(false);	
		}catch(java.lang.Exception ex) {
			LogWriter.appendLog("relay error:"+ex.toString());
		}
	}
	
	public void switchDigitalZoom() {
       String hexValue = "";

       switch (dZoomLevel)
       {
       		case 0:
                //camOBJ.setDZoomDirect0x();
                hexValue = CameraProperties.dZoomArr[0].hexValue;
                dZoomBtn.setText("D-Zoom 0x");
                //btnSet5xDizitalZoom.Image = Properties.Resources.dzoom5x;   
                dZoomLevel = 1;
                break;
            case 1:
                //camOBJ.setDZoomDirect5x();
                hexValue = CameraProperties.dZoomArr[1].hexValue;
                dZoomBtn.setText( "D-Zoom 2x");
                //btnSet5xDizitalZoom.Image = Properties.Resources.dzoom10x;
                dZoomLevel = 2;

                break;
            case 2:
                //camOBJ.setDZoomDirect5x();
                hexValue = CameraProperties.dZoomArr[2].hexValue;
                dZoomBtn.setText( "D-Zoom 3x");
                //btnSet5xDizitalZoom.Image = Properties.Resources.dzoom10x;
                dZoomLevel = 3;

                break;
            case 3:
                //camOBJ.setDZoomDirect10x();
                hexValue = CameraProperties.dZoomArr[3].hexValue;
                dZoomBtn.setText("D-Zoom 4x");
                //btnSet5xDizitalZoom.Image = Properties.Resources.dzoom15x;
                dZoomLevel = 0;

                break;
        }
        cameraObj.setDizitalZoomByValue(hexValue);
	}
	
	void setupTextFont() {
		//Rectangle2D screenBounds = Screen.getPrimary().getBounds();
	    //System.out.println(screenBounds);
	    
		DoubleProperty controlButtonsSize = new SimpleDoubleProperty(ResponsiveConst.fontSize.doubleValue()) ;
		DoubleProperty headerFontSize = new SimpleDoubleProperty(ResponsiveConst.fontSize.doubleValue());
		
		controlButtonsSize.bind(rootPane1.widthProperty().add(rootPane1.heightProperty()).divide(150));
		
		System.out.println(controlButtonsSize.asString());
		
		String btnBackGroundColor ="-fx-background-color: #303030; ";
		StringExpression str = Bindings.concat("-fx-background-color: ", "#303030", ";"
				,"-fx-font-size: ", controlButtonsSize.asString(), ";");
		
		//bottomSideControlGrid.setAlignment(Pos.CENTER_RIGHT);
		ObservableList<Node> bottomBtns = bottomSideControlGrid.getChildren();
		for(Node n:bottomBtns) {
			if(n instanceof Button) {
				//n.styleProperty().bind(Bindings.concat("-fx-font-size: ", controlButtonsSize.asString(), ";"));
				
				//n.setStyle("-fx-font-size:"+controlButtonsSize.asString());
				//n.scaleXProperty().bind(((Button) n).widthProperty().add(((Button) n).widthProperty()).divide(200));
				//n.scaleYProperty().bind(((Button) n).heightProperty().add(((Button) n).heightProperty()).divide(180));
				//n.styleProperty().unbind();
				((Button) n).setAlignment(Pos.CENTER_RIGHT);
				((Button) n).setWrapText(true);
				((Button) n).setPadding(new Insets(2, 6, 2, 6));
				n.setStyle(btnBackGroundColor);
			}
		}
		ObservableList<Node> leftBtns = leftSideControlGrid.getChildren();
		for(Node n:leftBtns) {
			if(n instanceof Button) {
				((Button) n).setAlignment(Pos.CENTER_RIGHT);
				((Button) n).setWrapText(true);
				((Button) n).setPadding(new Insets(2, 6, 2, 6));
				n.setStyle(btnBackGroundColor);
				//((Button) n).setAlignment(Pos.CENTER_RIGHT);
				//n.styleProperty().bind(Bindings.concat("-fx-font-size: ", controlButtonsSize.asString(), ";"));
				//n.styleProperty().bind(str);
			}
		}
		
		recBtn.textAlignmentProperty().set(TextAlignment.RIGHT);
		 
		try {
			
			List<ControlsHelper> controlBtns=new ArrayList<ControlsHelper>();
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/zoom_minus_50.png"),"Zoom -", zoomOutBtn, 30, 30) );
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/zoom_plus_50.png"),"Zoom +", zoomInBtn, 30, 30) );
			
			dZoomBtn.setTextAlignment(TextAlignment.CENTER);
			dZoomBtn.setFont(javafx.scene.text.Font.font ("Arial", FontWeight.BOLD, 14));
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/foucs_50.png"),"Focus\nAuto", focusAutoManualBtn, 30, 30) );
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/foucs_50.png"),"Focus -", focusMinusBtn, 30, 30) );
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/foucs_50.png"),"Focus +", focusPlusBtn, 30, 30) );
			
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/ir_50.png"),"Infrared", infraredBtn, 30, 30) );
			
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/laser_54.png"),"Laser", laserBtn, 40, 20) );
			//controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/rec_icon_50.png"),"Rec", recBtn, 45,20) );
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/recording_cam.png"),"Rec", recBtn, 40,20) );
			
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/car_icon_50.png"),"Car ", carSpeedBtn, 35,35) );
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/bike_icon_50.png"),"Bike ", bikeSpeedBtn, 35,35) );
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/truck icon_50.png"),"Truck ", truckSpeedBtn, 35,35) );
			
			defineSpeedBtn.setTextAlignment(TextAlignment.CENTER);
			defineSpeedBtn.setFont(javafx.scene.text.Font.font ("Arial", FontWeight.BOLD, 14));
			
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/menu_icon_50.png"),"Menu ", menuBtn, 30,30) );
			
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/sound_icon_50.png"),"Sound ", soundBtn, 30,30) );
			
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/helmet_50.png"),"Snap ", snapNoHelmetBtn, 30,30) );
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/snap_shot_50.png"),"Snap ", snapBtn, 30,30) );
			
			controlBtns.add(new ControlsHelper(getClass().getResource("/images/large/weather/sunny_58.png"),"Cam\nMode", camModeBtn, 30,30) );
			
			
			Image img;
			//Text text1; 
			//StackPane stackPane1;
			ImageView iv1;
			for(ControlsHelper ch : controlBtns ) {
				//img = new Image(inZp);
				iv1=new ImageView(new Image(ch.img.openStream()));
//				iv1.maxHeight(ch.iconHeight);
				iv1.setFitHeight(ch.iconHeight);
				iv1.setFitWidth(ch.iconWidth);
				iv1.setPreserveRatio(true);
				
				Text text1 = new Text( ch.text);
				text1.setFont(javafx.scene.text.Font.font ("Arial", FontWeight.BOLD, 14));
				text1.setFill(javafx.scene.paint.Color.WHITE);
				text1.setTextAlignment(TextAlignment.CENTER);
				
				StackPane stackPane1 = new StackPane( text1,iv1);
			    StackPane.setAlignment( text1, Pos.CENTER_RIGHT);
			    StackPane.setAlignment( iv1, Pos.CENTER_LEFT );
			    stackPane1.setMinHeight(ch.iconHeight);
			    stackPane1.maxHeight(ch.iconHeight);
			    ch.btn.setText("");
			    ch.btn.setGraphic(stackPane1);
			}
			
//			InputStream inZp= getClass().getResource("/images/large/zoom_plus_50.png").openStream();
//			Image imageZoomin = new Image(inZp);
//			ImageView iv1=new ImageView(imageZoomin);
//			iv1.setFitHeight(30);
//			iv1.setFitWidth(30);
//			
//			//String text = "Zoom +";
//			Text text1 = new Text( "Zoom +" );
//			text1.setFont(javafx.scene.text.Font.font ("Arial", 14));
//			text1.setFill(javafx.scene.paint.Color.WHITE);
//			StackPane stackPane1 = new StackPane( text1,iv1);
//		    StackPane.setAlignment( text1, Pos.CENTER_RIGHT);
//		    StackPane.setAlignment( iv1, Pos.CENTER_LEFT );
//		    
//		    //Button b=new Button("",stackPane1);
//		    zoomInBtn.setText("");
//		    zoomInBtn.setGraphic(stackPane1);
//
//		    InputStream in= getClass().getResource("/images/large/zoom_minus_50.png").openStream();
//			Image imageZoomMinus = new Image(in);
//			ImageView iv=new ImageView(imageZoomMinus);
//			iv.setFitHeight(30);
//			iv.setFitWidth(30);
//			
//		    text1=new Text();
//		    text1.setFill(javafx.scene.paint.Color.WHITE);
//		    text1.setFont(javafx.scene.text.Font.font ("Arial", 14));
//		    text1.setText("Zoom -");
//		    stackPane1=new StackPane(text1,iv);
//		    StackPane.setAlignment(text1, Pos.CENTER_RIGHT);
//		    StackPane.setAlignment(iv, Pos.CENTER_LEFT);
//		    zoomOutBtn.setText("");
//		    zoomOutBtn.setGraphic(stackPane1);
		    
		}catch(java.lang.Exception ex) {
			System.out.println("error loading resources "+ex.toString());
			ex.printStackTrace();
		}
		
	}
	
}
