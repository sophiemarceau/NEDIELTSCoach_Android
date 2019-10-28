package com.lels.alarm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lels.activity.myself.MyselfAddRemindActivity;
import com.lelts.tool.ClockInfo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class AlarmService extends Service {

	private AlarmManager alarmManager;

	private PendingIntent pendIntent;

	private long long_alarm_s;

//	private Intent intent = new Intent();

	// private List<HashMap<String, Object>> l_maps = new
	// ArrayList<HashMap<String,Object>>();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("AlarmManager", "AlarmManager.oncreate()");
		
		// l_maps = MyselfAddRemindActivity.mlist_clock;

//		long_alarm_s = MyselfAddRemindActivity.long_alarm;
//
//		System.out.println("距离设定时间多少秒" + long_alarm_s);

		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//		pendIntent = PendingIntent.getActivity(
//				MyselfAddRemindActivity.instance, 0, intent, 0);
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Log.d("AlarmManager", "AlarmManager.onStartCommand()");

		flags = START_STICKY;
		try {
			intent.setClass(MyselfAddRemindActivity.instance, AlarmActivity.class);
			
			long_alarm_s = intent.getLongExtra("long_alarm", 10);
			ClockInfo info = (ClockInfo) intent.getSerializableExtra("info");
			int num = intent.getIntExtra("num",0);
			int c_id = intent.getIntExtra("c_id",0);
			System.out.println("long_alarm_s == " + long_alarm_s +"       num =="+ num);
			
			pendIntent = PendingIntent.getActivity(
					MyselfAddRemindActivity.instance, c_id, intent, 0);

			alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
					+ long_alarm_s * 1000 - num * 1000, pendIntent);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.d("AlarmManager", "AlarmManager.onStart()");
//		intents.putExtra("long_alarm", long_alarm);
//		intents.putExtra("info", info);
//		long_alarm_s = intent.getLongExtra("long_alarm", 10);
//		ClockInfo info = (ClockInfo) intent.getSerializableExtra("info");
//		Log.d("AlarmManager", "long_alarm_s==="+long_alarm_s);
	}
}
