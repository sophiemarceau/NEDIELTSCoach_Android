/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年8月6日 
 * 
 *******************************************************************************/ 
package com.lelts.activity.classroomconnection.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.hello.R;
import com.lels.bean.AnswerInfo;
import com.lels.constants.Constants;
import com.lelts.tool.CalculateListviewGrideview;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年8月6日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class WholeTestReportAdapter extends BaseAdapter{
	private Context context;
	private List<AnswerInfo> mlist;
	//private boolean flag = true;
	private StrartMoreAnswerTestAdapter mgrideadapter;
	//这儿定义isSelected这个map是记录每个listitem的状态，初始状态全部为false。   
//	public static Map<Integer, Boolean> isSelected;    

	private int nGender;
	public WholeTestReportAdapter(Context context, List<AnswerInfo> mlist) {
		super();
		this.context = context;
		this.mlist = mlist;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mlist.isEmpty() ? 0 : mlist.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//final boolean flag = false;
		final Viewholder holder;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_start_more_answer, null);
			holder = new Viewholder();
			holder.mgrid = (ListView) convertView
					.findViewById(R.id.item_gridview_start_more_answer);
			holder.image_stu = (ImageView) convertView.findViewById(R.id.item_img_start_more_answer);
			holder.img_show = (RelativeLayout) convertView.findViewById(R.id.item_img_click_startore_answer);
			holder.image_sex = (ImageView) convertView.findViewById(R.id.item_img_sex_start_more_answer);
			holder.txt_name = (TextView) convertView.findViewById(R.id.item_txt_name_start_more_answer);
			holder.txt_num = (TextView) convertView.findViewById(R.id.item_txt_num_start_more_answer);
			holder.txt_time = (TextView) convertView.findViewById(R.id.item_txt_time_start_more_answer);
			holder.item_img_click_imageshow = (ImageView) convertView.findViewById(R.id.item_img_click_imageshow);
			holder.img_show.setTag(false);
			convertView.setTag(holder);

		}else{
			holder = (Viewholder) convertView.getTag();
		}
		
		holder.txt_name.setText(mlist.get(position).getName());
		holder.txt_num.setText(mlist.get(position).getsCode());
		holder.txt_time.setText(mlist.get(position).getCostTime());
		holder.mgrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
		nGender = mlist.get(position).getnGender();
		if(nGender==1){
			holder.image_sex.setBackgroundResource(R.drawable.ioc_nan2x);
		}else{
			holder.image_sex.setBackgroundResource(R.drawable.ioc_nv2x);
		}
		holder.mgrid.setItemsCanFocus(false);
		ImageLoader.getInstance().displayImage(Constants.URL_TeacherIMG + mlist.get(position).getIconUrl(), holder.image_stu);
		
		
		
//		OnOpenListener mOnOpenListener = new OnOpenListener(position);
//		holder.img_show.setOnClickListener(mOnOpenListener);
		
		holder.img_show.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				AnswerInfo info = mlist.get(position);
//				info.setFlag(!mlist.get(position).getFlag());
				// TODO Auto-generated method stub
				boolean	op =(Boolean) v.getTag();
				if (op) {
					CalculateListviewGrideview.setGridViewHeightBasedOnChildren(holder.mgrid);
//					LayoutParams params = new LayoutParams(
//							LayoutParams.MATCH_PARENT,
//							300);
//					holder.mgrid.setLayoutParams(params);
					holder.item_img_click_imageshow .setImageResource(R.drawable.zhangkaihou);
					v.setTag(false);  
					System.out.println("到底是不是能点击  op"+op);
				} else {
					LayoutParams params = new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT);
					holder.mgrid.setLayoutParams(params);
					holder.item_img_click_imageshow .setImageResource(R.drawable.zhangkaiqian);
					v.setTag(true);
					System.out.println("到底是不是能点击  op"+op);

				}
			}
			
		});
		
		
		
		mgrideadapter = new StrartMoreAnswerTestAdapter(context, mlist.get(position).getExamAnswerList());
		holder.mgrid.setAdapter(mgrideadapter);
		return convertView;
	}
	
//	class OnOpenListener implements OnClickListener{
//
//		private int mCurrPosition;
//		
//		public OnOpenListener(int position) {
//			// TODO Auto-generated constructor stub
//			this.mCurrPosition = position;
//		}
//		
//		
//		/* (non-Javadoc)
//		 * @see android.view.View.OnClickListener#onClick(android.view.View)
//		 */
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			
//		AnswerInfo info = mlist.get(mCurrPosition);
//		//info.setFlag(!mlist.get(mCurrPosition).getFlag());
//		
//		
//		// TODO Auto-generated method stub
//		boolean	op =(Boolean) v.getTag();
//		if (!mlist.get(mCurrPosition).getFlag()) {
//			CalculateListviewGrideview.setGridViewHeightBasedOnChildren(holder.mgrid);
////			LayoutParams params = new LayoutParams(
////					LayoutParams.MATCH_PARENT,
////					300);
////			holder.mgrid.setLayoutParams(params);
//			holder.img_show.setImageResource(R.drawable.zhangkaihou);
//			v.setTag(false);  
//			System.out.println("到底是不是能点击  op"+op);
//			
//		} else {
//			LayoutParams params = new LayoutParams(
//					LayoutParams.MATCH_PARENT,
//					LayoutParams.WRAP_CONTENT);
//			holder.mgrid.setLayoutParams(params);
//			holder.img_show.setImageResource(R.drawable.zhangkaiqian);
//			v.setTag(true);
//			System.out.println("到底是不是能点击  op"+op);
//
//		}
//			
//		}
//		
//		
//	}
	
	
	class Viewholder {
		ListView mgrid;
		TextView txt_name, txt_num, txt_time;
		ImageView image_stu, image_sex,item_img_click_imageshow;
		RelativeLayout img_show;
	}

}
