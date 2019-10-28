package com.lels.adapter.student;

import java.util.HashMap;
import java.util.List;

import com.example.hello.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Stu_Correct_Adapter extends BaseAdapter {
	private List<HashMap<String, Object>> mList;
	private Context mContext;

	public Stu_Correct_Adapter(List<HashMap<String, Object>> mList,
			Context mContext) {
		super();
		this.mList = mList;
		this.mContext = mContext;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.isEmpty() ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_stu_correct, null);
			holder.stu_correct_num = (TextView) convertView
					.findViewById(R.id.item_stucorrect_num);
			holder.stu_correct_name = (TextView) convertView
					.findViewById(R.id.item_stucorrect_name);
			holder.stu_correct_date = (TextView) convertView
					.findViewById(R.id.item_stucorrect_date);
			holder.stu_correct_stuname = (TextView) convertView
					.findViewById(R.id.item_stucorrect_stuname);
			holder.stu_correct_seximg = (ImageView) convertView
					.findViewById(R.id.item_stucorrect_sex);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.stu_correct_num.setText(position+1+"");
		holder.stu_correct_name.setText(mList.get(position).get("paperName")
				.toString());
		holder.stu_correct_date.setText(mList.get(position)
				.get("TaskSubmitTime").toString());
		holder.stu_correct_stuname.setText(mList.get(position).get("uName")
				.toString());
		// 判断性别
//		 String tagg=mList.get(position).get("num").toString();
		String tagg = mList.get(position).get("nGender").toString();
		if (tagg.equals("") || tagg == null) {

		} else {
			int tag = Integer.parseInt(tagg);
			switch (tag) {
			case 1:
				holder.stu_correct_seximg
						.setImageResource(R.drawable.ioc_nan2x);
				break;
			case 2:
				holder.stu_correct_seximg.setImageResource(R.drawable.ioc_nv2x);
				break;
			default:
				break;
			}
		}
		return convertView;
	}

	class ViewHolder {
		TextView stu_correct_num, stu_correct_name, stu_correct_date,
				stu_correct_stuname;
		ImageView stu_correct_seximg;

	}
}
