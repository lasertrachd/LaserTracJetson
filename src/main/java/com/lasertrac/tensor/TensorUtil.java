package com.lasertrac.tensor;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import com.lasertrac.main.TensorResult;

public class TensorUtil {

	public static String[]  labels = new String[] {"number_plate","CAR","TRUCK","BUS","PICKUP","AUTO","BIKE","HELMET","NOHELMET"};
	
	public static String removeScientificNotation(String value)
	{
	    return new BigDecimal(value).toPlainString();
	}
	public static double getDistanceFromCenter(Point p ) {
        Point center=new Point(1280/2,720/2);
        int dx = p.x - center.x;
        if (dx < 0) {
            dx = dx * -1;
        }

        int dy = p.y - center.y;
        if (dy < 0) {
            dy = dy * -1;
        }

        double distance = Math.sqrt( (dx*dx) + (dy*dy) );
        return distance;
    }
	
	public static TensorResult getCenterNumberPlate(List<TensorResult> list) {
        List<TensorResult> numberPlates = new ArrayList<TensorResult>();
        TensorResult selectedPlate = null;
        //List<TensorResult> result1 = new ArrayList<TensorResult>();
        //List<Bitmap> listPlates = new List<Bitmap>();
        if (list != null) {
            if (list.size() > 0)
            {

                for (int i = 0; i < list.size(); i++)
                {
                    if (list.get(i).class_name.contains("number_plate"))
                    {
                        numberPlates.add(list.get(i));
                    }
                }
                double min_distance = -1;
                Point centerPoint = new Point(1280 / 2, 720 / 2);

                if (numberPlates.size() == 1)
                {
                    selectedPlate = numberPlates.get(0);
                    return selectedPlate;
                    //listPlates.Add((Bitmap)cropImage(img, selectedPlate.rect));
                }
                else if (numberPlates.size()  > 1)
                {
                    for (int i = 0; i < numberPlates.size(); i++)
                    {
                        Point p1 = new Point(numberPlates.get(i).rect.width - numberPlates.get(i).rect.x, numberPlates.get(i).rect.height - numberPlates.get(i).rect.y);
                        double distance = TensorUtil.getDistanceFromCenter(p1);
                        if (i == 0)
                        {
                            min_distance = distance;
                            selectedPlate = numberPlates.get(i);
                        }
                        else if (distance < min_distance)
                        {
                            min_distance = distance;
                            selectedPlate = numberPlates.get(i);
                        }
                        //listPlates.Add((Bitmap)cropImage(img, selectedPlate.rect));
                    }
                    return selectedPlate;
                }
            }
        }
        return null;
    }
	
	public static  BufferedImage cropImage(BufferedImage src, Rectangle rect) {
	      BufferedImage dest = src.getSubimage(rect.x, rect.y, rect.width, rect.height);
	      return dest; 
	}
	
	public static BufferedImage cropImage(BufferedImage bufferedImage, int x, int y, int width, int height){
	    BufferedImage croppedImage = bufferedImage.getSubimage(x, y, width, height);
	    return croppedImage;
	}
	
	public static String encodeToString(BufferedImage image, String type) {
	    String imageString = "";
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();

	    try {
	        ImageIO.write(image, type, bos);
	        byte[] imageBytes = bos.toByteArray();

	        Base64.Encoder encoder = Base64.getEncoder();
	        imageString = encoder.encodeToString(imageBytes);

	        bos.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return imageString;
	}
	
	
}
