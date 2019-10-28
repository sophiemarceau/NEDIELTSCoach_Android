package com.lels.random.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.hello.R;
import com.lels.constants.Constants;
import com.lels.group.tool.GroupStu;
import com.lelts.tool.ImageLoder;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RandomGrid_Adapter extends BaseAdapter {
	private Context mContext;
	// private List<HashMap<String, Object>> mList;
	private List<GroupStu> mList;

	public RandomGrid_Adapter(Context mContext, List<GroupStu> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
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
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_group, parent, false);
			holder.stu_name = (TextView) convertView
					.findViewById(R.id.group_stuname);
			holder.stu_img = (ImageView) convertView
					.findViewById(R.id.classes_teacher_photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.stu_name.setText(mList.get(position).getsName());
		String iocul = mList.get(position).getIconUrl();
		if (iocul.length() > 0 && !iocul.equals("null")) {
			ImageLoader.getInstance().displayImage(
					Constants.URL_TeacherIMG + iocul, holder.stu_img);
		} else {
			holder.stu_img.setImageResource(R.drawable.mor);
		}
		return convertView;
	}

	class ViewHolder {
		TextView stu_name;
		ImageView stu_img;
	}
}
