package com.lels.adapter.classesfm_details;

import java.util.HashMap;
import java.util.List;

import com.example.hello.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PopAdapter extends BaseAdapter {
	List<HashMap<String, Object>> mList;
	Context mcontext;

	public PopAdapter(List<HashMap<String, Object>> mList, Context mcontext) {
		super();
		this.mList = mList;
		this.mcontext = mcontext;
	}

	@Override
	public int getCount() {
		return mList.isEmpty() ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mcontext).inflate(
					R.layout.item_popwind, null);			
			holder.t = (TextView) convertView.findViewById(R.id.item_pop_tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.t.setText(mList.get(position).get("nLessonNo").toString());
		return convertView;
	}

	class ViewHolder {
		TextView t;
	}
}
