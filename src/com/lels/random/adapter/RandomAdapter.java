package com.lels.random.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import com.example.hello.R;
import com.lels.group.tool.GroupList;
import com.lels.group.tool.GroupStu;
import com.lelts.tool.CalculateListviewGrideview;
import com.lelts.tool.GridViewForGridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class RandomAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<GroupList> mList;
	private RandomGrid_Adapter  adapter;
	

	public RandomAdapter(Context mContext, List<GroupList> mList) {
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
		ViewHolder holder=null;
		if (convertView==null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.random_grid_item, null);
			holder.item_group_tv = (TextView) convertView
					.findViewById(R.id.item_group_tv);
			holder.item_group_gridView = (GridViewForGridview) convertView
					.findViewById(R.id.item_group_gridView);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0 ) {
			convertView.setPadding(0, 30, 0, 0);
		}
		holder.item_group_tv.setText("第"+(position+1)+"组("+mList.get(position).getStuList().size()+")");
		//获取组内成员
		getList(holder,position);
		
		return convertView;
	}

	private void getList(ViewHolder holder , int position) {
		// TODO Auto-generated method stub
		
		RandomGrid_Adapter  adapter = new RandomGrid_Adapter(mContext, mList.get(position).getStuList());
		holder.item_group_gridView.setAdapter(adapter);
		CalculateListviewGrideview.setListViewHeightBasedOnChildren1(holder.item_group_gridView);
		notifyDataSetChanged();
	}

	class ViewHolder {
		TextView item_group_tv;
		GridViewForGridview item_group_gridView;
	}
	
	public void knowIfData(){
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_group, null);
		
	}

}
