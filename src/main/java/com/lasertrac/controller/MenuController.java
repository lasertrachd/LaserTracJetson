package com.lasertrac.controller;

import com.lasertrac.common.NavigationContstants;
import com.lasertrac.main.StartScreen;

//import application.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class MenuController {

	@FXML
	private Button liveScreen;
	
	@FXML
	private Button videoScreen;
	
	@FXML
	private Button exitMenu;
	
	
	public void initialize() {
		
		exitMenu.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
			public void handle(ActionEvent event) {
				((Node) event.getSource()).getScene().getWindow().hide();
			}
		});
		
		liveScreen.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
			public void handle(ActionEvent event) {
				StartScreen.screenController.activate(NavigationContstants.liveScreen);
			}
		});
		
		videoScreen.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
			public void handle(ActionEvent event) {
				StartScreen.screenController.activate(NavigationContstants.videoScreen);
				//((Node) event.getSource()).getScene().getWindow().hide();
			}
		});
		
	}
	
}
