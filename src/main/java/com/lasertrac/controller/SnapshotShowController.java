package com.lasertrac.controller;


import java.awt.image.BufferedImage;

import com.lasertrac.common.JavaCVUtils;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import jfxtras.labs.scene.control.window.Window;

public class SnapshotShowController {

	@FXML
	private Pane rootPane;
	
	@FXML
	private GridPane gridPaneContainer;
	
	@FXML
	private GridPane gridPane2;
	
	@FXML
	private Button cancelBtn;
	
	@FXML
	private Button createChallanBtn;
	
	@FXML
	private Button uploadChallanBtn;
	
	@FXML
	private ImageView snapShotImgView;
	
	
	@FXML
	private ImageView numberPlateImgView;
	
	@FXML
	private TextField numberPlateTextField;
	
	@FXML 
	private GridPane controlContainerGridPane;
	
	@FXML
	private Label msgLabel;
	
	@FXML
	private ComboBox violationActsComboBox;
	
	@FXML 
	private Button addViolationBtn;
	
	@FXML 
	private Button clearViolationBtn;
	
	
	/**
	 * Initialize method, automatically called by @{link FXMLLoader}
	 */
	public void initialize()
	{
		
		gridPaneContainer.prefWidthProperty().bind(rootPane.widthProperty());
		gridPaneContainer.prefHeightProperty().bind(rootPane.heightProperty());
		
		gridPaneContainer.getRowConstraints().get(0).setPercentHeight(24);
		gridPaneContainer.getRowConstraints().get(1).setPercentHeight(64);
		gridPaneContainer.getRowConstraints().get(2).setPercentHeight(12);
		
		gridPane2.prefWidthProperty().bind(gridPaneContainer.widthProperty());
		
		gridPane2.setVgap(5);
		gridPane2.setHgap(5);
		
		gridPane2.getColumnConstraints().get(0).setPercentWidth(33);
		gridPane2.getColumnConstraints().get(1).setPercentWidth(33);
		gridPane2.getColumnConstraints().get(2).setPercentWidth(33);
		
		controlContainerGridPane.prefWidthProperty().bind(gridPaneContainer.widthProperty());
	
		snapShotImgView.setPreserveRatio(true);
		
		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
					snapShotImgView.setImage(null);
					numberPlateImgView.setImage(null);
					numberPlateTextField.setText("");
					
					windowHandle.setVisible(false);
				
			}
		});
	}
	
	String currentFileName = "";
//	public void setFileName(String str) {
//		currentFileName = str;
//	}
	
	Window windowHandle;
	void handOverWindowHandle(Window handle)
	{
		windowHandle = handle;
	}
	
	public void updateImageView(BufferedImage imgB, String  str,int fitWidth) {
		
		
		currentFileName = str;
		msgLabel.setText(currentFileName);
		JavaCVUtils.onFXThread(snapShotImgView.imageProperty(), SwingFXUtils.toFXImage(imgB, null));
		//JavaCVUtils.onFXThread(snapShotImgView.fitWidthProperty(), (double)fitWidth);
		snapShotImgView.setFitWidth(fitWidth);
//		snapShotImgView.setImage(SwingFXUtils.toFXImage(imgB, null));
	}
	
	public void updateNumberPlateView(BufferedImage imgB, String  number_plate_str,int fitWidth) {
		//currentFileName = str;
		System.out.println("updating number plate");
		numberPlateTextField.setText(number_plate_str);
		//numberPlateImgView.setFitWidth(fitWidth);
		//numberPlateImgView.setImage(SwingFXUtils.toFXImage(imgB, null));
		JavaCVUtils.onFXThread(numberPlateImgView.imageProperty(), SwingFXUtils.toFXImage(imgB, null));
		
	}
	
	public void updateNumberPlateText( String str) {
		System.out.println("updating number plate text");
		numberPlateTextField.setText(str);
	}
	
	
}
