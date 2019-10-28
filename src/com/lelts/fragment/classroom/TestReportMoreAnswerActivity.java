/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年7月22日 
 * 
 *******************************************************************************/
package com.lelts.fragment.classroom;

import java.util.ArrayList;
import java.util.List;

import com.example.hello.R;
import com.lels.bean.OptionInfo;
import com.lels.bean.StartAnswertestInfo;
import com.lels.bean.StudentChooseInfo;
import com.lelts.activity.classroomconnection.adapter.TestReportMoreAnswerAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
public class TestReportMoreAnswerActivity extends Activity implements OnClickListener{
	private Context context;
	private ListView mlistview;
	private TestReportMoreAnswerAdapter madapter;
	private List<StudentChooseInfo> mlist;
	private TextView txt_title;
	private String select;
	private ImageButton img_back;
	private Intent intent;
	public static String trueanwer;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_report_moreanswer);
		initview();
	}

	/**
	 * 方法说明：初始化控件
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = this;
		intent  = new Intent();
		Intent getchooselist = getIntent();
		OptionInfo chooselistinfo = (OptionInfo) getchooselist
				.getSerializableExtra("chooselist");
		trueanwer = getchooselist.getStringExtra("sure_answer");
		System.out.println("传过来的正确答案===="+trueanwer);
		System.out.println("传过来的chooselist===" + chooselistinfo);
		img_back = (ImageButton) findViewById(R.id.btn_back_testreport_moreanswer);
		txt_title = (TextView) findViewById(R.id.txt_title_testreport_moreanswer);
		img_back.setOnClickListener(this);
		select = chooselistinfo.getOption();
		txt_title.setText("选择答案"+select+"的学员");
		mlist = chooselistinfo.getStudentChooseList();
		mlistview = (ListView) findViewById(R.id.listview_testreport_moreanswer);
		madapter = new TestReportMoreAnswerAdapter(mlist, context);
		mlistview.setAdapter(madapter);
		
		
		
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 实现 按钮的监听
	 * 
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		//返回键
		case R.id.btn_back_testreport_moreanswer:
//			intent.setClass(TestReportMoreAnswerActivity.this, TestReportActivity.class);
//			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

	
}