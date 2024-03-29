package com.lels.activity.myself.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.hello.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PopRemindAdapter extends BaseAdapter {

	private Context context;

	private List<String> list;

	public PopRemindAdapter(Context context,List<String> list) {

		this.context = context;
		this.list = list;

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {

		
		ViewHolder holder;
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.pop_item_myself_result_class, null);
			holder=new ViewHolder();
			
			convertView.setTag(holder);
			
			holder.groupItem=(TextView) convertView.findViewById(R.id.groupItem);
			
		}
		else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.groupItem.setTextColor(Color.BLACK);
		holder.groupItem.setText(list.get(position).toString());
		
		return convertView;
	}

	static class ViewHolder {
		TextView groupItem;
	}

}
