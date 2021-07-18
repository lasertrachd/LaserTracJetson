package com.lasertrac.main;

import com.lasertrac.controller.VideoViewController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Videos extends Application {

    @Override
    public void start(Stage primaryStage) {
        try{

            // load the FXML resource
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lasertrac/view/VideoView.fxml"));
            // store the root element so that the controllers can use it
            Pane rootElement = (Pane) loader.load();
            // create and style a scene
            Scene scene = new Scene(rootElement);
            //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            // create the stage with the given title and the previously created
            // scene
            primaryStage.setTitle("Video View");
            primaryStage.setScene(scene);

            primaryStage.setFullScreenExitHint("");

            // show the GUI
            primaryStage.show();
            primaryStage.setMaximized(true);
            primaryStage.setFullScreen(true);
            primaryStage.setFullScreenExitHint("");

            // set the proper behavior on closing the application
            VideoViewController controller = loader.getController();
            primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    //controller.setClosed();
                }
            }));

        } catch( Exception e)

        {
            e.printStackTrace();
        }
    }

}

