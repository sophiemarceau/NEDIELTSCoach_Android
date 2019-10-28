package com.lels.alarm;

import com.example.hello.R;
import com.lelts.tool.ClockInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class AlarmActivity extends Activity {

	MediaPlayer alarmMusic;
	WakeLock mWakelock;
	Vibrator mVibrator;
	public static AlarmActivity instance = null;
	private ClockInfo info;
	private long long_alarm;
	private boolean issound;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;

		info = (ClockInfo) getIntent().getSerializableExtra("info");
		long_alarm = getIntent().getLongExtra("long_alarm", 0);
		System.out.println("============" + info.getTitle());
		System.out.println("============" + info.getNote());
		System.out.println("============" + info.getSound());

		final Window win = getWindow();
		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		// 获取震动
		mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

		mVibrator.vibrate( 10000);

		// 加载指定音乐，并为之创建MediaPlayer对象

		alarmMusic = MediaPlayer.create(this, R.raw.fallbackring);
		alarmMusic.setLooping(true);

		String sss = info.getSound();
		Log.d("AlarmActivity", "info.sss======" + sss);
		issound = sss.equals("无");
		Log.d("AlarmActivity", "info.的sss=========" + String.valueOf(issound));
		// if (issound) {
		// issound = false;
		// Log.d("AlarmActivity", "判断" + "执行");
		// } else {
		// issound = true;
		// Log.d("AlarmActivity", "判断" + "未执行");
		// }

		if (issound) {
			Log.d("AlarmActivity", "未执行");
		} else {
			Log.d("AlarmActivity", "执行");
			Log.d("AlarmActivity", "info.getSound()===have musics tart");
			// 播放音乐
			alarmMusic.start();
		}

		// // 播放音乐
		// alarmMusic.start();

		// 创建一个对话框
		new AlertDialog.Builder(AlarmActivity.this).setTitle("我的提醒").setMessage(info.getTitle())
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 停止音乐
						alarmMusic.stop();
						// tingzhizhendong
						mVibrator.cancel();
						// 结束该Activity
						AlarmActivity.this.finish();
					}
				}).show();
	}
}
