package com.lels.activity.myself;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.R;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyselfActivity extends Activity implements OnClickListener {

	private static final String TAG = "MyselfActivity";

	// 上部布局声明
	private ImageButton imageview_back;
	private ImageView imageview_myself_pic;
	private TextView textview_myself_username;
	private TextView textview_myself_myschool;

	// 下面布局声明
	// private RelativeLayout relative_myself_message;
	private RelativeLayout radiobutton_myself_message;
	private RadioButton radiobutton_myself_mycollect;
	private RadioButton radiobutton_myself_remind;
	private RadioButton radiobutton_myself_sysset;
	private RadioButton radiobutton_myself_result;
	private RadioButton radiobutton_myself_contactus;
	private RadioButton radiobutton_myself_usehelp;
	private RadioButton radiobutton_myself_more;

	private ImageView imageview_message_dot;
	// 主布局
	private LinearLayout main_linearlayout;

	private ScrollView myscrollview;
	private String token;
	// private String nickname;
	// private String iconurl;

	// Email: teachervps02@163.com,
	// UID: 12,
	// NickName: 测试教师VPS02_,
	// Password: ,
	// Account: xdf0050009175,
	// RoleID: 2,
	// IconUrl:
	// teacherCode

	// 班级的列表
	private String str_studentresult = "";

	private String teacherId;
	private String sName;
	// private String UID;
	private String IconUrl;
	private String schoolName;

	private HashMap<String, Object> map_users = new HashMap<String, Object>();

	private LinearLayout linear_alter_personal_text; // new relative

	private TextView textview_totle_class;
	private TextView textview_preweek_class;
	private TextView textview_unfinish_class;

	private TextView textview_alter_sign; // alter button
	// 显示的个性签名
	private TextView textview_myself_introduce;// sign

	private EditText edittext_myself_introduce; // new sign
	// 匹配非表情符号的正则表达式
	private final String reg = "^([a-z]|[A-Z]|[0-9]|[\u2E80-\u9FFF]){3,}|@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?|[wap.]{4}|[www.]{4}|[blog.]{5}|[bbs.]{4}|[.com]{4}|[.cn]{3}|[.net]{4}|[.org]{4}|[http://]{7}|[ftp://]{6}$";
	private Pattern pattern = Pattern.compile(reg);
	// 个性签名【中文、字母、数字、符号 1-50】
	private String sign = "^[a-zA-Z0-9\u4e00-\u9fa5\u3000-\u301e\ufe10-\ufe19\ufe30-\ufe44\ufe50-\ufe6b\uff01-\uffee]{1,50}$";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself);

		getdatafromshare();

		init();

		getdatafromnet();

		// 开始获取未读消息
		getdataformessage();
		edhasFocus();
	}

	private void init() {
		main_linearlayout = (LinearLayout) findViewById(R.id.main_linearlayout);
		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		imageview_myself_pic = (ImageView) findViewById(R.id.imageview_myself_pic);
		textview_myself_username = (TextView) findViewById(R.id.textview_myself_username);

		textview_myself_introduce = (TextView) findViewById(R.id.textview_myself_introduce);

		// relative_myself_message = (RelativeLayout)
		// findViewById(R.id.relative_myself_message);

		radiobutton_myself_message = (RelativeLayout) findViewById(R.id.radiobutton_myself_message);

		radiobutton_myself_mycollect = (RadioButton) findViewById(R.id.radiobutton_myself_mycollect);
		radiobutton_myself_remind = (RadioButton) findViewById(R.id.radiobutton_myself_remind);
		radiobutton_myself_sysset = (RadioButton) findViewById(R.id.radiobutton_myself_sysset);
		radiobutton_myself_result = (RadioButton) findViewById(R.id.radiobutton_myself_result);
		radiobutton_myself_contactus = (RadioButton) findViewById(R.id.radiobutton_myself_contactus);
		// radiobutton_myself_usehelp = (RadioButton)
		// findViewById(R.id.radiobutton_myself_usehelp);
		// radiobutton_myself_more = (RadioButton)
		// findViewById(R.id.radiobutton_myself_more);

		textview_totle_class = (TextView) findViewById(R.id.textview_totle_class);
		textview_preweek_class = (TextView) findViewById(R.id.textview_preweek_class);
		textview_unfinish_class = (TextView) findViewById(R.id.textview_unfinish_class);

		textview_myself_myschool = (TextView) findViewById(R.id.textview_myself_myschool);

		// 小点点
		imageview_message_dot = (ImageView) findViewById(R.id.imageview_message_dot);

		// 修改 个性签名
		linear_alter_personal_text = (LinearLayout) findViewById(R.id.linear_alter_personal_text);

		edittext_myself_introduce = (EditText) findViewById(R.id.edittext_myself_introduce);

		imageview_back.setOnClickListener(this);
		// relative_myself_message.setOnClickListener(this);
		radiobutton_myself_message.setOnClickListener(this);
		radiobutton_myself_mycollect.setOnClickListener(this);
		radiobutton_myself_remind.setOnClickListener(this);
		radiobutton_myself_sysset.setOnClickListener(this);
		radiobutton_myself_result.setOnClickListener(this);
		radiobutton_myself_contactus.setOnClickListener(this);
		// radiobutton_myself_usehelp.setOnClickListener(this);
		// radiobutton_myself_more.setOnClickListener(this);

		// 个性签名的设计
		textview_myself_introduce.setOnClickListener(this);

		myscrollview = (ScrollView) findViewById(R.id.myscrollviwe);
		LodDialogClass.showCustomCircleProgressDialog(MyselfActivity.this,
				null, getString(R.string.common_Loading));
	}

	private void getdatafromnet() {

		String url = new Constants().URL_MYSELF_HEADPAGE;

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
							System.out.println("解析获取个人中心的首页数据"
									+ responseInfo.result);
							JSONObject str = new JSONObject(responseInfo.result);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");

							// Infomation = 提示信息;
							//  Result = 返回结果true/false;
							//  Data = ({
							//   UID = 教师用户ID;
							//   sName = 教师名称;
							//   sCode = 教师编号;
							//   list = 消息集合({
							//    sCode =班级编号;
							//    sName =班级名称;
							//   其他信息待确认
							// =====================================
							// "sCode": "TCGZ0010092",
							// "schoolName": "乌鲁木齐新东方学校",
							// "Email": "teachervps02@163.com",
							// "TotalLesson": null,
							// "IconUrl": null,
							// "LastWeekLesson": null,
							// "sMajor": null,
							// "teacherName": "伊力亚尔海米提",
							// "FutureLesson": null,
							// "Signature": "1112左城西枯一要2223"
							//
							// "list":
							str_studentresult = Data;

							JSONObject obj = new JSONObject(Data);

							// map_users.put("sCode", obj.getString("sCode"));
							map_users.put("schoolName",
									obj.getString("schoolName"));
							map_users.put("Email", obj.getString("Email"));
							map_users.put("TotalLesson",
									obj.getString("TotalLesson"));
							map_users.put("IconUrl", obj.getString("IconUrl"));
							map_users.put("teacherId",
									obj.getString("teacherId"));
							map_users.put("LastWeekLesson",
									obj.getString("LastWeekLesson"));
							// map_users.put("sMajor", obj.getString("sMajor"));
							map_users.put("teacherName",
									obj.getString("teacherName"));
							map_users.put("FutureLesson",
									obj.getString("FutureLesson"));
							map_users.put("Signature",
									obj.getString("Signature"));

							// UID = obj.getString("UID");
							// sCode = obj.getString("sCode");
							teacherId = obj.getString("teacherId");
							sName = obj.getString("teacherName");
							IconUrl = obj.getString("IconUrl");
							schoolName = obj.getString("schoolName");

							setview();

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("error" + error.toString());
						LodDialogClass.closeCustomCircleProgressDialog();
					}
				});
	}

	private void getdatafromshare() {

		SharedPreferences share = MyselfActivity.this.getSharedPreferences(
				"userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");
		sName = share.getString("NickName", "");
		IconUrl = share.getString("IconUrl", "");
		Log.d("StudyPlanActivity", "获取的token数值为=====" + token);
	}

	protected void setview() {
		textview_myself_username.setText(sName);

		if (map_users.get("TotalLesson").toString().equalsIgnoreCase("null")) {
			textview_totle_class.setText("0");
		} else {
			textview_totle_class.setText(map_users.get("TotalLesson")
					.toString());
		}

		if (map_users.get("LastWeekLesson").toString().equalsIgnoreCase("null")) {
			textview_preweek_class.setText("0");
		} else {
			textview_preweek_class.setText(map_users.get("LastWeekLesson")
					.toString());
		}

		if (map_users.get("FutureLesson").toString().equalsIgnoreCase("null")) {
			textview_unfinish_class.setText("0");
		} else {
			textview_unfinish_class.setText(map_users.get("FutureLesson")
					.toString());
		}

		// textview_totle_class.setText(map_users.get("TotalLesson").toString());
		// textview_preweek_class.setText(map_users.get("LastWeekLesson").toString());
		// textview_unfinish_class.setText(map_users.get("FutureLesson").toString());
		if (map_users.get("Signature").toString().equals("null")
				|| map_users.get("Signature").toString().equals("")) {
			textview_myself_introduce.setText("这家伙很懒，还没有个性签名");
		} else {
			textview_myself_introduce.setText(map_users.get("Signature")
					.toString());
		}
		// 把txt赋给edtext
		System.out.println("txt 的文字===" + textview_myself_introduce.getText());
		edittext_myself_introduce.setText(textview_myself_introduce.getText());
		textview_myself_myschool
				.setText(map_users.get("schoolName").toString());
		// IconUrl = "2222";
		if (!IconUrl.equalsIgnoreCase("null")) {
			System.out.println("有图片");
			IconUrl = new Constants().URL_TeacherIMG + IconUrl;
			ImageLoader.getInstance().displayImage(IconUrl,
					imageview_myself_pic);
		}
		LodDialogClass.closeCustomCircleProgressDialog();
		// 开始获取未读消息
		// getdataformessage();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		// case R.id.relative_myself_message://我的消息
		case R.id.radiobutton_myself_message:
			Intent intent = new Intent();
			// intent.setClass(MyselfActivity.this,
			// MyselfMessageActivity.class);
			intent.setClass(MyselfActivity.this,
					MyselfMessageTestActivity.class);
			startActivity(intent);
			break;

		case R.id.radiobutton_myself_mycollect:// 我的收藏
			intent = new Intent();
			intent.setClass(MyselfActivity.this, MyselfCollectActivity.class);
			startActivity(intent);
			break;

		case R.id.radiobutton_myself_remind:// 我的提醒
			intent = new Intent();
			intent.setClass(MyselfActivity.this, MyselfRemindActivity.class);
			startActivity(intent);
			break;

		case R.id.radiobutton_myself_sysset:// 系统设置
			intent = new Intent();
			intent.setClass(MyselfActivity.this, MyselfSyssetActivity.class);
			startActivity(intent);
			break;
		case R.id.radiobutton_myself_result:// 我的成绩
			intent = new Intent();
			intent.setClass(MyselfActivity.this, MyselfResultActivity.class);
			Bundle b = new Bundle();
			b.putString("studentresultinfo", str_studentresult);
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.radiobutton_myself_contactus:// 联系我们
			intent = new Intent();
			intent.setClass(MyselfActivity.this, MyselfContactusActivity.class);
			startActivity(intent);
			break;
		// case R.id.radiobutton_myself_usehelp://帮助我们
		// intent = new Intent();
		// intent.setClass(MyselfActivity.this, MyselfUseHelpActivity.class);
		// startActivity(intent);
		// break;
		// case R.id.radiobutton_myself_more://更多
		// intent = new Intent();
		// intent.setClass(MyselfActivity.this, MyselfMoreActivity.class);
		// startActivity(intent);
		// break;
		case R.id.imageview_back:
			finish();
			break;

		case R.id.textview_myself_introduce:
			linear_alter_personal_text.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		getdataformessage();
	}

	/**
	 * my messsage setting
	 * */
	private void getdataformessage() {

		@SuppressWarnings("static-access")
		String url = new Constants().URL_MYSELF_MESSAGE_REMIND;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		HttpUtils http = new HttpUtils();
		// http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.GET, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							Log.d(TAG, "获取未读消息数量" + responseInfo.result);
							JSONObject str = new JSONObject(responseInfo.result);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");

							JSONObject obj = new JSONObject(Data);
							String noReadCount = obj.getString("noReadCount");

							if (!noReadCount.equalsIgnoreCase("0")) {
								imageview_message_dot
										.setVisibility(View.VISIBLE);
							} else {
								imageview_message_dot
										.setVisibility(View.INVISIBLE);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure");
						System.out.println(error.toString());

					}

				});

	}

	/**
	 * my messsage setting 修改个性签名
	 * 
	 * */
	private void alterdataforteachersign() {

		final String newSignature = edittext_myself_introduce.getText()
				.toString();

		@SuppressWarnings("static-access")
		String url = new Constants().URL_MYSELF_SIGNATURECHANGE;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		params.addBodyParameter("newSignature", newSignature);

		HttpUtils http = new HttpUtils();
		// http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							Log.d(TAG, "修改个性签名返回的数据" + responseInfo.result);
							JSONObject str = new JSONObject(responseInfo.result);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");

							if (Result.equalsIgnoreCase("true")) {
								Toast.makeText(MyselfActivity.this, "修改成功",
										Toast.LENGTH_SHORT).show();
								// textview_alter_sign.setVisibility(View.INVISIBLE);
								linear_alter_personal_text
										.setVisibility(View.INVISIBLE);
								textview_myself_introduce.setText(newSignature);
							} else {
								Toast.makeText(MyselfActivity.this,
										"个性签名不能为空！", Toast.LENGTH_SHORT).show();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure");
						System.out.println(error.toString());

					}

				});

	}

	/**
	 * 方法说明：判断ed 是否有交点
	 *
	 */
	private void edhasFocus() {
		// TODO Auto-generated method stub
		// 监听 EdText 是否有焦点
		edittext_myself_introduce
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							// 获得焦点
							System.out.println("获得焦点");
						} else {
							// 失去焦点
							CharSequence input = edittext_myself_introduce
									.getText().toString();
							if (input.length() == 0) {
								Toast.makeText(MyselfActivity.this, "个性签名不能为空",
										Toast.LENGTH_SHORT).show();
							} else {
								Pattern pattern = Pattern.compile(sign);
								boolean tf = pattern.matcher(input).matches();
								if (tf == true) {
									alterdataforteachersign();
								} else {
									Toast.makeText(MyselfActivity.this,
											"个性签名格式不正确，请重新输入！",
											Toast.LENGTH_SHORT).show();
								}

							}
							System.out.println("失去焦点");
						}
					}
				});

		main_linearlayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				main_linearlayout.setFocusable(true);
				main_linearlayout.setFocusableInTouchMode(true);
				main_linearlayout.requestFocus();
				// 软键盘消失
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(main_linearlayout.getWindowToken(),
						0);
				return false;
			}
		});
		myscrollview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				main_linearlayout.setFocusable(true);
				main_linearlayout.setFocusableInTouchMode(true);
				main_linearlayout.requestFocus();
				// 软键盘消失
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(main_linearlayout.getWindowToken(),
						0);
				return false;
			}
		});
	}

}
