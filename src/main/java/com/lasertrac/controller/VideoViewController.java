package com.lasertrac.controller;

import javafx.fxml.FXML;
import com.lasertrac.main.StartScreen;

import javafx.event.EventHandler;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class VideoViewController {

	StartScreen startSreenObj;
	
	public void setStartScreenObject(StartScreen obj) {
		this.startSreenObj = obj;
	}
	
	@FXML
	private Button playBtn;

	@FXML
	private Button pauseBtn;

	@FXML
	private Button fastForwardBtn;

	@FXML
	private Button prevFrameBtn;

	@FXML
	private Button nextFrameBtn;

	@FXML
	private Button snapShotBtn;

	@FXML
	private Button createChallanBtn;

	@FXML
	private Button frameAnprBtn;

	@FXML
	private ImageView videoImgView;

	@FXML
	private AnchorPane videoImageAnchor;


	@FXML
	private Pane rootPane;

	@FXML
	private GridPane gridPaneParent;

	/**
	 * Initialize method, automatically called by @{link FXMLLoader}
	 */
	public void initialize()
	{

//		gridPaneParent.prefWidthProperty().bind(rootPane.widthProperty());
//		gridPaneParent.prefHeightProperty().bind(rootPane.heightProperty());
//
//		videoImgView.fitWidthProperty().bind(videoImageAnchor.widthProperty());
//		videoImgView.fitHeightProperty().bind(videoImageAnchor.heightProperty());
//
//		setListeners();
		

		
	}
	void setListeners(){
		playBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				((Node)(event.getSource())).getScene().getWindow().hide();
				startSreenObj.liveStage.show();
			}
		});

		pauseBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				((Node)(event.getSource())).getScene().getWindow().hide();
				startSreenObj.liveStage.show();
			}
		});
		fastForwardBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				((Node)(event.getSource())).getScene().getWindow().hide();
				startSreenObj.liveStage.show();
			}
		});

		prevFrameBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				((Node)(event.getSource())).getScene().getWindow().hide();
				startSreenObj.liveStage.show();
			}
		});

		nextFrameBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				((Node)(event.getSource())).getScene().getWindow().hide();
				startSreenObj.liveStage.show();
			}
		});

		snapShotBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				((Node)(event.getSource())).getScene().getWindow().hide();
				startSreenObj.liveStage.show();
			}
		});

		createChallanBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				((Node)(event.getSource())).getScene().getWindow().hide();
				startSreenObj.liveStage.show();
			}
		});

		frameAnprBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				((Node)(event.getSource())).getScene().getWindow().hide();
				startSreenObj.liveStage.show();
			}
		});

	}
}
