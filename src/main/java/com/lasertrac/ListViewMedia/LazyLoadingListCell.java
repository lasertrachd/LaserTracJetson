package com.lasertrac.ListViewMedia;

import javafx.animation.PauseTransition;
import javafx.scene.control.ListCell;
import javafx.util.Duration;

public class LazyLoadingListCell extends ListCell<LazyLoadItem<String>> {

    private PauseTransition pause = new PauseTransition(Duration.millis(1000));

    @Override
    protected void updateItem(LazyLoadItem<String> lazyItem, boolean empty) {
        pause.stop();
        super.updateItem(lazyItem, empty);
        if (empty) {
            setText("");
        } else if (lazyItem.isLoaded()) {
            setText(lazyItem.getValue());
        } else {
            pause.setOnFinished(e -> setText(lazyItem.getValue()));
            setText("Waiting...");
            pause.play();
        }
    }
}
