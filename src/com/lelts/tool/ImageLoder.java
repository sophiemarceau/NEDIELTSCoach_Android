/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年6月24日 
 * 
 *******************************************************************************/
package com.lelts.tool;

import java.util.ArrayList;
import java.util.List;




import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.constants.Constants;
import com.lelts.chatroom.activity.KickOffDialog;
import com.lelts.chatroom.context.MyContext;
import com.lelts.welcome.LoginActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIMClientWrapper;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ConnectionStatusListener;
import io.rong.imlib.RongIMClient.ErrorCode;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年6月24日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class ImageLoder extends Application implements ConnectionStatusListener {

	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 初始化融云
		RongIM.init(this);
		MyContext.getInstance().init(this);
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).showImageOnLoading(R.drawable.mor) // 默认图片
				.showImageForEmptyUri(R.drawable.mor) // url爲空會显示该图片，自己放在drawable里面的
				.showImageOnFail(R.drawable.mor)// 加载失败显示的图片
				.displayer(new RoundedBitmapDisplayer(360)) // 圆角，不需要请删除
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).memoryCacheExtraOptions(480, 800)// 缓存在内存的图片的宽和高度
				.diskCache(new UnlimitedDiscCache(getCacheDir())).diskCacheExtraOptions(480, 800, null)// CompressFormat.PNG类型，70质量（0-100）
				.memoryCache(new WeakMemoryCache()).memoryCacheSize(2 * 1024 * 1024) // 缓存到内存的最大数据
				.diskCacheSize(50 * 1024 * 1024) // 缓存到文件的最大数据
				.diskCacheFileCount(1000) // 文件数量
				.defaultDisplayImageOptions(options) // 上面的options对象，一些属性配置
				.build();
		ImageLoader.getInstance().init(config); // 初始化
		registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
	}

	public void connect(String token) {
		RongIM.connect(token, new ConnectCallback() {

			@Override
			public void onSuccess(String arg0) {
				// TODO Auto-generated method stub
				System.out.println("connection...OK." + arg0);
				setOtherListener();
			}

			@Override
			public void onError(ErrorCode arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTokenIncorrect() {
				// TODO Auto-generated method stub

			}
		});
	}

	public void reconnect(String token) {
		connect(token);
	}

	public void logout() {
		if (null != RongIM.getInstance()) {
			RongIM.getInstance().logout();
			RongIM.getInstance().disconnect();
		}
	}

	@Override
	public void onChanged(ConnectionStatus connectionStatus) {
		switch (connectionStatus) {

		case CONNECTED:// 连接成功。
			System.out.println("connection...CONNECTED");
			break;
		case DISCONNECTED:// 断开连接。
			System.out.println("connection...DISCONNECTED");
			break;
		case CONNECTING:// 连接中。
			System.out.println("connection...CONNECTING");
			break;
		case NETWORK_UNAVAILABLE:// 网络不可用。
			System.out.println("connection...NETWORK_UNAVAILABLE");
			break;
		case KICKED_OFFLINE_BY_OTHER_CLIENT:// 用户账户在其他设备登录，本机会被踢掉线
			System.out.println(" connection...KICKED_OFFLINE_BY_OTHER_CLIENT++吴帅锋");
//			Toast.makeText(getApplicationContext(), "kick off", Toast.LENGTH_LONG).show();
			if (isAppOnForeground()) {
				notifyKickOff();
			} else {
				waitToShowKickDlg = true;
			}
			break;
		default:
			System.out.println("connection...unknown");
			break;
		}
	}

	private void setOtherListener() {
		RongIMClientWrapper.setConnectionStatusListener(this);
	}

	private void notifyKickOff() {
		SharedPreferences stushare = getSharedPreferences("stushare", MODE_PRIVATE);
		Editor editor = stushare.edit();
		editor.remove("userPass");
		editor.commit();
		Intent intent1 = new Intent();
		intent1.setAction("com.lelts.tool");
		sendBroadcast(intent1);
		Intent intent = new Intent(this, KickOffDialog.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	// 网络解析聊天token数据
	public void checkNewestUserInfo(String token) {
		String url = Constants.URL_ActiveClass_getChatToken;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);
		params.addBodyParameter("checkUpdate", "true");
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("网络解析聊天token数据    ===  onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				System.out.println("网络解析聊天token数据wu   dsfs    ===  onSuccess : " + result);
				try {
					JSONObject str = new JSONObject(result);
					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					String data = str.getString("Data");
					JSONObject obj_data = new JSONObject(data);
					String chatToken = obj_data.getString("chatToken");
					System.out.println("login的聊天数据 ？？？=" + chatToken);
					SharedPreferences share = getSharedPreferences("userChatToken", MODE_PRIVATE);
					Editor editor = share.edit();
					editor.putString("chatToken", chatToken);
					editor.commit();

					SharedPreferences userInfo = getSharedPreferences("userChatToken", MODE_PRIVATE);
					Editor et_userInfo = userInfo.edit();
					et_userInfo.putString("chatToken", chatToken);
					et_userInfo.commit();
				} catch (JSONException e) {
					e.printStackTrace();
				} finally {
					SharedPreferences share = getSharedPreferences("userinfo", MODE_PRIVATE);
					connect(share.getString("chatToken", ""));
				}
			}
		});
	}

	public boolean isAppOnForeground() {

		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}
	
	private boolean waitToShowKickDlg = false;
	@SuppressLint("NewApi")
	class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

		@Override
		public void onActivityCreated(Activity arg0, Bundle arg1) {
			// TODO Auto-generated method stub
			allActivities.add(arg0);
		}

		@Override
		public void onActivityDestroyed(Activity activity) {
			// TODO Auto-generated method stub
			allActivities.remove(activity);
		}

		@Override
		public void onActivityPaused(Activity activity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityResumed(Activity activity) {
			// TODO Auto-generated method stub
			if (waitToShowKickDlg && null != RongIM.getInstance() && null != RongIM.getInstance().getRongIMClient()) {
				if (RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus() == ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT) {
					notifyKickOff();
				}
				waitToShowKickDlg = false;
			}
		}

		@Override
		public void onActivitySaveInstanceState(Activity activity,
				Bundle outState) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityStarted(Activity activity) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onActivityStopped(Activity activity) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private List<Activity> allActivities = new ArrayList<Activity>();
	public void finishAllDelay() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					if (!allActivities.isEmpty()) {
						for (int i = 0; i < allActivities.size(); i++) {
							if (!(allActivities.get(i) instanceof LoginActivity))
							allActivities.get(i).finish();
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}, 500);
		
	}
}
