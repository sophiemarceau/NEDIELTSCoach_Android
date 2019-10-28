package com.lels.activity.myself;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.R;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 联系我们
 * **/
public class MyselfContactusActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "MyselfContactusActivity";

	private ImageButton imageview_back;
	private EditText edittext_contactus;
	private TextView textview_send;

	private String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_contactus);

		init();
		getdatafromshare();
	}

	private void init() {

		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		edittext_contactus = (EditText) findViewById(R.id.edittext_contactus);

		textview_send = (TextView) findViewById(R.id.textview_send);

		imageview_back.setOnClickListener(this);
		textview_send.setOnClickListener(this);
		edittext_contactus.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				temp = s;
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (temp.length() > 100) {
					Toast.makeText(MyselfContactusActivity.this, "最多只能输入100个字", Toast.LENGTH_LONG).show();;
					CharSequence cs = s.subSequence(0, 100);
					edittext_contactus.setText(cs);
					edittext_contactus.setSelection(100);
				}
			}
		});
	}

	private void getdatafromshare() {

		SharedPreferences share = MyselfContactusActivity.this
				.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		case R.id.textview_send:
			
			if(edittext_contactus.getText().toString().equalsIgnoreCase("")){
				Toast.makeText(MyselfContactusActivity.this, "请输入你的宝贵意见！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			sendmessage();
			break;
		default:
			break;
		}
	}

	private void sendmessage() {

		String content = edittext_contactus.getText().toString();

		String url = new Constants().URL_MYSELF_SENDMESSAGE_TOUS;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		params.addBodyParameter("contentText", content);

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
							Log.d(TAG, "解析获取个人中心的首页数据" + responseInfo.result);
							JSONObject str = new JSONObject(responseInfo.result);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");

							if (Result.equalsIgnoreCase("true")) {
								Toast.makeText(MyselfContactusActivity.this,
										"发送成功", Toast.LENGTH_SHORT).show();
								finish();
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

}
