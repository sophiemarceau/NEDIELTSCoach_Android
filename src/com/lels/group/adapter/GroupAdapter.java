package com.lels.group.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import com.example.hello.R;
import com.lels.constants.Constants;
import com.lelts.tool.ImageLoder;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupAdapter extends BaseAdapter {
	private Context context;
	private List<HashMap<String, Object>> list;

	public GroupAdapter(Context context, List<HashMap<String, Object>> list) {
		super();
		this.context = context;
		this.list = list;
	}
	public void setdatachanges(List<HashMap<String, Object>> mlist
			) {
		this.list = mlist;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_group, null);
			holder.stu_name = (TextView) convertView
					.findViewById(R.id.group_stuname);
			holder.stu_img = (ImageView) convertView
					.findViewById(R.id.classes_teacher_photo);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.stu_name.setText(list.get(position).get("studentname")
				.toString());
		String studentloginstatus = list.get(position)
				.get("studentloginstatus").toString();
		if (studentloginstatus.equals("1")) {
			ImageLoader.getInstance().displayImage(
					Constants.URL_TeacherIMG
							+ list.get(position).get("iconUrl").toString(),
					holder.stu_img);
			holder.stu_img.setAlpha(1.0f);
		} else {
			ImageLoader.getInstance().displayImage(
					Constants.URL_TeacherIMG
							+ list.get(position).get("iconUrl").toString(),
					holder.stu_img);
			holder.stu_img.setAlpha(0.5f);
			holder.stu_name.setTextColor(context.getResources().getColor(R.color.gray));
		}
		return convertView;
	}

	class ViewHolder {
		TextView stu_name;
		ImageView stu_img;
	}
}
