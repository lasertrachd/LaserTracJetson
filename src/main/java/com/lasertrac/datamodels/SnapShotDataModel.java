package com.lasertrac.datamodels;

import java.awt.image.BufferedImage;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SnapShotDataModel {

	public SnapShotDataModel (String text, BufferedImage img  ) {
        setFilename(text);
        setImg(img);
    }
	
	private final StringProperty filename = new SimpleStringProperty(this, "filename", "");
	public final StringProperty filenameProperty() {
        return this.filename;
    }
	public final java.lang.String getFilename() {
        return this.filenameProperty().get();
    }
	public final void setFilename(final java.lang.String text) {
        this.filenameProperty().set(text);
    }
	
	
	private final ObjectProperty<BufferedImage> img = new SimpleObjectProperty<BufferedImage>(this,"img");
	public final ObjectProperty<BufferedImage> imgProperty(){
		return this.img;
	}
	public final BufferedImage getImg() {
		return this.imgProperty().get();
	}
	public final void setImg(final BufferedImage img1) {
		this.imgProperty().set(img1);
	}
    
	
}
