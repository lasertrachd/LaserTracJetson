package com.lasertrac.common;

import javafx.scene.image.ImageView;

public class ListViewImageItem {
	
	    final String text;
	    final ImageView image;

	    public ListViewImageItem(final String text, final String imageURL) {
	        this.text = text;
	        ImageView iv=new ImageView("file:"+imageURL);
	        iv.setFitHeight(100);
	        iv.setPreserveRatio(true);
	        this.image = iv;
	    }
	    
	    public String getText() {
	    	return text;
	    }
	    
	    public ImageView getImage() {
	    	return image;
	    }
	    
	
}
