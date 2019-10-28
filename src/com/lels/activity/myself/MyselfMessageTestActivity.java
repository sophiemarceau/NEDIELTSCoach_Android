package com.lels.activity.myself;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.com.demo.app.view.FastListView;
import me.com.demo.app.view.FastListView.Feedback;
import me.com.demo.app.view.FastListView.FreshOrLoadListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hello.R;
import com.lels.activity.myself.adapter.MymessageTestAdapter;
import com.lels.bean.LodDialogClass;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 我的消息
 **/
public class MyselfMessageTestActivity extends Activity implements
		OnClickListener, FreshOrLoadListener {

	private static final String TAG = "MyselfMessageActivity";
	private RelativeLayout studyOnLine_no_data;
	private TextView studyOnLine_text;
	private ImageView imageview_back;
	private FastListView listview_myself_message;
	private List<HashMap<String, Object>> list;
	private MymessageTestAdapter adapter;
	private List<HashMap<String, Object>> l_map_message = new ArrayList<HashMap<String, Object>>();
	private String token;
	private String nickname;
	private String iconurl;
	private int pageindex = 1;
	private int OnClickPosition;
	Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_message_test);
		initView();
		getDataFromShare();
		getDataFromNet();
	}

	private void initView() {
		studyOnLine_no_data = (RelativeLayout) findViewById(R.id.studyOnLine_no_data);
		studyOnLine_text = (TextView) findViewById(R.id.studyOnLine_text);
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		imageview_back.setOnClickListener(this);

		listview_myself_message = (FastListView) findViewById(R.id.listview_myself_message);

		listview_myself_message
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View child,
							int position, long id) {

						String MessageID = l_map_message.get(position)
								.get("MI_ID").toString();
						System.out.println("点击的消息MI_ID是：==" + MessageID);
						HashMap<String, Object> obj = l_map_message
								.get(position);
						if (obj.get("MR_ID").toString()
								.equalsIgnoreCase("null")) {
							obj.put("MR_ID", "1");
						}
						OnClickPosition = position;
						// 读消息
						Readmessage(MessageID);
						Intent intent = new Intent();
						intent.setClass(MyselfMessageTestActivity.this,
								MyselfMessageDetailActivity.class);
						Bundle b = new Bundle();
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
				});
		listview_myself_message.setFreshOrLoadListener(this);
		LodDialogClass.showCustomCircleProgressDialog(
				MyselfMessageTestActivity.this, null,
				getString(R.string.common_Loading));
	}

	private void getDataFromShare() {

		SharedPreferences share = MyselfMessageTestActivity.this
				.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");
		nickname = share.getString("NickName", "");
		iconurl = share.getString("IconUrl", "");
		Log.d(TAG, "获取的token数值为===getDataFromShare==" + token);
	}

	@SuppressWarnings("static-access")
	private void getDataFromNet() {
		// +"?pageIndex="+index
		String url = new Constants().URL_MYSELF_MESSAGE;
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);

		params.addBodyParameter("pageIndex", String.valueOf(pageindex));

		HttpUtils http = new HttpUtils();
		// http.configCurrentHttpCacheExpiry(1000 * 10);
		http.configCurrentHttpCacheExpiry(0);
		http.configDefaultHttpCacheExpiry(0);

		http.send(HttpRequest.HttpMethod.POST, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							Log.d(TAG,
									"getDataFromNet----------onSuccess-------------解析获取我的消息列表"
											+ responseInfo.result);
							JSONObject str = new JSONObject(responseInfo.result);
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");
							// totalCount = 总信息数;

							// list = 消息集合({
							// MI_ID =消息ID;
							// Title =标题;
							// Body =内容;
							// CreateTime =创建时间;
							// AssignRoleID =指派角色;
							// MState =状态 0未发布1已发布;
							// Account =账号名称
							// MR_ID =已读信息ID;

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

							if (pageindex == 1) {
								l_map_message = new ArrayList<HashMap<String, Object>>();
							}

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

							if (pageindex == 1) {
								if (l_map_message.size() == 0) {
									knowIfDataBackground();
								} else {
									adapter = new MymessageTestAdapter(
											MyselfMessageTestActivity.this,
											l_map_message, token);
									listview_myself_message.setAdapter(adapter);
								}
							} else {
								adapter.notifyDataSetChanged();
							}
							// adapter.updataforlist(l_map_message);
							/* 通知列表，刷新完成。参数1表示请求结果是否成功，参数2表示数据是否全部加载 */
							if (l_map_message.size() == Integer
									.valueOf(totalCount)) {

								listview_myself_message.noticeFreshDone(true,
										true);
							} else {
								listview_myself_message.noticeFreshDone(true,
										false);
							}
							adapter.notifyDataSetChanged();
							LodDialogClass.closeCustomCircleProgressDialog();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						LodDialogClass.closeCustomCircleProgressDialog();
						Log.d(TAG,
								"错误原因为===-------------------------------解析获取我的消息列表"
										+ error.toString());
					}
				});
	}

	// 如果没有数据则显示一张背景图
	private void knowIfDataBackground() {
		listview_myself_message.setAdapter(null);
		listview_myself_message.setVisibility(View.GONE);
		studyOnLine_no_data.setVisibility(View.VISIBLE);
		studyOnLine_text.setText("暂无消息");
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (!this.l_map_message.isEmpty() && null != listview_myself_message) {
			MymessageTestAdapter adapter = (MymessageTestAdapter) listview_myself_message
					.getAdapter();
			adapter.updataforlist(l_map_message);
		}
	}

	private void Readmessage(String messageId) {
		// Log.d(TAG,
		// "读取消息接口的数据============Readmessage================================================="
		// + messageId);

		String url = new Constants().URL_MYSELF_MESSAGE_REMIND_READED
				+ "?messageId=" + messageId + "&type=" + "read";
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(0);
		http.configDefaultHttpCacheExpiry(0);

		http.send(HttpRequest.HttpMethod.GET, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Log.d(TAG, "获取资料的数据" + responseInfo.result);
						System.out.println("wwwwwwwwwww:=="
								+ responseInfo.result);
						try {
							JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息
							String Result = str.getString("Result");
							String Infomation = str.getString("Infomation");
							String Data = str.getString("Data");

							adapter.notifyDataSetChanged();

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.d(TAG,
								"错误信息=onFailureonFailureonFailureonFailureonFailureonFailureonFailure==="
										+ error.toString());
					}
				});
	}

	@Override
	public void onNeedFresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				pageindex = 1;
				getDataFromNet();
			}
		}, 2000);
	}

	@Override
	public void onNeedLoad(Feedback feedback) {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				pageindex++;
				getDataFromNet();
			}
		}, 2000);
	}
}
