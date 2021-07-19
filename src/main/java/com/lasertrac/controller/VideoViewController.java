package com.lasertrac.controller;

import com.lasertrac.ListViewMedia.*;
import com.lasertrac.common.ConfigurationProps;
import com.lasertrac.common.MyUtils;
import com.lasertrac.common.SoundAndMic;
import com.lasertrac.main.VideoPlayback;
import com.lasertrac.test.VideoPlayBackTest1;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.lasertrac.main.StartScreen;

import javafx.event.EventHandler;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.JavaFXFrameConverter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VideoViewController {

	private static final Logger LOG = Logger.getLogger(VideoPlayback.class.getName());

	StartScreen startSreenObj;
	
	public void setStartScreenObject(StartScreen obj) {
		this.startSreenObj = obj;
	}
	
	@FXML
	private Button playBtn;

	@FXML
	private Button pauseBtn;

	@FXML
	private Button fastForwardBtn;

	@FXML
	private Button prevFrameBtn;

	@FXML
	private Button nextFrameBtn;

	@FXML
	private Button snapShotBtn;

	@FXML
	private Button createChallanBtn;

	@FXML
	private Button frameAnprBtn;

	@FXML
	private ImageView videoImgView;

	@FXML
	private AnchorPane videoImageAnchor;


	@FXML
	private Pane rootPane;

	@FXML
	private GridPane gridPaneParent;

	@FXML
	private Pane videoListViewPane;

	@FXML
	private Button calenderBtn;

//	@FXML
//	private ListView videosListView;

	// array of supported extensions (use a List if you prefer)
	static final String[] EXTENSIONS = new String[]{
			"mp4" // and other formats you need
	};

	// filter to identify images based on their extensions
	static final FilenameFilter VIDEO_FILTER = new FilenameFilter() {

		@Override
		public boolean accept(final File dir, final String name) {
			for (final String ext : EXTENSIONS) {
				if (name.endsWith("." + ext)) {
					return (true);
				}
			}
			return (false);
		}
	};

	/**
	 * Initialize method, automatically called by @{link FXMLLoader}
	 */
	public void initialize()
	{

		gridPaneParent.prefWidthProperty().bind(rootPane.widthProperty());
		gridPaneParent.prefHeightProperty().bind(rootPane.heightProperty());

		videoImgView.fitWidthProperty().bind(videoImageAnchor.widthProperty());
		videoImgView.fitHeightProperty().bind(videoImageAnchor.heightProperty());

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		LocalDateTime now = LocalDateTime.now();

		calenderBtn.setText(dtf.format(now));

		System.err.println("set listener");
		setListeners();

		initvideoListView();
		System.err.println("init video list view");


		Date date=new Date();
		//loadData(ConfigurationProps.video_dir+"/"+ MyUtils.directoryDTFormatter.format(date1));
		loadData(ConfigurationProps.video_dir+"/20_07_2021/");
		System.err.println("data loaded");
	}

	void loadData(String videoDir){
		System.err.println("load data "+videoDir);
		if(new File(videoDir).isDirectory()){
			File repo = new File(videoDir);
			// Create list with extractor, so the list cell gets updated
			// when the loadedProperty() changes (i.e. when loading completes)
			ObservableList<LazyLoadItem<MediaProps>> items = FXCollections.observableArrayList(item -> new Observable[] {item.loadedProperty()});

			videoListView.setCellFactory(lv -> new LazyLoadingListCellMediaType());
			File[] videoFiles = repo.listFiles(VIDEO_FILTER);
			int i=1;

			System.err.println("count "+videoFiles.length);
			for (File f : videoFiles) {
				items.add(new MockLazyLoadItemMediaType(i, f.getName(), f.getAbsolutePath(), f.getAbsolutePath() ));
				System.out.println(f.getAbsolutePath());
				i++;
			}

			videoListView.setItems(items);

		}else{
			System.err.println("not a directory");
		}
	}

	ListView<LazyLoadItem<MediaProps>> videoListView;
	VideoPlayBackThread videoPlaybackThread;

	void initvideoListView(){
		if(videoListView==null){
			videoListView = new ListView<>();
			videoListViewPane.getChildren().add(videoListView);
			videoListView.setStyle(".list-view .scroll-bar:vertical,\n" +
					".list-view .scroll-bar:horizontal{\n" +
					"\t-fx-pref-width: 0;-fx-padding: 0px;\n" +
					"}");
			//videoListView.setStyle("-fx-background-color:yellow;");
			videoListView.prefHeightProperty().bind(videoListViewPane.heightProperty());

			videoListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					System.out.println("file name ="+videoListView.getSelectionModel().getSelectedItem().getValue().fileName);
					playVideoFile(videoListView.getSelectionModel().getSelectedItem().getValue().fileName);
				}
			});
		}
	}
	void playVideoFile(String videoFile){
		stopVideoPlayBack();
		videoPlaybackThread = new VideoPlayBackThread(videoFile);
		videoPlaybackThread.start();
	}
	void pauseVideo(){
		if(videoPlaybackThread!=null){
			videoPlaybackThread.doPause();
		}
	}
	void playVideo(){
		if(videoPlaybackThread!=null){
			videoPlaybackThread.doPlay();
		}
	}
	void stopVideoPlayBack(){
		if(videoPlaybackThread!=null){
			try{
				videoPlaybackThread.doStop();
				Thread.sleep(1000);
			}catch(Exception ex){

			}
		}
	}

	private static class PlaybackTimer {
		private Long startTime = -1L;
		private final DataLine soundLine;

		public PlaybackTimer(DataLine soundLine) {
			this.soundLine = soundLine;
		}

		public PlaybackTimer() {
			this.soundLine = null;
		}

		public void start() {
			if (soundLine == null) {
				startTime = System.nanoTime();
			}
		}

		public long elapsedMicros() {
			if (soundLine == null) {
				if (startTime < 0) {
					throw new IllegalStateException("PlaybackTimer not initialized.");
				}
				return (System.nanoTime() - startTime) / 1000;
			} else {
				return soundLine.getMicrosecondPosition();
			}
		}
	}
	class VideoPlayBackThread extends Thread {
		String videoFileName="";
		public VideoPlayBackThread(String name) {
			//super(name);
			videoFileName = name;
		}
		private boolean doStop = false;
		public synchronized void doStop() {
			this.doStop = true;
		}
		private synchronized boolean keepRunning() {
			return this.doStop == false;
		}

		private boolean doPause = false;
		public synchronized void doPause(){
			this.doPause=true;
		}
		public synchronized void doPlay(){
			this.doPause=false;
		}
		private synchronized boolean keepPause() {
			return this.doPause == true;
		}

		public void run() {
			System.out.println("thread is running...");
			try {
				//final String videoFilename = getParameters().getRaw().get(0);
				final FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoFileName);
				grabber.start();
				//primaryStage.setWidth(grabber.getImageWidth());
				//primaryStage.setHeight(grabber.getImageHeight());
				final PlaybackTimer playbackTimer;
				final SourceDataLine soundLine;
				if (grabber.getAudioChannels() > 0) {
					final AudioFormat audioFormat = new AudioFormat(grabber.getSampleRate(), 16, grabber.getAudioChannels(), true, true);

					final DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
					soundLine = (SourceDataLine) AudioSystem.getLine(info);
					soundLine.open(audioFormat);
					soundLine.start();
					playbackTimer = new PlaybackTimer(soundLine);
				} else {
					soundLine = null;
					playbackTimer = new PlaybackTimer();
				}

				final JavaFXFrameConverter converter = new JavaFXFrameConverter();

				final ExecutorService audioExecutor = Executors.newSingleThreadExecutor();
				final ExecutorService imageExecutor = Executors.newSingleThreadExecutor();

				final long maxReadAheadBufferMicros = 1000 * 1000L;

				long lastTimeStamp = -1L;
				while (!Thread.interrupted() && keepRunning()) {

					if(keepPause()){
						while (keepPause()){
							try{
								Thread.sleep(100);
							}catch(Exception ex){}
						}
					}
					final Frame frame = grabber.grab();
					if (frame == null) {
						break;
					}
					if (lastTimeStamp < 0) {
						playbackTimer.start();
					}
					lastTimeStamp = frame.timestamp;
					if (frame.image != null) {
						final Frame imageFrame = frame.clone();

						imageExecutor.submit(new Runnable() {
							public void run() {
								final Image image = converter.convert(imageFrame);
								final long timeStampDeltaMicros = imageFrame.timestamp - playbackTimer.elapsedMicros();
								imageFrame.close();
								if (timeStampDeltaMicros > 0) {
									final long delayMillis = timeStampDeltaMicros / 1000L;
									try {
										Thread.sleep(delayMillis);
									} catch (InterruptedException e) {
										Thread.currentThread().interrupt();
									}
								}
								Platform.runLater(new Runnable() {
									public void run() {
										videoImgView.setImage(image);
									}
								});
							}
						});
					} else if (frame.samples != null) {
						if (soundLine == null) {
							throw new IllegalStateException("Internal error: sound playback not initialized");
						}
						final ShortBuffer channelSamplesShortBuffer = (ShortBuffer) frame.samples[0];
						channelSamplesShortBuffer.rewind();

						final ByteBuffer outBuffer = ByteBuffer.allocate(channelSamplesShortBuffer.capacity() * 2);

						for (int i = 0; i < channelSamplesShortBuffer.capacity(); i++) {
							short val = channelSamplesShortBuffer.get(i);
							outBuffer.putShort(val);
						}

						audioExecutor.submit(new Runnable() {
							public void run() {
								soundLine.write(outBuffer.array(), 0, outBuffer.capacity());
								outBuffer.clear();
							}
						});
					}
					final long timeStampDeltaMicros = frame.timestamp - playbackTimer.elapsedMicros();
					if (timeStampDeltaMicros > maxReadAheadBufferMicros) {
						Thread.sleep((timeStampDeltaMicros - maxReadAheadBufferMicros) / 1000);
					}
				}

				if (!Thread.interrupted()) {
					long delay = (lastTimeStamp - playbackTimer.elapsedMicros()) / 1000 +
							Math.round(1 / grabber.getFrameRate() * 1000);
					Thread.sleep(Math.max(0, delay));
				}
				grabber.stop();
				grabber.release();
				if (soundLine != null) {
					soundLine.stop();
				}
				audioExecutor.shutdownNow();
				audioExecutor.awaitTermination(10, TimeUnit.SECONDS);
				imageExecutor.shutdownNow();
				imageExecutor.awaitTermination(10, TimeUnit.SECONDS);

				//Platform.exit();
			} catch (Exception exception) {
				LOG.log(Level.SEVERE, null, exception);
				System.exit(1);
			}
		}
	}


	void setListeners(){
		playBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				playVideo();
			}
		});
		playBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
				playVideo();
			}
		});

		pauseBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
					pauseVideo();
			}
		});
		pauseBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
//				((Node)(event.getSource())).getScene().getWindow().hide();
//				startSreenObj.liveStage.show();
				pauseVideo();
			}
		});
		fastForwardBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
//				((Node)(event.getSource())).getScene().getWindow().hide();
//				startSreenObj.liveStage.show();
			}
		});

		prevFrameBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
//				((Node)(event.getSource())).getScene().getWindow().hide();
//				startSreenObj.liveStage.show();
			}
		});

		nextFrameBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
//				((Node)(event.getSource())).getScene().getWindow().hide();
//				startSreenObj.liveStage.show();
			}
		});

		snapShotBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
//				((Node)(event.getSource())).getScene().getWindow().hide();
//				startSreenObj.liveStage.show();
			}
		});

		createChallanBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
//				((Node)(event.getSource())).getScene().getWindow().hide();
//				startSreenObj.liveStage.show();
			}
		});

		frameAnprBtn.setOnTouchPressed(new EventHandler<TouchEvent>() {
			@Override
			public void handle(TouchEvent event) {
//				((Node)(event.getSource())).getScene().getWindow().hide();
//				startSreenObj.liveStage.show();
			}
		});

	}
}
