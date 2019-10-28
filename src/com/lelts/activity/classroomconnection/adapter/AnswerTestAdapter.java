/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年6月29日 
 * 
 *******************************************************************************/
package com.lelts.activity.classroomconnection.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.hello.R;
import com.lels.bean.AnswerTestInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年6月29日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class AnswerTestAdapter extends BaseAdapter {
	private Context context;
	private List<AnswerTestInfo> mlist;
//	public List<Boolean> mChecked;
//	HashMap<Integer,View> map = new HashMap<Integer,View>(); 
	public static HashMap<Integer, Boolean> isSelected;
	public AnswerTestAdapter(Context context, List<AnswerTestInfo> mlist) {
		super();
		this.context = context;
		this.mlist = mlist;
//		 mChecked = new ArrayList<Boolean>();  
//         for(int i=0;i<mlist.size();i++){  
//             mChecked.add(false);  
//         }
         //初始化 设置所有checkbox都为未选择 
         isSelected = new HashMap<Integer, Boolean>(); 
         for (int i = 0; i < mlist.size(); i++) { 
         isSelected.put(i, false);
         }
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
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		final AnswerTestInfo info = mlist.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_answer_test, null);
			holder = new ViewHolder();
			holder.txt_testnum = (TextView) convertView.findViewById(R.id.txt_num_answer_test_item);
			holder.txt_testname = (TextView) convertView
					.findViewById(R.id.txt_content_answer_test_item);
			holder.ck = (CheckBox) convertView
					.findViewById(R.id.checkbox_answer_test_item);
//			map.put(position, convertView);
//			holder.ck.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//					CheckBox cb = (CheckBox)v; 
//					mChecked.set(position, cb.isChecked());  
//	              //  mChecked.set(position, info.isIschexck()); 
//					if (info.isIschexck()) {
//						info.setIschexck(true);
//					} else {
//						info.setIschexck(false);
//					}
//					notifyDataSetChanged();
//				}
//			});

			convertView.setTag(holder);
		
		} else {
//			convertView = map.get(position);
			holder = (ViewHolder) convertView.getTag();
		
		}
		holder.txt_testnum.setText((position+1)+". ");
		holder.txt_testname.setText(mlist.get(position).getTestname());
		//设置checkbox的 状态
//		holder.ck.setChecked(isSelected.get(position)); 
//		holder.ck.setChecked(mChecked.get(position));  
	//	holder.ck.setChecked(info.isIschexck());
		final AnswerTestInfo state = mlist.get(position);
		holder.ck.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (state.isIschexck()) {
					state.setIschexck(false);
				} else {
					state.setIschexck(true);
				}
				notifyDataSetChanged();
			}
		});
		
		holder.ck.setChecked(state.isIschexck());
		return convertView;
	}

	class ViewHolder {
		TextView txt_testname,txt_testnum;
		CheckBox ck;
	}

}
