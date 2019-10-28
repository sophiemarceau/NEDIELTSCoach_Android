/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年7月1日 
 * 
 *******************************************************************************/
package com.lelts.activity.classroomconnection.adapter;

import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Text;

import com.example.hello.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年7月1日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class TeacherClassAdapter extends BaseAdapter {
	private Context context;
	private List<HashMap<String, Object>> mlist;
	public static int choose;
	// 用于记录每个RadioButton的状态，并保证只可选一个
	private HashMap<String, Boolean> states = new HashMap<String, Boolean>();

	public TeacherClassAdapter(Context context,
			List<HashMap<String, Object>> mlist) {
		super();
		this.context = context;
		this.mlist = mlist;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.size();
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
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_teacher_classes, null);
			holder = new ViewHolder();
			holder.txt_code = (TextView) convertView
					.findViewById(R.id.item_txtcode_teacher_classes);
			holder.txt_num = (TextView) convertView
					.findViewById(R.id.item_txtnum_teacher_classes);
			holder.txt_name = (TextView) convertView
					.findViewById(R.id.item_txtname_teacher_classes);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_code.setText("第"
				+ mlist.get(position).get("nLessonNo").toString() + "课次");
		holder.txt_name
				.setText(mlist.get(position).get("className").toString());
		holder.txt_num
				.setText(mlist.get(position).get("sClassCode").toString());

		final RadioButton radio = (RadioButton) convertView
				.findViewById(R.id.item_radio_teacher_classes);
		
		holder.rb_state = radio;
		
		holder.rb_state.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				// 重置，确保最多只有一项被选中
				for (String key : states.keySet()) {
					states.put(key, false);
				}
				choose = position;
				states.put(String.valueOf(position), radio.isChecked());
				TeacherClassAdapter.this.notifyDataSetChanged();
				getposition();
			}
		});

		boolean res = false;
		if (states.get(String.valueOf(position)) == null
				|| states.get(String.valueOf(position)) == false) {
			res = false;
			states.put(String.valueOf(position), false);
		} else
			res = true;
		
		if(res){
			
			holder.txt_name.setTextColor(Color.RED);
			holder.txt_num.setTextColor(Color.RED);
			holder.txt_code.setTextColor(Color.RED);
		}else{
			holder.txt_name.setTextColor(Color.BLACK);
			holder.txt_num.setTextColor(Color.BLACK);
			holder.txt_code.setTextColor(Color.BLACK);
		}

		holder.rb_state.setChecked(res);
		holder.rb_state.setFocusable(false);
		return convertView;
	}

	class ViewHolder {
		TextView txt_name, txt_code, txt_num;
		RadioButton rb_state;

	}
	public static int  getposition(){
		System.out.println("choose======================="+choose);
		return choose;
		
	}
}
