package com.lels.activity.student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.constants.Constants;
import com.lelts.fragment.classroom.AddTestActivity;
import com.lelts.fragment.classroom.AnswerSheetActivity;
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

public class Add_StuStart_Activity extends Activity implements OnClickListener {
	private ImageButton mImageview;
	private Button mSureAdd_btn;
	private EditText mEditText;
	private String PATH = Constants.URL_ActiveClassExerciseByPaperNumber;
	private Context context;
	private AlertDialog alertDialog;
	private SharedPreferences share,shares;
	private TextView txt_num, txt_name, txt_count;
	private String paperNumber, ccId, resultinfo, P_ID;
	private JSONObject obj1;
	private Button btn_add;
	private ImageView img_close;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_stustart);
		getintent();
		initview();
	}
	private void getintent() {
		shares = getSharedPreferences("clcode", MODE_PRIVATE);
		ccId = shares.getString("nowLessonId", "");
		
	}
	/**
	 * 方法说明：弹出Dialog
	 * @throws JSONException 
	 *
	 */
	private void ShowDiglog() throws JSONException {
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View myLoginView = layoutInflater.inflate(R.layout.dialog_add_stu_task,
				null);
		alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setView(myLoginView, 0, 0, 0, 30);
		alertDialog.show();
		alertDialog.getWindow().setGravity(Gravity.CENTER);
		alertDialog.setCanceledOnTouchOutside(true);
		btn_add = (Button) myLoginView
				.findViewById(R.id.dialog_addtest_btn_sure);
		img_close = (ImageButton) myLoginView
				.findViewById(R.id.dialog_add_test_imgbtn);
		btn_add.setOnClickListener(this);
		img_close.setOnClickListener(this);
		txt_num = (TextView) myLoginView
				.findViewById(R.id.dialog_testnum_txt_addtest);
		txt_name = (TextView) myLoginView
				.findViewById(R.id.dialog_testname_waring_addtest);
		txt_count = (TextView) myLoginView
				.findViewById(R.id.dialog_testcount_waring_addtest);
		txt_num.setText(obj1.getString("PaperNumber"));
		txt_name.setText(obj1.getString("Name"));
		txt_count.setText(obj1.getString("QuestionCount"));
	}

	/**
	 * 初始化组件
	 */
	private void initview() {
		context = this;
		
		mImageview = (ImageButton) findViewById(R.id.back_stustart_add_img);
		mImageview.setOnClickListener(this);
		
		mSureAdd_btn = (Button) findViewById(R.id.btn_add_stustart_sure);
		mSureAdd_btn.setOnClickListener(this);
		
		mEditText = (EditText) findViewById(R.id.stu_ed_add_test);
//		mEditText.setText("V2_Paper");
		
		
		// 实例化share
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
	}
	/**
	 * 组件监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//返回按钮
		case R.id.back_stustart_add_img:
			finish();
			break;
			//dialog叉号图标
		case R.id.dialog_add_test_imgbtn:
			alertDialog.dismiss();
			break;
			//dialog添加按钮
		case R.id.dialog_addtest_btn_sure:
			savetest();
			alertDialog.dismiss();
		    finish();
			break;
			//添加按钮
		case R.id.btn_add_stustart_sure:
			paperNumber = mEditText.getText().toString();
			System.out.println("add_stustart>>>>>>>>>>>paperNumber=========" + paperNumber
					+ "========ccId" + ccId);
			if(paperNumber.equals("")||paperNumber ==""){
				Toast.makeText(context, "请输入试卷编号！", Toast.LENGTH_SHORT).show();
			}else{
				getaddtest();
			}
			break;
		}
	}
		private void savetest() {
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
		/**
		 * 方法说明：获取网络添加的数据显示在diaglog
		 *
		 */
		private void getaddtest() {
			// TODO Auto-generated method stub
			
			RequestParams params = new RequestParams();
			params.addHeader("Authentication", share.getString("Token", ""));
			HttpUtils utils = new HttpUtils();
			utils.configCurrentHttpCacheExpiry(0);
			utils.configDefaultHttpCacheExpiry(0);
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
									Toast.makeText(context, "试卷编号不存在！", Toast.LENGTH_SHORT)
											.show();
								} else {
									obj1 = obj.getJSONObject("Data");
									System.out.println("Name=========="+obj1.getString("Name"));
									
									P_ID = obj1.getString("P_ID");
									ShowDiglog();
								}
								
							} catch (JSONException e) {
								e.printStackTrace();
							}
						
						}
					});
		}
}
