package com.lels.manual.adapter;

import java.util.List;

import com.example.hello.R;
import com.lels.constants.Constants;
import com.lels.group.tool.GroupStu;
import com.lels.manual.adapter.MymanualAdapter.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MymanualGrid_Adapter extends BaseAdapter {
	private Context context;
	private List<GroupStu> students;

	public MymanualGrid_Adapter(Context context, List<GroupStu> students) {
		super();
		this.context = context;
		this.students = students;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return students.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return students.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_group, null);
			holder.stu_name = (TextView) convertView
					.findViewById(R.id.group_stuname);
			holder.stu_img = (ImageView) convertView
					.findViewById(R.id.classes_teacher_photo);
			convertView.setTag(holder);
		}
		// hsl modify
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		// hsl modify

		/*
		 * map.put("sName", NND.getString("sName")); map.put("IconUrl",
		 * NND.getString("IconUrl"));
		 */
		System.out.println("students is null = "
				+ (students == null ? "null" : students.size()));
		holder.stu_name.setText(students.get(position).getsName());
		if (students.get(position).getIconUrl().length() > 0) {
			ImageLoader.getInstance().displayImage(
					Constants.URL_TeacherIMG
							+ students.get(position).getIconUrl(),
					holder.stu_img);
		} else { // 没有头像路径情况
			holder.stu_img.setImageResource(R.drawable.mor);
		}

		return convertView;
	}

	class ViewHolder {
		TextView stu_name;
		ImageView stu_img;
	}
}
