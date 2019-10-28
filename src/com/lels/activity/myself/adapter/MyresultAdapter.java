package com.lels.activity.myself.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hello.R;
import com.lels.activity.myself.ImageActivity;
import com.lels.constants.Constants;
import com.lelts.tool.IntentUtlis;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyresultAdapter extends BaseAdapter {

	private LayoutInflater inflater;

	Context mContext;
	List<HashMap<String, Object>> mlist;
	private Map<Integer, String> map;

	public MyresultAdapter(Context context, List<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mlist = list;
		map = new HashMap<Integer, String>();
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
					R.layout.item_myself_myresult, null);

			viewholer = new ViewHolder();
			viewholer.roundimageview_student_pic = (ImageView) convertView
					.findViewById(R.id.roundimageview_student_pic);
			viewholer.textview_student_name = (TextView) convertView
					.findViewById(R.id.textview_student_name);

			viewholer.imageview_sex = (ImageView) convertView
					.findViewById(R.id.imageview_sex);

			viewholer.textview_student_num = (TextView) convertView
					.findViewById(R.id.textview_student_num);
			viewholer.textview_student_day = (TextView) convertView
					.findViewById(R.id.textview_student_day);

			viewholer.textview_listener_score = (TextView) convertView
					.findViewById(R.id.textview_listener_score);
			viewholer.textview_read_score = (TextView) convertView
					.findViewById(R.id.textview_read_score);
			viewholer.textview_write_score = (TextView) convertView
					.findViewById(R.id.textview_write_score);
			viewholer.textview_speak_score = (TextView) convertView
					.findViewById(R.id.textview_speak_score);
			viewholer.textview_all_score = (TextView) convertView
					.findViewById(R.id.textview_all_score);
			viewholer.btn = (Button) convertView
					.findViewById(R.id.button_study_result);
			viewholer.relative = (RelativeLayout) convertView
					.findViewById(R.id.relative);
			convertView.setTag(viewholer);
		} else {

			viewholer = (ViewHolder) convertView.getTag();

		}

		if (mlist.size() > 0) {
			if (mlist.get(position).get("IconUrl").toString().equals("")
					|| mlist.get(position).get("IconUrl").toString()
							.equals("null")) {
				viewholer.roundimageview_student_pic
						.setImageResource(R.drawable.mor);
			} else {
				ImageLoader.getInstance()
						.displayImage(
								Constants.URL_TeacherIMG
										+ mlist.get(position).get("IconUrl")
												.toString(),
								viewholer.roundimageview_student_pic);
			}
			// nGender =性别[1男2女];
			if (mlist.get(position).get("nGender").toString()
					.equalsIgnoreCase("1")) {
				viewholer.imageview_sex.setImageResource(R.drawable.ioc_nan2x);
			} else {
				viewholer.imageview_sex.setImageResource(R.drawable.ioc_nv2x);
			}
			if (mlist.get(position).get("sName").toString().equals("null")) {
				viewholer.textview_student_name.setText("");
			} else {
				viewholer.textview_student_name.setText(mlist.get(position)
						.get("sName").toString());
			}
			if (mlist.get(position).get("sCode").toString().equals("null")) {
				viewholer.textview_student_num.setText("");
			} else {
				viewholer.textview_student_num.setText(mlist.get(position)
						.get("sCode").toString());
			}
			String date = mlist.get(position).get("DateDiff").toString();
			map.put(position, date);
			if (!map.get(position).endsWith("-1")) {
				// 天数
				viewholer.relative.setVisibility(View.VISIBLE);
				viewholer.textview_student_day.setText(mlist.get(position)
						.get("DateDiff").toString());
			} else {
				viewholer.relative.setVisibility(View.INVISIBLE);
			}
			viewholer.textview_listener_score.setText(Float.valueOf(mlist
					.get(position).get("ListenScore").toString())
					+ "");
			viewholer.textview_read_score.setText(Float.valueOf(mlist
					.get(position).get("ReadScore").toString())
					+ "");
			viewholer.textview_write_score.setText(Float.valueOf(mlist
					.get(position).get("WriteScore").toString())
					+ "");
			viewholer.textview_speak_score.setText(Float.valueOf(mlist
					.get(position).get("SpeakScore").toString())
					+ "");
			viewholer.textview_all_score.setText(Float.valueOf(mlist
					.get(position).get("TotalScore").toString())
					+ "");
			viewholer.btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String reportigname = mlist.get(position)
							.get("ReportImgName").toString();
					System.out.println("reportigname>>>>>" + reportigname);
					if (reportigname.equals("null") || reportigname.equals("")) {
						Toast.makeText(mContext, "此学生还未上传成绩单", 0).show();
					} else {
						Intent intent = new Intent();
						intent.putExtra("image", reportigname);
						intent.setClass(mContext, ImageActivity.class);
						mContext.startActivity(intent);
					}
				}
			});
		}
		return convertView;
	}

	class ViewHolder {

		ImageView roundimageview_student_pic; // head pic

		ImageView imageview_sex; // sex

		TextView textview_student_name;// name

		TextView textview_student_num;// student num

		TextView textview_student_day;// away test days

		TextView textview_listener_score;
		TextView textview_read_score;
		TextView textview_write_score;
		TextView textview_speak_score;
		TextView textview_all_score;
		Button btn;
		RelativeLayout relative;
	}

}
