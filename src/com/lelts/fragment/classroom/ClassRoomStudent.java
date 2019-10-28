/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年6月25日 
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
import com.lels.bean.ButtonControl;
import com.lels.bean.ClassRoomConnection_info;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.group.activity.GroupActivity;
import com.lels.manual.activity.ManualActivity;
import com.lels.random.activity.RandomActivity;
import com.lels.vote.activity.VoteActivity;
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
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年6月25日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class ClassRoomStudent extends Activity implements OnClickListener {
	private Button btn_end, btn_close;
	private RadioButton btn_answersheet, btn_group, btn_vote, btn_responder,
			btn_more;
	private Context context;
	private List<HashMap<String, Object>> mlist;
	private ClassroomConnectionAdapter madapter;
	private GridView mgridview;
	private AlertDialog alertDialog;
	private ImageButton img_close;
	private TextView txt_onlinenum,connection_classcode;
	private SharedPreferences share, teachershare;
	private Editor editor;
	private String code, ccId;
	private Timer timer;
	private ImageButton img_back;
	private Intent intent;
	private String activeClassId;
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
		setContentView(R.layout.activity_classroom_student);
		ExitApplication.getInstance().addActivity(this);
		initview();
		getstudentdata();
//		timer();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getstudentdata();
	}

	/**
	 * 方法说明：初始化控件
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
	
		ccId = intent.getStringExtra("ccId");
		context = this;
		connection_classcode = (TextView) findViewById(R.id.connection_classcode);
		img_back = (ImageButton) findViewById(R.id.img_end_studentcount_classroom);
		btn_answersheet = (RadioButton) findViewById(R.id.btn_answersheet_classroom_student);
		btn_end = (Button) findViewById(R.id.btn_end_studentcount_classroom);
		mgridview = (GridView) findViewById(R.id.gridView_studentcount_classroom);
		btn_group = (RadioButton) findViewById(R.id.btn_group_fragment_classroom);
		btn_more = (RadioButton) findViewById(R.id.btn_more_fragment_classroom);
		btn_vote = (RadioButton) findViewById(R.id.btn_vote_fragment_classroom);
		btn_responder = (RadioButton) findViewById(R.id.btn_responder_fragment_classroom);
		btn_group.setOnClickListener(this);
		btn_more.setOnClickListener(this);
		btn_responder.setOnClickListener(this);
		btn_vote.setOnClickListener(this);
		btn_answersheet.setOnClickListener(this);
		btn_end.setOnClickListener(this);
		img_back.setOnClickListener(this);
		txt_onlinenum = (TextView) this
				.findViewById(R.id.txt_studentcount_classroom);
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		teachershare = getSharedPreferences("teacherinfo", Context.MODE_PRIVATE);
		editor = share.edit();
		code = share.getString("passcode", "");
		activeClassId = share.getString("activeClassId", "");
		connection_classcode.setText(code);
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
		// 结束下课
		case R.id.btn_end_studentcount_classroom:
			ShowDiglog();

			break;
		// 进入答题卡页面
		case R.id.btn_answersheet_classroom_student:
			intent = new Intent(context, AnswerSheetActivity.class);
			System.out.println("mycode=============" + code);
			System.out.println("myccid=============" + ccId);
			intent.putExtra("code", code);
			intent.putExtra("ccId", ccId);
			startActivity(intent);
			// finish();
			break;
		// 点击关闭diaglog
		case R.id.waing_end_btn_close:
			alertDialog.dismiss();

			break;
		// 点击关闭diaglog 并结束课堂
		case R.id.waing_end_btn_sure:
			alertDialog.dismiss();
			FinishActiveClass();
			intent = new Intent(ClassRoomStudent.this, MainActivity.class);
			intent.putExtra("tag", 2);
			startActivity(intent);
			finish();
			break;
		// 返回按钮
		case R.id.img_end_studentcount_classroom:
			// intent = new Intent(context,
			// ClassRoomConnectionRoomActivity.class);
			// startActivity(intent);
			finish();
			break;
		// 投票功能
		case R.id.btn_vote_fragment_classroom:
//			 ShowDiglogDevelop();
			IntentUtlis.sysStartActivity(context, VoteActivity.class);
			break;
		case R.id.btn_responder_fragment_classroom:
			ShowDiglogDevelop();
			break;
		case R.id.btn_group_fragment_classroom:
			
//			 ShowDiglogDevelop();
			
			if (ButtonControl.isFastClick()) {
				return;
			} else {
				LodDialogClass.showCustomCircleProgressDialog(context, null, getString(R.string.common_Loading));
				getClassroomGroup(activeClassId);
			}
			
			break;
		case R.id.btn_more_fragment_classroom:
			ShowDiglogDevelop();
			break;
		// 点击 X 图片按钮
		case R.id.waing_develop_btn_close:
			alertDialog.dismiss();

		default:
			break;
		}
	}

	// 获取课堂分组信息 iphone、Android 教师App
	private void getClassroomGroup(String activeClassId) {
		// TODO Auto-generated method stub
		String path = Constants.URL_ActiveClass_loadActiveClassGroup;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("activeClassId", activeClassId);
		HttpUtils util = new HttpUtils();
		util.send(HttpMethod.POST, path, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				LodDialogClass.closeCustomCircleProgressDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub

				/*
				 * "Data": {   "size": 用于判断是否有分组，0没有,大于0有；只有大于0时，才会有下面信息
				 *   "groupMode": 分组规则,   "groupCnt": 分组数,   "groupInfos": [
				 * 多个分组LIST    [{     "GroupMethod": 分组规则,     "GroupCnt": 分组数,
				 *     "GroupNum": 组号     "sName": 学生姓名,     "IconUrl": 学生头像,
				 *     "GroupOrder": 组内顺序号    }]   ]  }
				 */
				String result = arg0.result;
				System.out.println("获取课堂分组信息--" + result);

				JSONObject obj;
				try {
					obj = new JSONObject(result);
					JSONObject data = obj.getJSONObject("Data");
					String size = data.getString("size");
					editor.putString("passcode", code);
					editor.putString("size", size);
					editor.commit();

					Intent mIntent = new Intent();
					if (size.equals("0")) {// 没有分组
						mIntent.setClass(context, GroupActivity.class);
						mIntent.putExtra("passcode", code);
					} else {
						String groupMode = data.getString("groupMode");
						String groupCnt = data.getString("groupCnt");
						JSONArray groupInfos = data.getJSONArray("groupInfos");
						mIntent.setClass(context, RandomActivity.class);
						mIntent.putExtra("groupCnt", groupCnt);
						mIntent.putExtra("groupMode", groupMode);
						mIntent.putExtra("JSONArray", groupInfos.toString());
						
					}
					startActivity(mIntent);
					LodDialogClass.closeCustomCircleProgressDialog();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 方法说明：弹出 开发中 Dialog
	 */
	private void ShowDiglogDevelop() {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View myLoginView = layoutInflater.inflate(
				R.layout.waring_classroom_development, null);
		alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setView(myLoginView, 0, 0, 0, 30);
		alertDialog.show();
		alertDialog.getWindow().setGravity(Gravity.CENTER);
		alertDialog.setCanceledOnTouchOutside(true);
		ImageButton btn_close = (ImageButton) myLoginView
				.findViewById(R.id.waing_develop_btn_close);
		btn_close.setOnClickListener(this);
	}

	/**
	 * 方法说明：弹出是否下课的Dialog
	 *
	 */
	private void ShowDiglog() {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View myLoginView = layoutInflater.inflate(
				R.layout.waring_end_classroom, null);
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setView(myLoginView, 0, 0, 0, 30);
		alertDialog.show();
		alertDialog.getWindow().setGravity(Gravity.CENTER);
		alertDialog.setCanceledOnTouchOutside(true);
		btn_close = (Button) myLoginView.findViewById(R.id.waing_end_btn_sure);
		img_close = (ImageButton) myLoginView
				.findViewById(R.id.waing_end_btn_close);
		btn_close.setOnClickListener(this);
		img_close.setOnClickListener(this);
	}

	/**
	 * 方法说明：网络获取全部学生的数据
	 *
	 */
	private void getstudentdata() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("token==========" + share.getString("Token", ""));
		System.out.println("code================="
				+ intent.getStringExtra("code"));
		utils.send(HttpMethod.GET,
				Constants.URL_TeacherOrStudentGetStudentOnLine + "?passCode="
						+ teachershare.getString("code", "") + "&roleId=2",
				params, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						try {
							JSONObject obj = new JSONObject(result);
							JSONObject obj1 = obj.getJSONObject("Data");
							txt_onlinenum.setText("学员列表("
									+ obj1.getString("studentOnLineNum") + "/"
									+ obj1.getString("classStudentTotalNum")
									+ ")");
							mlist = new ArrayList<HashMap<String, Object>>();
							JSONArray array = obj1.getJSONArray("studentList");
							for (int i = 0; i < array.length(); i++) {
								JSONObject data = array.getJSONObject(i);
								HashMap<String, Object> map = new HashMap<String, Object>();
								map.put("studentname",
										data.getString("studentname"));
								map.put("studentloginstatus",
										data.getString("studentloginstatus"));

								map.put("iconUrl", data.getString("iconUrl"));
								if (data.getString("studentloginstatus").equals("1")) {
									
									mlist.add(map);
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
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out
								.println("result===================" + result);
					}
				});

	}

	/**
	 * 方法说明：结束下课
	 *
	 */
	private void FinishActiveClass() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, Constants.URL_FinishActiveClass
				+ "?passCode=" + teachershare.getString("code", ""), params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;

						System.out
								.println("FinishActiveClassresult==================="
										+ result);
					}
				});

	}

	/**
	 * 方法说明：每隔三秒 刷新数据
	 *
	 */
	private void timer() {
		timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				getstudentdata();
			}
		};
		timer.schedule(task, 3000, 3000);
	}

	/**
	 * 销毁定时器的方式
	 */
	@Override
	protected void onStop() {
		if (timer != null) {
			timer.cancel();// 程序退出时cancel timer
		}
		timer = null;
		super.onStop();
	}

}
