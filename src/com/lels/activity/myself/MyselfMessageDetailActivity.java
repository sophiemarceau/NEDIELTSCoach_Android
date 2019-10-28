package com.lels.activity.myself;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hello.R;
import com.lels.activity.myself.adapter.MymessageAdapter;
import com.lels.constants.Constants;
import com.lelts.tool.IntentUtlis;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * 我的消息
 * **/
public class MyselfMessageDetailActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "MyselfMessageDetailActivity";

	private ImageButton imageview_back;
//	private TextView textview_message_title;
//	private TextView textview_message_time;
	private TextView textview_message_body;

	private List<HashMap<String, Object>> list;
	private MymessageAdapter adapter;

	private String token;
	
	private String title;
	private String time;
	private String body;
	private Context context;
	private String index = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myself_message_detail);
		getdatafromshare();
		initview();
		getdatafromintent();
//		getdatafromnet();

	}

	private void getdatafromintent() {
		context = this;
		Bundle b = getIntent().getExtras();
		title = b.getString("title");
		time = b.getString("time");
		body = b.getString("body");
		
//		textview_message_title.setText(title);
//		textview_message_time.setText(time);
		textview_message_body.setText(body);
	}

	private void initview() {
		imageview_back = (ImageButton) findViewById(R.id.imageview_back);
//		textview_message_title = (TextView) findViewById(R.id.textview_message_title);
//		textview_message_time = (TextView) findViewById(R.id.textview_message_time);
		textview_message_body = (TextView) findViewById(R.id.textview_message_body);

		imageview_back.setOnClickListener(this);
	}

	private void getdatafromshare() {

		SharedPreferences share = MyselfMessageDetailActivity.this
				.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		token = share.getString("Token", "");

		Log.d(TAG, "获取的token数值为=====" + token);

	}

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
							List<HashMap<String, Object>> l_map = new ArrayList<HashMap<String, Object>>();
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

									l_map.add(map);
								}
							}

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

}
