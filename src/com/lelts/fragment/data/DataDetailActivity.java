package com.lelts.fragment.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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

@SuppressLint("SetJavaScriptEnabled")
public class DataDetailActivity extends Activity {

	private static final String TAG = "DataDetailActivity";

	private ImageButton imageview_back;
	private TextView textview_data_collect; // 视频名称
	private TextView textview_data_type;// 资料类型
	private TextView textview_data_teacher_name;// 老师名字
	private TextView textview_data_createtime;// 创建 时间
	private TextView textview_data_looknum;// 浏览次数
	private TextView textview_data_name;// 视频名称
	
	private String token;
	private String url_video = "";
	private String mate_id;
	private String MF_ID;
	private String sT_ID;
	private String readcount;
	private String name;
	private String videoThumbnail;

	private String optType;
	// 视频的详情
	private String path = Constants.URL_STUDYONLINE_LOOKVIDEOINFO;
	private String url = Constants.URL_STUDYONLINE_MEDIA_LOOKUPVIDEO;

	private LodDialogClass lodclass;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_detail);
		ExitApplication.getInstance().addActivity(this);
		init();
		
		getdatafromshare();
		getdatafromintent();
		getVideoSrc();
		getdatafrovideo();
		
		initview();

	}

	@Override
	protected void onPause() {
		// webview_public_class_video.reload();

		super.onPause();
	}

	private void init() {
		textview_data_collect = (TextView) findViewById(R.id.textview_data_collect);
		textview_data_name = (TextView) findViewById(R.id.textview_data_name);
		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		textview_data_type = (TextView) findViewById(R.id.textView7);
		textview_data_teacher_name = (TextView) findViewById(R.id.textview_data_teacher_name);
		textview_data_createtime = (TextView) findViewById(R.id.textview_data_createtime);
		textview_data_looknum = (TextView) findViewById(R.id.textview_data_looknum);
		LodDialogClass.showCustomCircleProgressDialog(DataDetailActivity.this,
		  null, getString(R.string.common_Loading));
		 
		surfaceView = (SurfaceView) this.findViewById(R.id.surfaceView1);

		ImageView btn_play_pause = (ImageView) this.findViewById(R.id.btn_play_pause);
		//btn_play_pause.setOnClickListener(new ClickEvent());
		//btn_alterscreen = (ImageButton) this.findViewById(R.id.btn_alterscreen);
		//btn_alterscreen.setOnClickListener(new ClickEvent());
		rl_controller_play = (RelativeLayout) this.findViewById(R.id.rl_controller_play);
		rl_controller_play.setOnClickListener(new ClickEvent());
		rl_alter = (RelativeLayout) this.findViewById(R.id.rl_alter);
		rl_alter.setOnClickListener(new ClickEvent());
		
		skbProgress = (SeekBar) this.findViewById(R.id.skbProgress);
		skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		player = new Player(this, surfaceView, skbProgress, btn_play_pause,lodclass);
	}

	class ClickEvent implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			if (arg0 == rl_controller_play) {
				System.out.println("url_video alter : " + player.isPlaying());
				if (player.isPlaying()) {
					//btn_play_pause.setBackgroundResource(R.drawable.play);
					player.pause();
				} else {
					//btn_play_pause.setBackgroundResource(R.drawable.pause);
					player.start();
				}
				
			} else if (arg0 == rl_alter) {
				
				FrameLayout fl_1 = (FrameLayout) findViewById(R.id.fl_1);
				LinearLayout ll_info = (LinearLayout) findViewById(R.id.ll_info);
				RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				ImageView btn_alterscreen = (ImageView) findViewById(R.id.btn_alterscreen);
				if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					ll_info.setVisibility(View.VISIBLE);
					layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.rl_2);
					layoutParams.height = (int) getResources().getDimension(R.dimen.video_height);
					fl_1.setLayoutParams(layoutParams);
					btn_alterscreen.setBackgroundResource(R.drawable.alterscreen);
				} else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		            fl_1.setLayoutParams(layoutParams);
		            ll_info.setVisibility(View.GONE);
		            btn_alterscreen.setBackgroundResource(R.drawable.alterscreenin);
				}
				/*if (fl_1.getLayoutParams().height < 0) {
					// to normal
					ll_info.setVisibility(View.VISIBLE);
					layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.rl_2);
					layoutParams.height = (int) getResources().getDimension(R.dimen.video_height);
					fl_1.setLayoutParams(layoutParams);
				} else {
		            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		            fl_1.setLayoutParams(layoutParams);
		            ll_info.setVisibility(View.GONE);
				}*/
			}

		}
	}

	class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
		int progress;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
			this.progress = progress * player.mediaPlayer.getDuration()
					/ seekBar.getMax();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
			player.mediaPlayer.seekTo(progress);
		}
	}

	@SuppressWarnings("static-access")
	private void initview() {

		imageview_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("click finish");
				if (null != player) {
					player.finish();
				}
				finish();
			}
		});
		textview_data_collect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("clieck collect");
				if (optType.endsWith("1")) {
					LodDialogClass.showCustomCircleProgressDialog(DataDetailActivity.this, "","收藏中...");
				}else{
					LodDialogClass.showCustomCircleProgressDialog(DataDetailActivity.this, "","取消收藏中...");
				}
				setcollectdata();
			}
		});

	}

	/*
	 * private void loadVideo(String url) { Uri uri = Uri.parse(url);
	 * video_public_class_video.setMediaController(new MediaController(this));
	 * video_public_class_video.setVideoURI(uri);
	 * video_public_class_video.requestFocus();
	 * video_public_class_video.start();
	 * video_public_class_video.setOnPreparedListener(new OnPreparedListener() {
	 * 
	 * @Override public void onPrepared(MediaPlayer mp) { // TODO Auto-generated
	 * method stub System.out.println("prepare..ok");
	 * RelativeLayout.LayoutParams layoutParams= new
	 * RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
	 * RelativeLayout.LayoutParams.WRAP_CONTENT);
	 * //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	 * layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	 * layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	 * layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	 * video_public_class_video.setLayoutParams(layoutParams); } }); }
	 */

	private SurfaceView surfaceView;
	//private ImageButton btn_play_pause, btn_alterscreen;
	private RelativeLayout rl_controller_play, rl_alter;
	private SeekBar skbProgress;
	private Player player;

	private void getdatafromshare() {
		SharedPreferences share = DataDetailActivity.this.getSharedPreferences(
				"userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");

		Log.d(TAG, "获取的token数值为=====" + token);

	}

	private void getdatafromintent() {
		Bundle b = getIntent().getExtras();
		mate_id = b.getString("mate_id");
		MF_ID = b.getString("MF_ID");
		sT_ID = b.getString("ST_ID");
		name = b.getString("name");

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
		url_video = b.getString("url");

	}

	/**
	 * 收藏功能
	 * */
	private void setcollectdata() {

		Log.d(TAG, "getdatafromnet()执行" + "mateId" + mate_id + "optType=="
				+ optType);

		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_COLLECT_DATA + "?mateId="
				+ mate_id + "&optType=" + optType + "&ST_ID=" + sT_ID;

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		// params.addBodyParameter("pageIndex", pageIndex);
		// params.addBodyParameter("mateId", mate_id);
		// params.addBodyParameter("optType", optType);

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);
		http.send(HttpRequest.HttpMethod.GET, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						Log.d(TAG, "getdatafromnet()执行之接收数据成功");

						Log.d(TAG, "收藏资料的数据结果为=========" + responseInfo.result);

						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息

							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							if (Result.equalsIgnoreCase("true")) {
								if (optType.equalsIgnoreCase("0")) {
									Toast.makeText(DataDetailActivity.this,
											"取消收藏成功", 2).show();
									optType = "1";
									textview_data_collect.setText("收藏");
									LodDialogClass.closeCustomCircleProgressDialog();
								} else {
									Toast.makeText(DataDetailActivity.this,
											"收藏成功", Toast.LENGTH_SHORT).show();
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

	// 获取视频的信息
	private void getdatafrovideo() {

		@SuppressWarnings("static-access")
		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		params.addBodyParameter("mateId", mate_id);

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
							System.out.println("获取的视频的数据===" + str);
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
								textview_data_name.setText(materialsName);
								textview_data_createtime.setText(CreateTime);
								textview_data_type.setText(Name);
								textview_data_teacher_name.setText(sName);
								textview_data_looknum.setText(ReadCount);

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure");
						LodDialogClass.closeCustomCircleProgressDialog();
					}

				});

	}
	
	private void getVideoSrc() {

		@SuppressWarnings("static-access")
		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		params.addBodyParameter("mId", mate_id);

		HttpUtils http = new HttpUtils();

		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息
							System.out.println("获取的视频的数据===" + str);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");

							JSONObject obj = new JSONObject(Data);
							url_video = obj.getString("videoUrl");
							player.playUrl(url_video);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("onFailure");
						LodDialogClass.closeCustomCircleProgressDialog();
					}

				});

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				FrameLayout fl_1 = (FrameLayout) findViewById(R.id.fl_1);
				LinearLayout ll_info = (LinearLayout) findViewById(R.id.ll_info);
				ImageView btn_alterscreen = (ImageView) findViewById(R.id.btn_alterscreen);
				RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				ll_info.setVisibility(View.VISIBLE);
				layoutParams.addRule(RelativeLayout.ALIGN_TOP, R.id.rl_2);
				layoutParams.height = (int) getResources().getDimension(R.dimen.video_height);
				fl_1.setLayoutParams(layoutParams);
				btn_alterscreen.setBackgroundResource(R.drawable.alterscreen);
				return true;
			}
			if (null != player) {
				player.finish();
			}
			finish();
		}
		return false;
	}

}
