package com.lasertrac.tensor;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.types.UInt8;

import com.lasertrac.common.MyUtils;
import com.lasertrac.main.TensorResult;

//import javafx.embed.swing.SwingFXUtils;

public class TensorLibrary {

	
	SavedModelBundle smb = null;
	Session smbSession = null; 
	public TensorLibrary() {
		if(smb==null) {
        	//System.out.println(" initialize smb start="+ dateFormat.format(new Date()) );
        	smb = SavedModelBundle.load("/home/LaserConfig/model/output_inference_graph_v1.pb/saved_model", "serve");
        	smbSession = smb.session();
        	//System.out.println(" initialize smb end="+dateFormat.format(new Date()));
        }
	}
	
	public List<TensorResult> runTensor(BufferedImage img ){
		
        try {
            if(smb==null) {
            	smb = SavedModelBundle.load("/home/LaserConfig/model/output_inference_graph_v1.pb/saved_model", "serve");
            	smbSession = smb.session();
            }
            System.out.println("ob start scan ="+MyUtils.logTimeFormatter.format(new Date()));
//            BufferedImage orgImg = ImageIO.read(new File(fileName));
            BufferedImage orgImg = img;
            int padX=185;
            int padY=165;
            orgImg = TensorUtil.cropImage(orgImg, 185, 165, 910, 470);
            
            //System.out.println(" after crop="+MyUtils.logTimeFormatter.format(new Date()));
            
            BufferedImage bi = new BufferedImage(orgImg.getWidth(), orgImg.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D)bi.getGraphics();
            g.drawImage(orgImg, 0, 0, null);


            int w = bi.getWidth();
            int h = bi.getHeight();
            int bufferSize = w * h * 3;

            ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    int pixel = bi.getRGB(j, i);

                    byteBuffer.put((byte)((pixel >> 16) & 0xFF));
                    byteBuffer.put((byte)((pixel >> 8) & 0xFF));
                    byteBuffer.put((byte)((pixel) & 0xFF));
                }
            }
            byteBuffer.rewind();

            Tensor inputTensor = Tensor.create(UInt8.class, new long[] {1, bi.getHeight(), bi.getWidth(), 3}, byteBuffer);
            	
            List<Tensor<?>> result = smbSession.runner()
                    .feed("image_tensor", inputTensor)
                    .fetch("detection_boxes")
                    .fetch("detection_scores")
                    .fetch("detection_classes")
                    .fetch("num_detections")
                    .run();
            //System.out.println(" end tensor run 0="+MyUtils.logTimeFormatter.format(new Date()) );
            int numMaxClasses = 54;

            float[][][] boxes = new float[1][numMaxClasses][4];
            float[][][] detection_boxes = result.get(0).copyTo(boxes);
            float[][] scores = new float[1][numMaxClasses];
            float[][] detection_scores = result.get(1).copyTo(scores);
            float[][] classes = new float[1][numMaxClasses];
            float[][] detection_classes = result.get(2).copyTo(classes);
            float[] n = new float[1];
            float[] numDetections = result.get(3).copyTo(n);

            int numDet = Math.round(numDetections[0]);
            //System.out.println("Number of detected: " + numDet);
            int limitResult=20;
            if(numDet<limitResult) {
            	limitResult = numDet;
            }	
            
            System.out.println(" ob start scan ends ="+MyUtils.logTimeFormatter.format(new Date()) +" ,limit="+limitResult);
            List<TensorResult> listResult=new ArrayList<TensorResult>();
            TensorResult row;
            for(int i=0; i<limitResult; i++) {
            	
            	float d=Float.parseFloat(TensorUtil.removeScientificNotation(detection_scores[0][i]+""));
            	if(d>0.50) {
            		row=new TensorResult();
            		System.out.println("-----------------------------------");

                    int ymin = Math.round(detection_boxes[0][i][0] * h);
                    int xmin = Math.round(detection_boxes[0][i][1] * w);
                    int ymax = Math.round(detection_boxes[0][i][2] * h);
                    int xmax = Math.round(detection_boxes[0][i][3] * w);

                    //System.out.println("X1 " + xmin + " Y1 " + ymin + " X2 " + xmax + " Y2 " + ymax);
                    System.out.println("Score " + detection_scores[0][i]);
                    //System.out.println("Predicted " + (int)detection_classes[0][i]);

//                    g.setColor(Color.RED);
//                    g.drawRect(xmin, ymin, xmax - xmin, ymax - ymin);
//                    g.drawString(TensorUtil.labels[ (int) (Math.round(detection_classes[0][i]))-1 ], xmin, ymin);
//                    System.out.println("predected class="+Math.round(detection_classes[0][i]));
                    
                    if(TensorUtil.labels[ (int) (Math.round(detection_classes[0][i]))-1 ]=="number_plate") {
//                    	System.out.println("step 1 ocr="+TensorUtil.labels[ (int) (Math.round(detection_classes[0][i]))-1 ]);
                        Rectangle rect=new Rectangle(xmin+padX, ymin+padY, xmax-xmin, ymax-ymin);
                        
                        row.rect=rect;	
                        row.score=d;
                        row.class_name=TensorUtil.labels[ (int) (Math.round(detection_classes[0][i]))-1 ];
                        
                        listResult.add(row);
                    	//BufferedImage np = cropImage(orgImg, rect);
                    	//callPost(np);
                    }
            	}else {
            		
            	}
            }
            return listResult;
            //System.out.println(" end scan="+new Date());
            
            //currentImage.setImage(SwingFXUtils.toFXImage(bi, null ));
            //ImageIO.write(bi, "PNG", new File(args[1]));
            //ImageIO.write(bi, "PNG", new File("C:\\Users\\LaserTrac\\Desktop\\bi1.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		
	}
	
	
	
	
}
