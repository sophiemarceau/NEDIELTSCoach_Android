/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年6月24日 
 * 
 *******************************************************************************/
package com.lelts.fragment.classroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.bean.ClassRoomConnection_info;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lelts.activity.classroomconnection.adapter.ClassroomConnectionAdapter;
import com.lelts.activity.main.MainActivity;
import com.lelts.tool.IntentUtlis;
import com.lelts.welcome.LoginActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.AvoidXfermode.Mode;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
public class ClassRoomConnectionRoomActivity extends Activity implements
		OnClickListener {
	private Button btn_next;
	private Context context;
	private List<HashMap<String, Object>> mlist;
	private ClassroomConnectionAdapter madapter;
	private GridView mgridview;
	private TextView txt_passcode;
	private SharedPreferences share;
	private String passcode;
	private TextView txt_stunum;
	private String studentOnLineNum, classStudentTotalNum, ccId;
	private Timer timer;
	private ImageButton img_back;
	private Intent intent;
	// 判断是否是第一次请求 。true 是第一次
	private boolean flag = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classroom_connection);
		initview();
		getcode();
		getdata();
		timer();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		initview();
		getcode();
		getdata();
		timer();

		super.onRestart();
	}

	/**
	 * 方法说明：初始化控件
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = this;
		btn_next = (Button) findViewById(R.id.btn_next_ClassRoom_conncetion);
		mgridview = (GridView) findViewById(R.id.ClassRoom_conncetion_gridView);
		txt_passcode = (TextView) findViewById(R.id.fragment_classroom_txt_code);
		txt_stunum = (TextView) findViewById(R.id.txt_ClassRoom_conncetion_count);
		btn_next.setOnClickListener(this);
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		img_back = (ImageButton) findViewById(R.id.btn_back_ClassRoom_conncetion);
		img_back.setOnClickListener(this);
		LodDialogClass.showCustomCircleProgressDialog(context, null,
				getString(R.string.common_Loading));
	}

	/**
	 * 方法说明：生成暗号
	 *
	 */
	private void getcode() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		passcode = intent.getStringExtra("passcode");
		txt_passcode.setText(passcode);
		ccId = intent.getStringExtra("ccId");

		SharedPreferences share = getSharedPreferences("teacherinfo",
				MODE_PRIVATE);
		Editor editor = share.edit();
		// editor.putString("BSSNAME", BSSNAME);
		editor.putString("code", passcode);
		editor.putString("ccId", ccId);
		editor.commit();
		System.out.println("passcode==========" + passcode);
		System.out.println("ccId==========" + ccId);
	}

	/**
	 * 方法说明：网络获取全部学生的数据
	 *
	 */
	private void getdata() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("token==========" + share.getString("Token", ""));

		utils.send(HttpMethod.GET,
				Constants.URL_TeacherOrStudentGetStudentOnLine + "?passCode="
						+ passcode + "&roleId=2", params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						LodDialogClass.closeCustomCircleProgressDialog();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						try {
							JSONObject obj = new JSONObject(result);
							JSONObject obj1 = obj.getJSONObject("Data");
							studentOnLineNum = obj1
									.getString("studentOnLineNum");
							classStudentTotalNum = obj1
									.getString("classStudentTotalNum");
							txt_stunum.setText("学员列表(" + studentOnLineNum + "/"
									+ classStudentTotalNum + ")");
							mlist = new ArrayList<HashMap<String, Object>>();
							if (obj1.getString("studentList").equals("null")) {
								
							}else{
								JSONArray array = obj1.getJSONArray("studentList");
								for (int i = 0; i < array.length(); i++) {
									JSONObject data = array.getJSONObject(i);
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("studentname",
											data.getString("studentname"));

									map.put("studentloginstatus",
											data.getString("studentloginstatus"));

									map.put("iconUrl", data.getString("iconUrl"));

									mlist.add(map);

									// 判断list的集合是否发生改变 ，发生改变 set adapter

								}
								
							}
							

							if (flag == true) {
								madapter = new ClassroomConnectionAdapter(
										mlist, context);
								mgridview.setAdapter(madapter);
								flag = false;
							} else {
								madapter.setdatachanges(mlist);
								madapter.notifyDataSetChanged();
							}
							LodDialogClass.closeCustomCircleProgressDialog();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out
								.println("result===================" + result);
					}
				});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_next_ClassRoom_conncetion:
			intent = new Intent(context, ClassRoomStudent.class);
			intent.putExtra("code", passcode);
			intent.putExtra("ccId", ccId);
			startActivity(intent);
			// 销毁页面 网络请求停止
			// finish();
			break;

		// 返回键
		case R.id.btn_back_ClassRoom_conncetion:

			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 方法说明：每隔3秒请求数据 刷新页面
	 *
	 */
	private void timer() {
		timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getdata();
			}
		};
		timer.schedule(task, 3000, 3000);
	}

	/**
	 * 销毁定时器的方式
	 */
	@Override
	protected void onStop() {
		timer.cancel();// 程序退出时cancel timer
		super.onStop();
	}

}
