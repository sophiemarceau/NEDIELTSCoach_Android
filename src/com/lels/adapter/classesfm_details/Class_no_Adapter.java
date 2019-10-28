package com.lels.adapter.classesfm_details;

import java.util.HashMap;
import java.util.List;

import com.example.hello.R;
import com.lels.bean.Teacher;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Class_no_Adapter extends BaseAdapter {
	private List<HashMap<String, Object>> mList;
	private Context mContext;

	public Class_no_Adapter(List<HashMap<String, Object>> mList,
			Context mContext) {
		super();
		this.mList = mList;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return mList.isEmpty() ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_classes_teachers, null);
			holder.mTeacher_photo = (ImageView) convertView
					.findViewById(R.id.classes_teacher_photo);
			holder.mTeacher_name = (TextView) convertView
					.findViewById(R.id.classes_teacher_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mTeacher_name.setText(mList.get(position).get("sName")
				.toString());
		System.out.println(holder.mTeacher_name);
		String path= mList.get(position).get("IconUrl").toString();
    	if(path.equals("null")||path.equals("")){
    		
    	}else{
    		
    		String p = path;
    		ImageLoader.getInstance().displayImage(p, holder.mTeacher_photo);
    	}
		return convertView;
	}

	class ViewHolder {
		TextView mTeacher_name, mTeacher_function;
		ImageView mTeacher_photo;
	}
}
