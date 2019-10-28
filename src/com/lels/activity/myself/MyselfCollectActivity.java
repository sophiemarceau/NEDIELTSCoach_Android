package com.lels.activity.myself;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.example.hello.R;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lels.activity.myself.adapter.MycollectAdapter;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lelts.fragment.data.DataDetailActivity;
import com.lelts.fragment.data.DataForDocxDetailActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * 我的收藏
 **/
public class MyselfCollectActivity extends Activity implements OnClickListener {

	private static final String TAG = "MyselfCollectActivity";

	private ImageView imageview_back;
	private PullToRefreshListView listview_myself_collect;

	private List<HashMap<String, Object>> l_map_collect;
	private MycollectAdapter adapter;

	private int index = 1;
	private String token = "";
	// 文件的类型
	private String filetype;
	// 判断时候数据全部加载成功
	private boolean flag = false;

	private RelativeLayout relative_warn_nulldata_clastudy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_collect);

		init();
		getshare();
		getdatafromnet(index);
	}

	private void getshare() {
		SharedPreferences share = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");

		Log.d("StudyPlanActivity", "获取的token数值为=====" + token);

	}

	private void getdatafromnet(final int index) {

		String url = new Constants().URL_MYSELF_MYCOLLECT + "?pageIndex=" + index;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		HttpUtils http = new HttpUtils();
		// http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				try {
					Log.d(TAG, "解析获取我的收藏列表" + responseInfo.result);
					JSONObject str = new JSONObject(responseInfo.result);
					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					String Data = str.getString("Data");
					JSONArray array = new JSONArray(Data);

					if (index == 1) {
						l_map_collect = new ArrayList<HashMap<String, Object>>();
					}

					if (array.length() > 0) {

						for (int i = 0; i < array.length(); i++) {
							JSONObject obj = array.optJSONObject(i);

							// "NickName": null,
							// "RoleID": 1,
							// "MF_ID": 1032,
							// "Mate_ID": 305260,
							// "Name":
							// "鏂颁笢鏂归泤鎬濅簰鍔ㄥ涔犲钩鍙帮紙涓€鏈燂級web閮ㄧ讲鏂囨。",
							// "Url":
							// "http://yunku.gokuai.com/file/iajadm2m#?mId=305260",
							// "UID": 7419,
							// "FileType": "docx",
							// "CreateTime": 1429170670000,

							// "StoreID":
							// "725d792af346d8f6cbc8d81ed4cf3c2fa042a4b4",
							// "IsPublic": null,
							// "StoreState": 2,
							// "ReadCount": 19,
							// "StorePoint": 1

							HashMap<String, Object> map = new HashMap<String, Object>();
							map.put("NickName", obj.getString("NickName").toString());
							map.put("RoleID", obj.getString("RoleID").toString());
							map.put("MF_ID", obj.getString("MF_ID").toString());
							map.put("Mate_ID", obj.getString("Mate_ID").toString());
							map.put("Name", obj.getString("Name").toString());
							map.put("Url", obj.getString("Url").toString());
							map.put("UID", obj.getString("UID").toString());
							map.put("FileType", obj.getString("FileType").toString());
							map.put("CreateTime", obj.getString("CreateTime").toString());

							map.put("StoreID", obj.getString("StoreID").toString());
							map.put("IsPublic", obj.getString("IsPublic").toString());
							map.put("StoreState", obj.getString("StoreState").toString());
							map.put("ReadCount", obj.getString("ReadCount").toString());
							map.put("StorePoint", obj.getString("StorePoint").toString());

							l_map_collect.add(map);
						}
					}
					if (l_map_collect.size() / index < 10) {
						flag = true;
					} else {
						flag = false;
					}
					// 数据为空则提示，listview 消失
					if (l_map_collect.size() > 0) {
						listview_myself_collect.setVisibility(View.VISIBLE);
						relative_warn_nulldata_clastudy.setVisibility(View.GONE);
						if (index == 1) {
							adapter = new MycollectAdapter(MyselfCollectActivity.this, l_map_collect);
							listview_myself_collect.setAdapter(adapter);
						} else if (l_map_collect.size() >= 10) {
							adapter.notifyDataSetChanged();
						}
						listview_myself_collect.onRefreshComplete();
					} else {
						listview_myself_collect.setVisibility(View.GONE);
						relative_warn_nulldata_clastudy.setVisibility(View.VISIBLE);
					}
					initIndicator(listview_myself_collect);
					LodDialogClass.closeCustomCircleProgressDialog();
					initdata();
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

	// 设置刷新时间
	private void setMyLable(PullToRefreshBase<ListView> refreshView) {
		String label = DateUtils.formatDateTime(MyselfCollectActivity.this, System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
	}

	/**
	 * 自定义上拉刷新和下拉加载的显示文字
	 */
	private void initIndicator(PullToRefreshListView listview) {
		ILoadingLayout endLabel = listview.getLoadingLayoutProxy(true, false);

		endLabel.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
		endLabel.setRefreshingLabel("加载中。。。");// 刷新时
		endLabel.setReleaseLabel("放开以加载更多");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = listview.getLoadingLayoutProxy(false, true);
		if (flag == true) {
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

	private void init() {
		LodDialogClass.showCustomCircleProgressDialog(MyselfCollectActivity.this, null,
				getString(R.string.common_Loading));
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		imageview_back.setOnClickListener(this);
		relative_warn_nulldata_clastudy = (RelativeLayout) findViewById(R.id.relative_warn_nulldata_clastudy);
		listview_myself_collect = (PullToRefreshListView) findViewById(R.id.listview_myself_collect);

		listview_myself_collect.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				Log.e("TAG", "onPullDownToRefresh");

				setMyLable(refreshView);

				// 这里写下拉刷新的任务
				index = 1;
				getdatafromnet(index);
				new FinishRefresh(listview_myself_collect).execute();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				setMyLable(refreshView);
				index = index + 1;
				getdatafromnet(index);
				new FinishRefresh(listview_myself_collect).execute();
			}

		});

	}

	private void addHttps(String mateId) {
		// TODO Auto-generated method stub
		String url = Constants.URL_Material_lookUpMaterials;
		RequestParams params = new RequestParams();

		params.addHeader("Authentication",token);// 添加保密的东西

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

	private void initdata() {

		listview_myself_collect.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				position--;
				//增加浏览次数
				addreadcount(l_map_collect.get(position).get("Mate_ID").toString());
				//增加浏览次数
				addHttps(l_map_collect.get(position).get("Mate_ID").toString());
				Intent intent = new Intent();
				filetype = l_map_collect.get(position).get("FileType").toString();
				;
				System.out.println("filetype 文件类型 === " + filetype);
				if (filetype.equals("doc") || filetype.equals("docx")) {
					intent.setClass(MyselfCollectActivity.this, DataForDocxDetailActivity.class);
				} else if (filetype.equals("xls") || filetype.equals("xlsx")) {
					intent.setClass(MyselfCollectActivity.this, DataForDocxDetailActivity.class);
				} else if (filetype.equals("ppt") || filetype.equals("pptx")) {
					intent.setClass(MyselfCollectActivity.this, DataForDocxDetailActivity.class);
				} else if (filetype.equals("PDF") || filetype.equals("pdf") || filetype.equals("Pdf")) {
					intent.setClass(MyselfCollectActivity.this, DataForDocxDetailActivity.class);
				} else {
					intent.setClass(MyselfCollectActivity.this, DataDetailActivity.class);
				}

				Bundle b = new Bundle();
				b.putString("mate_id", l_map_collect.get(position).get("Mate_ID").toString());
				b.putString("MF_ID", l_map_collect.get(position).get("MF_ID").toString());
				b.putString("name", l_map_collect.get(position).get("Name").toString());
				// b.putString("videoThumbnail",
				// l_map_collect.get(position).get("videoThumbnail").toString());
				b.putString("url", l_map_collect.get(position).get("Url").toString());
				intent.putExtras(b);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		getdatafromnet(index);
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

	/**
	 * 增加浏览次数
	 */
	private void addreadcount(String mateId) {

		@SuppressWarnings("static-access")
		String url = new Constants().URL_STUDYONLINE_ADDREADCOUNT + "?pageIndex=";

		RequestParams params = new RequestParams();

		params.addHeader("Authentication", token);// 添加保密的东西

		// params.addBodyParameter("pageIndex", pageIndex);
		params.addBodyParameter("mateId", mateId);

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(1000 * 10);

		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

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

}
