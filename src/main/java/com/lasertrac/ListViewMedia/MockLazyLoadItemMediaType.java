package com.lasertrac.ListViewMedia;

import com.lasertrac.common.VideoThumbnails;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MockLazyLoadItemMediaType extends LazyLoadItem<MediaProps> {

    // should not really be here: just for demo... Also limiting threads for demo
    private static final Executor EXEC = Executors.newFixedThreadPool(10, runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t;
    });

    private final Random rng = new Random();

    private final int id;

    private MediaProps mp1;
    public MockLazyLoadItemMediaType(int id, String text, String fileName, String thumbImg) {
        super(EXEC, new MediaProps());
        MediaProps mp=new MediaProps();
        mp.text=text;
        mp.thumbImg="loading";
        mp.fileName=fileName;

        mp1 = mp;
        this.id = id;
    }

    @Override
    protected MediaProps load() throws Exception {
        System.out.println("loading item " + id);
        //Thread.sleep(rng.nextInt(2000) + 1000);
        MediaProps mp = new MediaProps();

        String fileName = "";

        try{
            fileName = VideoThumbnails.randomGrabberFFmpegImage(mp1.fileName,2);

        }catch(Exception ex){
            System.out.println("error= "+ex.toString());
            URL url = getClass().getResource("/images/no_file_found.png");
            fileName =  url.toString();
            //fileName = getClass().getResource("/com/lasertrac/view/VideoPlayer.fxml");
        }

        File f = new File(mp1.fileName);

        mp.fileName = mp1.fileName;
        mp.thumbImg = fileName;
        mp.text = f.getName();
        return mp;
    }

}
