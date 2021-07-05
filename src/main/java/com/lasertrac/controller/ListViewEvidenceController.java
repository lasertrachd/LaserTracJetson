package com.lasertrac.controller;

import com.lasertrac.common.ListViewImageItem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import jfxtras.labs.scene.control.window.Window;

public class ListViewEvidenceController {

	@FXML
	private Pane rootPaneListViewEvidence;
	
	
	//Window listViewWindow; 
	ListView<ListViewImageItem> autoEvidenceList;
	ObservableList<ListViewImageItem> autoEvidenceMag=FXCollections.<ListViewImageItem>observableArrayList();
	
	public void initialize()
	{
		rootPaneListViewEvidence.setStyle("-fx-background-color: #fff");
		autoEvidenceMag.add(new ListViewImageItem("straight", "https://d30y9cdsu7xlg0.cloudfront.net/png/17383-200.png"));
		autoEvidenceList =new ListView<>(autoEvidenceMag);
		
		//autoautoEvidenceList.
		// Custom cell factory
		autoEvidenceList.setCellFactory(l -> new ListCell<ListViewImageItem>() {
		    @Override
		    public void updateItem(final ListViewImageItem item, final boolean empty) {
		        if (empty) {
		            setText("");
		            setGraphic(null);
		        } else {
		            setText(item.getText());
		            setGraphic(item.getImage());
		        }
		    }
		});
		rootPaneListViewEvidence.getChildren().add(autoEvidenceList);
		//listViewWindow.getContentPane().getChildren().add(autoEvidenceList);
	
	}
}
