package com.lels.activity.myself.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.hello.R;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.lels.activity.myself.MyselfMessageActivity;
import com.lels.activity.myself.MyselfMessageDetailActivity;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class MymessageAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	Context mContext;
	List<HashMap<String, Object>> mlist;
	private SwipeListView mSwipeListView;
	
	private String token;
	
	private HashMap<String, String> status;
	
	public MymessageAdapter(Context context, List<HashMap<String, Object>> list,SwipeListView mSwipeListView,String token_s) {
		this.mContext = context;
		this.mlist = list;
		this.mSwipeListView = mSwipeListView;
		this.token = token_s;
		
		status = new HashMap<String, String>();
		setstatus();
	}

	
	
	private void setstatus() {
		for(int i = 0;i<mlist.size();i++){
			String s = mlist.get(i).get("MR_ID").toString();
			if(s.equalsIgnoreCase("null")){
				status.put(String.valueOf(i), "0");
				System.out.println("bian-00000000000");
			}else{
				status.put(String.valueOf(i), s);
			}
			
			
		}
	}



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
		if (mlist.size() > 0) {
			return mlist.size();
		} else {
			return 0;
		}
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_myself_mymessage, null);

			viewholer = new ViewHolder();
			viewholer.textview_message_title = (TextView) convertView
					.findViewById(R.id.textview_message_title);
			viewholer.textview_message_time = (TextView) convertView
					.findViewById(R.id.textview_message_time);
			viewholer.btn_delete = (Button) convertView
					.findViewById(R.id.btn_delete_stu_start_item);
			convertView.setTag(viewholer);
		} else {

			viewholer = (ViewHolder) convertView.getTag();

		}
		//MR_ID

		if (mlist.size() > 0) {
			
//			String MR_ID = mlist.get(position).get("MR_ID").toString();
//			System.out.println("MR_ID======="+MR_ID);
			
			if(status.get(String.valueOf(position)).toString().equalsIgnoreCase("0")){
				System.out.println("变色啊 ");
				viewholer.textview_message_title.setTextColor(Color.RED);
				viewholer.textview_message_time.setTextColor(Color.RED);
			}else{
				viewholer.textview_message_title.setTextColor(Color.BLACK);
				viewholer.textview_message_time.setTextColor(Color.BLACK);
			}
			
			viewholer.textview_message_title.setText(mlist.get(position)
					.get("Title").toString());
			viewholer.textview_message_time.setText(mlist.get(position)
					.get("CreateTime").toString());
			
		}
//		convertView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//
//				Intent intent = new Intent();
//				intent.setClass(mContext,
//						MyselfMessageDetailActivity.class);
//				Bundle b = new Bundle();
//				// title = b.getString("title");
//				// time = b.getString("time");
//				// body
//				b.putString("title",
//						mlist.get(position).get("Title")
//								.toString());
//				b.putString("time",
//						mlist.get(position).get("CreateTime")
//								.toString());
//				b.putString("body",
//						mlist.get(position).get("Body")
//								.toString());
//
//				intent.putExtras(b);
//				mContext.startActivity(intent);
//
//			
//			}
//		});
		
		viewholer.btn_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSwipeListView.closeAnimate(position);
//				mSwipeListView.dismiss(position);
				deletetest(position);
				notifyDataSetChanged();
			}
		});
		
		return convertView;
	}
	/**
	 * 方法说明：删除试题
	 *
	 */
	private void deletetest(final int position) {
		RequestParams params = new RequestParams();
		HttpUtils utils = new HttpUtils();
		String url = Constants.URL_MYSELF_MESSAGE_DELECT;
		
		params.addHeader("Authentication", token);
		params.addBodyParameter("mrId", mlist.get(position)
					.get("MI_ID").toString());
		
		utils.send(HttpMethod.POST,url,params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						System.out.println("========失败=======");

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String result = arg0.result;
						System.out.println("deleteresult====================="
								+ result);
						mSwipeListView.dismiss(position);
					}
				});
	}

	class ViewHolder {
		TextView textview_message_title;
		TextView textview_message_time;
		Button btn_delete;
	}

}
