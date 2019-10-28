/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015-10-17 
 * 
 *******************************************************************************/ 
package com.lels.vote.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.hello.R;
import com.lels.bean.Voteopts;
import com.lels.vote.adapter.HistoryVoteAdapter.ViewHolder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015-10-17
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class HistoryVoteChooseAdapter extends BaseAdapter{

	private Context mContext;
	private List<Voteopts> mlist;
	private int percent;
	private String ownVote;
	
	/**
	 * @param mContext
	 * @param mlist
	 */
	public HistoryVoteChooseAdapter(Context mContext, List<Voteopts> mlist) {
		super();
		this.mContext = mContext;
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
	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_history_vote_choose, null);
			holder = new ViewHolder();
			holder.txt_vote_choose = (TextView) convertView.findViewById(R.id.txt_vote_choose);
			holder.txt_vote_percent = (TextView) convertView.findViewById(R.id.txt_vote_percent);
			holder.txt_vote_content = (TextView) convertView.findViewById(R.id.txt_vote_history_content);
			convertView.setTag(holder);
		}else{
			
			holder = (ViewHolder) convertView.getTag();
		}
//		System.out.println("选项的详情记录="+mlist);
//		 ownVote = 是否是自己投的票，1是0不是,
		ownVote = mlist.get(position).getOwnVote();
		if(ownVote.equals("1")){
			holder.txt_vote_choose.setBackground(mContext.getResources().getDrawable(R.drawable.own_toupiao));

		}else{
			
			holder.txt_vote_choose.setBackground(mContext.getResources().getDrawable(R.drawable.other_toupiao));
		}
		
		//获取屏幕的高和宽
		WindowManager wm = ((Activity) mContext).getWindowManager();
		    int screen_width = wm.getDefaultDisplay().getWidth();
		    int screen_heigth = wm.getDefaultDisplay().getHeight();
		    System.out.println("屏幕的高和宽====="+screen_heigth+"==="+screen_width);
		    percent = Integer.parseInt(mlist.get(position).getVoteNum());
		    
			LayoutParams lp = 	holder.txt_vote_choose.getLayoutParams();
			lp.width = (int) ((int) (screen_width*(percent*0.01)));
			holder.txt_vote_choose.setLayoutParams(lp);
			

		holder.txt_vote_content.setText(mlist.get(position).getOptionDesc());
		holder.txt_vote_percent.setText(mlist.get(position).getVoteNum()+"%");
		return convertView;
	}
	
	class ViewHolder {
		private TextView txt_vote_choose;
		private TextView txt_vote_percent;
		private TextView txt_vote_content;
	}

}
