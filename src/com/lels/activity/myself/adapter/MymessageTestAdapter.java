package com.lels.activity.myself.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.lels.activity.myself.MyselfMessageActivity;
import com.lels.activity.myself.MyselfMessageDetailActivity;
import com.lels.activity.myself.MyselfMessageTestActivity;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MymessageTestAdapter extends BaseAdapter {
	private static final String TAG = "MymessageTestAdapter";
	private LayoutInflater inflater;
	Context mContext;
	List<HashMap<String, Object>> mlist = new ArrayList<HashMap<String, Object>>();
	private String token;
	// private HashMap<String, String> status;

	public MymessageTestAdapter(Context context, List<HashMap<String, Object>> list, String token_s) {
		this.mContext = context;
		this.mlist = list;
		this.token = token_s;
		inflater = LayoutInflater.from(mContext);
		// status = new HashMap<String, String>();
		// setstatus();
	}

	public void updataforlist(List<HashMap<String, Object>> list) {
		this.mlist = list;
		notifyDataSetChanged();
	}
	// private void setstatus() {
	// for (int i = 0; i < mlist.size(); i++) {
	// String s = mlist.get(i).get("MR_ID").toString();
	// if (s.equalsIgnoreCase("null")) {
	// status.put(String.valueOf(i), "0");
	// } else {
	// status.put(String.valueOf(i), s);
	// }
	// }
	// }

	@Override
	public int getCount() {

		if (mlist.size() > 0) {
			return mlist.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int arg0) {
		return mlist.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewholer;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_myself_mymessage_test, null);
			viewholer = new ViewHolder();
			viewholer.textview_message_title = (TextView) convertView.findViewById(R.id.textview_message_title);
			viewholer.textview_message_time = (TextView) convertView.findViewById(R.id.textview_message_time);
			viewholer.btn_del = (Button) convertView.findViewById(R.id.btn_del);
			convertView.setTag(viewholer);
		} else {
			viewholer = (ViewHolder) convertView.getTag();
		}
		viewholer.textview_message_title.setText(mlist.get(position).get("Title").toString());
		viewholer.textview_message_time.setText(mlist.get(position).get("CreateTime").toString());
		// if (mlist.size() > 0) {
		System.out.println(" ====MR_ID== " + mlist.get(position).get("MR_ID").toString());
		if (mlist.get(position).get("MR_ID").toString().equalsIgnoreCase("null")) {
			System.out.println("变色啊 ");
			viewholer.textview_message_title.setTextColor(Color.RED);
			viewholer.textview_message_time.setTextColor(Color.RED);
		} else {
			viewholer.textview_message_title.setTextColor(Color.BLACK);
			viewholer.textview_message_time.setTextColor(Color.BLACK);
		}
		// }
		
		viewholer.btn_del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("1111111position=" + position);
				deletetest(position);
//				mlist.clear();
//				mlist.remove(position);
				MymessageTestAdapter.this.mlist.remove(position);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	/**
	 * 方法说明：删除试题
	 */
	private void deletetest(final int position) {
		
		System.out.println("==deletetest======失败======="+position);
		
		
		System.out.println("==deletetest======失败======="+mlist.get(position).get("MI_ID").toString());
		
		
		String url = new Constants().URL_MYSELF_MESSAGE_REMIND_READED+"?messageId="+mlist.get(position).get("MI_ID").toString()+"&type="+"del";
		
		Log.e(TAG, "url----------------------Readmessage----" +url );
		
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", token);// 添加保密的东西

		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(0);
		http.configDefaultHttpCacheExpiry(0);
		
		http.send(HttpRequest.HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Log.e(TAG, "获取资料的数据" + responseInfo.result);
				try {
					JSONObject str = new JSONObject(responseInfo.result);// 获取请求的数据信息
					String Result = str.getString("Result");
					String Infomation = str.getString("Infomation");
					String Data = str.getString("Data");
					
//							adapter.notifyDataSetChanged();
					Log.e(TAG, "Data=======================" + Data);
					
					
					
				
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Log.e(TAG, "错误信息=onFailureonFailureonFailureonFailureonFailureonFailureonFailure===" + error.toString());
			}
		});
	}

	class ViewHolder {
		TextView textview_message_title;
		TextView textview_message_time;
		Button btn_del;
	}
}
