package com.lelts.fragment.data;

import org.json.JSONException;
import org.json.JSONObject;

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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class DataForDocxDetailActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "DataDetailActivity";

	private ImageButton imageview_back;
	private TextView textview_data_collect;
	private TextView textview_data_type;// 资料类型
	private TextView textview_data_teacher_name;// 老师名字
	private TextView textview_data_createtime;// 创建 时间
	private TextView textview_data_looknum;// 浏览次数

	private WebView webview_public_class_video;
	private String token;

//	private String url_video = "";

	private String mate_id;
	private String MF_ID;
	private String readcount;
	private String name;
	private String videoThumbnail;
	private String url;

	private String optType;

	private String sT_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_fordocx_detail);
		ExitApplication.getInstance().addActivity(this);
		init();
		
		getdatafromshare();
		getdatafromintent();

		initdate();
	}

	private void initdate() {
		// 加载docx
		setwebview(webview_public_class_video, url);
	}

	private void getdatafromshare() {
		SharedPreferences share = DataForDocxDetailActivity.this
				.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");

		Log.d(TAG, "获取的token数值为=====" + token);

	}

	private void getdatafromintent() {
		Bundle b = getIntent().getExtras();
		mate_id = b.getString("mate_id");
		MF_ID = b.getString("MF_ID");
		sT_ID = b.getString("ST_ID");
		url = b.getString("url");

		System.out.println("MF_ID===" + MF_ID);

		if (MF_ID.equalsIgnoreCase("null")) {
			System.out.println("MF_ID为空，可以收藏");
			optType = "1";
			textview_data_collect.setText("收藏");
		} else {
			System.out.println("MF_ID不为空，可以取消收藏");
			optType = "0";
			textview_data_collect.setText("取消收藏");
		}
		
//		url_video = new Constants().URL_STUDYONLINE_STUDY_PUBLICCLASS_DETAIL_VIDEO
//				+ mate_id;

	}

	private void init() {

		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		textview_data_collect = (TextView) findViewById(R.id.textview_data_collect);
		textview_data_type = (TextView) findViewById(R.id.textview_data_type);
		textview_data_teacher_name = (TextView) findViewById(R.id.textview_data_teacher_name);
		textview_data_createtime = (TextView) findViewById(R.id.textview_data_createtime);

		webview_public_class_video = (WebView) findViewById(R.id.webview_public_class_video);

		imageview_back.setOnClickListener(this);
		textview_data_collect.setOnClickListener(this);
		LodDialogClass.showCustomCircleProgressDialog(DataForDocxDetailActivity.this, null, getString(R.string.common_Loading));
	}

	@SuppressWarnings("deprecation")
	private void setwebview(WebView wv, String url) {
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setPluginState(PluginState.ON);
		// wv.getSettings().setPluginsEnabled(true);//可以使用插件
		// wv.getSettings().setEnableSmoothTransition(true);
		wv.setEnabled(true);
		wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		wv.getSettings().setAllowFileAccess(true);
		wv.getSettings().setDefaultTextEncodingName("UTF-8");
		wv.getSettings().setLoadWithOverviewMode(true);
		wv.getSettings().setUseWideViewPort(true);
		wv.setVisibility(View.VISIBLE);

		wv.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				LodDialogClass.closeCustomCircleProgressDialog();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}
		});
		wv.loadUrl(url);
		
	}

	/**
	 * 收藏功能
	 * */
	private void setcollectdata() {

		Log.d(TAG, "getdatafromnet()执行");

		@SuppressWarnings("static-access")
		// String url = new Constants().URL_STUDYONLINE_COLLECT_DATA;
		String url = new Constants().URL_STUDYONLINE_COLLECT_DATA + "?mateId="
				+ mate_id + "&optType=" + optType+"&ST_ID="+sT_ID;

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		// params.addBodyParameter("pageIndex", pageIndex);
		// params.addBodyParameter("mateId", mate_id);

		HttpUtils http = new HttpUtils();
//		http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.GET, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						Log.d(TAG, "收藏资料的数据结果为=========" + responseInfo.result);

						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息

							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							if (Result.equalsIgnoreCase("true")) {
								if (optType.equalsIgnoreCase("0")) {
									Toast.makeText(
											DataForDocxDetailActivity.this,
											"取消收藏成功", 2).show();
									optType = "1";
									textview_data_collect.setText("收藏");
									LodDialogClass.closeCustomCircleProgressDialog();
								} else {
									Toast.makeText(
											DataForDocxDetailActivity.this,
											"收藏成功", Toast.LENGTH_SHORT)
											.show();
									optType = "0";
									textview_data_collect.setText("取消收藏");
									LodDialogClass.closeCustomCircleProgressDialog();
								}

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.d(TAG, error.toString());
						LodDialogClass.closeCustomCircleProgressDialog();
					}

				});

	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		case R.id.textview_data_collect:
			if (optType.endsWith("1")) {
				LodDialogClass.showCustomCircleProgressDialog(DataForDocxDetailActivity.this, "", "收藏中...");
			}else{
				LodDialogClass.showCustomCircleProgressDialog(DataForDocxDetailActivity.this, "", "取消收藏中...");
			}
			
			setcollectdata();
			break;

		default:
			break;
		}
	}

}
