package com.satotoru.desktop_recorder;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;

public class DesktopRecorder {

	private static int FRAME_RATE = 10;
	private static final int VIDEO_CODEC = avcodec.AV_CODEC_ID_MPEG4;
	private static final int VIDEO_BITRATE = 2560000;
	private static final String VIDEO_FORMAT = "mp4";

	private final String captureFilePath;
	private final FFmpegFrameGrabber grabber;
	private final FFmpegFrameRecorder recorder;
	
	private boolean recording;
	
	public DesktopRecorder(String captureFilePath) throws Exception {
		this(captureFilePath, FRAME_RATE, VIDEO_BITRATE);
	}
	
	public DesktopRecorder(String captureFilePath, int frameRate, int bitRate) {
		this.captureFilePath = captureFilePath;
		Dimension desktopSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle screenRect = new Rectangle(desktopSize);

		this.grabber = new FFmpegFrameGrabber("desktop");
		this.grabber.setFormat("gdigrab");
		this.grabber.setFrameRate(10);
		this.grabber.setImageWidth(screenRect.width);
		this.grabber.setImageHeight(screenRect.height);

		this.recorder = new FFmpegFrameRecorder(DesktopRecorder.this.captureFilePath + "." + VIDEO_FORMAT,
				screenRect.width, screenRect.height);
		recorder.setFrameRate(frameRate);
		recorder.setVideoCodec(VIDEO_CODEC);
		recorder.setVideoBitrate(bitRate);
		recorder.setFormat(VIDEO_FORMAT);
	}

	public void start() throws Exception {
		this.recording = true;
		Thread captureThread = new Thread() {
			@Override
			public void run() {
				try {
					grabber.start();
					recorder.start();

					while (DesktopRecorder.this.recording) {
						recorder.record(grabber.grab());
					}

					grabber.stop();
					recorder.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		};

		captureThread.start();
	}

	public void stop() throws Exception {
		this.recording = false;
	}

	public static void main(String args[]) {
		try {
			int duration = Integer.parseInt(args[1]);
			DesktopRecorder recorder = new DesktopRecorder(args[0]);
			recorder.start();
			Thread.sleep(duration * 1000);
			recorder.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
