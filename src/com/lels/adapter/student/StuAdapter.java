package com.lels.adapter.student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hello.R;
import com.lels.bean.Students;
import com.lels.constants.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StuAdapter extends BaseAdapter {
	private List<HashMap<String, Object>> mList;
	private Context mContext;
	private Map<Integer, String> map;
	public StuAdapter(List<HashMap<String, Object>> mList, Context mContext) {
		super();
		this.mList = mList;
		this.mContext = mContext;
		map = new HashMap<Integer, String>();
	}

	@Override
	public int getCount() {
		return mList.isEmpty() ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
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
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_student_list, null);
			holder = new ViewHolder();
			holder.head_image = (ImageView) convertView
					.findViewById(R.id.stulist_head_img);
			holder.sex_image = (ImageView) convertView
					.findViewById(R.id.stulist_sex_img);
			holder.txt_name = (TextView) convertView
					.findViewById(R.id.stulist_name);
			holder.txt_num = (TextView) convertView
					.findViewById(R.id.stulist_num);
			holder.txt_finished = (TextView) convertView
					.findViewById(R.id.stulist_finished);
			holder.txt_finishedname = (TextView) convertView
					.findViewById(R.id.finished);
			holder.txt_downdays = (TextView) convertView
					.findViewById(R.id.stulist_downdays);
			holder.relative = (RelativeLayout) convertView.findViewById(R.id.dayrelative);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_name.setText(mList.get(position).get("sName").toString());
		String avgScore = mList.get(position).get("avgScore").toString();
		if (avgScore.equals("0.0")||avgScore.equals("")||avgScore.equals("null")) {
			holder.txt_finishedname.setText("任务完成率");
			holder.txt_finished.setText(mList.get(position).get("finishTask")
					.toString()
					+ "%");
		}else{
			holder.txt_finishedname.setText("模考平均分");
			holder.txt_finished.setText(avgScore);
		}
		
		String datediff = mList.get(position).get("DateDiff").toString();
		map.put(position, datediff);
		/**
		 * 判断倒计时
		 */
		if (map.get(position).equals("-1")) {
			holder.relative.setVisibility(View.INVISIBLE);
		}else{
			holder.relative.setVisibility(View.VISIBLE);
			holder.txt_downdays.setText(datediff);
		}
		
		
		holder.txt_num.setText(mList.get(position).get("sCode").toString());
		String sex_tag = mList.get(position).get("nGender").toString();
		if (sex_tag.equals("1")) {
			holder.sex_image.setImageResource(R.drawable.nan);
		} else if (sex_tag.equals("2")) {
			holder.sex_image.setImageResource(R.drawable.nv);
		}
		if (mList.get(position).get("IconUrl").toString().equals("null")||mList.get(position).get("IconUrl").toString().equals("")) {
			holder.head_image.setImageResource(R.drawable.mor);
		} else {
			ImageLoader.getInstance().displayImage(
					Constants.URL_TeacherIMG
							+ mList.get(position).get("IconUrl").toString(),
					holder.head_image);
		}

		return convertView;
	}

	class ViewHolder {
		ImageView head_image, sex_image;
		TextView txt_name, txt_num, txt_finished, txt_downdays,txt_finishedname;
		RelativeLayout relative;
	}

}
