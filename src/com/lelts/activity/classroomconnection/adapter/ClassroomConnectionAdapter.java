/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年6月24日 
 * 
 *******************************************************************************/
package com.lelts.activity.classroomconnection.adapter;

import java.util.HashMap;
import java.util.List;








import com.example.hello.R;
import com.lels.bean.ClassRoomConnection_info;
import com.lels.constants.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年6月24日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class ClassroomConnectionAdapter extends BaseAdapter {
	private List<HashMap<String, Object>> mlist;
	private Context context;

	public ClassroomConnectionAdapter(List<HashMap<String, Object>> mlist,
			Context context) {
		super();
		this.mlist = mlist;
		this.context = context;
	}
	public void setdatachanges(List<HashMap<String, Object>> mlist
			) {
		this.mlist = mlist;
		this.notifyDataSetChanged();
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
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_classroomconnection, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.img_classroom_coonection);
			holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name_classroom_coonection);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txt_name.setText(mlist.get(position).get("studentname").toString());
		String iconUrl = mlist.get(position).get("iconUrl").toString();
		if (iconUrl.length()>0&&!iconUrl.equals("null")) {
			ImageLoader.getInstance().displayImage(Constants.URL_TeacherIMG+mlist.get(position).get("iconUrl").toString(),holder.image);
			
		}else{
			holder.image.setImageResource(R.drawable.mor);
		}
		if(mlist.get(position).get("studentloginstatus").equals("1")){
			holder.image.setAlpha(1.0f);
			holder.txt_name.setTextColor(Color.BLACK);
		}else{
			ImageLoader.getInstance().displayImage(Constants.URL_TeacherIMG+mlist.get(position).get("iconUrl").toString(),holder.image);
			holder.txt_name.setTextColor(context.getResources().getColor(R.color.gray));
			holder.image.setAlpha(0.5f);
		}
		
		return convertView;
	}

}

class ViewHolder {
	ImageView image;
	TextView txt_name;
}
