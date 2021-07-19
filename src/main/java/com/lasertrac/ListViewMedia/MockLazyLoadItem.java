package com.lasertrac.ListViewMedia;


import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MockLazyLoadItem extends LazyLoadItem<String> {

    // should not really be here: just for demo... Also limiting threads for demo
    private static final Executor EXEC = Executors.newFixedThreadPool(10, runnable -> {
        Thread t = new Thread(runnable);
        t.setDaemon(true);
        return t ;
    });

    private final Random rng = new Random();

    private final int id ;

    public MockLazyLoadItem(int id) {
        super(EXEC, "Not yet loaded...");
        this.id = id ;
    }

    @Override
    protected String load() throws Exception {
        System.out.println("loading item "+id);
        Thread.sleep(rng.nextInt(2000)+1000);
        return "Item "+id ;
    }

}
