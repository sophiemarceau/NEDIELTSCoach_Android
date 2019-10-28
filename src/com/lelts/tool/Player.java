package com.lelts.tool;

import java.util.Timer;
import java.util.TimerTask;

import com.example.hello.R;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

public class Player {
	public MediaPlayer mediaPlayer;
	private SeekBar skbProgress;
	private ImageView imgplay;
	
	private Timer mTimer = new Timer();

	public Player(SeekBar skbProgress,ImageView imgplay) {
		this.skbProgress = skbProgress;
		this.imgplay = imgplay;
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}
		mTimer.schedule(mTimerTask, 0, 50);
	}

	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {

			if (mediaPlayer == null)
				return;
			if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
				handleProgress.sendEmptyMessage(0);
			} /*else {
				// skbProgress.setProgress(0);
				handleProgress.sendEmptyMessage(1);
			}*/
		}
	};

	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {
			int p = msg.arg1;
			
				long position = mediaPlayer.getCurrentPosition();
				long duration = mediaPlayer.getDuration();
				if (duration > 0) {
					long pos = skbProgress.getMax() * position / duration;
					skbProgress.setProgress((int)pos);
				}
			 /*else {
				skbProgress.setProgress(0);
			}*/
		};
	};

	public void play() {
		this.imgplay.setBackgroundResource(R.drawable.bofang);
		mediaPlayer.start();
		System.out.println("--start--");
	}

	public void playUrl(String videoUrl) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(videoUrl);
			mediaPlayer.prepare();
			skbProgress.setProgress(0);
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub

					skbProgress.setProgress(0);
					imgplay.setBackgroundResource(R.drawable.bofang);
					/*mp.release();
					mp = null;*/
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		this.imgplay.setBackgroundResource(R.drawable.tingzhi);
		mediaPlayer.pause();
		System.out.println("--pause()--");
	}

	public void stop() {
		this.imgplay.setBackgroundResource(R.drawable.tingzhi);
		if (mediaPlayer != null) {
			handleProgress.removeCallbacks(mTimerTask);
			System.out.println("stop()");
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	public void onPrepared(MediaPlayer arg0) {
		arg0.start();
		Log.e("mediaPlayer", "onPrepared");
	}

	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		int currentProgress = skbProgress.getMax()
				* mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
		Log.e(currentProgress + "% play", percent + "% buffer");
	}

}
