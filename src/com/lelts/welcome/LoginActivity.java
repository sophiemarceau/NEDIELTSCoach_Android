package com.lelts.welcome;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.hello.R;
import com.lels.bean.BooleanWife;
import com.lels.bean.ButtonControl;
import com.lels.bean.ExitApplication;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lels.customcontrols.EditDelectText;
import com.lelts.activity.main.MainActivity;
import com.lelts.tool.CodeUtil;
import com.lelts.tool.ImageLoder;
import com.lelts.tool.PrintTool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class LoginActivity extends Activity implements OnClickListener {
	SharedPreferences share;
	private EditDelectText editText_username;
	private EditDelectText editText_password;

	private String username, userpass;

	private Button button_login;
	private PrintTool ptool;
	private SharedPreferences stushare;
	private Editor editor;
	private  boolean isFirst;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
		ExitApplication.getInstance().addActivity(this);
		checkAutoLogin();
	}

	private void init() {
		ptool = new PrintTool(LoginActivity.this);

		editText_username = (EditDelectText) findViewById(R.id.editText_username);
		editText_password = (EditDelectText) findViewById(R.id.editText_password);
		button_login = (Button) findViewById(R.id.button_login);
		button_login.setOnClickListener(this);

		stushare = getSharedPreferences("userinfo", MODE_PRIVATE);
		editor = stushare.edit();
	}
	
	private void checkAutoLogin() {
		System.out.println("登录local check: has user : " + stushare.contains("username") + ", has pass : " + stushare.contains("userPass"));
		if (stushare.contains("username")) {
			String user = stushare.getString("username", "");
			System.out.println("登录local: username : " + user);
			editText_username.setText(user);
			if (stushare.contains("userPass")) {
				String pass = stushare.getString("userPass", null);
				System.out.println("登录local: pass : " + pass);
				if (!"".equals(pass)) {
				
					LodDialogClass.showCustomCircleProgressDialog(
							LoginActivity.this, null, "登录中...");
					System.out.println("登录自动 : user : " + user + ", pass : " + pass);
					//解密；
					editText_password.setText( CodeUtil.Decode(pass));
					login(user, pass);
				}
			} else {
				editText_password.getEditableText().clear();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_login:

			username = editText_username.getText().toString();
			userpass = editText_password.getText().toString();
			String encodePass = CodeUtil.Encode(userpass);
			System.out.println("登录加密: pass : " + userpass + ", encode : " + encodePass);
			if (BooleanWife.isNetworkAvailable(LoginActivity.this)) {
				if (ButtonControl.isFastClick()) {
					return;
				} else {
					LodDialogClass.showCustomCircleProgressDialog(
							LoginActivity.this, null, "登录中...");
					login(username, encodePass);
				}
			}else{
				Toast.makeText(LoginActivity.this, "请连接网络", 0).show();
			}

			// getChatDataFromNet();
			// Intent intent = new Intent();
			// intent.setClass(LoginActivity.this, MainActivity.class);
			// startActivity(intent);

			break;

		default:
			break;
		}
	}

	public void login(final String name, final String pass) {

		String url = Constants.URL_TEACHER_LOGIN;// 登录的网址

		// u=[教师企邮]&p=[密码]&DeviceToken=[设备唯一标识]&DeviceTokenType=[用户使用的客户端类型，Ipad/Iphone/Android/AndroidPad]

		RequestParams params = new RequestParams();

		params.addQueryStringParameter("u", name);
		params.addQueryStringParameter("p", pass);

		params.addQueryStringParameter("DeviceToken", "sssss");
		params.addQueryStringParameter("DeviceTokenType", "Android");
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		http.configTimeout(1000*5);
		System.out.println("paams的数据为===" + params.toString());

		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// ptool.printforToast("登陆成功，跳转到主页");
						
						ptool.printforLog("LoginActivity", responseInfo.result);
						System.out.println("登录信息==============="
								+ responseInfo.result);
						try {
							JSONObject str = new JSONObject(responseInfo.result);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");

							if (Result.equalsIgnoreCase("true")) {

								editor.putString("username", name);
								editor.putString("userPass", pass);
								editor.commit();
								savetosharepreference(responseInfo.result);
								// new 11.4
								SharedPreferences sp_user = LoginActivity.this
										.getSharedPreferences("userinfo",
												MODE_PRIVATE);
								ImageLoder app = (ImageLoder) LoginActivity.this
										.getApplication();
								app.checkNewestUserInfo(sp_user.getString(
										"Token", ""));
								// new 11.4
								Intent intent = new Intent();
								intent.setClass(LoginActivity.this,
										MainActivity.class);
								Bundle b = new Bundle();
								b.putString("", "");
								intent.putExtras(b);
								startActivity(intent);
								LodDialogClass.closeCustomCircleProgressDialog();
								// 销毁本activity
								finish();
						
							} else {
								LodDialogClass.closeCustomCircleProgressDialog();
								Toast.makeText(LoginActivity.this, Infomation,
										2).show();
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LodDialogClass.closeCustomCircleProgressDialog();
						System.out.println("onFailure");
						System.out.println(error.toString());

					}

				});

	}

	// 网络解析聊天token数据
	// public void getChatDataFromNet(String token){
	// String url = new Constants().URL_ActiveClass_getChatToken;
	// RequestParams params = new RequestParams();
	// params.addHeader("Authentication", token);
	// params.addBodyParameter("checkUpdate", "true");
	// HttpUtils utils = new HttpUtils();
	// utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
	//
	// @Override
	// public void onFailure(HttpException arg0, String arg1) {
	// // TODO Auto-generated method stub
	// System.out.println("网络解析聊天token数据    ===  onFailure");
	// }
	//
	// @Override
	// public void onSuccess(ResponseInfo<String> arg0) {
	// // TODO Auto-generated method stub
	// System.out.println("网络解析聊天token数据    ===  onSuccess");
	// String result = arg0.result;
	// System.out.println(" login的数据222222  " + result);
	// try {
	// JSONObject str = new JSONObject(result);
	// // JSONObject Infomation = str.getJSONObject("Infomation");
	// // JSONObject Result = str.getJSONObject("Result");
	// // JSONObject data = str.getJSONObject("Data");
	// String Result = str.getString("Result");
	// String Infomation = str.getString("Infomation");
	// String data = str.getString("Data");
	// JSONObject obj_data = new JSONObject(data);
	// String chatToken = obj_data.getString("chatToken");
	// System.out.println("login的聊天数据 ？？？=" + chatToken);
	// SharedPreferences share = LoginActivity.this.getSharedPreferences(
	// "userChatToken", MODE_PRIVATE);
	// Editor editor = share.edit();
	// editor.putString("chatToken", chatToken);
	// editor.commit();
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// });
	// }

	private void savetosharepreference(String str) {
		// 解析数据
		try {
			JSONObject obj = new JSONObject(str);
			// Result: true,
			// Infomation: ,
			// Data: {
			System.out.println("STR====解析登入信息==" + str);
			String result = obj.getString("Result");
			String infomation = obj.getString("Infomation");
			String data = obj.getString("Data");

			JSONObject obj_data = new JSONObject(data);
			String userInfo = obj_data.getString("UserInfo");
			JSONObject obj_info = new JSONObject(userInfo);

			// Email: teachervps02@163.com,
			// UID: 12,
			// NickName: 测试教师VPS02_,
			// Password: ,
			// Account: xdf0050009175,
			// RoleID: 2,
			// IconUrl:
			// teacherCode

			String Email = obj_info.getString("Email");
			String UID = obj_info.getString("UID");
			String NickName = obj_info.getString("NickName");
			String Password = obj_info.getString("Password");
			String Account = obj_info.getString("Account");
			String RoleID = obj_info.getString("RoleID");
			String IconUrl = obj_info.getString("IconUrl");
			String teacherCode = obj_info.getString("teacherCode");
			String sTeacherID = obj_info.getString("sTeacherID");
			// 聊天室的token
			String chatToken = obj_info.getString("chatToken");

			String Token = obj_data.getString("Token");
			// getChatDataFromNet(Token);
			SharedPreferences share = LoginActivity.this.getSharedPreferences(
					"userinfo", MODE_PRIVATE);
			Editor editor = share.edit();

			// editor.putString("BSSNAME", BSSNAME);

			editor.putString("Email", Email);
			editor.putString("UID", UID);
			editor.putString("NickName", NickName);
			editor.putString("Password", Password);
			editor.putString("Account", Account);
			editor.putString("RoleID", RoleID);
			editor.putString("IconUrl", IconUrl);
			editor.putString("Token", Token);
			// 聊天室的token
			editor.putString("chatToken", chatToken);
			editor.putString("teacherCode", teacherCode);
			editor.putString("sTeacherID", sTeacherID);

			editor.commit();
			System.out.println("login----" + teacherCode);
			// ptool.printforLog("LoginActivity", "保存用户数据完成" + sTeacherID);

		} catch (JSONException e) {

			e.printStackTrace();
		}

	}

	private boolean isOut;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isOut) {
				return super.onKeyDown(keyCode, event);
			} else {
				isOut = true;
				Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_LONG).show();
				//
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					@Override
					public void run() {
						isOut = false;
					}
				};
				timer.schedule(task, 3000); // 调试设置为5s
				return true;

			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
