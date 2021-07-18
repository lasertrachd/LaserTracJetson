package com.lasertrac.main;

import com.lasertrac.controller.LiveViewController1;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class LiveView extends Application{

	@Override
	public void start(Stage primaryStage) {
		try {
			
			//primaryStage.initStyle(StageStyle.UNDECORATED);
			
			// load the FXML resource
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lasertrac/view/LiveView.fxml"));
			// store the root element so that the controllers can use it
			Pane rootElement = (Pane) loader.load();
			// create and style a scene
			Scene scene = new Scene(rootElement);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			// create the stage with the given title and the previously created
			// scene
			primaryStage.setTitle("Live View");
			primaryStage.setScene(scene);
			
			primaryStage.setFullScreenExitHint("");
			
			// show the GUI
			primaryStage.show();
			primaryStage.setMaximized(true);
			primaryStage.setFullScreen(true);
			primaryStage.setFullScreenExitHint("");
			
			// set the proper behavior on closing the application
			LiveViewController1 controller = loader.getController();
			primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we)
				{
					//controller.setClosed();
				}
			}));
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args) {
//		launch(args);
//	}
	

}
