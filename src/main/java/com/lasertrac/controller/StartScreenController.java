package com.lasertrac.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.lasertrac.common.ResponsiveConst;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class StartScreenController {

	
	@FXML
	private Label labelDeptName;
	
	@FXML
	public Label labelLog;
	
	
	@FXML
	private ImageView imageViewLogo;
	
	@FXML
	private Pane rootPane;
	
	@FXML 
	private VBox vBoxContainer;
	/**
	 * Initialize method, automatically called by @{link FXMLLoader}
	 */
	public void initialize() {
		//rootPane.setStyle("-fx-background-color: #000;"  );
		DoubleProperty fontSize = new SimpleDoubleProperty(ResponsiveConst.fontSize.doubleValue()) ;
		DoubleProperty headerFontSize = new SimpleDoubleProperty(ResponsiveConst.fontSize.doubleValue()) ;
		
		vBoxContainer.prefWidthProperty().bind(rootPane.widthProperty());
		vBoxContainer.prefHeightProperty().bind(rootPane.heightProperty());
		
		headerFontSize.bind(rootPane.widthProperty().add(rootPane.heightProperty()).divide(80));
		
		labelDeptName.styleProperty().bind(Bindings.concat("-fx-font-size: ", headerFontSize.asString(), ";"));
		labelDeptName.prefWidthProperty().bind(vBoxContainer.widthProperty());
		labelDeptName.setTextFill(Color.web("#fff"));
		
		fontSize.bind(rootPane.widthProperty().add(rootPane.heightProperty()).divide(140));
		
		labelLog.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
		labelLog.setTextFill(Color.web("#fff"));
		labelLog.setText("Loading Application");
		
		imageViewLogo.setLayoutX( (rootPane.getWidth()-labelLog.getWidth())/2);
		
		try {
			//FileInputStream input = new FileInputStream("/images/LaserProHD_P435.png");
//			if(new File(getClass().getResource("/images/LaserProHD_P435.png").toExternalForm()).exists() ) {
//				System.out.println("file exist");
//			}
//			System.out.println(getClass().getResource("/images/LaserProHD_P435.png"));
			//FileInputStream input = new FileInputStream(getClass().getResource("/images/LaserProHD_P435.png").toExternalForm());
			//FileInputStream input = new FileInputStream(getClass().getResource("/com/lasertrac/images/LaserProHD_P435.png").toExternalForm());
			//FileInputStream input = new FileInputStream(getClass().getResource("/images/LaserProHD_P435.png").toExternalForm());
			InputStream in = getClass().getResource("/images/LaserProHD_P435.png").openStream(); 
			Image image = new Image(in);
			imageViewLogo.setImage(image);
			
		}catch(Exception ex) {
			System.out.println("ERROR loading resource file images/LaserProHD_P435.png:"+ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		
	}
	
	public void updateLog(String str){
		labelLog.setText(labelLog.getText()+"\n"+str);
	}
	
}
