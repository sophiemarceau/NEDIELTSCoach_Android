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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.hello.R;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.lels.activity.myself.adapter.MymessageAdapter;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 我的消息
 * **/
public class MyselfMessageActivity extends Activity implements OnClickListener {

	private static final String TAG = "MyselfMessageActivity";

	private ImageButton imageview_back;
	private SwipeListView listview_myself_message;

	private List<HashMap<String, Object>> list;
	private MymessageAdapter adapter;

	private List<HashMap<String, Object>> l_map_message = new ArrayList<HashMap<String, Object>>();

	private String token;
	private String nickname;
	private String iconurl;

	private String index = "1";
	// 滑动删除
//	private SwipeListView mSwipeListView;
	public static int deviceWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_message);

		initview();
		getdatafromshare();
		getdatafromnet();

		deviceWidth = getDeviceWidth();
		listview_myself_message
				.setSwipeListViewListener(new TestBaseSwipeListViewListener());

		reload();
		

	}


	private void initview() {
		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
		imageview_back.setOnClickListener(this);

		listview_myself_message = (SwipeListView) findViewById(R.id.listview_myself_message);

//		listview_myself_message
//				.setOnItemClickListener(new OnItemClickListener() {
//
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view,
//							int position, long id) {
//						Intent intent = new Intent();
//						intent.setClass(MyselfMessageActivity.this,
//								MyselfMessageDetailActivity.class);
//						Bundle b = new Bundle();
//						// title = b.getString("title");
//						// time = b.getString("time");
//						// body
//						b.putString("title",
//								l_map_message.get(position).get("Title")
//										.toString());
//						b.putString("time",
//								l_map_message.get(position).get("CreateTime")
//										.toString());
//						b.putString("body",
//								l_map_message.get(position).get("Body")
//										.toString());
//
//						intent.putExtras(b);
//						startActivity(intent);
//
//					}
//				});

	}

	private void getdatafromshare() {

		SharedPreferences share = MyselfMessageActivity.this
				.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");
		nickname = share.getString("NickName", "");
		iconurl = share.getString("IconUrl", "");

		Log.d(TAG, "获取的token数值为=====" + token);

	}

	// private void init() {
	// initdata();
	// }

	// private void initdata() {
	// list = new ArrayList<HashMap<String, Object>>();
	// for (int i = 0; i < 5; i++) {
	// HashMap<String, Object> map = new HashMap<String, Object>();
	// map.put("title", "雅思互动平台2.0正式发布上线");
	// map.put("time", "2015-6-23");
	// list.add(map);
	// }
	//
	// adapter = new MymessageAdapter(MyselfMessageActivity.this, list);
	// listview_myself_message.setAdapter(adapter);
	//
	// }

	@SuppressWarnings("static-access")
	private void getdatafromnet() {

		// +"?pageIndex="+index
		String url = new Constants().URL_MYSELF_MESSAGE;

		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		params.addBodyParameter("pageIndex", index);

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
						try {
							Log.d(TAG, "解析获取我的收藏列表" + responseInfo.result);
							JSONObject str = new JSONObject(responseInfo.result);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							//  totalCount = 总信息数;

							//   list = 消息集合({
							//    MI_ID =消息ID;
							//    Title =标题;
							//    Body =内容;
							//    CreateTime =创建时间;
							//    AssignRoleID =指派角色;
							//    MState =状态 0未发布1已发布;
							//    Account =账号名称
							//    MR_ID =已读信息ID;

							// "MR_ID": null,
							// "Body":
							// "111111111111111111111111111111111111111111111111111111111111111111111",
							// "MI_ID": 2003,
							// "MState": 1,
							// "CreateTime": "2015-07-23",
							// "Account": "xdf0050009175",
							// "AssignRoleID": 2,
							// "Title": "Test 1111"

							JSONObject obj = new JSONObject(Data);
							String totalCount = obj.getString("totalCount");
							String list = obj.getString("list");
							JSONArray array = new JSONArray(list);
							l_map_message = new ArrayList<HashMap<String, Object>>();
							if (array != null) {
								for (int i = 0; i < array.length(); i++) {
									JSONObject obj_l = array.optJSONObject(i);
									HashMap<String, Object> map = new HashMap<String, Object>();
									map.put("MR_ID", obj_l.get("MR_ID"));
									map.put("Body", obj_l.get("Body"));
									map.put("MI_ID", obj_l.get("MI_ID"));
									map.put("MState", obj_l.get("MState"));
									map.put("CreateTime",
											obj_l.get("CreateTime"));
									map.put("Account", obj_l.get("Account"));

									map.put("AssignRoleID",
											obj_l.get("AssignRoleID"));
									map.put("Title", obj_l.get("Title"));

									l_map_message.add(map);
								}
							}
							
							
							adapter = new MymessageAdapter(
									MyselfMessageActivity.this, l_map_message,listview_myself_message,token);
							listview_myself_message.setAdapter(adapter);

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						System.out.println("错误原因为===" + error.toString());

					}

				});

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
	
	private int getDeviceWidth() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	private void reload() {
		listview_myself_message.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
		listview_myself_message.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
		// mSwipeListView.setSwipeActionRight(settings.getSwipeActionRight());
		listview_myself_message.setOffsetLeft((float) (deviceWidth * 1 / 1.3));
		// mSwipeListView.setOffsetRight(convertDpToPixel(settings.getSwipeOffsetRight()));
		listview_myself_message.setAnimationTime(0);
		listview_myself_message.setSwipeOpenOnLongPress(false);
	}

	class TestBaseSwipeListViewListener extends BaseSwipeListViewListener {

		@Override
		public void onClickFrontView(int position) {
			Log.e(TAG, "onClickFrontView----------------------------->"+position);
			Readmessage(l_map_message.get(position).get("MI_ID").toString());
			
			Intent intent = new Intent();
			intent.setClass(MyselfMessageActivity.this,
					MyselfMessageDetailActivity.class);
			Bundle b = new Bundle();
			// title = b.getString("title");
			// time = b.getString("time");
			// body
			b.putString("title",
					l_map_message.get(position).get("Title")
							.toString());
			b.putString("time",
					l_map_message.get(position).get("CreateTime")
							.toString());
			b.putString("body",
					l_map_message.get(position).get("Body")
							.toString());

			intent.putExtras(b);
			startActivity(intent);
		}  

		@Override
		public void onDismiss(int[] reverseSortedPositions) {
			for (int position : reverseSortedPositions) {
				l_map_message.remove(position);
			}
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		getdatafromnet();
	}
	
	private void Readmessage(String messageId) {
		Log.d(TAG, "Readmessage------------------------------------------------------" +messageId);
		// +"?pageIndex="+index
		String url = new Constants().URL_MYSELF_MESSAGE_REMIND_READED+"?messageId="+messageId+"&type="+"read";
		Log.d(TAG, "url------------------------------------------------------" +url);
//		RequestParams params = new RequestParams();
//		params.addHeader("Authentication", token);
//
//		HttpUtils http = new HttpUtils();
//		http.configCurrentHttpCacheExpiry(1000 * 10);
//
//		http.send(HttpRequest.HttpMethod.GET, url, params,
//				new RequestCallBack<String>() {
//					@Override
//					public void onStart() {
//						super.onStart();
//					}
//
//					@Override
//					public void onSuccess(ResponseInfo<String> responseInfo) {
//						try {
//							Log.d(TAG, "读取消息接口的数据" + responseInfo.result);
//							JSONObject str = new JSONObject(responseInfo.result);
//							String Result = str.getString("Result");
//							String Infomation = str.getString("Infomation");
//							String Data = str.getString("Data");
//							
//							Log.d(TAG, Result);
//
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//					}
//
//					@Override
//					public void onFailure(HttpException error, String msg) {
//						Log.d(TAG, "onFailure--------------------------" + error.toString());
//
//					}
//
//				});
		
		
//
	}


}
