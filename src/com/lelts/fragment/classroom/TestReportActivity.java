/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年7月3日 
 * 
 *******************************************************************************/
package com.lelts.fragment.classroom;

import com.example.hello.R;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年7月3日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class TestReportActivity extends Activity implements OnClickListener {
	private TextView txt_testreport, txt_reportcard;
	private int choose;
	private String paperId;
	private ImageButton img_back;
	private Intent intent;
	private Context context;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_report);
		initview();
	}

	/**
	 * 方法说明：初始化控件
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub
		txt_testreport = (TextView) findViewById(R.id.txt_left_test_report);
		txt_reportcard = (TextView) findViewById(R.id.txt_rigth_test_report);
		img_back = (ImageButton) findViewById(R.id.img_back_test_report);
		txt_reportcard.setOnClickListener(this);
		txt_testreport.setOnClickListener(this);
		img_back.setOnClickListener(this);
		Intent getchoose = getIntent();
		choose = getchoose.getIntExtra("choose", -1);
		paperId = getchoose.getStringExtra("paperId");
		FragmentManager fragment = getFragmentManager();
		FragmentTransaction trans = fragment.beginTransaction();
		TestReportFm testreportfm = new TestReportFm();
		context = this;
		trans.add(R.id.my_relative_report, testreportfm);
		trans.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		FragmentManager fragment = getFragmentManager();
		FragmentTransaction trans = fragment.beginTransaction();
		switch (v.getId()) {
		//练习报告
		case R.id.txt_left_test_report:
			txt_testreport.setTextColor(Color.WHITE);
			txt_reportcard.setTextColor(Color.RED);
			txt_testreport.setBackgroundResource(R.drawable.left_red);
			txt_reportcard.setBackgroundResource(R.drawable.right_kong);
			TestReportFm testreportfm = new TestReportFm();
 			trans.replace(R.id.my_relative_report, testreportfm);
			break;
		//成绩单
		case R.id.txt_rigth_test_report:
			txt_testreport.setTextColor(Color.RED);
			txt_reportcard.setTextColor(Color.WHITE);
			txt_testreport.setBackgroundResource(R.drawable.left_null);
			txt_reportcard.setBackgroundResource(R.drawable.right_red);
			System.out.println("choose=====>"+choose);
			ReportCardFm reportcardfm = new ReportCardFm();
			trans.replace(R.id.my_relative_report, reportcardfm);
			break;
		//返回键 
		case R.id.img_back_test_report:
//			intent = new Intent(context,AnswerSheetActivity.class);
//			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		
		trans.commit();
	}
	
	

}
