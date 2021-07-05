package com.lasertrac.common;

import java.io.InputStream;
import java.net.URL;

import javafx.scene.control.Button;

public class ControlsHelper {

	public ControlsHelper(URL icon,String txt, Button btn, int iconWidth, int iconHeight) {
		this.img=icon;
		this.text=txt;
		this.btn=btn;
		
		this.iconWidth=iconWidth;
		this.iconHeight=iconWidth;
		
	}
	
	public int iconWidth;
	public int iconHeight;
	
	public URL img;
	public String  text;
	public Button  btn;
}
