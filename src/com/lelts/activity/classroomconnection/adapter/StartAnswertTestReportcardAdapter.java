/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年8月11日 
 * 
 *******************************************************************************/ 
package com.lelts.activity.classroomconnection.adapter;

import java.util.List;

import com.example.hello.R;
import com.lels.bean.StartAnswertestInfo;
import com.lels.constants.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年8月11日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class StartAnswertTestReportcardAdapter extends BaseAdapter{


	private Context context;
	private List<StartAnswertestInfo> mlist;
	Viewholder1 holder1 = null;

	//倒计时的天数
	private int TargetDateDiff;

	public StartAnswertTestReportcardAdapter(Context context,
			List<StartAnswertestInfo> mlist) {
		super();
		this.context = context;
		this.mlist = mlist;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.isEmpty() ? 0 : mlist.size();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlist.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			holder1 = new Viewholder1();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_start_answer_report_card, null);
			holder1.img_sex = (ImageView) convertView
					.findViewById(R.id.item_img_sex_start_answer);
			holder1.img_student = (ImageView) convertView
					.findViewById(R.id.item_img_student_start_answer);
			holder1.txt_answer = (TextView) convertView
					.findViewById(R.id.item_txt_answer_start_answer);
			holder1.txt_code = (TextView) convertView
					.findViewById(R.id.item_txt_studentcode_start_answer);
			holder1.txt_studentname = (TextView) convertView
					.findViewById(R.id.item_txt_studentname_start_answer);
			holder1.txt_time = (TextView) convertView
					.findViewById(R.id.item_txt_time_start_answer);
			convertView.setTag(holder1);
			holder1.txt_countdown = (TextView) convertView.findViewById(R.id.item_txt_countdowntime_start_answer);

		} else {
			holder1 = (Viewholder1) convertView.getTag();
		}

		if (mlist.get(position).getSex().toString().equals("1")) {
			holder1.img_sex.setImageResource(R.drawable.ioc_nan2x);
		} else {
			holder1.img_sex.setImageResource(R.drawable.ioc_nv2x);
		}
		ImageLoader.getInstance().displayImage(
				Constants.URL_TeacherIMG + mlist.get(position).getUrl(),
				holder1.img_student);
		holder1.txt_answer.setText(mlist.get(position).getAnswer());
		TargetDateDiff = Integer.parseInt(mlist.get(position).getCountdown().toString());
		if(TargetDateDiff == -1){
			//倒计时 隐藏
			holder1.txt_code.setVisibility(View.INVISIBLE);
		}else{
			
			holder1.txt_code.setText("雅思考试倒计时："+mlist.get(position).getCountdown()+"天");
		}
		//holder1.txt_code.setText("雅思考试倒计时："+mlist.get(position).getCountdown()+"天");
		holder1.txt_studentname.setText(mlist.get(position).getName());
		holder1.txt_time.setText(mlist.get(position).getTime());
		holder1.txt_countdown.setText(mlist.get(position).getCode());
		return convertView;

	}

	class Viewholder1 {
		ImageView img_student, img_sex;
		TextView txt_studentname, txt_code, txt_answer, txt_time,txt_countdown;

	}

}
