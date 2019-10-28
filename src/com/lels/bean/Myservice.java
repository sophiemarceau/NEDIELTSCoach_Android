package com.lels.bean;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class Myservice extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("oncreate-----启动oncreate方法");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("onstartcommand----启动了onstartcommand方法");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
