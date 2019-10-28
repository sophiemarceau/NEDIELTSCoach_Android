package com.lels.activity.student;

import org.json.JSONObject;

import com.example.hello.R;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.widget.ImageButton;
import android.widget.ImageView;

@SuppressLint("SetJavaScriptEnabled")
public class Renwu_doccontentActivity extends Activity implements OnClickListener {
	
	private SharedPreferences share;
	private String url=Constants.URL_HomegetMaterialsInfo;
	private String mateId;
	private WebView zlwebview;
	private ImageButton stu_task_back_img;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_doc_content);

		getintent();
		intview();
		gethttps();
	}

	private void gethttps() {

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));// 添加保密的东西
		params.addBodyParameter("mateId", mateId);
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				try {
					JSONObject obj = new JSONObject(result);
					JSONObject objdata = obj.getJSONObject("Data");
					String Url = objdata.getString("Url");
					setwebviews(zlwebview, Url);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	private void intview() {
		share =getSharedPreferences("userinfo",
				Context.MODE_PRIVATE);
		zlwebview = (WebView) findViewById(R.id.docwebview);
		stu_task_back_img = (ImageButton) findViewById(R.id.stu_task_back_img);
		stu_task_back_img.setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	private void setwebviews(WebView wv,String url) {
		 wv.getSettings().setJavaScriptEnabled(true);
		  wv.getSettings().setPluginState(PluginState.ON);
//		  wv.getSettings().setPluginsEnabled(true);//可以使用插件
//		  wv.getSettings().setEnableSmoothTransition(true);
		  wv.setEnabled(true);
		  wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		  wv.getSettings().setAllowFileAccess(true);
		  wv.getSettings().setDefaultTextEncodingName("UTF-8");
		  wv.getSettings().setLoadWithOverviewMode(true);
		  wv.getSettings().setUseWideViewPort(true);
		  wv.setVisibility(View.VISIBLE);		 
		  wv.loadUrl(url);
		  
	}

	private void getintent() {
		Intent intent = getIntent();
		mateId = intent.getStringExtra("RefID");
		System.out.println("???"+mateId);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stu_task_back_img:
			finish();
			break;

		default:
			break;
		}
	}
}
