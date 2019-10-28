package com.lelts.fragment.data;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.drocode.swithcer.GuideGallery;
import com.drocode.swithcer.ImageAdapter;
import com.example.hello.R;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lelts.fragment.data.adapter.DatadataAdapter;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 资料页
 */
public class DataDFm extends Fragment implements OnClickListener {

	private static final String TAG = "DataDFm";

	private View mview;
	private RelativeLayout relative_data_nulldata;
	private PullToRefreshListView listview_data_main;

	private List<HashMap<String, Object>> list;
	private DatadataAdapter adapter;

	private TextView textview_data_screening;

	private String token;

	// private String index = "1";

	private int index_s = 1;

	// 获取数据资料
	private List<HashMap<String, Object>> l_map = new ArrayList<HashMap<String, Object>>();
	// 广告图片的 数组
	private List<HashMap<String, Object>> l_images = new ArrayList<HashMap<String, Object>>();

	private String images[];

	private String pageIndex = "1";

	private BitmapUtils bitutils;

	// private
	public HashMap<String, Bitmap> imagesCache = new HashMap<String, Bitmap>();
	public List<String> urls;
	public GuideGallery images_ga;
	private int positon = 0;
	private Thread timeThread = null;
	public boolean timeFlag = true;
	private boolean isExit = false;
	public ImageTimerTask timeTaks = null;
	Uri uri;
	Intent intent;
	int gallerypisition = 0;

	private LinearLayout pointLinear;

	// 筛选 的 必须要的参数 方倒数阻力
	private String[] str_screen;
	private boolean isScreen = true;

	String str_code = "0";
	String str1_code = "0";
	String str2_code = "0";
	String str3_code = "0";
	//判断数据是否全部加载完成
	private boolean fresh = false;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	final Handler autoGalleryHandler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			switch (message.what) {
			case 1:
				images_ga.setSelection(message.getData().getInt("pos"));
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mview = inflater.inflate(R.layout.fragment_data_main, null);
		// 设置广告的组件和参数
		init();
		// 设置 广告为
		initadv();

		initview();
		bitutils = new BitmapUtils(getActivity());
		getdatafromshare();

		str_screen = new String[4];
		str_screen[0] = str_code;
		str_screen[1] = str1_code;
		str_screen[2] = str2_code;
		str_screen[3] = str3_code;

		// getdatafromnet(1);
		getdatafromnetforscreen(str_screen, index_s);

		getdatafromnetforimages();

		return mview;
	}

	private void init() {

		relative_data_nulldata = (RelativeLayout) mview
				.findViewById(R.id.relative_data_nulldata);

		images_ga = (GuideGallery) mview.findViewById(R.id.image_wall_gallery);
		images_ga.setImageActivity(DataDFm.this);
		pointLinear = (LinearLayout) mview
				.findViewById(R.id.gallery_point_linear);
		// pointLinear.setBackgroundColor(Color.argb(200, 135, 135, 152));

	}

	/**
	 * 设置广告位
	 * */
	private void initadv() {
		timeTaks = new ImageTimerTask();
		autoGallery.scheduleAtFixedRate(timeTaks, 5000, 5000);
		timeThread = new Thread() {
			public void run() {
				while (!isExit) {
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					synchronized (timeTaks) {
						if (!timeFlag) {
							timeTaks.timeCondition = true;
							timeTaks.notifyAll();
						}
					}
					timeFlag = true;
				}
			};
		};
		timeThread.start();
	}

	private void getdatafromnetforimages() {

		@SuppressWarnings("static-access")
		String url = new Constants().URL_MYSELF_LOADADVERTISEMENTS;
		// String url =
		// "http://testielts2.staff.xdf.cn/IELTS_2/api/onlinestudy/teacherMaterials?pageIndex=1";

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		// type = [用户类型，1001:教师、1002:学生、1003:预测,举例:"1003",不可为空]
		params.addBodyParameter("type", "1001");

		// params.addBodyParameter("pageIndex", "1");

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

						Log.d("DataDFm", "获取资料广告的数据" + responseInfo.result);
						System.out.println("获取资料广告的数据  " + responseInfo.result);

						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息

							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							JSONObject obj_d = new JSONObject(Data);
							String llist = obj_d.getString("list");
							if (!llist.equalsIgnoreCase("")) {
								JSONArray array = new JSONArray(llist);
								images = new String[array.length()];
								for (int i = 0; i < array.length(); i++) {
									JSONObject obj = array.optJSONObject(i);
									HashMap<String, Object> map = new HashMap<String, Object>();

									// Sort = 广告序号;
									//   Interval = 时间间隔，每隔多少秒切换广告;
									//   Title = 广告标题;
									//   Picture = 广告封面，图片名称;
									//   Link = 广告地址;
									//   Content = 广告正文;

									map.put("Sort", obj.getString("Sort"));
									map.put("Interval",
											obj.getString("Interval"));
									map.put("Title", obj.getString("Title"));
									map.put("Picture", obj.getString("Picture"));
									map.put("Link", obj.getString("Link"));
									map.put("Content", obj.getString("Content"));
									// if(obj.getString("Picture").equalsIgnoreCase("null")){
									// images[i] =
									// "http://www.apkbus.com/template/devc/style/logo.png";
									// }else{
									// images[i] = new
									// Constants().URL_IMAGE_ADVERT
									// + obj.getString("Picture")
									// .toString();
									images[i] = obj.getString("Picture")
											.toString();
									// }

									l_images.add(map);
								}

							}
							InitViewPager(l_images);

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

	// 加载轮播图片
	protected void InitViewPager(final List<HashMap<String, Object>> l_images2) {

		ImageAdapter imageAdapter = new ImageAdapter(getActivity(),
				DataDFm.this, images);
		images_ga.setAdapter(imageAdapter);

		for (int i = 0; i < l_images2.size(); i++) {
			ImageView pointView = new ImageView(getActivity());
			if (i == 0) {
				pointView.setBackgroundResource(R.drawable.feature_point_cur);
			} else
				pointView.setBackgroundResource(R.drawable.feature_point);
			pointLinear.addView(pointView);
		}
		images_ga.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), StudyADVDetailActivity.class);
				Bundle b = new Bundle();
				b.putString("url", l_images2.get(position % l_images2.size())
						.get("Link").toString());
				b.putString("Title", l_images2.get(position % l_images2.size())
						.get("Title").toString());
				b.putString(
						"Content",
						l_images2.get(position % l_images2.size())
								.get("Content").toString());
				intent.putExtras(b);
				startActivity(intent);
			}
		});

	}

	private void initview() {

		textview_data_screening = (TextView) mview
				.findViewById(R.id.textview_data_screening);
		textview_data_screening.setOnClickListener(this);

		listview_data_main = (PullToRefreshListView) mview
				.findViewById(R.id.listview_data_main);

		// initdata();
		// 点击跳转， 不同的情况
		listview_data_main.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// position =position+1;
				position--;
				addreadcount(l_map.get(position).get("mate_id").toString());
				addHttps(l_map.get(position).get("mate_id").toString());
				intent = new Intent();
				// StorePoint 1资料，2视频
				if (l_map.get(position).get("StorePoint").toString()
						.equals("1")) {
					intent.setClass(getActivity(),
							DataForDocxDetailActivity.class);
				} else {
					// 视频
					intent.setClass(getActivity(), DataDetailActivity.class);
				}
				Bundle b = new Bundle();
				b.putString("ST_ID", l_map.get(position).get("ST_ID").toString());
				b.putString("mate_id", l_map.get(position).get("mate_id")
						.toString());
				b.putString("MF_ID", l_map.get(position).get("MF_ID")
						.toString());
				b.putString("name", l_map.get(position).get("name").toString());
				b.putString("videoThumbnail",
						l_map.get(position).get("videoThumbnail").toString());
				b.putString("url", l_map.get(position).get("url").toString());

				intent.putExtras(b);

				getActivity().startActivity(intent);

			}

			
		});

		listview_data_main
				.setMode(com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH);

		listview_data_main
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						Log.e("TAG", "onPullDownToRefresh");

						setMyLable(refreshView);
						// 这里写下拉刷新的任务
						index_s = 1;
						getdatafromnetforscreen(str_screen, index_s);
						new FinishRefresh(listview_data_main).execute();

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> arg0) {
						setMyLable(arg0);
						 index_s ++;
						getdatafromnetforscreen(str_screen, index_s);
						new FinishRefresh(listview_data_main).execute();

					}
				});
		LodDialogClass.showCustomCircleProgressDialog(getActivity(), null,
				getString(R.string.common_Loading));

	}
	private void addHttps(String mateId) {
		// TODO Auto-generated method stub
		String url = Constants.URL_Material_lookUpMaterials;
		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		// params.addBodyParameter("pageIndex", pageIndex);
		params.addBodyParameter("mateId", mateId);
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				String result = arg0.result;
				try {
					JSONObject obj = new JSONObject(result);
					String Result = obj.getString("Result");
					System.out.println(result);
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
	// 设置刷新时间
	private void setMyLable(PullToRefreshBase<ListView> refreshView) {
		String label = DateUtils.formatDateTime(getActivity(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
	}

	/**
	 * 自定义上拉刷新和下拉加载的显示文字
	 */
	private void initIndicator(PullToRefreshListView listview) {
		ILoadingLayout endLabel = listview.getLoadingLayoutProxy(true, false);

		endLabel.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
		endLabel.setRefreshingLabel("加载中。。。");// 刷新时
		endLabel.setReleaseLabel("放开以刷新");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = listview.getLoadingLayoutProxy(false, true);
		System.out.println("fresh====="+fresh);
		if (fresh == true) {
			endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
			endLabels.setRefreshingLabel("全部加载完毕");// 刷新时
			endLabels.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示
		} else {
			endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
			endLabels.setRefreshingLabel("加载中。。。");// 刷新时
			endLabels.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示
		}

	}

	private class FinishRefresh extends AsyncTask<Void, Void, Void> {
		private PullToRefreshListView listview;

		public FinishRefresh(PullToRefreshListView listview) {
			super();
			this.listview = listview;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// adapter.notifyDataSetChanged();
			listview.onRefreshComplete();
		}
	}

	private void getdatafromshare() {
		SharedPreferences share = getActivity().getSharedPreferences(
				"userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");

		Log.d("StudyPlanActivity", "获取的token数值为=====" + token);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();

		index_s = 1;
		// if(isScreen){
		// getdatafromnet(1);
		// }else{
		getdatafromnetforscreen(str_screen, index_s);
		// }

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textview_data_screening:
			Intent intent = new Intent();
			intent.setClass(getActivity(), DataScreenActivity.class);

			Bundle b = new Bundle();
			b.putString("fileType", str_code);
			b.putString("nameCode", str1_code);
			b.putString("roleId", str2_code);
			b.putString("uploadYear", str3_code);

			intent.putExtras(b);

			startActivityForResult(intent, 11);

			break;

		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LodDialogClass.showCustomCircleProgressDialog(getActivity(), null, getString(R.string.common_Loading));
		if (data != null) {
			isScreen = false;

			Bundle b = data.getExtras();
			str_code = b.getString("fileType");
			str1_code = b.getString("nameCode");
			str2_code = b.getString("roleId");
			str3_code = b.getString("uploadYear");
			str_screen = new String[4];
			str_screen[0] = str_code;
			str_screen[1] = str1_code;
			str_screen[2] = str2_code;
			str_screen[3] = str3_code;

			index_s = 1;
			getdatafromnetforscreen(str_screen, index_s);

		}
	}

	private void getdatafromnetforscreen(String[] str, final int index) {

		String url = new Constants().URL_Teacher_DATE_SCREEN;

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		params.addBodyParameter("fileType", str[0]);
		params.addBodyParameter("nameCode", str[1]);
		params.addBodyParameter("roleId", str[2]);
		params.addBodyParameter("uploadYear", str[3]);

		params.addBodyParameter("pageIndex", String.valueOf(index));

		HttpUtils http = new HttpUtils();
		// http.configCurrentHttpCacheExpiry(1000 * 10);

		System.out.println("paams的数据为===" + params.getCharset());

		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						Log.d("DataDFm", "获取资料的数据" + responseInfo.result);

						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息

							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");

							Log.d("DataDFm", "获取资料的数据" + responseInfo.result
									+ "data====" + Data);

							if (index == 1) {
								l_map = new ArrayList<HashMap<String, Object>>();
							}

							if (!Data.equalsIgnoreCase("[]")) {
								JSONArray array = new JSONArray(Data);
								for (int i = 0; i < array.length(); i++) {
									JSONObject obj = array.optJSONObject(i);
									HashMap<String, Object> map = new HashMap<String, Object>();

									// filetype = 文件类型（doc等）
									//   uid = 用户ID
									//   RoleID = 角色ID，1集团2个人
									//   name = 标题
									//   mate_id = 材料ID
									//   type = 分类标签（听力等）
									//   createtime = 创建日期
									//   url = 点击打开网址

									//   StorePoint = 存储点 1:够快网盘; 2:CC视频
									//   ReadCount = 被查看次数
									//   VideoDuration = 视频长度（分钟）
									//   VideoThumbNail = 视频截图

									// createtime: 1432023681000,
									// uid: 12,
									// mate_id: 305329,
									// name: 雅思试卷与试题表关系,
									// videoduration: ,
									// RoleID: 2,
									// filetype: docx,
									// readcount: ,
									// type: 听力,
									// StorePoint: 1,
									// url: ,
									// videoThumbnail:

									map.put("filetype",
											obj.getString("filetype"));
									map.put("uid", obj.getString("uid"));
									map.put("RoleID", obj.getString("RoleID"));
									map.put("name", obj.getString("name"));
									map.put("mate_id", obj.getString("mate_id"));
									map.put("type", obj.getString("type"));
									map.put("createtime",
											obj.getString("createtime"));
									map.put("url", obj.getString("url"));
									if (obj.has("ST_ID")) {
										map.put("ST_ID", obj.getString("ST_ID"));
									}else{
										map.put("ST_ID", "0");
									}
									map.put("StorePoint",
											obj.getString("StorePoint"));
									map.put("readcount",
											obj.getString("readcount"));
									map.put("videoduration",
											obj.getString("videoduration"));
									map.put("videoThumbnail",
											obj.getString("videoThumbnail"));
									map.put("MF_ID", obj.getString("MF_ID"));

									l_map.add(map);
								}
							

								if (index == 1) {
									adapter = new DatadataAdapter(
											getActivity(), l_map);
									listview_data_main.setAdapter(adapter);

								} else {
									adapter.notifyDataSetChanged();
								}
								
								Log.d("DataDFm", "获取资料的数据l_map.size()===="
										+ l_map.size());
							}
							Log.d("DataDFm",
									"获取资料的数据l_map.size()====" + l_map.size());

							// adapter.up(l_map);
							if ((l_map.size()/index) < 10) {
								fresh = true;
							}else{
								fresh = false;
							}
							System.out.println("刷新的数据个数======"+(l_map.size()/index)+"index===="+index );
							if (l_map.size() == 0) {
								listview_data_main.setVisibility(View.GONE);
								relative_data_nulldata
										.setVisibility(View.VISIBLE);
							} else {
								listview_data_main.setVisibility(View.VISIBLE);
								relative_data_nulldata.setVisibility(View.GONE);
							}

//							listview_data_main.onRefreshComplete();
							initIndicator(listview_data_main);
							LodDialogClass.closeCustomCircleProgressDialog();
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

	/**
	 * 增加浏览次数
	 * */
	private void addreadcount(String mateId) {

		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_ADDREADCOUNT
				+ "?pageIndex=";

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		// params.addBodyParameter("pageIndex", pageIndex);
		params.addBodyParameter("mateId", mateId);

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
						Log.d(TAG, "增加浏览次数==" + responseInfo.result.toString());
						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息

							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							// if (Result.equalsIgnoreCase("true")) {
							// Toast.makeText(
							// DataPublicclassDetailActivity.this,
							// "收藏成功", Toast.LENGTH_SHORT).show();
							// } else {
							// Toast.makeText(
							// DataPublicclassDetailActivity.this,
							// "失败", 2).show();
							// }

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

	public class ImageTimerTask extends TimerTask {
		public volatile boolean timeCondition = true;

		// int gallerypisition = 0;
		public void run() {
			synchronized (this) {
				while (!timeCondition) {
					try {
						Thread.sleep(100);
						wait();
					} catch (InterruptedException e) {
						Thread.interrupted();
					}
				}
			}
			try {
				gallerypisition = images_ga.getSelectedItemPosition() + 1;
				System.out.println(gallerypisition + "");
				Message msg = new Message();
				Bundle date = new Bundle();
				date.putInt("pos", gallerypisition);
				msg.setData(date);
				msg.what = 1;
				autoGalleryHandler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	Timer autoGallery = new Timer();

	public void changePointView(int cur) {
		pointLinear = (LinearLayout) mview
				.findViewById(R.id.gallery_point_linear);
		View view = pointLinear.getChildAt(positon);
		View curView = pointLinear.getChildAt(cur);
		if (view != null && curView != null) {
			ImageView pointView = (ImageView) view;
			ImageView curPointView = (ImageView) curView;
			pointView.setBackgroundResource(R.drawable.feature_point);
			curPointView.setBackgroundResource(R.drawable.feature_point_cur);
			positon = cur;
		}
	}

	// private class GetDataTask extends
	// AsyncTask<Void, Void, HashMap<String, Object>> {
	//
	// @Override
	// protected HashMap<String, Object> doInBackground(Void... params) {
	//
	// try {
	// Thread.sleep(2000);
	// } catch (InterruptedException e) {
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(HashMap<String, Object> result) {
	// l_map.add(result);
	// adapter.notifyDataSetChanged();
	// listview_data_main.onRefreshComplete();
	// }
	// }
	public static class DateUtil {

		public static Date stringToDate(String dateString) {
			ParsePosition position = new ParsePosition(0);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd");
			Date dateValue = simpleDateFormat.parse(dateString, position);
			return dateValue;
		}

	}

}

// private void getdatafromnet(final int inc) {
//
// Log.d(TAG, "更新的第几页==" + inc);
//
// String url = new Constants().URL_Teacher_DATE + "?pageIndex=" + inc;
// // String url =
// //
// "http://testielts2.staff.xdf.cn/IELTS_2/api/onlinestudy/teacherMaterials?pageIndex=1";
//
// RequestParams params = new RequestParams();
//
// params.addHeader("Authentication", token);// 添加保密的东西
//
// HttpUtils http = new HttpUtils();
// // http.configCurrentHttpCacheExpiry(1000 * 10);
//
// http.send(HttpRequest.HttpMethod.GET, url, params,
// new RequestCallBack<String>() {
//
// @Override
// public void onStart() {
// super.onStart();
//
// }
//
// @Override
// public void onSuccess(ResponseInfo<String> responseInfo) {
//
// Log.d("DataDFm", "获取资料的数据" + responseInfo.result);
//
// try {
// JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息
//
// String Result = str.getString("Result");
// String Infomation = str.getString("Infomation");
// String Data = str.getString("Data");
//
// if (inc == 1) {
// l_map = new ArrayList<HashMap<String, Object>>();
// }
//
// if (!Data.equalsIgnoreCase("")) {
// JSONArray array = new JSONArray(Data);
// for (int i = 0; i < array.length(); i++) {
// JSONObject obj = array.optJSONObject(i);
// HashMap<String, Object> map = new HashMap<String, Object>();
//
// map.put("filetype",
// obj.getString("filetype"));
// map.put("uid", obj.getString("uid"));
// map.put("RoleID", obj.getString("RoleID"));
// map.put("name", obj.getString("name"));
// map.put("mate_id", obj.getString("mate_id"));
// map.put("type", obj.getString("type"));
// map.put("createtime",
// obj.getString("createtime"));
// map.put("url", obj.getString("url"));
//
// map.put("StorePoint",
// obj.getString("StorePoint"));
// map.put("readcount",
// obj.getString("readcount"));
// map.put("videoduration",
// obj.getString("videoduration"));
// map.put("videoThumbnail",
// obj.getString("videoThumbnail"));
//
// map.put("MF_ID", obj.getString("MF_ID"));
//
// l_map.add(map);
// }
// System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
// try {
//
// Collections.sort(l_map,new Comparator<HashMap<String, Object>>() {
//
// @Override
// public int compare(HashMap<String, Object> lhs,
// HashMap<String, Object> rhs) {
//
// Date date1 = DateUtil.stringToDate(lhs.get("createtime").toString());
// Date date2 = DateUtil.stringToDate(rhs.get("createtime").toString());
// if (date1.before(date2)) {
// return 1;
// }
// return -1;
// }
//
// });
// } catch (Exception e) {
// System.out.println("sort_error==========="+e.toString());
// }
//
// if (0 < l_map.size() && l_map.size() <= 10) {
// adapter = new DatadataAdapter(
// getActivity(), l_map);
// listview_data_main.setAdapter(adapter);
// } else if (l_map.size() >= 10) {
// adapter.notifyDataSetChanged();
// }
//
// adapter.up(l_map);
//
// listview_data_main.onRefreshComplete();
// }
//
// } catch (JSONException e) {
// e.printStackTrace();
// }
// }
//
// @Override
// public void onFailure(HttpException error, String msg) {
// System.out.println("onFailure");
// System.out.println(error.toString());
//
// }
//
// });
//
// }
