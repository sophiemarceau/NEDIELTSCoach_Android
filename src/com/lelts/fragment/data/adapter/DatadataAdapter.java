package com.lelts.fragment.data.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hello.R;
import com.lelts.fragment.data.DataDetailActivity;
import com.lidroid.xutils.BitmapUtils;

public class DatadataAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	Context mContext;
	List<HashMap<String, Object>> mlist;
	BitmapUtils bputil;

	public DatadataAdapter(Context context, List<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mlist = list;
		bputil = new BitmapUtils(mContext);

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

	@SuppressLint("SimpleDateFormat")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewholer;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_data_maindata_test, null);

			viewholer = new ViewHolder();
			viewholer.imageview_date_type = (ImageView) convertView
					.findViewById(R.id.imageview_date_type);
			viewholer.textview_date_title_andtype = (TextView) convertView
					.findViewById(R.id.textview_date_title_andtype);
			viewholer.textview_data_time = (TextView) convertView
					.findViewById(R.id.textview_data_time);

			// viewholer.textview_data_item_title = (TextView) convertView
			// .findViewById(R.id.textview_data_item_title);
			// viewholer.textview_data_item_time = (TextView) convertView
			// .findViewById(R.id.textview_data_item_time);
			// viewholer.textview_data_item_browsenum = (TextView) convertView
			// .findViewById(R.id.textview_data_item_browsenum);

			convertView.setTag(viewholer);
		} else {

			viewholer = (ViewHolder) convertView.getTag();

		}

		// if (mlist.size() > 0) {
		// // viewholer.imageview_data_item_type
		// bputil.display(viewholer.imageview_data_item_type,
		// mlist.get(position).get("url").toString());
		//
		// // 角色ID，1集团2个人
		// if (mlist.get(position).get("RoleID").toString()
		// .equalsIgnoreCase("1")) {
		// viewholer.textview_data_item_title.setText(mlist.get(position)
		// .get("name").toString()
		// + "  |  " + "集团");
		// } else {
		// viewholer.textview_data_item_title.setText(mlist.get(position)
		// .get("name").toString()
		// + "  |  " + "个人");
		// }
		// viewholer.textview_data_item_time.setText(mlist.get(position).get("createtime").toString());
		//
		// }
		if (mlist.size() > 0) {

			if (mlist.get(position).get("StorePoint").toString().equals("1")) {
				viewholer.imageview_date_type
						.setBackgroundResource(R.drawable.icon_myself_collect_doc);
			} else {
				viewholer.imageview_date_type
						.setBackgroundResource(R.drawable.icon_myself_collect);
			}

			// // 角色ID，1集团2个人
			if (mlist.get(position).get("RoleID").toString()
					.equalsIgnoreCase("1")) {
				viewholer.textview_date_title_andtype.setText(mlist
						.get(position).get("name").toString()
						+ "  |  " + "集团");
			} else {
				viewholer.textview_date_title_andtype.setText(mlist
						.get(position).get("name").toString()
						+ "  |  " + "个人");
			}

			// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// String time = mlist.get(position).get("createtime").toString();
			//
			// Log.d("getview", time);
			//
			// Long long_t = Long.valueOf(time);
			// String dd = sdf.format(new Date(long_t));

			viewholer.textview_data_time.setText(mlist.get(position)
					.get("createtime").toString());
		}
		// convertView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent intent = new Intent();
		// intent.setClass(mContext, DataDetailActivity.class);
		// Bundle b = new Bundle();
		// b.putString("mate_id",
		// mlist.get(position).get("mate_id").toString());
		// b.putString("MF_ID", mlist.get(position).get("MF_ID").toString());
		// b.putString("name", mlist.get(position).get("name").toString());
		// b.putString("videoThumbnail",
		// mlist.get(position).get("videoThumbnail").toString());
		// intent.putExtras(b);
		//
		// mContext.startActivity(intent);
		//
		// }
		// });

		return convertView;
	}

	class ViewHolder {
		// ImageView imageview_data_item_type;
		// TextView textview_data_item_title;
		// TextView textview_data_item_time;
		// TextView textview_data_item_browsenum;

		ImageView imageview_date_type;
		TextView textview_date_title_andtype;
		TextView textview_data_time;

	}

	public void up(List<HashMap<String, Object>> l_map) {
		this.mlist = l_map;
		notifyDataSetChanged();
	}

}
