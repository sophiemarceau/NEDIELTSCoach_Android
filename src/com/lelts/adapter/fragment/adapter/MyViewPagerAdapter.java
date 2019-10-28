package com.lelts.adapter.fragment.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MyViewPagerAdapter extends PagerAdapter {

	private ArrayList<View> mList;
	@SuppressWarnings("unused")
	private Context context;

	public MyViewPagerAdapter(Context context, ArrayList<View> mList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mList = mList;
	}

	// 销毁position位置的界面
	@Override
	public void destroyItem(View v, int position, Object arg2) {
		((ViewPager) v).removeView(mList.get(position));

	}

	@Override
	public void finishUpdate(View arg0) {
	}

	// 获取当前窗体界面数
	@Override
	public int getCount() {
		return mList.size();
	}

	// 初始化position位置的界面
	@Override
	public Object instantiateItem(View v, int position) {
		((ViewPager) v).addView(mList.get(position));
		return mList.get(position);
	}

	// 判断是否由对象生成界面
	@Override
	public boolean isViewFromObject(View v, Object arg1) {
		return v == arg1;
	}

	@Override
	public void startUpdate(View arg0) {
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

}

