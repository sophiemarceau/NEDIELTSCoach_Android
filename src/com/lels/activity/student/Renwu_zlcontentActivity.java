package com.lels.activity.student;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class Renwu_zlcontentActivity extends Activity implements
		OnClickListener {

	private String mateId;
	private WebView webview_public_class_video;
	private ImageButton imageview_back;
	private TextView textview_data_collect;
	private TextView textview_class_video_name;// ship品名称
	private TextView textview_video_type;// 类型
	private TextView textview_data_type;// 资料类型
	private TextView textview_data_teacher_name;// 老师名字
	private TextView textview_data_createtime;// 创建 时间
	private TextView textview_data_looknum;// 浏览次数
	private SharedPreferences share;
	private String path = Constants.URL_STUDYONLINE_LOOKVIDEOINFO;
	private String url = Constants.URL_STUDYONLINE_MEDIA_LOOKUPVIDEO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_renwu_zl);

		getintents();
		initview();
		gethttps();
		getdatafrovideo();

	}

	private void gethttps() {
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));// 添加保密的东西
		params.addBodyParameter("mId", mateId);
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				System.out.println("????result=" + result);
				try {
					JSONObject str = new JSONObject(arg0.result);// 获取请求的数据信息

					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					JSONObject Data = str.getJSONObject("Data");
					String VideoUrl = Data.getString("videoUrl");
					setwebview(webview_public_class_video, VideoUrl);
					System.out.println("-----------p" + VideoUrl);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	protected void onPause() {
		webview_public_class_video.reload();
		super.onPause();
	}

	@SuppressWarnings("deprecation")
	private void setwebview(WebView wv, String url) {
		wv.getSettings().setJavaScriptEnabled(true);
		
		wv.getSettings().setPluginState(PluginState.ON);         
		wv.setVisibility(View.VISIBLE);
		wv.getSettings().setUseWideViewPort(true); 
		
		wv.loadUrl(url);
		wv.setWebViewClient(new WebViewClient() {
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				LodDialogClass.closeCustomCircleProgressDialog();
			}
		});
	}

	private void getdatafrovideo() {

		@SuppressWarnings("static-access")
		RequestParams params = new RequestParams();

		params.addHeader("Authentication", share.getString("Token", ""));// 添加保密的东西

		params.addBodyParameter("mateId", mateId);

		HttpUtils http = new HttpUtils();

		http.send(HttpRequest.HttpMethod.POST, path, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息

							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");

							// JSONArray array = new JSONArray(Data);

							// materialsName
							// "Name": "阅读",
							// "sName": null,
							// "ReadCount": 116,
							// "CreateTime": "2015-04-14",
							// "FileType": "FLV"
							if (Result.equalsIgnoreCase("false")) {
								return;
							} else {

								JSONObject obj = new JSONObject(Data);
								String materialsName = obj
										.getString("materialsName");
								String Name = obj.getString("Name");
								String sName = obj.getString("sName");
								String ReadCount = obj.getString("ReadCount");
								String CreateTime = obj.getString("CreateTime");
								String FileType = obj.getString("FileType");

								// 设置 各个字段的属性
								textview_class_video_name.setText("名称 "
										+ materialsName);
								textview_data_createtime.setText(CreateTime);
								textview_data_type.setText(FileType);
								textview_data_teacher_name.setText(sName);

								if (Name.equalsIgnoreCase("null")) {

								} else {
									textview_video_type.setText(Name);
								}

								textview_data_looknum.setText(ReadCount);

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

	@SuppressWarnings("static-access")
	private void initview() {
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		textview_data_collect = (TextView) findViewById(R.id.textview_data_collect);

		textview_class_video_name = (TextView) findViewById(R.id.textview_class_video_name);
		textview_video_type = (TextView) findViewById(R.id.textview_video_type);

		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		imageview_back.setOnClickListener(this);
		textview_data_type = (TextView) findViewById(R.id.textview_data_type);
		textview_data_teacher_name = (TextView) findViewById(R.id.textview_data_teacher_name);
		textview_data_createtime = (TextView) findViewById(R.id.textview_data_createtime);

		textview_data_looknum = (TextView) findViewById(R.id.textview_data_looknum);

		webview_public_class_video = (WebView) findViewById(R.id.webview_public_class_video);
		LodDialogClass.showCustomCircleProgressDialog(Renwu_zlcontentActivity.this, null, getString(R.string.common_Loading));
	}

	private void getintents() {
		Intent intent = getIntent();
		mateId = intent.getStringExtra("RefID");
		System.out.println("???" + mateId);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;

		default:
			break;
		}
	}

}
