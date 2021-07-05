package com.lasertrac.main;

import com.fazecast.jSerialComm.SerialPort;
import com.lasertrac.common.CommonFunctions;
import com.lasertrac.common.ConfigurationProps;
import com.lasertrac.common.IniFile;
import com.lasertrac.common.NavigationContstants;
import com.lasertrac.common.SessionValues;
import com.lasertrac.controller.LiveViewController1;
//import com.lasertrac.controller.LiveViewController;
import com.lasertrac.controller.ScreenController;
import com.lasertrac.controller.StartScreenController;
import com.lasertrac.dao.UserDao;
import com.lasertrac.dao.impl.UserDaoImpl;
import com.lasertrac.entity.HibernateUtil;
import com.lasertrac.entity.Test;
import com.lasertrac.entity.Violations;
import com.lasertrac.settings.AutoModeEvidenceGenerate;
import com.lasertrac.settings.CameraProperties;
import com.lasertrac.settings.FtpHelper;
import com.lasertrac.settings.PrinterDefaults;
import com.lasertrac.settings.TrafficViolationsHelper;
import com.lasertrac.settings.ZoomMap;
import com.lasertrac.usb.CameraInterface;
//import com.lasertrac.controller.VideoViewController;
import com.lasertrac.usb.CameraWonwoo;
import com.lasertrac.usb.ComLaser;
import com.lasertrac.usb.Gps;
//import com.sun.org.apache.xpath.internal.operations.Bool;
import com.lasertrac.usb.LaserInterface;

import javafx.application.Application;
import javafx.application.Platform;
//import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hibernate.Session;

public class StartScreen extends Application {

	public static ScreenController screenController;
	public static ContextMenu contextMenu;
	
	public Stage liveStage;
	public Stage videoStage;
	//public Stage liveStage;
	
	public CameraInterface cameraObj = null; 
	public LaserInterface laserObj = null; 
	public Gps gpsObj = null; 
	
	@Override
	public void start(Stage primaryStage) {
		try {
			File configDir = new File(ConfigurationProps.config_dir);
			if(configDir.exists()==false) {
				// create a alert 
		        Alert a = new Alert(AlertType.INFORMATION);
		        
		        a.setHeaderText("Config directory not set.");
		        //a.setContentText("Config directory not set.");
		        a.show();
		        return;
			}
			
			primaryStage.initStyle(StageStyle.UNDECORATED);
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lasertrac/view/StartScreen.fxml"));
			
			// store the root element so that the controllers can use it
			Pane rootElement = (Pane) loader.load();
			
			// create and style a scene
			Scene scene = new Scene(rootElement);
			scene.getStylesheets().add(getClass().getResource("/com/lasertrac/css/application.css").toExternalForm());
			
			// create the stage with the given title and the previously created
			// scene
			primaryStage.setTitle("Welcome Screen");
			primaryStage.setScene(scene);
			
			primaryStage.setFullScreenExitHint("");
			
			// show the GUI
			primaryStage.show();
			primaryStage.setMaximized(true);
			primaryStage.setFullScreen(true);
			primaryStage.setFullScreenExitHint("");
			
			// set the proper behavior on closing the application
			StartScreenController controller = loader.getController();
			primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we)
				{
					//controller.setClosed();
				}
			}));
			
			contextMenu = new ContextMenu();
			contextMenu.getItems().add(new MenuItem("Live"));
			contextMenu.getItems().add(new MenuItem("Video"));
			contextMenu.getItems().add(new MenuItem("Exit"));
			
			initializeApp();
			
			SerialPort cameraPort=null, gpsPort=null, laserPort=null;
			int cameraBoudrate = 9600, laserBoudrate = 15200, gpsBoudrate = 4800;
			
			try {
				SerialPort[] portArr= SerialPort.getCommPorts();
				
				IniFile ini = new IniFile(ConfigurationProps.usb_config_file);
				
				if(ini.readStringValue(ConfigurationProps.IniSectionAndKeys.USB_PROPS.cameraSection, ConfigurationProps.IniSectionAndKeys.USB_PROPS.detectKey).equals("auto")) {
					cameraPort = getSerialPort(portArr, ini.readStringValue(ConfigurationProps.IniSectionAndKeys.USB_PROPS.cameraSection, ConfigurationProps.IniSectionAndKeys.USB_PROPS.portDescKey), true, false);
				}else {
					cameraPort = getSerialPort(portArr, ini.readStringValue(ConfigurationProps.IniSectionAndKeys.USB_PROPS.cameraSection, ConfigurationProps.IniSectionAndKeys.USB_PROPS.portKey), false, true);
				}
				System.out.println("cameraPort loading usb port="+cameraPort.getDescriptivePortName());
				int temp = ini.readIntValue(ConfigurationProps.IniSectionAndKeys.USB_PROPS.cameraSection, ConfigurationProps.IniSectionAndKeys.USB_PROPS.boudrateKey);
				if(temp>0) {
					cameraBoudrate = temp;
				}
				
				if(ini.readStringValue(ConfigurationProps.IniSectionAndKeys.USB_PROPS.laserSection, ConfigurationProps.IniSectionAndKeys.USB_PROPS.detectKey).equals("auto")) {
					laserPort = getSerialPort(portArr, ini.readStringValue(ConfigurationProps.IniSectionAndKeys.USB_PROPS.laserSection, ConfigurationProps.IniSectionAndKeys.USB_PROPS.portDescKey), true, false);
				}else {
					laserPort = getSerialPort(portArr, ini.readStringValue(ConfigurationProps.IniSectionAndKeys.USB_PROPS.laserSection, ConfigurationProps.IniSectionAndKeys.USB_PROPS.portKey), false, true);
				}
				
				temp = ini.readIntValue(ConfigurationProps.IniSectionAndKeys.USB_PROPS.laserSection, ConfigurationProps.IniSectionAndKeys.USB_PROPS.boudrateKey);
				if(temp>0) {
					laserBoudrate = temp;
				}
				
				if(ini.readStringValue(ConfigurationProps.IniSectionAndKeys.USB_PROPS.gpsSection, ConfigurationProps.IniSectionAndKeys.USB_PROPS.detectKey).equals("auto")) {
					gpsPort = getSerialPort(portArr, ini.readStringValue(ConfigurationProps.IniSectionAndKeys.USB_PROPS.gpsSection, ConfigurationProps.IniSectionAndKeys.USB_PROPS.portDescKey), true, false);
				}else {
					gpsPort = getSerialPort(portArr, ini.readStringValue(ConfigurationProps.IniSectionAndKeys.USB_PROPS.gpsSection, ConfigurationProps.IniSectionAndKeys.USB_PROPS.portKey), false, true);
				}
				
				temp = ini.readIntValue(ConfigurationProps.IniSectionAndKeys.USB_PROPS.gpsSection, ConfigurationProps.IniSectionAndKeys.USB_PROPS.boudrateKey);
				if(temp>0) {
					gpsBoudrate = temp;
				}
				
			}catch(Exception ex) {
				System.out.println("error loading usb port="+ex.toString());
			}
			SerialPort[] arr = SerialPort.getCommPorts();
			for( int i = 0; i < arr.length; i++) {
				System.out.println("port "+i+" = "+arr[i].getPortDescription()+", sys port name="+arr[i].getSystemPortName()); 
			}
			boolean cameraConnected = false;
			if(cameraPort!=null) {
				
				cameraObj = new CameraWonwoo();
				
				cameraObj.setBoudrate(cameraBoudrate); ;
				System.out.print("bopudrate "+cameraBoudrate+" camera port "+cameraPort.getDescriptivePortName()+" , port"+cameraPort.getSystemPortName());
				
				if(cameraObj.connectCamera(cameraPort)) {
					updateUI(controller, "Camera connected");
					//controller.updateLog("Camera connected");
					cameraConnected=true;
					System.out.println("Camera connected");
				}else {
					updateUI(controller, "Camera not found");
					//controller.updateLog("Camera not found ");	
					System.out.println("Camera not found1");
				}
			}else {
				updateUI(controller, "Camera not found");
				//controller.updateLog("Camera not found");
				System.out.println("Camera not found");
			}
			
			if(laserPort!=null) {
				laserObj.setBoudRate(laserBoudrate);
				if(laserObj.connectLaser(laserPort)) {
					updateUI(controller, "Laser connected");
					System.out.println("laser connected");
				}else {
					updateUI(controller, "Laser not found");
					System.out.println("laser not found1");
				}
			}else {
				updateUI(controller, "Laser not found");
				System.out.println("laser not found");
			}
			
			if(gpsPort != null) {
				//f(gpsObj.)
				updateUI(controller, "Gps connected");
				System.out.println("gps port not null");
			}else {
				updateUI(controller, "Gps not found");
				System.out.println("gps not found");
			}
			
			if(cameraConnected) {
				setCameraPresets(controller);
			}
			System.out.println("after set camera presets");
			
			try{
				Thread.sleep(100);
			}catch(Exception ex) {
				System.out.println("thread wait="+ex.getMessage());
			}
			
			screenController=new ScreenController(scene);
			
			screenController.addScreen(NavigationContstants.videoScreen, (Pane)FXMLLoader.load(getClass().getResource( "/com/lasertrac/view/VideoView.fxml" )) );
			Platform.runLater(new Runnable() {
//	            @Override
	            public void run() {
	            	//System.out.println(controller.labelLog.getText());
	                //label.setText("Input was : " + userInput);
	            	try{
	            		FXMLLoader loaderLive = new FXMLLoader(getClass().getResource("/com/lasertrac/view/LiveView.fxml"));
	        			
	        			// store the root element so that the controllers can use it
	        			Pane rootElementLive = (Pane) loaderLive.load();
	            		screenController.addScreen(NavigationContstants.liveScreen, rootElementLive );
	            		
	            		LiveViewController1 liveController = loaderLive.getController();
	            		liveController.setCameraObject(cameraObj);
	            	}
	            	catch(Exception ex) {
	            		ex.printStackTrace();
	            		System.out.println(ex.toString());
	            	}
	            }
	        });
			
			
			//screenController.activate("live");
			//primaryStage.hide();
			
//			//Parent root;
//	        try {
//	        	FXMLLoader loader1 = new FXMLLoader();
//	        	loader1.setLocation(getClass().getResource("/com/lasertrac/view/LiveView.fxml"));
//	            
//	        	//FXMLLoader loader1= new FXMLLoader(getClass().getClassLoader().getResource("/com/lasertrac/view/LiveView.fxml"));
//	        	
//	        	Pane rootElementLive = (Pane) loader1.load();
//	        	
//	        	Stage stage = new Stage();
//	        	
//	        	stage.setTitle("Live");
//	            stage.setScene(new Scene(rootElementLive));
//	            stage.setX(0);
//	            stage.setY(0);
//	            
//	            stage.setFullScreenExitHint("");
//	            stage.show();
//	            stage.setMaximized(true); 
//	            stage.setFullScreen(true);
//	            stage.hide();
//	            
//	            LiveViewController controller1 = loader1.getController();
//	            controller1.setStartScreenObject(this);
//	            //controller1.setStageAndSetupListeners(primaryStage);
//				primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
//					public void handle(WindowEvent we)
//					{
//						//controller.setClosed();
//					}
//				}));
//	            // Hide this current window (if this is what you want)
//	            //((Node)(event.getSource())).getScene().getWindow().hide();
//	        }
//	        catch (Exception e) {
//	        	System.out.print(e.getLocalizedMessage());
//	            e.printStackTrace();
//	        }
//	        
//	        try {
//	        	FXMLLoader loader2 = new FXMLLoader();
//	        	loader2.setLocation(getClass().getResource("/com/lasertrac/view/VideoView.fxml"));
//	            
//	        	//FXMLLoader loader1= new FXMLLoader(getClass().getClassLoader().getResource("/com/lasertrac/view/LiveView.fxml"));
//	        	
//	        	Pane rootElementLive = (Pane) loader2.load();
//	        	
//	        	Stage stage = new Stage();
//	        	
//	        	stage.setTitle("Live");
//	            stage.setScene(new Scene(rootElementLive));
//	            stage.setX(0);
//	            stage.setY(0);
//	            
//	            stage.setFullScreenExitHint("");
//	            stage.show();
//	            stage.setMaximized(true); 
//	            stage.setFullScreen(true);
//	            
//	            VideoViewController controller1 = loader2.getController();
//	            controller1.setStartScreenObject(this);
//	            //controller1.setStageAndSetupListeners(primaryStage);
//				primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
//					public void handle(WindowEvent we)
//					{
//						//controller.setClosed();
//					}
//				}));
//	            // Hide this current window (if this is what you want)
//	            //((Node)(event.getSource())).getScene().getWindow().hide();
//	        }
//	        catch (Exception e) {
//	        	System.out.print(e.getLocalizedMessage());
//	            e.printStackTrace();
//	        }
	        
	        
		}catch(Exception ex){
			System.out.println("error="+ex.toString());
			System.out.println("error="+ex.getLocalizedMessage());
			ex.printStackTrace();
		}
				
	}
	
	void initializeApp() {
		
		File videoDir= new File(ConfigurationProps.video_dir);
		if(videoDir.exists()==false) {
			videoDir.mkdir();
		}
		
		File snapDir= new File(ConfigurationProps.snap_dir);
		if(snapDir.exists()==false) {
			snapDir.mkdir();
		}
		
		File pdfDir= new File(ConfigurationProps.pdf_dir);
		if(pdfDir.exists()==false) {
			pdfDir.mkdir();
		}
		
		// create log file if not exists
		File laserLog = new File (ConfigurationProps.laser_log);
		if(laserLog.exists()==false) {
			try {
				laserLog.createNewFile();	
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		File commonLog = new File (ConfigurationProps.common_log);
		if(commonLog.exists()==false) {
			try {
				commonLog.createNewFile();	
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		File log = new File (ConfigurationProps.config_file);
		if(log.exists()==false) {
			try {
				log.createNewFile();	
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		
		CameraProperties.reloadCameraProperties();
		try {
			IniFile ini=new IniFile(ConfigurationProps.config_file);	
			try {
				String pdfLogo=ini.readStringValue(ConfigurationProps.IniSectionAndKeys.logoImageSection, ConfigurationProps.IniSectionAndKeys.keyValue);
				if(pdfLogo.length()>0) {
					File f=new File(pdfLogo);
					SessionValues.logoFileName = pdfLogo;
					if(f.exists()==false) {
						//CommonFunctions.copy(new File("defaultPdfLogo.png"), new File(ConfigurationProps.config_dir+"\\"+"pdfLogoSelected.png"));
						CommonFunctions.copy(new File(getClass().getClassLoader().getResource("defaultPdfLogo.png").getFile()), new File(ConfigurationProps.config_dir+"\\"+"pdfLogoSelected.png"));
						ini.putValue(ConfigurationProps.IniSectionAndKeys.logoImageSection, ConfigurationProps.IniSectionAndKeys.keyValue, ConfigurationProps.config_dir+"\\"+"pdfLogoSelected.png");
						SessionValues.logoFileName=ConfigurationProps.config_dir+"\\"+"pdfLogoSelected.png";
					}
				}else {
					//CommonFunctions.copy(new File("defaultPdfLogo.png"), new File(ConfigurationProps.config_dir+"\\"+"pdfLogoSelected.png"));
					CommonFunctions.copy(new File(getClass().getClassLoader().getResource("defaultPdfLogo.png").getFile()), new File(ConfigurationProps.config_dir+"\\"+"pdfLogoSelected.png"));
					ini.putValue(ConfigurationProps.IniSectionAndKeys.logoImageSection, ConfigurationProps.IniSectionAndKeys.keyValue, ConfigurationProps.config_dir+"\\"+"pdfLogoSelected.png");
					SessionValues.logoFileName=ConfigurationProps.config_dir+"\\"+"pdfLogoSelected.png";
				}
			}catch(Exception e) {
				System.out.println("error="+e.toString());
				e.printStackTrace();
			}
			
			SessionValues.sound_data_script = ini.readStringValue(ConfigurationProps.IniSectionAndKeys.sound_data_script_section, ConfigurationProps.IniSectionAndKeys.keyValue);
			
			//load audto evidence parameters - distance and timer value
			AutoModeEvidenceGenerate.loadParams();
			
			//get last selected printer
			PrinterDefaults.readDefaultPrinterAndPaper();
			
			FtpHelper.loadFtpUrls();
			
//			UserDao udao=new UserDaoImpl();
//			TrafficViolationsHelper.violationsList = udao.loadViolations();
//			for(Violations v:TrafficViolationsHelper.violationsList) {
//				System.out.println(v.getSection_id()+" , "+v.getSection_name()+" ,"+v.getDescription()+"");
//			}
			
			//check if encryption is true or false
			String encryptOption = ini.readStringValue(ConfigurationProps.IniSectionAndKeys.encryptPdfOptionSection, ConfigurationProps.IniSectionAndKeys.keyValue);
	        if (encryptOption.length() > 0)
	        {
	            if (encryptOption == "true")
	            {
	                SessionValues.encryptPdf = true;
	            }
	            else
	            {
	            	SessionValues.encryptPdf  = false;
	            }
	        }
	        else
	        {
	        	SessionValues.encryptPdf = false;
	            ini.putValue(ConfigurationProps.IniSectionAndKeys.encryptPdfOptionSection, ConfigurationProps.IniSectionAndKeys.keyValue, "false");
	        }
	        
	        ZoomMap.readZoomValue();
			
	        SessionValues.wrongSideDirection = 0;
            if (ini.readStringValue(ConfigurationProps.IniSectionAndKeys.wrongSideSection, ConfigurationProps.IniSectionAndKeys.wrongSideDirectionKey) == "+")
            {
                ConfigurationProps.IniSectionAndKeys.wrongSideDirection = 1;
            }
            else if (ini.readStringValue(ConfigurationProps.IniSectionAndKeys.wrongSideSection, ConfigurationProps.IniSectionAndKeys.wrongSideDirectionKey) == "-")
            {
                ConfigurationProps.IniSectionAndKeys.wrongSideDirection = -1;
            }
            
            try {
            	File f=new File(ConfigurationProps.config_file_camera_presets);
                if (!f.exists()==false)
                {
                    f.createNewFile();
                }	
            }catch(Exception ex) {
            	System.out.println("error creating camera presets config="+ex.toString());
            }
            
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	
	void updateUI(final StartScreenController controller, final String msg) {
		Platform.runLater(new Runnable() {
//            @Override
            public void run() {
            	//System.out.println(controller.labelLog.getText());
                //label.setText("Input was : " + userInput);
            	controller.labelLog.setText(controller.labelLog.getText()+"\n"+ msg);
            }
        });
	}
	
	
	void setCameraPresets(StartScreenController controller) {
		try{
			Thread.sleep(100);
		}catch(Exception ex) {
			
		}
		boolean result = cameraObj.callPresetValue(CameraWonwoo.CameraModes.presetSunnyModeAddress);
		if(result==false) {
			updateUI(controller, "error while setting camera presets");
		}
		
		try{
			Thread.sleep(100);
		}catch(Exception ex) {
			
		}
		result = cameraObj.zoomSeperateMode();
		if(result==false) {
			updateUI(controller, "error while setting seperate zoom mode");
		}
		try{
			Thread.sleep(100);
		}catch(Exception ex) {
			
		}
		result = cameraObj.setDizitalZoomOn();
		if(result==false) {
			updateUI(controller, "error while setting d-zoom on");
		}
		try{
			Thread.sleep(100);
		}catch(Exception ex) {
			
		}
		result = cameraObj.setCamWB(CameraWonwoo.Cam_WB_outdoor);
		if(result==false) {
			updateUI(controller, "error while setting cam WB outdoor");
		}
		try{
			Thread.sleep(100);
		}catch(Exception ex) {
			
		}
		result = cameraObj.setDisplayOn();
		if(result==false) {
			updateUI(controller, "error while setting camera display on");
		}
		try{
			Thread.sleep(100);
		}catch(Exception ex) {
			
		}
		result = cameraObj.setZoomDisplayOn();
		if(result==false) {
			updateUI(controller, "error while setting zoom display");
		}
		
	}
	SerialPort getSerialPort(SerialPort[] portArr, String value,boolean portDesc, boolean portName ) {
		for(SerialPort port: portArr) {
			if(portDesc) {
				if(port.getPortDescription().toLowerCase().contains(value.toLowerCase())) {
					
					return port;
				}
			}else if(portName){
				if(port.getSystemPortName().toLowerCase().contains(value.toLowerCase())) {
					return port;
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		
//		Session session = HibernateUtil.getSessionFactory().openSession(); 
//        session.beginTransaction();
// 
//        Test student = new Test();
// 
//        student.setUserName("JavaFun");
//        student.setPassword("19");
//
//        session.save(student);
////        try {
////            //Thread.sleep(20000);
////        } catch (InterruptedException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
// 
//        session.getTransaction().commit();
        
		launch();
		
	}
}
