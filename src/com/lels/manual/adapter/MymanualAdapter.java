package com.lels.manual.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.hello.R;
import com.lels.group.tool.GroupList;
import com.lels.group.tool.GroupStu;
import com.lelts.tool.CalculateListviewGrideview;
import com.lelts.tool.GridViewForGridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MymanualAdapter extends BaseAdapter {
	private Context context;
	private List<Integer> list;
	private int id = 100;
	private int crrentIndex=0;
	private List<GroupStu> students;
	private List<GroupList> groupList;
	private MymanualGrid_Adapter adapter;

	public int getCrrentIndex() {
		return crrentIndex;
	}

	public void setCrrentIndex(int crrentIndex) {
		this.crrentIndex = crrentIndex;
	}

	public List<GroupList> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<GroupList> groupList) {
		this.groupList = groupList;
		notifyDataSetChanged();
	}

	// hsl modify
	HashMap<Integer, GroupList> groups;

	public void setGroups(HashMap<Integer, GroupList> map) {
		this.groups = map;
		
	}

	// hsl modify

	/*
	 * public List<GroupStu> getStudents() { return students; }
	 * 
	 * public void setStudents(List<GroupStu> students) { this.students =
	 * students; notifyDataSetChanged(); }
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MymanualAdapter(Context context, List<Integer> list) {
		super();
		this.context = context;
		this.list = list;
		// students =new ArrayList<GroupStu>();
		groupList = new ArrayList<GroupList>();
		this.groups = new HashMap<Integer, GroupList>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
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
					R.layout.random_group_sj, null);
			holder.item_group_tv = (TextView) convertView
					.findViewById(R.id.item_group_tv);
			holder.item_group_gridView = (GridViewForGridview) convertView
					.findViewById(R.id.item_group_gridView);
			holder.noData_img = (ImageView) convertView
					.findViewById(R.id.noData_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
			
		if (position == 0) {
			convertView.setPadding(0, 30, 0, 0);
		} else {
		}
		// hsl modify
		if (null != groups.get(position + 1)) {

			List<GroupStu> students = groups.get(position + 1).getStuList();
			holder.item_group_tv.setText("第" + (position + 1) + "组("+students.size()+")");

			List<GroupStu> studentImgs = new ArrayList<GroupStu>();
			for (int i = 0; i < students.size(); i++) {
				if ((position + 1) == Integer.valueOf(students.get(i)
						.getGroupNum())) {
					holder.noData_img.setVisibility(View.GONE);
					holder.item_group_gridView.setVisibility(View.VISIBLE);
					studentImgs.add(students.get(i));
				}
			}
			if (!studentImgs.isEmpty()) {
				MymanualGrid_Adapter adapter = new MymanualGrid_Adapter(
						context, studentImgs);
				holder.item_group_gridView.setAdapter(adapter);
				CalculateListviewGrideview.setListViewHeightBasedOnChildren1(holder.item_group_gridView);
				notifyDataSetChanged();
			}
		}else{
			holder.item_group_tv.setText("第" + (position + 1) + "组");
		}
		// hsl modify
		return convertView;
	}

	class ViewHolder {
		TextView item_group_tv;
		GridViewForGridview item_group_gridView;
		ImageView noData_img;
	}
}
