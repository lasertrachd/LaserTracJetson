package com.lasertrac.controller;

import com.lasertrac.main.StartScreen;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.GridPane;

public class VideoViewController {

	StartScreen startSreenObj;
	
	public void setStartScreenObject(StartScreen obj) {
		this.startSreenObj = obj;
	}
	
	@FXML
	private Button zoomOutBtn;
	
	@FXML
	private GridPane gridPane1;
	
	public void initialize() {
		
		
		zoomOutBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				((Node)(event.getSource())).getScene().getWindow().hide();
				startSreenObj.liveStage.show();
			}
		});
		
	}
}
