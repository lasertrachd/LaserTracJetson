package com.lasertrac.ListViewMedia;

import javafx.animation.PauseTransition;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.File;

public class LazyLoadingListCellMediaType extends ListCell<LazyLoadItem<MediaProps>> {

    private PauseTransition pause = new PauseTransition(Duration.millis(1000));



    @Override
    protected void updateItem(LazyLoadItem<MediaProps> lazyItem, boolean empty) {
        pause.stop();
        super.updateItem(lazyItem, empty);
        if (empty) {
            setText("");
            setGraphic(null);
        } else if (lazyItem.isLoaded()) {
            //setText(lazyItem.getValue().text);
            File file = new File(lazyItem.getValue().thumbImg);
            if(file.exists()==false){
                System.err.println("file not found "+file.getAbsolutePath());
                file = new File(getClass().getResource("/images/vlc_icon_90.jpg").getFile());

            }
            Image image = new Image(file.toURI().toString());
            ImageView iv = new ImageView(image);

            // Create a VBox to hold our displayed value
            VBox vBox = new VBox(5);
            vBox.setStyle("-fx-padding: 0px;");
            vBox.setAlignment(Pos.CENTER_LEFT);

            //vBox.maxHeightProperty().bind(heightProperty);
            // Add the values from our piece to the HBox
            vBox.getChildren().addAll(
                    iv,
                    new Label(lazyItem.getValue().text)
                    //,new Label("x " + piece.getCount())
            );

            // Set the VBox as the display
            setGraphic(vBox);

        } else {
            pause.setOnFinished(e -> setText(lazyItem.getValue().text));
            setText("Waiting...");
            pause.play();
        }
    }
}
