/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015-10-16 
 * 
 *******************************************************************************/
package com.lels.vote.adapter;

import java.util.HashMap;
import java.util.List;

import com.example.hello.R;
import com.lels.bean.VotehisVotes;
import com.lels.bean.Voteopts;
import com.lelts.tool.CalculateListviewGrideview;
import com.lelts.tool.GridViewForListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015-10-16
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class HistoryVoteAdapter extends BaseAdapter {
	private Context mContext;
	private List<VotehisVotes> mlist;
	private HistoryVoteChooseAdapter madapter;

	// private List<Voteopts> mchooselist;

	/**
	 * @param mContext
	 * @param mlist
	 */
	public HistoryVoteAdapter(Context mContext, List<VotehisVotes> mlist) {
		super();
		this.mContext = mContext;
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
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_history_vote, null);
			holder = new ViewHolder();
			holder.txt_history_content = (TextView) convertView
					.findViewById(R.id.votedetails_txt_content);
			holder.mlistview = (ListView) convertView
					.findViewById(R.id.votedetails_txt_choose_one);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}
		System.out.println("mlist的 历史的记录=" + mlist);
		holder.txt_history_content.setText(mlist.get(position).getSubject());
		madapter = new HistoryVoteChooseAdapter(mContext, mlist.get(position).getVoteoptslist());
		holder.mlistview.setAdapter(madapter);
		CalculateListviewGrideview
				.setListViewHeightBasedOnChildren(holder.mlistview);
		return convertView;
	}

	class ViewHolder {
		private TextView txt_history_content;
		private ListView mlistview;
	}

}
