package com.lels.activity.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.constants.Constants;
import com.lelts.tool.IntentUtlis;
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
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class Correct_writeActivity extends Activity implements OnClickListener {
	@SuppressWarnings("unused")
	private Context mContext;
	private WebView mWebView;// webview
	private ImageButton back_img;// 返回按钮
	private Button surebtn;// 确认按钮
	private TextView correct;// 总分
	private LinearLayout tab_linear, view_linear, tr_linear, cc_linear,
			lr_linear, gra_linear;// 批分layout
	private List<TextView> textlist;// 批分集合
	private SharedPreferences share;// shara
	private String examInfoId;// 答题内容id
	private String answerContent, ex_ID, eXA_ID;// 参数
	private String url = Constants.URL_TeacherClassesexamCorrecting;// 写作路径
	private String urll = Constants.URL_TeacherClassesfinishXzCorrecting;// 写作完成路径
	private List<String> contentlist;

	private float aTR, aCC, aLR, aGRA;
	private int iTR, iCC, iLR, iGRA;
	private HashMap<Integer, String> exaidmap;
	private List<HashMap<String, Object>> maplist;
	private String[] str_index;

	private int page_index = 0;
	private String paperId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_correct_write);
		mContext = this;
		getintent();
		initview();

		getLinearpgnow(tr_linear, "TA");
		getLinearpgnow(lr_linear, "LR");
		getLinearpgnow(gra_linear, "GRA");
		getLinearpgnow(cc_linear, "CC");

		getHttp();

	}

	private void getintent() {
		Intent intent = getIntent();
		examInfoId = intent.getStringExtra("ExamID");
		paperId = intent.getStringExtra("paperId");
		System.out.println("examInfoId" + examInfoId);
	}

	/**
	 * 动态获取选项卡、下标线
	 */
	private void getLineartab(final List<String> conlist) {

		tab_linear.removeAllViews();
		view_linear.removeAllViews();
		int winWidth = getWindowManager().getDefaultDisplay().getWidth();
		textlist = new ArrayList<TextView>();
		for (int i = 0; i < conlist.size(); i++) {

			TextView textview = new TextView(mContext);
			View vv = new View(mContext);
			vv.setLayoutParams(new LayoutParams(winWidth / 5,
					LayoutParams.MATCH_PARENT));
			vv.setBackgroundColor(Color.RED);
			textview.setLayoutParams(new LayoutParams(winWidth / 5,
					LayoutParams.MATCH_PARENT));
			textview.setId(i);
			textview.setText("TASK" + (i + 1));
			textview.setTextSize(15);
			textview.setGravity(Gravity.CENTER);
			textlist.add(textview);
			tab_linear.addView(textview);
			view_linear.addView(vv);
			if (i == 0) {
				vv.setVisibility(View.VISIBLE);
				textview.setSelected(true);
				textview.setTextColor(Color.RED);
				getWebview(contentlist.get(0));
			} else {
				vv.setVisibility(View.INVISIBLE);
			}
			/**
			 * Teb监听
			 */
			textview.setOnClickListener(new OnClickListener() {

				@SuppressWarnings("unused")
				@Override
				public void onClick(View v) {

					if (conlist == null) {
						getHttp();
					}
					page_index = v.getId();
					for (int i = 0; i < textlist.size(); i++) {
						
						TextView view = (TextView) tab_linear.getChildAt(i);
						int vindex = Integer.parseInt(view.getText().toString()
								.substring(4));
						if (view != v) {
							view.setSelected(false);
							view.setTextColor(Color.BLACK);
							view_linear.getChildAt(i).setVisibility(
									View.INVISIBLE);
						} else {
							view.setSelected(true);
							view.setTextColor(Color.RED);
							getWebview(contentlist.get(i));
							view_linear.getChildAt(i).setVisibility(
									View.VISIBLE);
							getLinearpg(i, tr_linear, "TA", maplist);
							getLinearpg(i, cc_linear, "CC", maplist);
							getLinearpg(i, lr_linear, "LR", maplist);
							getLinearpg(i, gra_linear, "GRA", maplist);
							eXA_ID = exaidmap.get(vindex - 1);
							System.out.println("vindex---" + vindex);
							setButton(vindex - 1, maplist);
						}
					}
				}
			});
		}
	}

	/**
	 * 批改的未批改
	 * 
	 * @param list
	 * @param lay_linear
	 */
	private void getLinearpgnow(final LinearLayout lay_linear, final String a) {
		int winWidth = getWindowManager().getDefaultDisplay().getWidth();
		aTR = (float) 0.0;
		aCC = (float) 0.0;
		aLR = (float) 0.0;
		aGRA = (float) 0.0;
		correct.setText("0.0");
		lay_linear.removeAllViews();
		for (int i = 0; i < 10; i++) {
			TextView textview = new TextView(mContext);

			if (i == 0) {
				textview.setText(a);
				textview.setBackgroundResource(R.drawable.lianxi_hongdian);
				textview.setTextColor(Color.WHITE);
				textview.setLayoutParams(new LayoutParams(winWidth / 9,
						LayoutParams.MATCH_PARENT));
				textview.setGravity(Gravity.CENTER);
				textview.setTextSize(15);

			} else {
				textview.setLayoutParams(new LayoutParams(
						(int) (winWidth / 11), LayoutParams.WRAP_CONTENT));
				textview.setText(i + "");
				textview.setTextSize(15);
				textview.setGravity(Gravity.CENTER);
				textview.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						for (int i = 1; i < 10; i++) {
							TextView view = (TextView) lay_linear.getChildAt(i);

							if (view != v) {
								view.setSelected(false);
								view.setTextSize(15);
								view.setTextColor(Color.BLACK);
							} else {
								view.setSelected(true);
								view.setTextSize(20);
								view.setTextColor(Color.RED);
								view.setGravity(Gravity.CENTER);
								if (a.equals("TA")) {
									aTR = (float) i;
								} else if (a.equals("CC")) {
									aCC = (float) i;
								} else if (a.equals("LR")) {
									aLR = (float) i;
								} else if (a.equals("GRA")) {
									aGRA = (float) i;
								}
							}
						}
						float correctAll = aTR + aCC + aLR + aGRA;
						float ert = correctAll / 4;
						float b = (float) (Math.round(ert * 100)) / 100;// 保留小数点后一位
						if (aTR > 0 && aCC > 0 && aLR > 0 && aGRA > 0) {
							int c = (int) b;
							float d = c;
							if ((b - d) >= 0.25 && (b - d) < 0.75) {
								correct.setText((d + 0.5) + "");
							} else if ((b - d) >= 0.75) {
								correct.setText((d + 1) + "");
							} else {
								correct.setText(d + "");
							}
						} else {
							correct.setText("0.0");
						}
					}
				});
			}
			lay_linear.addView(textview);

		}
	}

	/**
	 * 批改的已批改
	 * 
	 * @param list
	 * @param lay_linear
	 */
	private void getLinearpg(int index, LinearLayout lay_linear,
			String quesType, List<HashMap<String, Object>> scorelist) {
		if (scorelist == null) {
			return;
		}
		int winWidth = getWindowManager().getDefaultDisplay().getWidth();
		lay_linear.removeAllViews();
		for (int i = 0; i < 10; i++) {
			TextView textview = new TextView(mContext);

			if (i == 0) {
				textview.setText(quesType);
				textview.setBackgroundResource(R.drawable.lianxi_hongdian);
				textview.setTextColor(Color.WHITE);
				textview.setLayoutParams(new LayoutParams(winWidth / 9,
						LayoutParams.FILL_PARENT));
				textview.setGravity(Gravity.CENTER);
				textview.setTextSize(15);

			} else {
				textview.setLayoutParams(new LayoutParams(
						(int) (winWidth / 10.5), LayoutParams.WRAP_CONTENT));
				textview.setText(i + "");
				textview.setTextSize(15);
			}
			// 填写分值
			for (int j = 0; j < scorelist.size(); j++) {
				System.out.println("iscore==="
						+ Double.valueOf(
								scorelist.get(j).get("Score").toString())
								.intValue());
				boolean c1 = scorelist.get(j).get("Index").toString()
						.equals(String.valueOf(index));
				boolean c2 = scorelist.get(j).get("Name").toString()
						.equals(quesType);
				boolean c3 = i == Double.valueOf(
						scorelist.get(j).get("Score").toString()).intValue();

				if (c1 && c2 && c3) {

					textview.setTextSize(20);
					textview.setTextColor(Color.RED);

					if (quesType.equals("TA")) {
						aTR = Float.valueOf(textview.getText().toString());
					} else if (quesType.equals("CC")) {
						aCC = Float.valueOf(textview.getText().toString());
						;
					} else if (quesType.equals("LR")) {
						aLR = Float.valueOf(textview.getText().toString());
						;
					} else if (quesType.equals("GRA")) {
						aGRA = Float.valueOf(textview.getText().toString());
						;
					}
				}
			}
			float correctAll = aTR + aCC + aLR + aGRA;
			float ert = correctAll / 4;
			float b = (float) (Math.round(ert * 10)) / 10;// 保留小数点后一位
			int c = (int) b;
			float d = c;
			if ((b - d) >= 0.25 && (b - d) < 0.75) {
				correct.setText((d + 0.5) + "");
			} else if ((b - d) >= 0.75) {
				correct.setText((d + 1) + "");
			} else {
				correct.setText(d + "");
			}
			lay_linear.addView(textview);

		}
	}

	/**
	 * Button按钮显示、隐藏
	 * 
	 * @param index
	 * @param list
	 */
	private void setButton(int index, List<HashMap<String, Object>> list) {
		boolean flag = false;
		for (int i = 0; i < list.size(); i++) {
			if (String.valueOf(index).equals(
					list.get(i).get("Index").toString())) {

				flag = true;
				break;
			}

		}

		if (flag == true) {// 已批改
			surebtn.setVisibility(View.INVISIBLE);
		} else {// 为批改
			surebtn.setVisibility(View.VISIBLE);
			getLinearpgnow(tr_linear, "TA");
			getLinearpgnow(lr_linear, "LR");
			getLinearpgnow(gra_linear, "GRA");
			getLinearpgnow(cc_linear, "CC");
			/*
			 * getLinearpgnow(tr_linear, "TR"); getLinearpgnow(cc_linear, "CC");
			 * getLinearpgnow(lr_linear, "LR"); getLinearpgnow(gra_linear,
			 * "GRA");
			 */
		}

	}

	/**
	 * 網絡請求
	 */
	@SuppressWarnings("unused")
	private void getHttp() {
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("examInfoId", examInfoId);
		params.addBodyParameter("paperId", paperId);
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				System.out.println("???" + result);
				try {
					JSONObject obj = new JSONObject(result);
					JSONObject obj1 = obj.getJSONObject("Data");
					JSONArray array = obj1.getJSONArray("examCorrectingList");

					str_index = new String[array.length()];
					contentlist = new ArrayList<String>();
					maplist = new ArrayList<HashMap<String, Object>>();
					exaidmap = new HashMap<Integer, String>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject objitem = array.getJSONObject(i);
						answerContent = objitem.getString("AnswerContent");
						if (i == 0) {
							eXA_ID = objitem.getString("EXA_ID");
						}
						exaidmap.put(i, objitem.getString("EXA_ID"));
						ex_ID = objitem.getString("Ex_ID");// 答题信息ID

						String IsCorrected = objitem.getString("IsCorrected");// 批改状态

						str_index[i] = IsCorrected;

						if (IsCorrected.equals("0")) {

						} else if (IsCorrected.equals("1")) {

							JSONArray scores = objitem.getJSONArray("scores");

							for (int j = 0; j < scores.length(); j++) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								JSONObject objscores = scores.getJSONObject(j);
								// task的位置
								map.put("Index", i);
								map.put("EA_ID", objscores.getString("EA_ID"));
								map.put("Name", objscores.getString("Name"));
								map.put("Score", objscores.getString("Score"));
								maplist.add(map);
							}
						}

						contentlist.add(answerContent);//
					}
					
					
					getLineartab(contentlist);

					getLinearpg(0, tr_linear, "TA", maplist);
					getLinearpg(0, cc_linear, "CC", maplist);
					getLinearpg(0, lr_linear, "LR", maplist);
					getLinearpg(0, gra_linear, "GRA", maplist);
					setButton(0, maplist);

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});
	}

	/**
	 * 对webview的操作
	 */
	@SuppressWarnings("deprecation")
	private void getWebview(String data) {
		// 得到WebSettings对象，设置支持JavaScript的参数
		mWebView.getSettings().setJavaScriptEnabled(true);
		// 设置可以支持缩放
		mWebView.getSettings().setSupportZoom(true);
		// 设置默认缩放尺寸是FAR
		mWebView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		// 设置出现缩放工具
		mWebView.getSettings().setBuiltInZoomControls(true);
		// MyWebViewClient client = new MyWebViewClient();
		// client.shouldOverrideUrlLoading(mWebView, "http://www.baidu.com");
		mWebView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
		// mWebView.setWebViewClient(client);
	}

	/**
	 * 初始化组件
	 */
	private void initview() {

		mWebView = (WebView) findViewById(R.id.write_correct_webview);
		correct = (TextView) findViewById(R.id.correct_xztotal_score);
		back_img = (ImageButton) findViewById(R.id.write_correct_back);
		back_img.setOnClickListener(this);

		surebtn = (Button) findViewById(R.id.listener_xzcorrect_finished);
		surebtn.setOnClickListener(this);
		tab_linear = (LinearLayout) findViewById(R.id.weite_choise_tab_linear);
		view_linear = (LinearLayout) findViewById(R.id.weite_choise_view_linear);
		tr_linear = (LinearLayout) findViewById(R.id.correct_write_tr_linear);
		lr_linear = (LinearLayout) findViewById(R.id.correct_write_cc_linear);
		gra_linear = (LinearLayout) findViewById(R.id.correct_write_lr_linear);
		cc_linear = (LinearLayout) findViewById(R.id.correct_write_gra_linear);
		// 实例化share
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		
	}

	/**
	 * 在WebView中而不是默认浏览器中显示页面
	 */
	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

	}

	public void getcorrectHttp() {

		/*
		 * TRV=[TRV成绩，不可为空]  CCV=[CCV成绩，不可为空]  LRV=[LRV成绩，不可为空]
		 *  GRAV=[GRAV成绩，不可为空]  examInfoId=[答题信息ID ExamInfo，不可为空]
		 *  examAnswerId=[答题内容ID ExamAnswer，不可为空] aTR,aCC,aLR,aGRA
		 */
		iTR = (int) aTR;
		iCC = (int) aCC;
		iLR = (int) aLR;
		iGRA = (int) aGRA;
		System.out.println(iTR + "/" + iCC + "/" + iLR + "/" + iGRA);
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("TRV", String.valueOf(iTR));
		params.addBodyParameter("CCV", String.valueOf(iCC));
		params.addBodyParameter("LRV", String.valueOf(iLR));
		params.addBodyParameter("GRAV", String.valueOf(iGRA));
		params.addBodyParameter("examInfoId", ex_ID);
		params.addBodyParameter("examAnswerId", eXA_ID);
		HttpUtils uti = new HttpUtils();
		uti.send(HttpMethod.POST, urll, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				System.out.println("onFailure");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				try {
					JSONObject obj = new JSONObject(result);
					String Result = obj.getString("Result");
					System.out.println(Result);
					if (Result.equalsIgnoreCase("true")) {
						
						str_index[page_index] = "1";
						int m = 0;
						for (int i = 0; i < str_index.length; i++) {
							if (str_index[i].equalsIgnoreCase("0")) {
								m = 1;
							}
						}
						if(m == 1){
							getHttp();
							Toast.makeText(mContext, "批改成功", 0).show();
						}else{
							finish();
							Toast.makeText(mContext, "批改成功", 0).show();
						}

						


					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

	}

	/**
	 * 组件监听
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.write_correct_back:
			finish();
			break;
		case R.id.listener_xzcorrect_finished:
			if (!correct.getText().toString().equals("0.0")) {
				getcorrectHttp();
			} else {
				Toast.makeText(mContext, "批改未完成", 0).show();
			}
			break;
		}
	}
}
