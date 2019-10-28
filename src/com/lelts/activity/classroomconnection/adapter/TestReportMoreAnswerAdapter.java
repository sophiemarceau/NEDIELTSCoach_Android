/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年7月22日 
 * 
 *******************************************************************************/ 
package com.lelts.activity.classroomconnection.adapter;

import java.util.List;

import com.example.hello.R;
import com.lels.bean.StartAnswertestInfo;
import com.lels.bean.StudentChooseInfo;
import com.lels.constants.Constants;
import com.lelts.fragment.classroom.TestReportMoreAnswerActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Color;
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
 * 编写日期:	2015年7月22日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class TestReportMoreAnswerAdapter extends BaseAdapter{
	private List<StudentChooseInfo> mlist;
	private Context context;
	//判断是否为正确的答案
	private String true_answer;
	//倒计时的天数
	private int TargetDateDiff;
	public TestReportMoreAnswerAdapter(List<StudentChooseInfo> mlist,
			Context context) {
		super();
		this.mlist = mlist;
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.isEmpty()?0:mlist.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mlist.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.item_testreport_moreanswer, null);
			holder = new ViewHolder();
			holder.txt_name = (TextView) convertView.findViewById(R.id.item_txt_name_testreport_moreanswer);
			holder.txt_code = (TextView) convertView.findViewById(R.id.item_txt_code_testreport_moreanswer);
			holder.txt_time = (TextView) convertView.findViewById(R.id.item_txt_time_testreport_moreanswer);
			holder.txt_answer = (TextView) convertView.findViewById(R.id.item_txt_answer_testreport_moreanswer);
			holder.img_stu  = (ImageView) convertView.findViewById(R.id.item_img_stu_testreport_moreanswer);
			holder.img_sex = (ImageView) convertView.findViewById(R.id.item_img_sex_testreport_moreanswer);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance().displayImage(Constants.URL_TeacherIMG+mlist.get(position).getIconUrl(), holder.img_stu);
		holder.txt_name.setText(mlist.get(position).getsName());
		holder.txt_code.setText(mlist.get(position).getsCode());
		holder.txt_answer.setText(mlist.get(position).getAnswerContent());
		TargetDateDiff = Integer.parseInt(mlist.get(position).getTargetDateDiff().toString());
		if(TargetDateDiff == -1){
			//倒计时 隐藏
			holder.txt_time.setVisibility(View.INVISIBLE);
		}else{
			
			holder.txt_time.setText("雅思考试倒计时："+mlist.get(position).getTargetDateDiff()+"天");
		}
		true_answer = TestReportMoreAnswerActivity.trueanwer;
		//如果 符合正确的答案则是 绿色，错误答案为 红色
		if(true_answer.equals(mlist.get(position).getAnswerContent())){
			//绿色

			holder.txt_answer.setTextColor(Color.parseColor("#20b2aa")); 
		}else{
			//红色
			holder.txt_answer.setTextColor(Color.RED);
		}
		
		//"nGender": 学生性别(1男2女)
		String sex = mlist.get(position).getnGender();
		if(sex.equals("1")){
			holder.img_sex.setImageResource(R.drawable.ioc_nan2x);
		}else{
			holder.img_sex.setImageResource(R.drawable.ioc_nv2x);
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView txt_name,txt_code,txt_time,txt_answer;
		ImageView img_stu,img_sex;
	}

}
