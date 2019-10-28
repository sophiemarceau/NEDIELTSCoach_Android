/*******************************************************************************
+ * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年7月9日 
 * 
 *******************************************************************************/
package com.lelts.activity.classroomconnection.adapter;

import java.util.List;

import com.example.hello.R;
import com.lels.bean.OptionInfo;
import com.lels.bean.QuestionData;
import com.lels.bean.StudentChooseInfo;
import com.lels.costum_widget.ReportView;
import com.lels.costum_widget.ReportView.MyOnclick;
import com.lelts.fragment.classroom.TestReportMoreAnswerActivity;
import com.lelts.tool.RoundProgressBar;
import com.lelts.tool.UtiltyHelper;
import com.lidroid.xutils.db.sqlite.CursorUtils.FindCacheSequence;

import android.R.layout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.ViewGroup.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年7月9日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
@SuppressLint("ResourceAsColor")
public class TestReportAdapter extends BaseAdapter implements OnClickListener {

	final int TYPE_1 = 0;
	final int TYPE_2 = 1;
	final int TYPE_3 = 2;

	private List<QuestionData> mlist;
	private List<OptionInfo> optionlist;
	private Context context;
	private List<StudentChooseInfo> chooselist;
	private int progress;
	private String select;

	private Intent intent;
	private int stuAnswerTotalCount;
	private String sure_answer;
	private float refer_margin;
	private int   screen_width;
	private int defaulta_width;

	public TestReportAdapter(List<QuestionData> mlist, Context context) {
		super();
		this.mlist = mlist;
		this.context = context;
		this.refer_margin = this.context.getResources().getDimension(R.dimen.refer_margin);
		WindowManager wm = ((Activity) context).getWindowManager();
	     screen_width = wm.getDefaultDisplay().getWidth();
	     defaulta_width = (int) (screen_width-(this.refer_margin*2));
		System.out.println("screen_width = " + screen_width + ",margin = " + this.refer_margin);
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

	// 返回list的子布局的总个数
	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 3;
	}

	// 确定加载的布局
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		int p = Integer.parseInt(mlist.get(position).getQuestionType());
		if (p == 1) {
			return TYPE_1;
		} else if (p == 2) {
			return TYPE_3;
		} else if (p==3){
			return TYPE_2;
		}
		return 0;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder1 holder1 = null;
		ViewHolder2 holder2 = null;
		ViewHolder3 holder3 = null;
		// 判断加载第几个布局的type变量
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case TYPE_1:
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_report, null);
				holder1 = new ViewHolder1();
				holder1.choose_txt_answer = (TextView) convertView
						.findViewById(R.id.item_txtanswer_test_report_choose);
				holder1.choose_txt_name = (TextView) convertView
						.findViewById(R.id.item_txtname_test_report_choose);
				holder1.choose_pro2 = (RoundProgressBar) convertView
						.findViewById(R.id.roundProgressBar2_choose);
				holder1.choose_pro3 = (RoundProgressBar) convertView
						.findViewById(R.id.roundProgressBar3_choose);
				holder1.image_show = (ImageView) convertView.findViewById(R.id.image_choose);
				/*holder1.btn_A =  (TextView) convertView
						.findViewById(R.id.btn1_choose);
				holder1.btn_B = (TextView) convertView
						.findViewById(R.id.btn2_choose);
				holder1.btn_C = (TextView) convertView
						.findViewById(R.id.btn3_choose);
				holder1.btn_D = (TextView) convertView
						.findViewById(R.id.btn4_choose);
				holder1.btn_A.setOnClickListener(this);
				holder1.btn_B.setOnClickListener(this);
				holder1.btn_C.setOnClickListener(this);
				holder1.btn_D.setOnClickListener(this);
				//获得ABCD的控件
				holder1.txt_A = (TextView) convertView.findViewById(R.id.textView1_choose);
				holder1.txt_B = (TextView) convertView.findViewById(R.id.textView2_choose);
				holder1.txt_C = (TextView) convertView.findViewById(R.id.textView3_choose);
				holder1.txt_D = (TextView) convertView.findViewById(R.id.textView4_choose);*/
				holder1.reportView = (ReportView) convertView.findViewById(R.id.reportview);

				convertView.setTag(holder1);
				break;
			case TYPE_2:
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_test_repor_complet, null);
				holder2 = new ViewHolder2();
				holder2.complet_txt_name = (TextView) convertView
						.findViewById(R.id.item_txtname_test_report_complet);
				holder2.complet_txt_answer = (TextView) convertView
						.findViewById(R.id.item_txtanswer_test_report_complet);
				holder2.complet_pro2 = (RoundProgressBar) convertView
						.findViewById(R.id.roundProgressBar2_complet);
				holder2.complet_pro3 = (RoundProgressBar) convertView
						.findViewById(R.id.roundProgressBar3_complet);
				holder2.btn_yes = (TextView) convertView
						.findViewById(R.id.btn1_complet);
				holder2.btn_no =(TextView) convertView
						.findViewById(R.id.btn2_complet);
				holder2.btn_yes.setOnClickListener(this);
				holder2.btn_no.setOnClickListener(this);
				holder2.txt_true = (TextView) convertView
						.findViewById(R.id.item_txttrue_test_report_complet);
				holder2.txt_true.setTag(position);
				holder2.txt_true.setOnClickListener(this);
				holder2.txt_defalut = (TextView) convertView.findViewById(R.id.txt_defalut_complet);
				//填空默认的长度
				holder2.btn1_complete_default = (TextView) convertView.findViewById(R.id.btn1_complete_default);

				convertView.setTag(holder2);
				break;

			case TYPE_3:
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_test_repor_just, null);
				holder3 = new ViewHolder3();
				holder3.just_txt_name = (TextView) convertView
						.findViewById(R.id.item_txtname_test_report_just);
				holder3.just_txt_answer = (TextView) convertView
						.findViewById(R.id.item_txtanswer_test_report_just);
				holder3.just_pro2 = (RoundProgressBar) convertView
						.findViewById(R.id.roundProgressBar2_just);
				holder3.just_pro3 = (RoundProgressBar) convertView
						.findViewById(R.id.roundProgressBar3_just);
				holder3.btn_true = (TextView) convertView
						.findViewById(R.id.btn1_just);
				holder3.btn_false = (TextView) convertView
						.findViewById(R.id.btn3_just);
				holder3.btn_notgiven = (TextView) convertView
						.findViewById(R.id.btn4_just);
				holder3.btn_true.setOnClickListener(this);
				holder3.btn_false.setOnClickListener(this);
				holder3.btn_notgiven.setOnClickListener(this);
				holder3.txt_true = (TextView) convertView
						.findViewById(R.id.item_txttrue_test_report_just);
				holder3.txt_true.setTag(position);
				holder3.txt_true.setOnClickListener(this);
				holder3.image_show = (ImageView) convertView.findViewById(R.id.image_juset);
				//获得True,False,Defalus;
				holder3.txt_yes = (TextView) convertView.findViewById(R.id.textView1_just);
				holder3.txt_no = (TextView) convertView.findViewById(R.id.textView3_just);
				holder3.txt_notgiven = (TextView) convertView.findViewById(R.id.textView4_just);

				convertView.setTag(holder3);
				break;

			}

		} else {
			switch (type) {
			case TYPE_1:
				holder1 = (ViewHolder1) convertView.getTag();
				break;
			case TYPE_2:
				holder2 = (ViewHolder2) convertView.getTag();
				break;
			case TYPE_3:
				holder3 = (ViewHolder3) convertView.getTag();
				break;
			}
		}

		// 设置属性
		switch (type) {
		case TYPE_1:
			
			// 设置位置
			/*holder1.btn_A.setTag(position);
			holder1.btn_B.setTag(position);
			holder1.btn_C.setTag(position);
			holder1.btn_D.setTag(position);*/
			holder1.txt_true = (TextView) convertView
					.findViewById(R.id.item_txttrue_test_report_choose);
			holder1.txt_true.setTag(position);
			holder1.txt_true.setOnClickListener(this);
			
			holder1.choose_pro2.setOnClickListener(this);
			holder1.choose_pro2.setTag(position);
			
			optionlist = mlist.get(position).getOptionList();
			holder1.choose_txt_answer.setText("正确答案："+mlist.get(position).getQSValue());
			holder1.choose_txt_name.setText(mlist.get(position).getTitle());

			System.out.println("holder1======optionlist集合的数据=======标题="
					+ mlist.get(position).getOptionList().toString());
			//获取imageview的高度
			LayoutParams para;
			int w = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);
			int h = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);
			holder1.image_show.measure(w, h);
			int height = holder1.image_show.getMeasuredHeight();
			int width = holder1.image_show.getMeasuredWidth();
			height = holder1.image_show.getMeasuredHeight();
		    System.out.println("总长度========para==="+height);
		    String max = mlist.get(position).getAccuracy().substring(0,mlist.get(position).getAccuracy().length() - 1);
			holder1.choose_pro2.setProgress(  Integer.parseInt(max));
			holder1.choose_pro3.setProgress(Integer.parseInt(max));
			
			stuAnswerTotalCount = mlist.get(position).getStuAnswerTotalCount();
			sure_answer = mlist.get(position).getQSValue();
			List<OptionInfo> optionInfo = mlist.get(position).getOptionList();
			int size = optionInfo.size();
			holder1.reportView.fit(size);
			for (int i = 0; i < size; i++) {
				holder1.reportView.setText(i, optionInfo.get(i).getStudentChooseCount());
				holder1.reportView.setHeight(i, (int) ((float)Integer.parseInt(optionInfo.get(i).getStudentChooseCount())/(float)stuAnswerTotalCount*height));
				if(sure_answer.equals(optionInfo.get(i).getOptionName())){
					holder1.reportView.setBackground(i, R.drawable.report_num_round_selected);
				}else{
					holder1.reportView.setBackground(i, R.drawable.report_num_round);
				}
			}
			holder1.reportView.setOnClick(new MyOnclick() {
				
				@Override
				public void onClick(View v, int pos) {
					// TODO Auto-generated method stub
					System.out.println("to fit index : " + pos);
					chooselist = mlist.get(position).getOptionList().get(pos)
							.getStudentChooseList();
					select = mlist.get(position).getOptionList().get(pos).getOption();
					
					sure_answer = mlist.get(position).getQSValue();
					System.out.println("选择的正确答案==="+sure_answer);
					OptionInfo optionnfo = new OptionInfo(select, chooselist);
					if (UtiltyHelper.isEmpty(optionnfo.getStudentChooseList())) {
						System.out.println("optionnfo.getStudentChooseList()包括null、 和空格");
					}else{
						System.out.println("optionnfo.getStudentChooseList()的值是==="+optionnfo.getStudentChooseList());
						if (optionnfo.getStudentChooseList().size()==0) {
							
						}else{
							intent = new Intent(context, TestReportMoreAnswerActivity.class);
							intent.putExtra("chooselist", optionnfo);
							intent.putExtra("sure_answer", sure_answer);
							context.startActivity(intent);
						}
					}
				}
			});
				
			/*int btn_A,btn_B,btn_C,btn_D = 0;
			btn_A = Integer.parseInt(mlist.get(position).getOptionList().get(0).getStudentChooseCount());
			btn_B = Integer.parseInt(mlist.get(position).getOptionList().get(1).getStudentChooseCount());
			btn_C = Integer.parseInt(mlist.get(position).getOptionList().get(2).getStudentChooseCount());
			
			stuAnswerTotalCount = mlist.get(position).getStuAnswerTotalCount();
			sure_answer = mlist.get(position).getQSValue();
			System.out.println("正确答案==sure_answer==="+sure_answer);
			System.out.println("答案的总个数==stuAnswerTotalCount==="+stuAnswerTotalCount);
			System.out.println("选择的人数====A=="+btn_A+"===B===="+btn_B+"===C===="+btn_C+"===D===="+btn_D);
			//判断是否有D的选项  
			if(mlist.get(position).getOptionList().size()>=4){
				//有D选项
				btn_D = Integer.parseInt(mlist.get(position).getOptionList().get(3).getStudentChooseCount());
				holder1.btn_D.setVisibility(View.VISIBLE);
				holder1.txt_D.setVisibility(View.VISIBLE);
				if(sure_answer.equals(mlist.get(position).getOptionList().get(3).getOptionName())){
					holder1.btn_D.setBackgroundResource(R.drawable.edit_round);
					holder1.txt_D.setTextColor(Color.RED);
				}else{
					holder1.btn_D.setBackgroundResource(R.drawable.edit_round2);
					holder1.txt_A.setTextColor(Color.BLACK);
					holder1.txt_B.setTextColor(Color.BLACK);
					holder1.txt_C.setTextColor(Color.BLACK);
				}	
				holder1.btn_D.setText(mlist.get(position).getOptionList().get(3)
						.getStudentChooseCount());
				holder1.btn_D.setHeight((int) ((float)btn_D/(float)stuAnswerTotalCount*height));
			}else{
				//没有D选项  消失
				holder1.txt_D.setVisibility(View.INVISIBLE);
				holder1.btn_D.setVisibility(View.INVISIBLE);
				
			
			}
			
			
			if(sure_answer.equals(mlist.get(position).getOptionList().get(0).getOptionName())){
				holder1.btn_A.setBackgroundResource(R.drawable.edit_round);
				holder1.txt_A.setTextColor(Color.RED);
			}else{
				holder1.btn_A.setBackgroundResource(R.drawable.edit_round2);
				holder1.txt_B.setTextColor(Color.BLACK);
				holder1.txt_C.setTextColor(Color.BLACK);
				holder1.txt_D.setTextColor(Color.BLACK);
			}
			if(sure_answer.equals(mlist.get(position).getOptionList().get(1).getOptionName())){
				holder1.btn_B.setBackgroundResource(R.drawable.edit_round);
				holder1.txt_B.setTextColor(Color.RED);
			}else{
				holder1.btn_B.setBackgroundResource(R.drawable.edit_round2);
				holder1.txt_A.setTextColor(Color.BLACK);
				holder1.txt_C.setTextColor(Color.BLACK);
				holder1.txt_D.setTextColor(Color.BLACK);
			}
			if(sure_answer.equals(mlist.get(position).getOptionList().get(2).getOptionName())){
				holder1.btn_C.setBackgroundResource(R.drawable.edit_round);
				holder1.txt_C.setTextColor(Color.RED);
			}else{
				holder1.btn_C.setBackgroundResource(R.drawable.edit_round2);
				holder1.txt_A.setTextColor(Color.BLACK);
				holder1.txt_B.setTextColor(Color.BLACK);
				holder1.txt_D.setTextColor(Color.BLACK);
			}	
			
			holder1.btn_A.setText(mlist.get(position).getOptionList().get(0)
					.getStudentChooseCount());
			holder1.btn_B.setText(mlist.get(position).getOptionList().get(1)
					.getStudentChooseCount());
			holder1.btn_C.setText(mlist.get(position).getOptionList().get(2)
					.getStudentChooseCount());
			//设置柱状图A
			holder1.btn_A.setHeight((int) ((float)btn_A/(float)stuAnswerTotalCount*height));
			holder1.btn_B.setHeight((int) ((float)btn_B/(float)stuAnswerTotalCount*height));
			holder1.btn_C.setHeight((int) ((float)btn_C/(float)stuAnswerTotalCount*height));*/
		
			
			break;
		case TYPE_2:
		
			// 设置位置
			holder2.btn_no.setTag(position);
			holder2.btn_yes.setTag(position);
			holder2.complet_pro2.setOnClickListener(this);
			holder2.complet_pro2.setTag(position);
			
			optionlist = mlist.get(position).getOptionList();
			holder2.complet_txt_answer
					.setText("正确答案："+mlist.get(position).getQSValue());
			holder2.complet_txt_name.setText(mlist.get(position).getTitle());
			System.out.println("holder2=====optionlist集合的数据=======标题="
					+ optionlist.toString());
			String max2 = mlist
					.get(position)
					.getAccuracy()
					.substring(
							0,
							mlist.get(position).getAccuracy().length() - 1);
			holder2.complet_pro2.setProgress(Integer.parseInt(max2));
			holder2.complet_pro3.setProgress(Integer.parseInt(max2));
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					String max = mlist
//							.get(position)
//							.getAccuracy()
//							.substring(
//									0,
//									mlist.get(position).getAccuracy().length() - 1);
//					while (progress <= Integer.parseInt(max)) {
//						
//						System.out.println(progress);
//						holder2.complet_pro2.setProgress(progress);
//						holder2.complet_pro3.setProgress(progress);
//						progress += 1;
//						try {
//							Thread.sleep(10);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//
//				}
//			}).start();
//			
			int btn_yes ,btn_no;
			btn_yes = Integer.parseInt(mlist.get(position).getOptionList().get(0).getStudentChooseCount());
			btn_no = Integer.parseInt(mlist.get(position).getOptionList().get(1).getStudentChooseCount());
			//获得默认的长度
//			WindowManager wm = ((Activity) context).getWindowManager();
//		     int screen_width = wm.getDefaultDisplay().getWidth();
//		     int screen_heigth = wm.getDefaultDisplay().getHeight();
//		     System.out.println("屏幕的高和宽====="+screen_heigth+"==="+screen_width);
		     //获取填空题的默认
//		     ViewTreeObserver vto2 = holder2.btn1_complete_default.getViewTreeObserver(); 
//		     vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
//		
//
//			@Override
//		     public void onGlobalLayout() { 
//		    	 holder2.btn1_complete_default.getViewTreeObserver().removeGlobalOnLayoutListener(this); 
//		    	screen_heigth = holder2.btn1_complete_default.getHeight();
//		    	screen_width = holder2.btn1_complete_default.getWidth();
//		    	 
//		   System.out.println("高 === 宽==="+holder2.btn1_complete_default.getHeight()+","+holder2.btn1_complete_default.getWidth());
//		     } 
//		     });
//			
		
//			screen_heigth = this.screen_heigth;
//			screen_width = this.screen_width;
//			int txt_w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
//			int txt_h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
//			holder2.btn1_complete_default.measure(txt_w, txt_h); 
//			int screen_heigth =holder2.btn1_complete_default.getMeasuredHeight(); 
//			int screen_width =holder2.btn1_complete_default.getMeasuredWidth(); 
//			ViewTreeObserver vto = holder2.btn1_complete_default.getViewTreeObserver(); 
//			vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { 
//			public boolean onPreDraw() { 
//				System.out.println("监听 控件");
//				screen_heigth = holder2.btn1_complete_default.getMeasuredHeight(); 
//				screen_width = holder2.btn1_complete_default.getMeasuredWidth(); 
//			return true; 
//			} 
//			}); 
			
//			ViewTreeObserver observer = convertView.getViewTreeObserver();  
//			observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {  
//			  
//			    @Override  
//			    public void onGlobalLayout() {  
//			        // TODO Auto-generated method stub  
//			    	screen_heigth = holder2.btn1_complete_default.getMeasuredHeight(); 
//					screen_width = holder2.btn1_complete_default.getMeasuredWidth();
//			    }  
//			});
//			int screen_width = holder2.btn1_complete_default.getWidth();
//			System.out.println("gao ,kuan =,"+screen_width);
			stuAnswerTotalCount = mlist.get(position).getStuAnswerTotalCount();
			sure_answer = mlist.get(position).getQSValue();
			System.out.println("正确答案==stuAnswerTotalCount==="+sure_answer);
			

			
			if(sure_answer.equals(mlist.get(position).getOptionList().get(0).getOption())){
				holder2.btn_yes.setBackgroundResource(R.drawable.edit_round);
			}else{
				holder2.btn_yes.setBackgroundResource(R.drawable.edit_round2);
			}
			if(sure_answer.equals(mlist.get(position).getOptionList().get(1).getOption())){
				holder2.btn_no.setBackgroundResource(R.drawable.edit_round);
			}else{
				holder2.btn_no.setBackgroundResource(R.drawable.edit_round2);
			}

			holder2.txt_defalut.setText(mlist.get(position).getOptionList().get(0)
					.getOption());
			holder2.btn_no.setText(mlist.get(position).getOptionList().get(1)
					.getOption());
			
			//设置填空题的高度
//			System.out.println("=1=====2====="+(int) ((float)btn_yes/(float)stuAnswerTotalCount*(screen_width))+(int) ((float)btn_no/(float)stuAnswerTotalCount*(screen_width)));
//			holder2.btn_yes.setWidth((int) ((float)btn_yes/(float)stuAnswerTotalCount*(screen_width-126)));
//			holder2.btn_no.setWidth((int) ((float)btn_no/(float)stuAnswerTotalCount*(screen_width-126)));
			System.out.println("默认的高度===="+defaulta_width);
			LayoutParams lp = holder2.btn_yes.getLayoutParams();
			lp.width = (int) ((float)btn_yes/(float)stuAnswerTotalCount*(defaulta_width));
			holder2.btn_yes.setLayoutParams(lp);
			LayoutParams lp_no = holder2.btn_no.getLayoutParams();
			lp_no.width = (int) ((float)btn_no/(float)stuAnswerTotalCount*(defaulta_width));
			holder2.btn_no.setLayoutParams(lp_no);
			break;

		case TYPE_3:
			//判断 题
			// 设置位置
			holder3.btn_true.setTag(position);
			holder3.btn_false.setTag(position);
			holder3.btn_notgiven.setTag(position);
			holder3.just_pro2.setOnClickListener(this);
			holder3.just_pro2.setTag(position);
			
			holder3.just_txt_answer.setText("正确答案："+mlist.get(position).getQSValue());
			holder3.just_txt_name.setText(mlist.get(position).getTitle());
			//System.out.println("holder3=====optionlist集合的数据=======标题="+ optionlist.toString());
			//获取imageview的高度
			 int w_juset = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);
				int h_juset = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);
				holder3.image_show.measure(w_juset, h_juset);
				int height_juset = holder3.image_show.getMeasuredHeight();
				int width_juset = holder3.image_show.getMeasuredWidth();
			 height = holder3.image_show.getMeasuredHeight();
		     System.out.println("总长度========para==="+height_juset);
		 	String max3 = mlist
					.get(position)
					.getAccuracy()
					.substring(
							0,
							mlist.get(position).getAccuracy().length() - 1);
				holder3.just_pro2.setProgress(Integer.parseInt(max3));
				holder3.just_pro3.setProgress(Integer.parseInt(max3));
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					String max = mlist
//							.get(position)
//							.getAccuracy()
//							.substring(
//									0,
//									mlist.get(position).getAccuracy().length() - 1);
//					while (progress <= Integer.parseInt(max)) {
//						
//						System.out.println(progress);
//						holder3.just_pro2.setProgress(progress);
//						holder3.just_pro3.setProgress(progress);
//						progress += 1;
//						try {
//							Thread.sleep(10);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//
//				}
//			}).start();
			optionlist = mlist.get(position).getOptionList();
			
			
			int btn_true,btn_false,btn_notgiven;
			btn_true = Integer.parseInt(mlist.get(position).getOptionList().get(0).getStudentChooseCount());
			btn_false = Integer.parseInt(mlist.get(position).getOptionList().get(1).getStudentChooseCount());
			btn_notgiven = Integer.parseInt(mlist.get(position).getOptionList().get(2).getStudentChooseCount());
			stuAnswerTotalCount = mlist.get(position).getStuAnswerTotalCount();
			sure_answer = mlist.get(position).getQSValue();
			System.out.println("正确答案==sure_answer==="+sure_answer);
			System.out.println("答案的总个数==stuAnswerTotalCount==="+stuAnswerTotalCount);
			if(sure_answer.equals(mlist.get(position).getOptionList().get(0).getOption())){
				holder3.btn_true.setBackgroundResource(R.drawable.edit_round);
				holder3.txt_yes.setTextColor(Color.RED);
			}else{
				holder3.btn_true.setBackgroundResource(R.drawable.edit_round2);
				holder3.txt_no.setTextColor(Color.BLACK);
				holder3.txt_notgiven.setTextColor(Color.BLACK);
			}
			if(sure_answer.equals(mlist.get(position).getOptionList().get(1).getOption())){
				holder3.btn_false.setBackgroundResource(R.drawable.edit_round);
				holder3.txt_no.setTextColor(Color.RED);
			}else{
				holder3.btn_false.setBackgroundResource(R.drawable.edit_round2);
				holder3.txt_yes.setTextColor(Color.BLACK);
				holder3.txt_notgiven.setTextColor(Color.BLACK);
			}
			if(sure_answer.equals(mlist.get(position).getOptionList().get(2).getOption())){
				holder3.btn_notgiven.setBackgroundResource(R.drawable.edit_round);
				holder3.txt_notgiven.setTextColor(Color.RED);
			}else{
				holder3.btn_notgiven.setBackgroundResource(R.drawable.edit_round2);
				holder3.txt_yes.setTextColor(Color.BLACK);
				holder3.txt_no.setTextColor(Color.BLACK);
			}
			
			
			holder3.btn_true.setText(mlist.get(position).getOptionList().get(0)
					.getStudentChooseCount());
			holder3.btn_false.setText(mlist.get(position).getOptionList()
					.get(1).getStudentChooseCount());
			holder3.btn_notgiven.setText(mlist.get(position).getOptionList()
					.get(2).getStudentChooseCount());
			
			//设置柱状图A
			holder3.btn_true.setHeight((int) ((float)btn_true/(float)stuAnswerTotalCount*height_juset));
			holder3.btn_false.setHeight((int) ((float)btn_false/(float)stuAnswerTotalCount*height_juset));
			holder3.btn_notgiven.setHeight((int) ((float)btn_notgiven/(float)stuAnswerTotalCount*height_juset));
			
			break;

		}

		return convertView;
	}

	class ViewHolder1 {
		TextView choose_txt_name, choose_txt_answer, choose_txt_progress,
				txt_true;
		//TextView  btn_A,btn_B, btn_C, btn_D;
		ReportView reportView;
		//TextView  txt_A,txt_B,txt_C,txt_D;
		RoundProgressBar choose_pro2, choose_pro3;
		ImageView image_show;
	}

	class ViewHolder2 {
		TextView complet_txt_name, complet_txt_answer, complet_txt_progress,
				txt_true;
		//btn1_complete_default 默认的长度
		TextView btn_yes, btn_no,txt_defalut,btn1_complete_default;
		RoundProgressBar complet_pro2, complet_pro3;
	
	}

	class ViewHolder3 {
		TextView just_txt_name, just_txt_answer, just_txt_progress, txt_true;
		TextView btn_true, btn_false, btn_notgiven;
		RoundProgressBar just_pro2, just_pro3;
		TextView  txt_yes,txt_no,txt_notgiven;
		ImageView image_show;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * 
	 * 
	 * 实现点击各个选项的详情
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int po =  (Integer) v.getTag();
		switch (v.getId()) {

		/*case R.id.btn1_choose:

			chooselist = mlist.get(po).getOptionList().get(0)
					.getStudentChooseList();
			select = mlist.get(po).getOptionList().get(0).getOptionName();
			System.out.println("chooselist======数据====选择A" + chooselist);

			break;
		case R.id.btn2_choose:
			chooselist = mlist.get(po).getOptionList().get(1)
					.getStudentChooseList();
			select = mlist.get(po).getOptionList().get(1).getOptionName();
			System.out.println("chooselist======数据====选择B" + chooselist);
			break;
		case R.id.btn3_choose:
			chooselist = mlist.get(po).getOptionList().get(2)
					.getStudentChooseList();
			select = mlist.get(po).getOptionList().get(2).getOptionName();
			System.out.println("chooselist======数据====选择C" + chooselist);
			break;
		case R.id.btn4_choose:
			chooselist = mlist.get(po).getOptionList().get(3)
					.getStudentChooseList();
			select = mlist.get(po).getOptionList().get(3).getOptionName();
			System.out.println("chooselist======数据====选择D" + chooselist);
			break;*/
		case R.id.btn1_complet:
			chooselist = mlist.get(po).getOptionList().get(0)
					.getStudentChooseList();
			select = mlist.get(po).getOptionList().get(0).getOption();
			System.out.println("chooselist======数据====填空1" + chooselist);
			break;
		case R.id.btn2_complet:
			chooselist = mlist.get(po).getOptionList().get(1)
					.getStudentChooseList();
			select = mlist.get(po).getOptionList().get(1).getOption();
			System.out.println("chooselist======数据====填空2" + chooselist);
			break;

		case R.id.btn1_just:
			chooselist = mlist.get(po).getOptionList().get(0)
					.getStudentChooseList();
			select = mlist.get(po).getOptionList().get(0).getOption();
			System.out.println("chooselist======数据====判断TRUE" + chooselist);
			break;
		case R.id.btn3_just:
			chooselist = mlist.get(po).getOptionList().get(1)
					.getStudentChooseList();
			select = mlist.get(po).getOptionList().get(1).getOption();
			System.out.println("chooselist======数据====判断FALSE" + chooselist);
			break;
		case R.id.btn4_just:
			chooselist = mlist.get(po).getOptionList().get(2)
					.getStudentChooseList();
			select = mlist.get(po).getOptionList().get(2).getOption();
			System.out.println("chooselist======数据====判断NOGAVIN" + chooselist);
			break;
		// 选择题的正确
		case R.id.roundProgressBar2_choose:
			// 点击圆圈进入详情
			chooselist = mlist.get(po).getAccuracyStudentChooseList();
			select = mlist.get(po).getQSValue();
			System.out.println("选择题 正确的数据=======" + chooselist+"正确答案==="+select);
			break;
		// 判断题的正确
		case R.id.roundProgressBar2_just:
			chooselist = mlist.get(po).getAccuracyStudentChooseList();
			select = mlist.get(po).getQSValue();
			System.out.println("判断 正确的数据=======" + chooselist+"正确答案==="+select);
			break;
			// 填空题的正确
		case R.id.roundProgressBar2_complet:
			chooselist = mlist.get(po).getAccuracyStudentChooseList();
			select = mlist.get(po).getQSValue();
			System.out.println("填空题 正确的数据=======" + chooselist+"正确答案==="+select);
			break;

		default:
			break;
		}
		sure_answer = mlist.get(po).getQSValue();
		System.out.println("选择的正确答案==="+sure_answer);
		OptionInfo optionnfo = new OptionInfo(select, chooselist);
		if (UtiltyHelper.isEmpty(optionnfo.getStudentChooseList())) {
			System.out.println("optionnfo.getStudentChooseList()包括null、 和空格");
		}else{
			System.out.println("optionnfo.getStudentChooseList()的值是==="+optionnfo.getStudentChooseList());
			if (optionnfo.getStudentChooseList().size()==0) {
				
			}else{
				intent = new Intent(context, TestReportMoreAnswerActivity.class);
				intent.putExtra("chooselist", optionnfo);
				intent.putExtra("sure_answer", sure_answer);
				context.startActivity(intent);
			}
		}

	}
}
