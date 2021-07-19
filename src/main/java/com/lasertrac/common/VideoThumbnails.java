package com.lasertrac.common;


//import org.bytedeco.javacpp.opencv_core;
//import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.IplImage;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

//import org.apache.commons.io.FilenameUtils;

public class VideoThumbnails {

    private static final String IMAGEMAT = "png";
    private static final String ROTATE = "rotate";

    /**
     * Get video thumbnails
     * @param filePath: video path
     * @param mod: video length / mod gets the few frames
     * @throws Exception
     */
    public static String randomGrabberFFmpegImage(String filePath, int mod) throws Exception {
        String targetFilePath = getThumbFileName(filePath);

        FFmpegFrameGrabber ff = FFmpegFrameGrabber.createDefault(filePath);
        ff.start();
        String rotate = ff.getVideoMetadata(ROTATE);
        int ffLength = ff.getLengthInFrames();
        Frame f;
        int i = 0;
        int index = 40;
        while (i < ffLength) {
            f = ff.grabImage();
            if(i == index){
                if (null != rotate && rotate.length() > 1) {
                    OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
//                    IplImage src = converter.convert(f);
//                    f = converter.convert(rotate(src, Integer.valueOf(rotate)));
                }
                //targetFilePath = getImagePath(filePath, i);
                doExecuteFrame(f, targetFilePath);
                break;
            }
            //break;
            i++;
        }
        ff.stop();
        return targetFilePath;
    }

    public static String getThumbFileName(String filePath){
        String targetFilePath = ConfigurationProps.video_thumb_dir;
        File targetDirectory=new File(targetFilePath);
        if(targetDirectory.isDirectory()==false){
            targetDirectory.mkdir();
        }
        File videoFile= new File(filePath);
        String nameWithoutExt =videoFile.getName();
        nameWithoutExt = nameWithoutExt.substring(0,nameWithoutExt.lastIndexOf('.'));

        System.err.println("name without ext = "+nameWithoutExt);
        String video_date_folder = new File(videoFile.getParent()).getName();

        File videoDateFolder=new File(targetFilePath + "/"+video_date_folder);
        if(videoDateFolder.isDirectory()==false){
            videoDateFolder.mkdir();
        }

        targetFilePath += "/"+video_date_folder+"/"+nameWithoutExt+".png";
        return  targetFilePath;
    }

    /**
     * Generate thumbnail storage path based on video path
     * @param filePath: video path
     * @param index: the few frames
     * @return: the storage path of the thumbnail
     */
    private static String getImagePath(String filePath, int index){
        if(filePath.contains(".") && filePath.lastIndexOf(".") < filePath.length() - 1){
            filePath = filePath.substring(0, filePath.lastIndexOf(".")).concat("_").concat(String.valueOf(index)).concat(".").concat(IMAGEMAT);
        }
        return filePath;
    }


    /**
     * Rotate the picture
     * @param src
     * @param angle
     * @return
     */
//    public static IplImage rotate(IplImage src, int angle) {
//        IplImage img = IplImage.create(src.height(), src.width(), src.depth(), src.nChannels());
//        opencv_core.cvTranspose(src, img);
//        opencv_core.cvFlip(img, img, angle);
//        return img;
//    }

    /**
     * Capture thumbnails
     * @param f
     * @param targerFilePath: cover image
     */
    public static void doExecuteFrame(Frame f, String targerFilePath) {
        if (null == f || null == f.image) {
            return;
        }
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bi = converter.getBufferedImage(f);
        bi = scale(bi,160,90);
        File output = new File(targerFilePath);
        try {
            ImageIO.write(bi, IMAGEMAT, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage scale(BufferedImage src, int w, int h)
    {
        BufferedImage img =
                new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int x, y;
        int ww = src.getWidth();
        int hh = src.getHeight();
        int[] ys = new int[h];
        for (y = 0; y < h; y++)
            ys[y] = y * hh / h;
        for (x = 0; x < w; x++) {
            int newX = x * ww / w;
            for (y = 0; y < h; y++) {
                int col = src.getRGB(newX, ys[y]);
                img.setRGB(x, y, col);
            }
        }
        return img;
    }

    /**
     * Randomly generate random number sets based on video length
     * @param baseNum: the base number, here is the video length
     * @param length: random number set length
     * @return: a collection of random numbers
     */
    public static List<Integer> random(int baseNum, int length) {
        List<Integer> list = new ArrayList<>(length);
        while (list.size() < length) {
            Integer next = (int) (Math.random() * baseNum);
            if (list.contains(next)) {
                continue;
            }
            list.add(next);
        }
        Collections.sort(list);
        return list;
    }



}
