/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年7月14日 
 * 
 *******************************************************************************/ 
package com.lelts.activity.classroomconnection.adapter;

import java.util.List;

import com.example.hello.R;
import com.lels.bean.AnswerInfo;
import com.lels.bean.ExamAnswerListInfo;
import com.lels.bean.OptionInfo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年7月14日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class StrartMoreAnswerTestAdapter extends BaseAdapter{
	private Context context;
	private List<ExamAnswerListInfo> mlist;
	
	StartAnswerTestAdapter madater;
	public StrartMoreAnswerTestAdapter(Context context, List<ExamAnswerListInfo> mlist) {
		super();
		this.context = context;
		this.mlist = mlist;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlist.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_grideview_start_more_answer, null);
			holder.txt_answer = (TextView) convertView.findViewById(R.id.item_txt_answer_more_test);
			holder.txt_num = (TextView) convertView.findViewById(R.id.item_txt_num_more_test);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		String getlast=mlist.get(position).getAnswerContent();
		String dd=getlast.replace("[", "");
		String  answer = dd;
		System.out.println("answer............"+answer);
		System.out.println("Qnumber==="+mlist.get(position).getQNumber());
		holder.txt_answer.setText(answer);
		holder.txt_num.setText(mlist.get(position).getQNumber());
		if(position%2==0){
			holder.txt_answer.setTextColor(Color.RED);
		}else{
			holder.txt_answer.setTextColor(Color.parseColor("#20b2aa")); 
		}
		
		return convertView;
	}
	
	class ViewHolder{
		TextView txt_num,txt_answer;
	}

}
