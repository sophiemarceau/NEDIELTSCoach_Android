package com.lels.activity.student;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
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
import android.media.MediaPlayer;
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

public class Correct_listener_mkActivity extends Activity implements
		OnClickListener {

	private Context context;
	private ImageView play_img_one, play_img_tow, play_img_three;
	private ImageButton imgback;
	private TextView mTvSelf1, mTvSelf2, mTvSelf3, scoceAll;
	private SeekBar seekbar_one, seekbar_tow, seekbar_three;
	private Player mplayer_one, mplayer_tow, mplayer_three;
	private MediaPlayer media_one, media_tow, media_three;
	private int PLAYER_ONE = 1, PLAYER_TOW = 1, PLAYER_THREE = 1;
	// .amr文件路径
	private String examID;
	private String paperId;
	private SharedPreferences share;
	private String url = Constants.URL_TeacherClassesvoiceCorrecting;

	private LinearLayout tr_linear, cc_linear, lr_linear, gra_linear;
	private List<TextView> textlisttr, textlistcc, textlistlr, textlistgra;
	protected float aTR;
	protected float aCC;
	protected float aLR;
	protected float aGRA;
	private TextView tv_mktime_self1, tv_mktime_self2, tv_mktime_self3;
	private Button listener_mkcorrect_finished;
	private String examAnswerIds = "", ex_ID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_correct_listener);
		getintent();
		initview();
		getHttp();
		getLinearpg(textlisttr, tr_linear, "FC");
		getLinearpg(textlistcc, cc_linear, "LR");
		getLinearpg(textlistlr, lr_linear, "GRA");
		getLinearpg(textlistgra, gra_linear, "P");

	}

	@SuppressWarnings("deprecation")
	private void getLinearpg(List<TextView> list,
			final LinearLayout lay_linear, final String a) {
		int winWidth = getWindowManager().getDefaultDisplay().getWidth();
		list = new ArrayList<TextView>();
		for (int i = 0; i < 10; i++) {
			TextView textview = new TextView(context);

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
				list.add(textview);

			}
			lay_linear.addView(textview);
			textview.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					for (int i = 1; i <= 9; i++) {
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
								aTR = (float) i;
							} else if (a.equals("P")) {
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
					float b = (float) (Math.round(ert * 10)) / 10;// 保留小数点后一位
					if (aTR > 0 && aCC > 0 && aLR > 0 && aGRA > 0) {
						int c = (int) b;
						float d = c;
						if ((b - d) >= 0.25 && (b - d) < 0.75) {
							scoceAll.setText((d + 0.5) + "");
						} else if ((b - d) >= 0.75) {
							scoceAll.setText((d + 1) + "");
						} else {
							scoceAll.setText(d + "");
						}
					} else {
						scoceAll.setText("0.0");
					}
				}
			});
		}
	}

	private void getintent() {
		Intent gmkintent = getIntent();
		examID = gmkintent.getStringExtra("ExamID");
		paperId = gmkintent.getStringExtra("paperId");
		System.out.println("examID" + examID);
	}

	private void initview() {
		context = this;
		listener_mkcorrect_finished = (Button) findViewById(R.id.listener_mkcorrect_finished);
		listener_mkcorrect_finished.setOnClickListener(this);

		tv_mktime_self1 = (TextView) findViewById(R.id.tv_mktime_self1);
		tv_mktime_self2 = (TextView) findViewById(R.id.tv_mktime_self2);
		tv_mktime_self3 = (TextView) findViewById(R.id.tv_mktime_self3);

		play_img_one = (ImageView) findViewById(R.id.listener_correct_mkstart_img1);
		play_img_tow = (ImageView) findViewById(R.id.listener_correct_mkstart_img2);
		play_img_three = (ImageView) findViewById(R.id.listener_correct_mkstart_img3);
		play_img_one.setOnClickListener(this);
		play_img_tow.setOnClickListener(this);
		play_img_three.setOnClickListener(this);
		mTvSelf1 = (TextView) findViewById(R.id.tv_mk_self1);
		mTvSelf2 = (TextView) findViewById(R.id.tv_mk_self2);
		mTvSelf3 = (TextView) findViewById(R.id.tv_mk_self3);
		tr_linear = (LinearLayout) findViewById(R.id.score_tr_mklinear);
		cc_linear = (LinearLayout) findViewById(R.id.score_cc_mklinear);
		lr_linear = (LinearLayout) findViewById(R.id.score_lr_mklinear);
		gra_linear = (LinearLayout) findViewById(R.id.score_gra_mklinear);
		scoceAll = (TextView) findViewById(R.id.correct_mktotal_score);
		imgback = (ImageButton) findViewById(R.id.listener_correct_back_img);
		imgback.setOnClickListener(this);
		seekbar_one = (SeekBar) findViewById(R.id.seekbar1_mk_self1);
		mplayer_one = new Player(seekbar_one, play_img_one);
		// mplayer_one.playUrl(path);
		seekbar_one.setOnSeekBarChangeListener(new SeekBarChangeEvent(
				mplayer_one, mTvSelf1));

		seekbar_tow = (SeekBar) findViewById(R.id.seekbar1_mk_self2);
		mplayer_tow = new Player(seekbar_tow, play_img_tow);
		// mplayer_tow.playUrl(path2);
		seekbar_tow.setOnSeekBarChangeListener(new SeekBarChangeEvent(
				mplayer_tow, mTvSelf2));

		seekbar_three = (SeekBar) findViewById(R.id.seekbar1_mk_self3);
		mplayer_three = new Player(seekbar_three, play_img_three);
		// mplayer_three.playUrl(path3);
		seekbar_three.setOnSeekBarChangeListener(new SeekBarChangeEvent(
				mplayer_three, mTvSelf3));

		// 实例化share
		share = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		System.out.println("-" + url + "-" + examID + "-"
				+ share.getString("Token", ""));

	}

	/**
	 * 网络请求
	 */
	public void getHttp() {
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("examInfoId", examID);
		params.addBodyParameter("paperId", paperId);

		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			private List<String> playList;

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				System.out.println(result);
				try {
					JSONObject obj = new JSONObject(result);
					JSONObject Data = obj.getJSONObject("Data");
					if (obj.getString("Result").equals("true")) {
						JSONArray examCorrectSpeakList = Data
								.getJSONArray("examCorrectSpeakList");
						playList = new ArrayList<String>();
						for (int i = 0; i < examCorrectSpeakList.length(); i++) {
							JSONObject itemObj = examCorrectSpeakList
									.getJSONObject(i);
							String AnswerContent = itemObj
									.getString("AnswerContent");
							// String examid = itemObj.getString("EXA_ID");
							examAnswerIds += itemObj.getString("EXA_ID") + ",";
							ex_ID = itemObj.getString("Ex_ID") ;
							playList.add(AnswerContent);
						}
						if (playList.get(0).length() > 0
								&& !"null".equals(playList.get(0))
								&& !"undefined".equals(playList.get(0))) {
							mplayer_one.playUrl(playList.get(0));
							tv_mktime_self1.setText("/"
									+ toTime(mplayer_one.mediaPlayer
											.getDuration()));
						}

						if (playList.get(1).length() > 0
								&& !"null".equals(playList.get(1))
								&& !"undefined".equals(playList.get(1))) {
							mplayer_tow.playUrl(playList.get(1));
							tv_mktime_self2.setText("/"
									+ toTime(mplayer_tow.mediaPlayer
											.getDuration()));
						}
						if (playList.get(2).length() > 0
								&& !"null".equals(playList.get(2))
								&& !"undefined".equals(playList.get(2))) {
							mplayer_three.playUrl(playList.get(2));
							tv_mktime_self3.setText("/"
									+ toTime(mplayer_three.mediaPlayer
											.getDuration()));
						}

					} else {
						Toast.makeText(context, obj.getString("Infomation"), 0)
								.show();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	/**
	 * 控制播放
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.listener_correct_mkstart_img1:

			if (mplayer_one.mediaPlayer.isPlaying()) {
				mplayer_one.pause();
				play_img_one.setBackgroundResource(R.drawable.bofang);
			} else {
				mplayer_one.play();
				if (true == mplayer_tow.mediaPlayer.isPlaying()) {
					mplayer_tow.pause();
				}
				if (true == mplayer_three.mediaPlayer.isPlaying()) {
					mplayer_three.pause();
				}
				play_img_one.setBackgroundResource(R.drawable.tingzhi);
				play_img_tow.setBackgroundResource(R.drawable.bofang);
				play_img_three.setBackgroundResource(R.drawable.bofang);
			}

			/*
			 * PLAYER_ONE++; if (PLAYER_ONE % 2 == 0) { mplayer_one.play(); if
			 * (true == mplayer_tow.mediaPlayer.isPlaying()){
			 * mplayer_tow.pause(); PLAYER_TOW++; } if (true ==
			 * mplayer_three.mediaPlayer.isPlaying()){ mplayer_three.pause();
			 * PLAYER_THREE++; }
			 * play_img_one.setBackgroundResource(R.drawable.tingzhi);
			 * play_img_tow.setBackgroundResource(R.drawable.bofang);
			 * play_img_three.setBackgroundResource(R.drawable.bofang); } else {
			 * play_img_one.setBackgroundResource(R.drawable.bofang);
			 * mplayer_one.pause(); }
			 */
			break;
		case R.id.listener_correct_mkstart_img2:
			if (mplayer_tow.mediaPlayer.isPlaying()) {
				mplayer_tow.pause();
				play_img_tow.setBackgroundResource(R.drawable.bofang);
			} else {
				mplayer_tow.play();
				if (true == mplayer_one.mediaPlayer.isPlaying()) {
					mplayer_one.pause();
				}
				if (true == mplayer_three.mediaPlayer.isPlaying()) {
					mplayer_three.pause();
				}
				play_img_tow.setBackgroundResource(R.drawable.tingzhi);
				play_img_one.setBackgroundResource(R.drawable.bofang);
				play_img_three.setBackgroundResource(R.drawable.bofang);
			}
			/*
			 * PLAYER_TOW++; if (PLAYER_TOW % 2 == 0) { mplayer_tow.play(); if
			 * (true == mplayer_one.mediaPlayer.isPlaying()){
			 * mplayer_one.pause(); PLAYER_ONE++; } if (true ==
			 * mplayer_three.mediaPlayer.isPlaying()){ mplayer_three.pause();
			 * PLAYER_THREE++; }
			 * play_img_tow.setBackgroundResource(R.drawable.tingzhi);
			 * play_img_one.setBackgroundResource(R.drawable.bofang);
			 * play_img_three.setBackgroundResource(R.drawable.bofang);
			 * 
			 * } else { play_img_tow.setBackgroundResource(R.drawable.bofang);
			 * mplayer_tow.pause(); }
			 */
			break;
		case R.id.listener_correct_mkstart_img3:

			if (mplayer_three.mediaPlayer.isPlaying()) {
				mplayer_three.pause();
				play_img_three.setBackgroundResource(R.drawable.bofang);
			} else {
				mplayer_three.play();
				if (true == mplayer_one.mediaPlayer.isPlaying()) {
					mplayer_one.pause();
				}
				if (true == mplayer_tow.mediaPlayer.isPlaying()) {
					mplayer_tow.pause();
				}
				play_img_tow.setBackgroundResource(R.drawable.bofang);
				play_img_one.setBackgroundResource(R.drawable.bofang);
				play_img_three.setBackgroundResource(R.drawable.tingzhi);
			}
			/*
			 * 
			 * PLAYER_THREE++; if (PLAYER_THREE % 2 == 0) {
			 * mplayer_three.play(); if (true ==
			 * mplayer_one.mediaPlayer.isPlaying()){ mplayer_one.pause();
			 * PLAYER_ONE++; } if (true == mplayer_tow.mediaPlayer.isPlaying()){
			 * mplayer_tow.pause(); PLAYER_TOW++; }
			 * play_img_three.setBackgroundResource(R.drawable.tingzhi);
			 * play_img_one.setBackgroundResource(R.drawable.bofang);
			 * play_img_tow.setBackgroundResource(R.drawable.bofang); } else {
			 * play_img_three.setBackgroundResource(R.drawable.bofang);
			 * mplayer_three.pause();; }
			 */
			break;
		case R.id.listener_correct_back_img:
			if (true == mplayer_one.mediaPlayer.isPlaying()) {
				mplayer_one.stop();
			}
			if (true == mplayer_tow.mediaPlayer.isPlaying()) {
				mplayer_tow.stop();
			}
			if (true == mplayer_tow.mediaPlayer.isPlaying()) {
				mplayer_three.stop();
			}
			finish();
			break;
		case R.id.listener_mkcorrect_finished:
			if (!scoceAll.getText().equals("0.0")) {
				getfinishHttp();

			} else {
				Toast.makeText(context, "批改未完成", 0).show();
			}
		}
	}

	/**
	 * 完成批改接口
	 */
	private void getfinishHttp() {
		String finishurl = Constants.URL_TeacherClassesfinishKyCorrecting;
		String newexamAnswerIds = examAnswerIds.substring(0,
				examAnswerIds.length() - 1);
		String newex_ID = ex_ID;
		// > 0 && > 0 && > 0 && > 0
		int itra = (int) aTR;
		int ilra = (int) aLR;
		int igraa = (int) aGRA;
		int icca = (int) aCC;
		RequestParams params = new RequestParams();
		System.out.println("EXA_ID-----" + newexamAnswerIds);
		System.out.println("ex_ID-----" + newex_ID + "--" + itra + ilra + igraa
				+ icca);
		System.out.println(share.getString("Token", ""));
		params.addHeader("Authentication", share.getString("Token", ""));
		params.addBodyParameter("examAnswerIds", newexamAnswerIds);
		params.addBodyParameter("examInfoId", newex_ID);
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
							System.out.println("Result--"+Result);
							if (Result.equalsIgnoreCase("true")) {
								Toast.makeText(context, "完成批改", 0).show();
								finish();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
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
	 * seekbar1进度监听
	 * 
	 * @author Administrator
	 *
	 */
	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
		int progre;
		Player player;
		TextView tv;

		public SeekBarChangeEvent(Player player, TextView tv) {
			super();
			this.player = player;
			this.tv = tv;
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			this.progre = progress * player.mediaPlayer.getDuration()
					/ seekBar.getMax();
			// this.progre = player.mediaPlayer.getCurrentPosition();
			tv.setText(toTime(progre));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
			player.mediaPlayer.seekTo(progre);
		}
	}

}
