package com.lels.activity.myself;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.R;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lels.activity.myself.adapter.MyresultAdapter;
import com.lels.activity.myself.adapter.PopGroupAdapter;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 我的收藏
 * **/
public class MyselfResultActivity extends Activity implements OnClickListener {

	private static final String TAG = "MyselfResultActivity";

	private ImageButton imageview_back;
	private PullToRefreshListView listview_myself_result;

	// 求情的参数
	private String token = "";

	private String sName = "";
	private String sCode = "";

	private List<HashMap<String, Object>> l_map_result;
	// , AllList
	private MyresultAdapter adapter;
	private RelativeLayout relative_title;
	private TextView textview_class_name;
	// pop的声明
	private PopupWindow pop_class;
	private ListView lv_group;
	private View view;
	private int myindex;
	// private List<String> groups;
	private List<HashMap<String, Object>> groups;
	// 学生的班级列表
	private String studentresultinfo;

	private String pageIndex = "1";
	private RelativeLayout relative_warn_nulldata_clastudy;
	private boolean flag = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_result);

		init();
		getdatafromshare();
		getdatafromintent();

	}

	private void getdatafromintent() {
		Bundle b = getIntent().getExtras();
		studentresultinfo = b.getString("studentresultinfo");

		System.out.println("传递过来的数据为============" + studentresultinfo);

		groups = new ArrayList<HashMap<String, Object>>();

		if (!studentresultinfo.equalsIgnoreCase("")) {
			try {
				JSONObject obj = new JSONObject(studentresultinfo);

				String list = obj.getString("list");
				JSONArray array = new JSONArray(list);
				myindex = array.length();
				for (int i = 0; i < myindex; i++) {
					JSONObject obj_l = array.optJSONObject(i);
					HashMap<String, Object> map = new HashMap<String, Object>();
					sName = obj_l.getString("sName");
					sCode = obj_l.getString("ID");
					map.put("sCode", sCode);
					map.put("sName", sName);
					groups.add(map);
				}
				// 设置 第一个 sname 和 scode
				sCode = groups.get(0).get("sCode").toString();
				sName = groups.get(0).get("sName").toString();

				textview_class_name.setText(sName);

				getdatafromnet(sCode);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	private void getdatafromshare() {

		SharedPreferences share = MyselfResultActivity.this
				.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");

		Log.d(TAG, "获取的token数值为=====" + token);

	}

	private void getdatafromnet(String str) {

		String url = new Constants().URL_MYSELF_RESULT_STUDENTINFOS;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		params.addBodyParameter("sClassId", str);
		params.addBodyParameter("pageIndex", pageIndex);

		HttpUtils http = new HttpUtils();
		// http.configCurrentHttpCacheExpiry(1000 * 10);

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
							// AllList = new ArrayList<HashMap<String,
							// Object>>();
							if (pageIndex.equals("1")) {

								l_map_result = new ArrayList<HashMap<String, Object>>();
							}

							if (Data != null) {
								JSONObject obj = new JSONObject(Data);
								String list = obj.getString("list");

								JSONArray array = new JSONArray(list);
								if (array.length() > 0) {
									for (int i = 0; i < array.length(); i++) {
										JSONObject obj_l = array
												.optJSONObject(i);
										HashMap<String, Object> map = new HashMap<String, Object>();

										// "sCode": "BJ1255907",
										// "DateDiff": null,
										// "ListenScore": null,
										// "TotalScore": null,
										// "WriteScore": null,

										// "IconUrl": null,
										// "SpeakScore": null,
										// "sName": "刘丁燃",
										// "ReportImgName": null,
										// "ReadScore": null,
										// "nGender": 2

										map.put("sCode",
												obj_l.getString("sCode"));
										map.put("sStudentID",
												obj_l.getString("sStudentID"));
										map.put("DateDiff",
												obj_l.getString("DateDiff"));
										map.put("ListenScore",
												obj_l.getString("ListenScore"));
										map.put("TotalScore",
												obj_l.getString("TotalScore"));
										map.put("WriteScore",
												obj_l.getString("WriteScore"));
										map.put("IconUrl",
												obj_l.getString("IconUrl"));
										map.put("SpeakScore",
												obj_l.getString("SpeakScore"));
										map.put("sName",
												obj_l.getString("sName"));
										map.put("ReportImgName", obj_l
												.getString("ReportImgName"));
										map.put("ReadScore",
												obj_l.getString("ReadScore"));
										map.put("nGender",
												obj_l.getString("nGender"));

										l_map_result.add(map);

									}
									if (l_map_result.size()
											/ Integer.valueOf(pageIndex) < 10) {
										flag = true;
									} else {
										flag = false;
									}
								}

							}
							// 数据为空则提示，listview 消失
							if (l_map_result.size() > 0) {
								listview_myself_result
										.setVisibility(View.VISIBLE);
								relative_warn_nulldata_clastudy
										.setVisibility(View.GONE);
								if (pageIndex.equals("1")) {
									adapter = new MyresultAdapter(
											MyselfResultActivity.this,
											l_map_result);
									listview_myself_result.setAdapter(adapter);
								} else {
									adapter.notifyDataSetChanged();
								}
							} else {
								listview_myself_result.setVisibility(View.GONE);
								relative_warn_nulldata_clastudy
										.setVisibility(View.VISIBLE);
							}

							LodDialogClass.closeCustomCircleProgressDialog();
							System.out.println(l_map_result);
							refresh(listview_myself_result, adapter,
									l_map_result);
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

	private void init() {
		textview_class_name = (TextView) findViewById(R.id.textview_class_name);
		textview_class_name.setOnClickListener(this);

		relative_warn_nulldata_clastudy = (RelativeLayout) findViewById(R.id.relative_warn_nulldata_clastudy);
		relative_title = (RelativeLayout) findViewById(R.id.titile_relative);
		relative_title.setOnClickListener(this);

		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		imageview_back.setOnClickListener(this);

		listview_myself_result = (PullToRefreshListView) findViewById(R.id.listview_myself_result);
		LodDialogClass.showCustomCircleProgressDialog(
				MyselfResultActivity.this, null,
				getString(R.string.common_Loading));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
			finish();
			break;
		case R.id.titile_relative:
			showpop();
			break;
		default:
			break;
		}
	}

	private class FinishRefresh extends AsyncTask<Void, Void, Void> {
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
			listview_myself_result.onRefreshComplete();
		}
	}

	public void refresh(final PullToRefreshListView listview,
			final MyresultAdapter adapter,
			final List<HashMap<String, Object>> list) {
		listview.setMode(Mode.BOTH);
		initIndicator();
		listview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// pageIndex.equals("1");
				pageIndex = "1";
				getdatafromnet(sCode);

				new FinishRefresh().execute();
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {

				int x = Integer.parseInt(pageIndex);
				x++;
				pageIndex = String.valueOf(x);
				getdatafromnet(sCode);
				new FinishRefresh().execute();
				adapter.notifyDataSetChanged();
			}
		});

	}

	private void initIndicator() {
		ILoadingLayout startLabels = listview_myself_result
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在刷新...");// 刷新时
		startLabels.setReleaseLabel("放开以加载");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = listview_myself_result
				.getLoadingLayoutProxy(false, true);
		if (flag == true) {
			endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
			endLabels.setRefreshingLabel("数据全部加载完成");// 刷新时
			endLabels.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示
		} else {
			endLabels.setPullLabel("上拉加载");// 刚下拉时，显示的提示
			endLabels.setRefreshingLabel("加载中...");// 刷新时
			endLabels.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示
		}
	}

	private void showpop() {
		if (pop_class == null) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.pop_myself_result_class, null);
			lv_group = (ListView) view
					.findViewById(R.id.pop_listview_myself_result_class);

			PopGroupAdapter groupAdapter = new PopGroupAdapter(this, groups);
			lv_group.setAdapter(groupAdapter);

			// 创建一个PopuWidow对象
			pop_class = new PopupWindow(view, LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT);
		}
		// 使其聚集
		pop_class.setFocusable(true);
		// 设置允许在外点击消失
		pop_class.setOutsideTouchable(true);

		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		pop_class.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- pop_class.getWidth() / 2;
		Log.i("coder", "xPos:" + xPos);

		pop_class.showAsDropDown(textview_class_name, xPos, 0);

		lv_group.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				textview_class_name.setText(groups.get(position).get("sName")
						.toString());
				sCode = groups.get(position).get("sCode").toString();
				// pageIndex.equals("1");
				pageIndex = "1";
				getdatafromnet(sCode);

				if (pop_class != null) {
					pop_class.dismiss();
				}
			}
		});
	}
}
