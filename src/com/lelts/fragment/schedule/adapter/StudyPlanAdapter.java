package com.lelts.fragment.schedule.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hello.R;
import com.lelts.activity.classes.Class_ing_ContentActivity;

public class StudyPlanAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private SharedPreferences shareclass_ing;
	private Editor editor;
	Context mContext;
	List<HashMap<String, Object>> mlist;

	private boolean isccc;

	public StudyPlanAdapter(Context context,
			List<HashMap<String, Object>> list, boolean ss) {
		this.mContext = context;
		this.mlist = list;
		this.isccc = ss;
		shareclass_ing = mContext.getSharedPreferences("clcode", mContext.MODE_PRIVATE);
		editor = shareclass_ing.edit();
		System.out.println("是否可以跳转啊=========================" + isccc);
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
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_studyplan_main, null);

			viewholer = new ViewHolder();
			viewholer.textview_plan_starttime = (TextView) convertView
					.findViewById(R.id.textview_plan_starttime);
			viewholer.textview_plan_endtime = (TextView) convertView
					.findViewById(R.id.textview_plan_endtime);
			viewholer.textview_plan_title = (TextView) convertView
					.findViewById(R.id.textview_plan_title);
			viewholer.textview_plan_class_num = (TextView) convertView
					.findViewById(R.id.textview_plan_class_num);
			viewholer.textview_plan_address = (TextView) convertView
					.findViewById(R.id.textview_plan_address);
			viewholer.imageview_plan_join = (ImageView) convertView
					.findViewById(R.id.imageview_plan_join);
			viewholer.view_redline = (View) convertView
					.findViewById(R.id.view_redline);

			convertView.setTag(viewholer);
		} else {

			viewholer = (ViewHolder) convertView.getTag();

		}

		if (isccc) {
			viewholer.imageview_plan_join.setVisibility(View.VISIBLE);
			viewholer.view_redline.setVisibility(View.VISIBLE);
			/*if (position == 0) {
				viewholer.view_redline.setVisibility(View.VISIBLE);
			} else {
				viewholer.view_redline.setVisibility(View.INVISIBLE);
			}*/
			if ((Boolean)mlist.get(position).get("inTime")) {
				viewholer.view_redline.setVisibility(View.VISIBLE);
			} else {
				viewholer.view_redline.setVisibility(View.INVISIBLE);
			}
		}

		if (mlist.size() > 0) {

			String begintime = mlist.get(position).get("SectBegin").toString();
			String endtime = mlist.get(position).get("SectEnd").toString();

			// if (position == 0) {
			// viewholer.view_redline.setVisibility(View.VISIBLE);
			// }

			viewholer.textview_plan_starttime.setText(getStrTime(begintime));
			viewholer.textview_plan_endtime.setText(getStrTime(endtime));

			viewholer.textview_plan_title.setText(mlist.get(position)
					.get("sNameBc").toString());
			viewholer.textview_plan_class_num.setText("第"
					+ mlist.get(position).get("nLessonNo").toString() + "次课");
			String sAddress = mlist.get(position)
					.get("sAddress").toString();
			String sNameBr = mlist.get(position)
					.get("sNameBr").toString();
			if (sAddress.equals("null")) {
				viewholer.textview_plan_address.setText(""+sNameBr);
			}else if (sNameBr.equals("null")) {
				viewholer.textview_plan_address.setText(sAddress+"");
			}else{
				viewholer.textview_plan_address.setText(sAddress+sNameBr);
			}
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				System.out.println("点击事件");

				if (isccc == true) {
					System.out.println("点击跳转事件");
					Intent intent = new Intent();
					intent.setClass(mContext, Class_ing_ContentActivity.class);
					editor.putString("sName", mlist.get(position).get("sNameBc").toString());
					editor.putString("scode", mlist.get(position).get("sCode").toString());
					editor.putString("nowLessonId", mlist.get(position).get("id").toString());
					editor.putString("id", mlist.get(position).get("classId").toString());
					editor.commit();
					/*intent.putExtra("sName", mlist.get(position).get("sNameBc")
							.toString());
					intent.putExtra("scode", mlist.get(position).get("sCode")
							.toString());
					intent.putExtra("nowLessonId",
							mlist.get(position).get("nowLessonId").toString());
					intent.putExtra("id", mlist.get(position).get("id")
							.toString());
*/
					mContext.startActivity(intent);
				} else {
					System.out.println("点击不跳转事件");
				}

			}
		});

		return convertView;
	}

	class ViewHolder {
		TextView textview_plan_starttime;
		TextView textview_plan_endtime;
		TextView textview_plan_title;
		TextView textview_plan_class_num;
		TextView textview_plan_address;

		ImageView imageview_plan_join;

		View view_redline;
	}

	// 将时间戳转为字符串
	public static String getStrTime(String cc_time) {
		String re_StrTime = null;
		String re_re = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);

		re_StrTime = sdf.format(new Date(lcc_time));
		System.out.println("re_re的长度为========================"
				+ re_StrTime.length());
		re_re = re_StrTime.substring(11, 16);
		System.out.println("re_re===============" + re_re);
		return re_re;

	}

}
