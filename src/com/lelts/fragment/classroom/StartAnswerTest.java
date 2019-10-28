/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年6月30日 
 * 
 *******************************************************************************/
package com.lelts.fragment.classroom;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.bean.AnswerInfo;
import com.lels.bean.AnswerTestInfo;
import com.lels.bean.ExamAnswerListInfo;
import com.lels.bean.StartAnswertestInfo;
import com.lels.constants.Constants;
import com.lelts.activity.classroomconnection.adapter.StartAnswerTestAdapter;
import com.lelts.activity.classroomconnection.adapter.WholeTestReportAdapter;
import com.lelts.tool.IntentUtlis;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年6月30日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class StartAnswerTest extends Activity implements OnClickListener {
	private Chronometer chronometer;
	private Button btn_strat;
	private int start_down;
	private Context context;
	private AlertDialog alertDialog;
	private Button btn_close;
	private ImageView img_close, img_show;
	private ListView mlistview;
	private StartAnswerTestAdapter madapter;
	private WholeTestReportAdapter wholeadapter;
	private List<StartAnswertestInfo> mlist;
	private List<AnswerInfo> mSinglelist;
	private List<ExamAnswerListInfo> examAnswerList;
	private int miss, choose;
	private SharedPreferences share, teacherinfo;
	private String paperId;
	private int type;
	private Timer timer,whole_timer;
	private ImageButton img_back;
	private Intent intent;
	private TextView txt_papername;
	//老师设置的答题时间
	private String testtime;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_answertest);

		initview();

		
	}

	/**
	 * 方法说明：初始化控件
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = this;
		chronometer = (Chronometer) findViewById(R.id.chron_main);
		btn_strat = (Button) findViewById(R.id.btn_strat_answertest);
		btn_strat.setOnClickListener(this);
		mlistview = (ListView) findViewById(R.id.listview_start_answertest);
		//chronometer.setText("00:00:00");
		chronometer.setText("00:00");
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		teacherinfo = getSharedPreferences("teacherinfo", Context.MODE_PRIVATE);
		Intent getchoose = getIntent();
		choose = getchoose.getIntExtra("choose", -1);
		System.out.println("================choose===="+choose);
		img_back = (ImageButton) findViewById(R.id.img_back_start_answertest);
		img_back.setOnClickListener(this);
		txt_papername = (TextView) findViewById(R.id.txt_name_start_answertest);
		Intent intent = getIntent();
		if(intent == null){
			
		}else{
			txt_papername.setText(intent.getStringExtra("paper_name"));
			testtime = intent.getStringExtra("testtime");
		}
		

	}

	/*
	 * (non-Javadoc) .
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_strat_answertest:
			if (start_down == 0) {
				// 开始
				type = 1;
				startorstoptest(type);
				System.out.println("choose======>+testtime==="+choose+"====="+Integer.parseInt(testtime));
				
				if(choose == 1){
					//返回按钮消失，停止计时器

					img_back.setVisibility(View.INVISIBLE);
					timer();
					
				}else if(choose == 2 ){
					//返回按钮消失
					img_back.setVisibility(View.INVISIBLE);
					wholetimer();
				}
				

				chronometer.setBase(SystemClock.elapsedRealtime());
//				chronometer
//						.setOnChronometerTickListener(new OnChronometerTickListener() {
//							public void onChronometerTick(Chronometer cArg) {
//								miss++;
//								cArg.setText(FormatMiss(miss));
//
//							}
//						});
				chronometer.start();

			}
			start_down++;
			if (start_down == 1) {
				btn_strat.setText("停止");

			} else {
				if (btn_strat.getText().equals("停止")) {
					ShowDiglog();
				}

			}

			if (btn_strat.getText().equals("完成")) {
				
				if(choose==1){
					timer.cancel();
				}else if(choose==2){
					whole_timer.cancel();
				}
				//timer.cancel();// 程序退出时cancel timer
				Editor editor = teacherinfo.edit();
				editor.putString("paperId", paperId);
				editor.putInt("choose", choose);
				editor.commit();
				intent = new Intent(context, TestReportActivity.class);
				intent.putExtra("choose", choose);
				intent.putExtra("paperId", paperId);
				startActivity(intent);
				//销毁页面
				finish();
			}

			break;
		// 关闭diaglog 并 确定停止计时
		case R.id.waing_stop_btn_sure:
			// 开始
			type = 2;
			startorstoptest(type);
			btn_strat.setText("完成");
			chronometer.stop();// 停止计时器
			alertDialog.dismiss();
			break;
		// 关闭diaglog
		case R.id.waing_stop_btn_close:
			alertDialog.dismiss();

			break;
		//返回
		case R.id.img_back_start_answertest:
//			intent = new Intent(context, AnswerSheetActivity.class);
//			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

//	/**
//	 * 方法说明：计时器格式化时间
//	 *
//	 */
//	private static String FormatMiss(int miss) {
//		String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
//		String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0"
//				+ (miss % 3600) / 60;
//		String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0"
//				+ (miss % 3600) % 60;
//		return hh + ":" + mm + ":" + ss;
//	}

	/**
	 * 方法说明：弹出Dialog
	 *
	 */
	private void ShowDiglog() {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View myLoginView = layoutInflater.inflate(
				R.layout.waring_stop_testclasses, null);
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setView(myLoginView, 0, 0, 0, 30);
		alertDialog.show();
		alertDialog.getWindow().setGravity(Gravity.CENTER);
		alertDialog.setCanceledOnTouchOutside(true);
		btn_close = (Button) myLoginView.findViewById(R.id.waing_stop_btn_sure);
		img_close = (ImageButton) myLoginView
				.findViewById(R.id.waing_stop_btn_close);
		btn_close.setOnClickListener(this);
		img_close.setOnClickListener(this);
	}



	/**
	 * 方法说明：网络请求 开始发题或者结束试卷
	 *
	 */
	private void startorstoptest(int type) {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		paperId = intent.getStringExtra("paperId");
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("ccId========" + teacherinfo.getString("ccId", "")
				+ "==========paperId=====" + paperId
				+ "===========type=========" + type);
		utils.send(HttpMethod.GET, Constants.URL_ActiveClassExerciseStartOrStop
				+ "?ccId=" + teacherinfo.getString("ccId", "") + "&paperId="
				+ paperId + "&type=" + type, params,
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
								.println("startorstopresult====================="
										+ result);
					}
				});
	}

	/**
	 * 方法说明：网络获取整套提交的(考试当中和考试结束后显示的)学生成绩单
	 *
	 */
	private void findWholeSubmit() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("ccId========" + teacherinfo.getString("ccId", "")
				+ "==========paperId=====" + paperId
				+ "===========type=========" + type);
		utils.send(HttpMethod.GET,
				Constants.URL_findWholeSubmitModeStudentExamMark
				 + "?ccId=" + teacherinfo.getString("ccId",
				 "")+"&paperId="+paperId, params,
	//					+ "?ccId=29908&paperId=9751", params,
				new RequestCallBack<String>() {

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
							JSONArray data = obj.getJSONArray("Data");

							mlist = new ArrayList<StartAnswertestInfo>();
							for (int i = 0; i < data.length(); i++) {
								JSONObject obj_data = data.getJSONObject(i);
								StartAnswertestInfo info = new StartAnswertestInfo();
								info.setAnswer(obj_data.getString("Accuracy"));
								info.setCode(obj_data.getString("sCode"));
								info.setName(obj_data.getString("sName"));
								info.setTime(obj_data.getString("CostTime"));
								info.setUrl(obj_data.getString("IconUrl"));
								info.setSex(obj_data.getString("nGender"));
								//新加数据
								info.setsStudentId(obj_data.getString("sStudentId"));
								mlist.add(info);

							}
							madapter = new StartAnswerTestAdapter(context, mlist);
							mlistview.setAdapter(madapter);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out
								.println("findSingleSubmitModeStudentExamMark====================="
										+ result);
					}
				});


	}
	
	/**
	 * 方法说明：单题提交的(考试当中(心跳)和考试结束后显示的)学生成绩单
	 *
	 */
	private void findSingleSubmitModeStudentExamMark() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("ccId========" + teacherinfo.getString("ccId", "")
				+ "==========paperId=====" + paperId);
		utils.send(HttpMethod.GET,
				Constants.URL_findSingleSubmitModeStudentExamMark
				 + "?ccId=" + teacherinfo.getString("ccId",
				 "")+"&paperId="+paperId, params,
		//				+ "?ccId=29908&paperId=9751", params,
				new RequestCallBack<String>() {

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
							JSONArray data = obj.getJSONArray("Data");
							mSinglelist = new ArrayList<AnswerInfo>();
							for (int i = 0; i < data.length(); i++) {
								JSONObject obj_data = data.getJSONObject(i);
								String sName = obj_data.getString("sName");
								int nGender = obj_data.getInt("nGender"); 
								String CostTime = obj_data.getString("CostTime");
								String sCode = obj_data.getString("sCode");
								String IconUrl = obj_data.getString("IconUrl");
								JSONArray exam = obj_data.getJSONArray("examAnswerList");
								examAnswerList = new ArrayList<ExamAnswerListInfo>();
								for (int j = 0; j < exam.length(); j++) {
									JSONObject exam_data = exam.getJSONObject(j);
									String QNumber = exam_data.getString("QNumber");
									// "AnswerContent": "学生答案(当RightCount-ScoreCount<0，答案颜色是红色，否则是绿色)",
									String AnswerContent = exam_data.getString("AnswerContent");
									int RightCount = exam_data.getInt("RightCount");
									int ScoreCount = exam_data.getInt("ScoreCount");
									ExamAnswerListInfo exam_info = new ExamAnswerListInfo(QNumber, AnswerContent, RightCount, ScoreCount);
									examAnswerList.add(exam_info);
								}
								AnswerInfo info = new AnswerInfo(sName,nGender,IconUrl,CostTime,sCode,examAnswerList);
								mSinglelist.add(info);
								
							}
							System.out.println("mSinglelist====="+mSinglelist.toString());
							wholeadapter = new WholeTestReportAdapter(context, mSinglelist);
							mlistview.setAdapter(wholeadapter);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out
								.println("findWholeSubmit====================="
										+ result);
					}
				});
					
		
	}

	/**
	 * 方法说明：每隔三秒 刷新数据整套提交 
	 *
	 */
	private void timer() {
		timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				findWholeSubmit();
	
			}
		};
		timer.schedule(task, 3000, 3000);
	}
	
	/**
	 * 方法说明：每隔三秒 刷新数据单题提交
	 *
	 */
	private void wholetimer() {
		whole_timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				findSingleSubmitModeStudentExamMark();
			}
		};
		whole_timer.schedule(task, 3000, 3000);
	}

	/**
	 * 销毁定时器的方式
	 */
	@Override
	protected void onStop() {
		
	
//	
//		timer.cancel();
//		whole_timer.cancel();
		super.onStop();
	}


}
