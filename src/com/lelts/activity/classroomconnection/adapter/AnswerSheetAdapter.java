/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年6月27日 
 * 
 *******************************************************************************/
package com.lelts.activity.classroomconnection.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.lels.bean.AnswerSheetInfo;
import com.lels.constants.Constants;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.R.color;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年6月27日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class AnswerSheetAdapter extends BaseAdapter {
	private Context context;
	private List<HashMap<String, Object>> mlist;
	private SwipeListView mSwipeListView ;
	private SharedPreferences share,teacherinfo;
	private String ccId,paperId;
	private int position;
	//判断试卷是否能点击或者删除
	private int PushStatus;
	public AnswerSheetAdapter(Context context, List<HashMap<String, Object>> mlist,
			SwipeListView mSwipeListView) {
		super();
		this.context = context;
		this.mlist = mlist;
		this.mSwipeListView = mSwipeListView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.isEmpty() ? 0 : mlist.size();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlist.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Asdapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * []
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		share = context.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
		teacherinfo = context.getSharedPreferences("teacherinfo", Context.MODE_PRIVATE);
		
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_answer_sheet, null);
			holder = new ViewHolder();
			holder.btn_delete = (Button) convertView.findViewById(R.id.btn_delete_answer_shheet_item);
			holder.txt_testname =(TextView) convertView.findViewById(R.id.txt_testname_answer_sheet_item);
			holder.txt_testnum = (TextView) convertView.findViewById(R.id.txt_testnum_answer_sheet_item);
			holder.txt_numtitle = (TextView) convertView.findViewById(R.id.num);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		PushStatus = Integer.parseInt(mlist.get(position).get("PushStatus").toString());
		if(PushStatus>0){
			//试卷已被使用  变灰
			holder.txt_numtitle.setTextColor(context.getResources().getColor(R.color.describe_color));
			holder.txt_testname.setTextColor(context.getResources().getColor(R.color.describe_color));
			holder.txt_testnum.setTextColor(context.getResources().getColor(R.color.describe_color));
		}else{
			//试卷已被使用  变灰
			holder.txt_numtitle.setTextColor(Color.BLACK);
			holder.txt_testname.setTextColor(Color.BLACK);
			holder.txt_testnum.setTextColor(Color.BLACK);
		}
		holder.txt_testname.setText(mlist.get(position).get("Name").toString());
		holder.txt_testnum.setText(mlist.get(position).get("QuestionCount").toString());
		holder.btn_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 PushStatus = Integer.parseInt(mlist.get(position).get("PushStatus").toString());
				 System.out.println("PushStatus===试卷的状态=="+Integer.parseInt(mlist.get(position).get("PushStatus").toString()));
				 if(PushStatus>0){
						//不可删除和点击
					 Toast.makeText(context, "该试卷已被使用！", Toast.LENGTH_SHORT).show();
					}else{
						//可以删除和点击
						mSwipeListView.closeAnimate(position);
						mSwipeListView.dismiss(position);
						deletetest(position);
					}
						
				
			}
		});
		return convertView;
	}
	
	
	/**
	 * 方法说明：删除试题
	 *
	 */
	private void deletetest(int position) {
		// TODO Auto-generated method stub
		ccId = teacherinfo.getString("ccId", null);
		paperId = mlist.get(position).get("P_ID").toString();
		System.out.println("ccId====="+ccId+"+++++++++P_ID+++++++"+paperId);
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, Constants.URL_ActiveClassExerciseDelete
				+"?ccId="+ccId+ "&paperId=" + paperId, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
							System.out.println("========失败=======");
							
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						
						
						System.out.println("deleteresult====================="
								+ result);

					}
				});
	}

	class ViewHolder {
		Button btn_delete;
		TextView txt_testname,txt_testnum,txt_numtitle;
	}

}
