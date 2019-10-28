/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015-10-22 
 * 
 *******************************************************************************/ 
package com.lels.vote.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.hello.R;
import com.lels.vote.adapter.DialogVoteEndAdapter.ViewHolder;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015-10-22
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class VoteDetalisAdapter extends BaseAdapter{
	private List<HashMap<String, Object>> mlist;
	private Context context;
	
	private int percent;
	private String ownVote;
	/**
	 * @param mlist
	 * @param context
	 */
	public VoteDetalisAdapter(List<HashMap<String, Object>> mlist,
			Context context) {
		super();
		this.mlist = mlist;
		this.context = context;
	}
	public void setdatachanges(List<HashMap<String, Object>> mlist
			) {
		this.mlist = mlist;
		this.notifyDataSetChanged();
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
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mlist.get(arg0);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0 ;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@SuppressLint("NewApi") @Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) { 
			convertView = LayoutInflater.from(context).inflate(R.layout.item_vote_detalis, null);
			holder = new ViewHolder();
			holder.txt_vote_choose = (TextView) convertView.findViewById(R.id.txt_vote_choose);
			holder.txt_vote_percent = (TextView) convertView.findViewById(R.id.txt_vote_percent);
			holder.txt_vote_content = (TextView) convertView.findViewById(R.id.txt_vote_content);
			convertView.setTag(holder);

		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		// ownVote = 是否是自己投的票，1是0不是,
		ownVote = mlist.get(position).get("ownVote").toString();
		if (ownVote.equals("1")) {
			holder.txt_vote_choose.setBackground(context.getResources().getDrawable(R.drawable.own_toupiao));
		}else{
			holder.txt_vote_choose.setBackground(context.getResources().getDrawable(R.drawable.other_toupiao));
		}
		
		
		
//		//定义DisplayMetrics 对象    
//	      DisplayMetrics  dm = new DisplayMetrics(); 
//	      //取得窗口属性    
//	      ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);    
//	      //窗口的宽度    
//	      int screenWidth = dm.widthPixels;    
//	      //窗口高度    
//	      int screenHeight = dm.heightPixels;     
//	      System.out.println("screenWidth===="+screenWidth+"==  screenHeight==="+screenHeight);
		//获取屏幕的高和宽
	
		WindowManager wm = ((Activity) context).getWindowManager();
		    int screen_width = wm.getDefaultDisplay().getWidth();
		    int screen_heigth = wm.getDefaultDisplay().getHeight();
		    System.out.println("屏幕的高和宽====="+screen_heigth+"==="+screen_width);
		    percent = Integer.parseInt(mlist.get(position).get("voteNum").toString());
			holder.txt_vote_content.setText(mlist.get(position).get("OptionDesc").toString());
		System.out.println("percent====="+percent);
		LayoutParams lp = holder.txt_vote_choose.getLayoutParams();
		lp.width = (int) ((int) (screen_width*(percent*0.01)));
		holder.txt_vote_choose.setLayoutParams(lp);
		holder.txt_vote_percent.setText(mlist.get(position).get("voteNum").toString()+"%");
		return convertView;
	}
	
	class ViewHolder{
		private TextView txt_vote_choose;
		private TextView txt_vote_percent;
		private TextView txt_vote_content;
	}

}
