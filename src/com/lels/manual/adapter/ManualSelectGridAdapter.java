package com.lels.manual.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.hello.R;
import com.lels.constants.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ManualSelectGridAdapter extends BaseAdapter {

	private List<HashMap<String, Object>> mList;
	private Context mContext;
	private boolean isChice[];

	public void setSelection(int position) {

	}

	public ManualSelectGridAdapter(List<HashMap<String, Object>> mList,
			Context mContext) {
		super();
		this.mList = mList;
		this.mContext = mContext;
		isChice = new boolean[mList.size()];
		for (int i = 0; i < mList.size(); i++) {
			isChice[i] = false;
		}
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
					R.layout.item_group, null);
			holder.stu_name = (TextView) convertView
					.findViewById(R.id.group_stuname);
			holder.stu_img = (ImageView) convertView
					.findViewById(R.id.classes_teacher_photo);
			holder.image_backgroud = (ImageView) convertView
					.findViewById(R.id.image_backgroud);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.stu_name.setText(mList.get(position).get("studentname")
				.toString());
		
		  if (mList.get(position).get("iconUrl").toString().length()>0) {
			  ImageLoader.getInstance().displayImage(
						Constants.URL_TeacherIMG
								+ mList.get(position).get("iconUrl").toString(),
						holder.stu_img);
		  
		  }else{ //没有头像路径情况
			  holder.stu_img.setImageResource(R.drawable.mor);
			  }
		  
		 
		getView(position, holder);
		return convertView;
	}

	class ViewHolder {
		TextView stu_name;
		ImageView stu_img, image_backgroud;
	}

	private void getView(int post, ViewHolder holder) {

		if (isChice[post] == true) {
			holder.image_backgroud.setVisibility(View.VISIBLE);

		} else {
			holder.image_backgroud.setVisibility(View.GONE);
		}

	}

	public void chiceState(int post) {
		isChice[post] = isChice[post] == true ? false : true;
		this.notifyDataSetChanged();
	}
}
