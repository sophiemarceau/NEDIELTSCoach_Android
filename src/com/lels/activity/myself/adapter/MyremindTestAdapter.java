package com.lels.activity.myself.adapter;

import java.util.List;

import com.example.hello.R;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.lels.activity.myself.MyselfRemindActivity;
import com.lels.alarm.AlarmService;
import com.lelts.tool.ClockInfo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyremindTestAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	Context mContext;
	// List<HashMap<String, Object>> mlist;
	List<ClockInfo> mlist;

	private SwipeListView listview_remind;

	public MyremindTestAdapter(Context context, List<ClockInfo> list_c,
			SwipeListView listview) {
		this.mContext = context;
		this.mlist = list_c;
		this.listview_remind = listview;

	}

	public void setUserEntities(List<ClockInfo> userEntities) {
		this.mlist = userEntities;
		this.notifyDataSetChanged();
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

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewholer;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_myself_myremind_test, null);

			viewholer = new ViewHolder();
			viewholer.textview_remind_time = (TextView) convertView.findViewById(R.id.textview_remind_time);
			viewholer.textview_remind_time1 = (TextView) convertView.findViewById(R.id.textview_remind_time1);

			viewholer.imageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
			
			viewholer.textview_remind_title = (TextView) convertView.findViewById(R.id.textview_remind_title);
			viewholer.textview_remind_address = (TextView) convertView.findViewById(R.id.textview_remind_address);

			viewholer.btn_delete = (Button) convertView.findViewById(R.id.btn_delete_stu_start_item);

			convertView.setTag(viewholer);
		} else {

			viewholer = (ViewHolder) convertView.getTag();

		}

		if (mlist.size() > 0) {

			String star = mlist.get(position).getStarttime();
			System.out.println("时间===============" + star);
			String[] star_s = star.split(" ");
			// 日期
			String s = star_s[0];
			// 时分
			String n = star_s[1];

			// 截取 时分 变成 二个字节
			String[] n_s = n.split(":");
			String n_m = n_s[0];
			String n_se = n_s[1];
			if (n_m.length() == 1) {
				n_m = "0" + n_m;
			}
			if (n_se.length() == 1) {
				n_se = "0" + n_se;
			}
			n = n_m + ":" + n_se;

			viewholer.textview_remind_time.setText(s);
			viewholer.textview_remind_time1.setText(n);
			//改变颜色
			viewholer.imageView1.setBackgroundResource(R.drawable.hongdian);
			viewholer.textview_remind_time.setTextColor(Color.RED);
			viewholer.textview_remind_time1.setTextColor(Color.RED);

			viewholer.textview_remind_title.setText(mlist.get(position).getTitle());
			viewholer.textview_remind_address.setText(mlist.get(position).getEndtime());
		}

		viewholer.btn_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int id = mlist.get(position).getId();
				listview_remind.closeAnimate(position);
				System.out.println("mlist.get(position).getId()====华东删除的id==" + mlist.get(position).getId());
				MyselfRemindActivity.delete(String.valueOf(id));
//				deletetest(position);
				listview_remind.dismiss(position);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView textview_remind_time,textview_remind_time1;
		TextView textview_remind_title;
		TextView textview_remind_address;
		ImageView imageView1;
		Button btn_delete;
	}
	
//	public void del(MyselfRemindActivity context){
//		Intent intent = new Intent(context,AlarmService.class);
//    	PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);
//		//获取闹钟管理器
//    	AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//    	alarmManager.cancel(pendingIntent);
//	}
}
