/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年6月27日 
 * 
 *******************************************************************************/
package com.lelts.fragment.classroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.bean.AnswerTestInfo;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lelts.activity.classroomconnection.adapter.AnswerTestAdapter;
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
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年6月27日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：	
 *    修改内容：
 * </pre>
 */
@SuppressLint("NewApi")
public class AnswerTestActivity extends Activity implements OnClickListener {
	private List<AnswerTestInfo> mlist;
	private Context context;
	private AnswerTestAdapter madapter;
	private ListView mlistview;
	private Button btn_all, btn_single, btn_start;
	private TextView txt_model, txt_time;
	private EditText ed_model,ed_percent;
	private int choose = 1;
	private String code, P_ID, ActiveClassPaperInfoId, ed_count;
	private SharedPreferences share;
	private List<Integer> listItemID = new ArrayList<Integer>();
	private StringBuilder sb;
	private String newsb;
	private ImageButton img_back;
	private Intent intent;
	private int ed_width;
	private String ed_str;
	private String str = "";
	private String paper_name;
	private TextView txt_papername; //试卷名称
	//判断试卷是否打包成功
	private String PaperState;
	//百分比
	private String percent_txt;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answer_test);
		ExitApplication.getInstance().addActivity(this);
		initview();
		measuer();
		gettestdetail();
	}

	/**
	 * 方法说明：初始化控件
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub

		context = this;
		mlistview = (ListView) findViewById(R.id.listview_answer_test);
		btn_all = (Button) findViewById(R.id.btn_chooseall_answer_test);
		btn_single = (Button) findViewById(R.id.btn_choosesingle_answer_test);
		btn_start = (Button) findViewById(R.id.btn_sure_answer_test);
		img_back = (ImageButton) findViewById(R.id.img_back_answer_test);
		btn_all.setOnClickListener(this);
		btn_single.setOnClickListener(this);
		btn_start.setOnClickListener(this);
		img_back.setOnClickListener(this);
		txt_model = (TextView) findViewById(R.id.txt_answer_model);
		txt_time = (TextView) findViewById(R.id.txt_answer_time);
		ed_model = (EditText) findViewById(R.id.ed_content_answer_test);
		ed_percent = (EditText) findViewById(R.id.ed_content_answer_test_percent);
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		sb = new StringBuilder();
		txt_papername = (TextView) findViewById(R.id.txt_name_answer_test);
		Intent intent = getIntent();
		if(intent == null){
			
		}else{
			paper_name = intent.getStringExtra("Name");
			txt_papername.setText(intent.getStringExtra("Name"));
			PaperState = intent.getStringExtra("PaperState");
		}
		LodDialogClass.showCustomCircleProgressDialog(context, null, getString(R.string.common_Loading));
	}

	/**
	 * 方法说明：测量控件
	 *
	 */
	private void measuer() {
		// TODO Auto-generated method stub
		int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        ed_model.measure(w, h);
        ed_width = ed_model.getMeasuredWidth();
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
		// 全选
		case R.id.btn_chooseall_answer_test:
			choose = 1;
			//隐藏百分
			ed_percent.setVisibility(View.INVISIBLE);
			//展示分钟
			ed_model.setVisibility(View.VISIBLE);
			madapter.notifyDataSetChanged();
			txt_model.setText("答题时间");
			txt_time.setText("分钟");
			ed_model.setWidth(ed_width);
			btn_all.setTextColor(Color.WHITE);
			btn_single.setTextColor(Color.RED);
			btn_all.setBackground(getResources().getDrawable(R.drawable.anniuxuanzhong));
			btn_single.setBackground(getResources().getDrawable(R.drawable.anniu_select));
			break;
		// 单选
		case R.id.btn_choosesingle_answer_test:
			//展示百分
			ed_percent.setVisibility(View.VISIBLE);
			//隐藏分钟
			ed_model.setVisibility(View.INVISIBLE);
			choose = 2;
			madapter.notifyDataSetChanged();
			txt_time.setText("");
			txt_model.setText("完 成 率");
			ed_model.setWidth(ed_width);
			ed_percent.setText("%");
			//输入框监听
			ed_percent.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					String str=s.toString();
					str = str.replace("%", "");
					if(!str.equals("")){
						int i=Integer.parseInt(str);
						if(i>0&&i<=100){
						
					}else{
						//设置 edtext的文本
						ed_percent.setText(str.substring(0, 2)+"%");
						//设置edtext的光标
						ed_percent.setSelection(ed_percent.getText().length()-1);
						//Toast.makeText(MainActivity.this, "請輸入1-100的數字", Toast.LENGTH_SHORT).show();
					}
				}
					}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
			btn_single.setTextColor(Color.WHITE);
			btn_all.setTextColor(Color.RED);
			btn_single.setBackground(getResources().getDrawable(R.drawable.anniuxuanzhong));
			btn_all.setBackground(getResources().getDrawable(R.drawable.anniu_select));
			break;
		// 开始答题
		case R.id.btn_sure_answer_test:
			// listItemID.clear();
//			 PaperState = 试卷状态，0 试卷初始创建、1 试卷未发布、2 试卷发布成功等待打包、3 试卷打包完成,
			if (PaperState.endsWith("3")) {
				System.out.println("试卷打包完成=====");
				for (int i = 0; i < mlist.size(); i++) {
					AnswerTestInfo info = mlist.get(i);
					if (info.isIschexck()) {
						listItemID.add(i);
					}
				}

				if (listItemID.size() == 0) {

					Toast.makeText(AnswerTestActivity.this, "请选择试题！",
							Toast.LENGTH_SHORT).show();
				} else {
					percent_txt = ed_percent.getText().toString();
					percent_txt = percent_txt.replace("%", "");
					str = ed_model.getText().toString();
					str = str.replace("%", "");
					if (choose ==1) {
						str = ed_model.getText().toString();
						str = str.replace("%", "");
					}else if (choose == 2) {
						str = ed_percent.getText().toString();
						str = str.replace("%", "");
					}
					if(str.equals("")||str.length()<=0){
						if(choose == 1){
								if (str.equals("")||str.length()<=0) {
									Toast.makeText(AnswerTestActivity.this, "请输入答题时间！",
											Toast.LENGTH_SHORT).show();
								}
							
						} else if(choose == 2){
						if (str.equals("")||str.length()<=0) {
							Toast.makeText(AnswerTestActivity.this, "请输入答题百分率！",
									Toast.LENGTH_SHORT).show();
						}
							
						}
			
					}else{
						System.out.println("=======跳转");
					for (int i = 0; i < listItemID.size(); i++) {

						sb.append(mlist.get(listItemID.get(i)).getQ_ID() + ","
								+ mlist.get(listItemID.get(i)).getSectionID() 
//								//多了PID
//							","+	mlist.get(listItemID.get(i)).getPID()
								+";");

					}
					newsb = sb.deleteCharAt(sb.length() - 1).toString();
					savechoosetest();
					intent = new Intent(context, StartAnswerTest.class);
					intent.putExtra("paperId", P_ID);
					intent.putExtra("choose", choose);
					intent.putExtra("paper_name", paper_name);
					intent.putExtra("testtime", str.toString());
					startActivity(intent);
					//页面销毁
					finish();
					}
				}
				
				
			}else if (PaperState.endsWith("0")){
				Toast.makeText(context, "试卷初始创建", Toast.LENGTH_SHORT).show();
			}else if(PaperState.endsWith("1")){
				Toast.makeText(context, "试卷未发布", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(context, "试卷发布成功等待打包", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.img_back_answer_test:
//			intent = new Intent(context, AnswerSheetActivity.class);
//			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
		// choose_method();
	}


	/**
	 * 方法说明：网络获取试卷的具体题目信息
	 *
	 */
	private void gettestdetail() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		code = intent.getStringExtra("code");
		P_ID = intent.getStringExtra("pId");
		ActiveClassPaperInfoId = intent
				.getStringExtra("ActiveClassPaperInfoId");
		System.out.println("=========code=====" + code + "======pId====="
				+ P_ID);
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		// + "?passCode=" + code+"&ccId="+ccId,
		utils.send(HttpMethod.GET, Constants.URL_ActiveClassExerciseDetail
				+ "?passCode=" + code + "&pId=" + P_ID, params,
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
						System.out.println("detailresult====================="
								+ result);
						try {
							JSONObject obj = new JSONObject(result);
							JSONArray array = obj.getJSONArray("Data");
							mlist = new ArrayList<AnswerTestInfo>();
							for (int i = 0; i < array.length(); i++) {
								JSONObject data = array.getJSONObject(i);
								AnswerTestInfo info = new AnswerTestInfo();
								info.setTestname(data.getString("QName"));
								info.setTestnum(data.getString("QNumber"));
								info.setQ_ID(data.getString("Q_ID"));
								info.setSectionID(data.getString("SectionID"));
								//info.setPID(data.getString("PID"));
								mlist.add(info);
							}
							madapter = new AnswerTestAdapter(context, mlist);

							mlistview.setAdapter(madapter);
							// madapter.notifyDataSetChanged();
							LodDialogClass.closeCustomCircleProgressDialog();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
	}

	/**
	 * 方法说明：保存选择的试卷。
	 *
	 */
	private void savechoosetest() {
		// TODO Auto-generated method stub
		ed_count = str.toString();
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("activeClassPaperInfoId========"
				+ ActiveClassPaperInfoId + "==========paperId=====" + P_ID
				+ "===========pid,qid,secctionid=========" + newsb
				+ "choose===================" + choose + "ed_count==========="
				+ ed_count);
		utils.send(HttpMethod.GET,
				Constants.URL_ActiveClassExerciseChooseQuestions
						+ "?activeClassPaperInfoId=" + ActiveClassPaperInfoId
						+ "&paperId=" + P_ID + "&qIds=" + newsb
						+ "&paperSubmitMode=" + choose
						+ "&paperSubmitCountdown=" + ed_count, params,
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
								.println("choosetestresult====================="
										+ result);
					}
				});
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		sb.delete(0, sb.length());
		System.out.println("清空stringbuffer===" + sb);
		super.onDestroy();
	}

}
