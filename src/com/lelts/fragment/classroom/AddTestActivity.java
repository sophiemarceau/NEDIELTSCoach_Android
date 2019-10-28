/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年7月1日 
 * 
 *******************************************************************************/
package com.lelts.fragment.classroom;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.bean.ExitApplication;
import com.lels.constants.Constants;
import com.lelts.activity.classroomconnection.adapter.ClassroomConnectionAdapter;
import com.lelts.tool.IntentUtlis;
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
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年7月1日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class AddTestActivity extends Activity implements OnClickListener {
	private Context context;
	private Button btn_sure, btn_add;
	private AlertDialog alertDialog;
	private ImageView img_close;
	private ImageButton img_back;
	private TextView txt_num, txt_name, txt_count;
	private SharedPreferences share, teachershare;
	private EditText ed_number;
	private String paperNumber, ccId, resultinfo, P_ID;
	private JSONObject obj1;
	private Intent intent;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_test);
		ExitApplication.getInstance().addActivity(this);
		initview();
	}
	

	/**
	 * 方法说明：初始化控件
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = this;
		btn_sure = (Button) findViewById(R.id.btn_sure_add_test);
		btn_sure.setOnClickListener(this);
		ed_number = (EditText) findViewById(R.id.ed_add_test);
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		teachershare = getSharedPreferences("teacherinfo", Context.MODE_PRIVATE);
		img_back =(ImageButton) findViewById(R.id.img_back_add_test);
		img_back.setOnClickListener(this);
	}

	/**
	 * 方法说明：弹出Dialog
	 * @throws JSONException 
	 *
	 */
	private void ShowDiglog() throws JSONException {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View myLoginView = layoutInflater.inflate(R.layout.waring_add_test,
				null);
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setView(myLoginView, 0, 0, 0, 30);
		alertDialog.show();
		alertDialog.getWindow().setGravity(Gravity.CENTER);
		alertDialog.setCanceledOnTouchOutside(true);
		btn_add = (Button) myLoginView
				.findViewById(R.id.waing_addtest_btn_sure);
		img_close = (ImageButton) myLoginView
				.findViewById(R.id.waing_addtest_btn_close);
		btn_add.setOnClickListener(this);
		img_close.setOnClickListener(this);
		txt_num = (TextView) myLoginView
				.findViewById(R.id.txt_testnum_waring_addtest);
		txt_name = (TextView) myLoginView
				.findViewById(R.id.txt_testname_waring_addtest);
		txt_count = (TextView) myLoginView
				.findViewById(R.id.txt_testcount_waring_addtest);
		txt_num.setText(obj1.getString("PaperNumber"));
		txt_name.setText(obj1.getString("Name"));
		txt_count.setText(obj1.getString("QuestionCount"));
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
		// 点击确定 ,并网络请求添加的数据 显示在diaglog
		case R.id.btn_sure_add_test:
			paperNumber = ed_number.getText().toString();
			ccId = teachershare.getString("ccId", null);
			System.out.println("paperNumber=========" + paperNumber
					+ "========ccId" + ccId);
			if(paperNumber.equals("")||paperNumber ==""){
				Toast.makeText(AddTestActivity.this, "请输入试卷编号！", Toast.LENGTH_SHORT).show();
			}else{
				getaddtest();
			}

			

			break;
		// 点击确定添加
		case R.id.waing_addtest_btn_sure:
			savetest();
			alertDialog.dismiss();
//			intent = new Intent(context, AnswerSheetActivity.class);
//			startActivity(intent);
			finish();
			break;
		// diaglog消失
		case R.id.waing_addtest_btn_close:
			alertDialog.dismiss();
			break;
		//返回按钮	
		case R.id.img_back_add_test:
//			intent = new Intent(context, AnswerSheetActivity.class);
//			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 方法说明：获取网络添加的数据显示在diaglog
	 *
	 */
	private void getaddtest() {
		// TODO Auto-generated method stub
		
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET,
				Constants.URL_ActiveClassExerciseByPaperNumber
						+ "?paperNumber=" + paperNumber + "&ccId=" + ccId,
				params, new RequestCallBack<String>() {
				

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						System.out.println("addtestresult==================="
								+ result);
						try {
							JSONObject obj = new JSONObject(result);
							resultinfo = obj.getString("Result");
							if (resultinfo.equals("false")) {
								Toast.makeText(AddTestActivity.this, "试卷编号不存在！", Toast.LENGTH_SHORT)
										.show();
							} else {
								obj1 = obj.getJSONObject("Data");
								System.out.println("Name=========="+obj1.getString("Name"));
								
								P_ID = obj1.getString("P_ID");
								ShowDiglog();
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					}
				});
	}
	
	
	/**
	 * 方法说明：保存试卷练习题 传给服务器
	 *
	 */
	private void savetest() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		System.out.println("?paperId=" + P_ID + "&ccId=" + ccId);
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET,
				Constants.URL_ActiveClassExerciseByPaperNumberSave
						+ "?paperId=" + P_ID + "&ccId=" + ccId,
				params, new RequestCallBack<String>() {
				
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						System.out.println("savetest==================="+ result);
						
					}
				});
	}
	
/* (non-Javadoc)
 * @see android.app.Activity#onDestroy()
 */
@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
}

}
