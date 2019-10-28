package com.lels.activity.student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.activity.student.Correct_listener_mkActivity.SeekBarChangeEvent;
import com.lels.constants.Constants;
import com.lelts.tool.IntentUtlis;
import com.lelts.tool.Player;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Correct_listener_lxActivity extends Activity implements
		OnClickListener {
	private List<TextView> textlist;
	@SuppressWarnings("unused")
	private List<View> viewlist;
	private Button btnsure;
	private LinearLayout tab_linear, view_linear;
	private TextView allcorrect, timetv,timeall;
	private Context mContext;
	private ImageView imgplay;
	private ImageButton imgback;
	private Player mplayer;
	private SeekBar seekbar;
	private SharedPreferences share;
	private int PLAYER_INT = 1;
	private String videoUrl;
	private String paperId;
	private List<String> videolist;
	private String url = Constants.URL_TeacherClassescontactCorrecting;
	private String finishurl = Constants.URL_TeacherClassesfinishKyCorrecting;
	// private String videopath = "mnt/sdcard/Music/lkq.mp3";
	private String examID, eXA_ID, type2, ex_ID;
	private LinearLayout tr_linear, cc_linear, lr_linear, gra_linear;

	private float tra, cca, lra, graa;
	private int itra, ilra, igraa, icca;

	List<HashMap<String, Object>> maplist;

	private String eXA_ID_new;
	private String[] str_index;
	private int page_index = 0;
	private HashMap<Integer, String> exaidmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lx);
		getitentvalue();
		initView();
		
		getLinearpgnow(tr_linear, "FC");
		getLinearpgnow(cc_linear, "LR");
		getLinearpgnow(lr_linear, "GRA");
		getLinearpgnow(gra_linear, "P");
		getlxhttps();

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
			btnsure.setVisibility(View.INVISIBLE);
		} else {// 未批改
			btnsure.setVisibility(View.VISIBLE);
			getLinearpgnow(tr_linear, "FC");
			getLinearpgnow(cc_linear, "LR");
			getLinearpgnow(lr_linear, "GRA");
			getLinearpgnow(gra_linear, "P");
		}

	}

	/**
	 * 获取传递值
	 */
	private void getitentvalue() {
		Intent gmkintent = getIntent();
		examID = gmkintent.getStringExtra("ExamID");
		paperId = gmkintent.getStringExtra("paperId");
	}

	/**
	 * 网络请求数据
	 */
	private void getlxhttps() {
		RequestParams params = new RequestParams();
		System.out.println(">>>>>>>>>>>>>" + share.getString("Token", ""));
		params.addHeader("Authentication", share.getString("Token", ""));

		params.addBodyParameter("examInfoId", examID);
		params.addBodyParameter("paperId", paperId);
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				System.out.println("onFailure" + arg0.toString());
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				System.out.println(result);
				try {
					JSONObject obj = new JSONObject(result);
					JSONObject objdata = obj.getJSONObject("Data");
					JSONArray array = objdata
							.getJSONArray("examCorrectSpeakList");
					str_index = new String[array.length()];
					videolist = new ArrayList<String>();
					StringBuffer sb = new StringBuffer();
					// 已批改的值 集合;
					maplist = new ArrayList<HashMap<String, Object>>();
					// 保存完成批改所需的参数EXA_ID;
					exaidmap = new HashMap<Integer, String>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject itemobj = array.getJSONObject(i);
						videoUrl = itemobj.getString("AnswerContent");
						if (i == 0) {
							eXA_ID = itemobj.getString("EXA_ID");
						}
						exaidmap.put(i, itemobj.getString("EXA_ID"));
						ex_ID = itemobj.getString("Ex_ID");
						type2 = itemobj.getString("type2");
						String IsCorrected = itemobj.getString("IsCorrected");// 批改状态
						str_index[i] = IsCorrected;
						if (IsCorrected.equals("0")) {

						} else if (IsCorrected.equals("1")) {
							JSONArray scores = itemobj.getJSONArray("scores");
							for (int j = 0; j < scores.length(); j++) {
								HashMap<String, Object> map = new HashMap<String, Object>();
								JSONObject objscores = scores.getJSONObject(j);
								// task的位置
								map.put("Index", i);
								map.put("EA_ID", objscores.getString("EA_ID"));
								map.put("Name", objscores.getString("Name"));
								map.put("Score", objscores.getString("Score"));
								maplist.add(map);
								System.out.println(map.get("Name") + "!!"
										+ map.get("Score") + maplist.size());
							}

						}
						videolist.add(videoUrl);
						System.out.println(maplist.size()
								+ "!!!!!!!!!!!!!!!!!!!!");
					}
					getLinear(videolist);
					getLinearpg(0, tr_linear, "FC", maplist);
					getLinearpg(0, cc_linear, "LR", maplist);
					getLinearpg(0, lr_linear, "GRA", maplist);
					getLinearpg(0, gra_linear, "P", maplist);
					setButton(0, maplist);
//					timeall.setText(toTime((mplayer.mediaPlayer.getDuration())));
					System.out.println(">>>>>>" + videolist.size());
				} catch (JSONException e) {
					e.printStackTrace();

				}

			}
		});
	}

	@Override
	protected void onDestroy() {
		mplayer.stop();
		super.onDestroy();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		mContext = this;
		tab_linear = (LinearLayout) findViewById(R.id.listener_lx_choise_tab_linear);
		view_linear = (LinearLayout) findViewById(R.id.listener_lx_choise_view_linear);
		tr_linear = (LinearLayout) findViewById(R.id.score_tr_lx_lx_linear);
		cc_linear = (LinearLayout) findViewById(R.id.score_cc_lx_lx_linear);
		lr_linear = (LinearLayout) findViewById(R.id.score_lr_lx_lx_linear);
		gra_linear = (LinearLayout) findViewById(R.id.score_gra_lx_lx_linear);
		imgback = (ImageButton) findViewById(R.id.listener_lx_correct_back);
		imgback.setOnClickListener(this);
		btnsure = (Button) findViewById(R.id.listener_lx_correct_finished);
		btnsure.setOnClickListener(this);
		allcorrect = (TextView) findViewById(R.id.correct_total_score);
		timetv = (TextView) findViewById(R.id.tv_lx_self);
		timeall = (TextView) findViewById(R.id.timeall);
		imgplay = (ImageView) findViewById(R.id.listener_correct_start_img1);
		seekbar = (SeekBar) findViewById(R.id.seekbar1_self1);
		mplayer = new Player(seekbar,imgplay);
		
		imgplay.setOnClickListener(this);
		

		seekbar.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		// 实例化share
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		System.out.println(share.getString("Token", ""));
	}

	/**
	 * 转换成时间
	 * 
	 */
	private String toTime(int time) {
		int minute = time / 1000 / 60;
		int s = time / 1000 % 60;
		String mm = null;
		String ss = null;
		if (minute < 10)
			mm = "0" + minute;
		else
			mm = minute + "";

		if (s < 10)
			ss = "0" + s;
		else
			ss = "" + s;

		return mm + ":" + ss;
	}

	/**
	 * seekbar进度监听
	 * 
	 * @author Administrator
	 *
	 */
	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
		int progre;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			this.progre = progress * mplayer.mediaPlayer.getCurrentPosition()
					/ seekBar.getMax();
			timetv.setText(toTime(progre));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
			mplayer.mediaPlayer.seekTo(progre);
		}
	}

	/**
	 * 评分条未批改
	 */
	private void getLinearpgnow(final LinearLayout lay_linear, final String a) {
		int winWidth = getWindowManager().getDefaultDisplay().getWidth();
		
		tra = (float) 0.0;
		cca = (float) 0.0;
		lra = (float) 0.0;
		graa = (float) 0.0;
		allcorrect.setText("0.0");
		
		lay_linear.removeAllViews();
		for (int i = 0; i < 10; i++) {
			TextView textview = new TextView(mContext);
			if (i == 0) {
				textview.setText(a);
				textview.setBackgroundResource(R.drawable.lianxi_hongdian);
				textview.setTextColor(Color.WHITE);
				textview.setLayoutParams(new LayoutParams(winWidth / 9,
						LayoutParams.FILL_PARENT));
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
								if (a.equals("FC")) {
									tra = (float) i;
								} else if (a.equals("P")) {
									cca = (float) i;
								} else if (a.equals("LR")) {
									lra = (float) i;
								} else if (a.equals("GRA")) {
									graa = (float) i;
								}
							}
							float correctAll = tra + cca + lra + graa;
							float ert = correctAll / 4;
							float b = (float) (Math.round(ert * 100)) / 100;// 保留小数点后2位
							if (tra > 0 && cca > 0 && lra > 0 && graa > 0) {
								int c = (int) b;
								float d = c;
								if ((b - d) >= 0.25 && (b - d) < 0.75) {
									allcorrect.setText((d + 0.5) + "");
								} else if ((b - d) >= 0.75) {
									allcorrect.setText((d + 1) + "");
								} else {

									allcorrect.setText(d + "");
								}
							} else {
								allcorrect.setText("0.0");
							}
						}
					}
				});
			}
			lay_linear.addView(textview);

		}
	}

	/**
	 * 评分条已批改
	 */
	@SuppressWarnings({ "unused", "deprecation" })
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
						(int) (winWidth / 11), LayoutParams.WRAP_CONTENT));
				textview.setText(i + "");
				textview.setTextSize(15);
				textview.setGravity(Gravity.CENTER);
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

					if (quesType.equals("FC")) {
						tra = Integer.parseInt(textview.getText().toString());
					} else if (quesType.equals("P")) {
						cca = (float) Integer.parseInt(textview.getText()
								.toString());
						;
					} else if (quesType.equals("LR")) {
						lra = (float) Integer.parseInt(textview.getText()
								.toString());
						;
					} else if (quesType.equals("GRA")) {
						graa = (float) Integer.parseInt(textview.getText()
								.toString());
						;
					}
				}
			}
			float correctAll = tra + cca + lra + graa;
			float ert = correctAll / 4;
			float b = (float) (Math.round(ert * 100)) / 100;// 保留小数点后一位
			int c = (int) b;
			float d = c;
			if ((b - d) >= 0.25 && (b - d) < 0.75) {
				allcorrect.setText((d + 0.5) + "");
			} else if ((b - d) >= 0.75) {
				allcorrect.setText((d + 1) + "");
			} else {
				allcorrect.setText(d + "");
			}
			System.out.println(c + "/" + d + "/" + b + "/" + (b - d));
			lay_linear.addView(textview);

		}
	}

	/**
	 * 动态获取选项卡
	 */
	private void getLinear(final List<String> list) {
		
		tab_linear.removeAllViews();
		view_linear.removeAllViews();
		int winWidth = getWindowManager().getDefaultDisplay().getWidth();
		textlist = new ArrayList<TextView>();
		for (int i = 0; i < list.size(); i++) {
			
			TextView textview = new TextView(mContext);
			View vv = new View(mContext);
			vv.setLayoutParams(new LayoutParams(winWidth / 5,
					LayoutParams.MATCH_PARENT));
			vv.setBackgroundColor(Color.RED);
			textview.setLayoutParams(new LayoutParams(winWidth / 5,
					LayoutParams.MATCH_PARENT));
			textview.setText("PART" + (i + 1));
			textview.setTextSize(18);
			
			textview.setId(i);
			
			textview.setGravity(Gravity.CENTER);
			textlist.add(textview);
			tab_linear.addView(textview);
			view_linear.addView(vv);
			if (i == 0) {
				
				vv.setVisibility(View.VISIBLE);
				textview.setSelected(true);
				textview.setTextColor(Color.RED);
				if (mplayer.mediaPlayer.isPlaying()) {
					mplayer.mediaPlayer.pause();
				}
				if (list.get(0).length()>0&&!"null".equals(list.get(0))&&!"undefined".equals(list.get(0))) {
					mplayer.playUrl(list.get(0));
					timeall.setText("/"+toTime(mplayer.mediaPlayer.getDuration()));
				}
				
			} else {
				vv.setVisibility(View.INVISIBLE);
			}
			textview.setOnClickListener(new OnClickListener() {

				@SuppressWarnings("unused")
				@Override
				public void onClick(View v) {
					page_index = v.getId();
					/*mplayer.mediaPlayer.stop();
					imgplay.setBackgroundResource(R.drawable.bofang);
					*/
					if (list == null) {
						getlxhttps();
					}
					for (int i = 0; i < list.size(); i++) {
						TextView view = (TextView) tab_linear.getChildAt(i);
						int vindex = Integer.parseInt(view.getText().toString()
								.substring(4));
						if (view != v) {
							view.setSelected(false);
							view.setTextColor(Color.BLACK);
							view_linear.getChildAt(i).setVisibility(
									View.INVISIBLE);
							imgplay.setBackgroundResource(R.drawable.bofang);
						} else {
							if (mplayer.mediaPlayer.isPlaying()) {
								mplayer.mediaPlayer.pause();
							}
							if (list.get(i).length()>0&&!"null".equals(list.get(i))&&!"undefined".equals(list.get(i))) {
								mplayer.playUrl(list.get(i));
								timeall.setText("/"+toTime(mplayer.mediaPlayer.getDuration()));
							}
							view.setSelected(true);
							view.setTextColor(Color.RED);
							view_linear.getChildAt(i).setVisibility(
									View.VISIBLE);
							getLinearpg(i, tr_linear, "FC", maplist);
							getLinearpg(i, cc_linear, "LR", maplist);
							getLinearpg(i, lr_linear, "GRA", maplist);
							getLinearpg(i, gra_linear, "P", maplist);
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
	 * 完成批改接口
	 */
	private void getfinishHttp() {
		itra = (int) tra;
		ilra = (int) lra;
		igraa = (int) graa;
		icca = (int) cca;
		RequestParams params = new RequestParams();
		System.out.println(">>>>getfinishhttp()>>>>" + itra + ilra + igraa
				+ icca);
		System.out.println("EXA_ID-----" + eXA_ID);
		System.out.println("ex_ID-----" + ex_ID);
		// 08-05 06:12:10.917: I/System.out(14316):
		// token==========MTJ8eGRmMDA1MDAwOTE3NXwxNDM4NzQ3ODMzMjI2

		System.out.println(share.getString("Token", ""));
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("examAnswerIds", eXA_ID);
		params.addBodyParameter("examInfoId", ex_ID);
		params.addBodyParameter("FCV", String.valueOf(itra));
		params.addBodyParameter("LRV", String.valueOf(ilra));
		params.addBodyParameter("GRAV", String.valueOf(igraa));
		params.addBodyParameter("PV", String.valueOf(icca));
		params.addBodyParameter("type", "2");
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, finishurl, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						System.out.println("onFailure");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String result = arg0.result;
						JSONObject obj;
						try {
							obj = new JSONObject(result);
							String Result = obj.getString("Result");
							
							if (Result.equalsIgnoreCase("true")) {
								
								str_index[page_index] = "1";
								int m = 0;
								for (int i = 0; i < str_index.length; i++) {
									if (str_index[i].equalsIgnoreCase("0")) {
										m = 1;
									}
								}
								if(m == 1){
									getlxhttps();
								}else{
									finish();
								}

							
						}
							} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	/**
	 * 播放按钮点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.listener_correct_start_img1:
			if (mplayer.mediaPlayer.isPlaying()) {
				mplayer.pause();
				imgplay.setBackgroundResource(R.drawable.bofang);
			}else{
				mplayer.play();
				imgplay.setBackgroundResource(R.drawable.tingzhi);
			}
			break;
		case R.id.listener_lx_correct_back:
			finish();
			break;
		case R.id.listener_lx_correct_finished:
			if (!allcorrect.getText().equals("0.0")) {
				getfinishHttp();
				
			} else {
				Toast.makeText(mContext, "批改未完成", 0).show();
			}
			break;
		}
	}
}
