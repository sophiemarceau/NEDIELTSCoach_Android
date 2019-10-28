package com.lelts.indexpaper;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.example.hello.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class IndexPager extends Activity{
	private Context mContext;
	private ViewPager mViewPager;
	private List<View> viewList;
	private LayoutInflater inflater;
	private View vOnew,vTow,vThree,vFore;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_indexpager);
		
		initview();
	}
	private void initview() {
		mContext = this;
		mViewPager = (ViewPager) findViewById(R.id.indexvp);
		viewList = new ArrayList<View>();
		/*vOnew = inflater.inflate(R.layout.index_vp_one, null);
		vTow = inflater.inflate(R.layout.index_vp_tow, null);
		vThree = inflater.inflate(R.layout.index_vp_three, null);
		vFore = inflater.inflate(R.layout.index_vp_fore, null);*/
	}
}
 