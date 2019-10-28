package com.lels.activity.myself;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.R;
import com.lels.bean.ButtonControl;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lelts.tool.DataCleanManager;
import com.lelts.welcome.LoginActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 我的收藏
 * **/
public class MyselfSyssetActivity extends Activity implements OnClickListener {

	private ImageButton imageview_back;

	private LinearLayout linear_clear;
	private LinearLayout linear_exit;

	private String token;
	// 缓存 数据
	private TextView txt_cache;
	private String cache;

	private DataCleanManager manager;

	private Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_sysset);
		getdatafromshare();
		init();
		ExitApplication.getInstance().addActivity(this);
	}

	private void getdatafromshare() {

		SharedPreferences share = MyselfSyssetActivity.this
				.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		editor = share.edit();
		token = share.getString("Token", "");

		Log.d("StudyPlanActivity", "获取的token数值为=====" + token);

	}

	private void init() {

		imageview_back = (ImageButton) findViewById(R.id.imageview_back);

		linear_clear = (LinearLayout) findViewById(R.id.linear_clear);
		linear_exit = (LinearLayout) findViewById(R.id.linear_exit);
		txt_cache = (TextView) findViewById(R.id.sys_txt_cache);
		imageview_back.setOnClickListener(this);
		linear_clear.setOnClickListener(this);
		linear_exit.setOnClickListener(this);
		manager = new DataCleanManager();
		try {
			cache = manager.getTotalCacheSize(MyselfSyssetActivity.this);
			txt_cache.setText(cache);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		case R.id.linear_exit:
			if (ButtonControl.isFastClick()) {
				return;
			} else {
				LodDialogClass.showCustomCircleProgressDialog(
						MyselfSyssetActivity.this, null, "退出中...");
				logoffuser();
			}
			

			
			break;
		// 清除缓存 ，弹出diaglog
		case R.id.linear_clear:
			sHowDialog();
	
			break;
		default:
			break;
		}
	}

	private void logoffuser() {

		@SuppressWarnings("static-access")
		String url = new Constants().URL_MYSELF_LOGOFFUSER;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							Log.d("MyselfSyssetActivity", "退出登录信息为"
									+ responseInfo.result);
							JSONObject str = new JSONObject(responseInfo.result);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							if (Result.equals("true")) {
								LodDialogClass.closeCustomCircleProgressDialog();
								editor.putString("userPass", "");
								editor.commit();
								Intent intent = new Intent();
								intent.setClass(MyselfSyssetActivity.this, LoginActivity.class);
								// DataCleanManager.cleanSharedPreference(MyselfSyssetActivity.this);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							}else{
								Toast.makeText(MyselfSyssetActivity.this, Infomation, 0).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure");

					}

				});

	}

	/**
	 * 方法说明：弹出是否清除diaglog
	 *
	 */
	private void sHowDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(MyselfSyssetActivity.this);
		builder.setMessage("确认清除吗?");
		builder.setTitle("清除缓存");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				manager.clearAllCache(MyselfSyssetActivity.this);
				txt_cache.setText("0.0K");
				Toast.makeText(MyselfSyssetActivity.this, "清除缓存成功",
						Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		});
	builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});


		 builder.create().show();
	}


}
