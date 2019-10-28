/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年7月7日 
 * 
 *******************************************************************************/ 
package com.lelts.fragment.classroom;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.hello.R;
import com.lels.bean.AnswerInfo;
import com.lels.bean.ExamAnswerListInfo;
import com.lels.bean.LodDialogClass;
import com.lels.bean.StartAnswertestInfo;
import com.lels.constants.Constants;
import com.lelts.activity.classroomconnection.adapter.StartAnswerTestAdapter;
import com.lelts.activity.classroomconnection.adapter.StartAnswertTestReportcardAdapter;
import com.lelts.activity.classroomconnection.adapter.WholeTestReportAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年7月7日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class ReportCardFm extends Fragment{
	private View mview;
	private ListView mlistview;
	private Context context;
	private StartAnswertTestReportcardAdapter madapter;
	private WholeTestReportAdapter wholeadapter;
	private List<StartAnswertestInfo> mlist;
	private Bundle bun;
	private int choose;
	private SharedPreferences share, teacherinfo;
	private String ccId,paperId;
	private List<AnswerInfo> mSinglelist;
	private List<ExamAnswerListInfo> examAnswerList;
	//判断是否有数据
	private RelativeLayout txt_nullreport;
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mview = inflater.inflate(R.layout.fragment_report_card, null);
		return mview;
	}

	/* (non-Javadoc)
	 * @see android.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initview();
		LodDialogClass.showCustomCircleProgressDialog(getActivity(), "", "加载中...");
		System.out.println("choose<==========>"+choose);
		
		if(choose==1){
			
			findWholeSubmit();
			
		}else if(choose == 2)
			
		findSingleSubmitModeStudentExamMark();
	}
	
	
	/**
	 * 方法说明：初始化控件
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = getActivity();
		mlistview = (ListView) getActivity().findViewById(R.id.listview_report_card);
		share =getActivity().getSharedPreferences("userinfo", getActivity().MODE_PRIVATE);
		teacherinfo =getActivity().getSharedPreferences("teacherinfo", getActivity().MODE_PRIVATE);
		paperId = teacherinfo.getString("paperId", "");
		ccId = teacherinfo.getString("ccId", "");
		choose = teacherinfo.getInt("choose", -1);
		txt_nullreport = (RelativeLayout) getActivity().findViewById(R.id.txt_nullreport);
	}

	/**
	 * 方法说明：网络获取整套提交的(考试当中和考试结束后显示的)学生成绩单
	 *
	 */
	private void findWholeSubmit() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("ccId========" + teacherinfo.getString("ccId", "")
				+ "==========paperId=====" + paperId);
		utils.send(HttpMethod.GET,
				Constants.URL_findWholeSubmitModeStudentExamMark
				 + "?ccId=" + ccId+"&paperId="+paperId, params,
		//				+ "?ccId=29908&paperId=9751", params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						LodDialogClass.closeCustomCircleProgressDialog();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						String result = arg0.result;
						try {
							JSONObject obj = new JSONObject(result);
							if (obj.getString("Data").endsWith("[]")) {
								System.out.println("无数据========");
								txt_nullreport.setVisibility(View.VISIBLE);
								mlistview.setVisibility(View.GONE);
							}else{
								txt_nullreport.setVisibility(View.GONE);
								mlistview.setVisibility(View.VISIBLE);
							}
							JSONArray data = obj.getJSONArray("Data");
						
							

							mlist = new ArrayList<StartAnswertestInfo>();
							for (int i = 0; i < data.length(); i++) {
								JSONObject obj_data = data.getJSONObject(i);
								StartAnswertestInfo info = new StartAnswertestInfo();
								info.setAnswer(obj_data.getString("Accuracy"));
								info.setCode(obj_data.getString("sCode"));
								info.setName(obj_data.getString("sName"));
								info.setTime(obj_data.getString("CostTime"));
								info.setUrl(obj_data.getString("IconUrl"));
								info.setSex(obj_data.getString("nGender"));
								info.setCountdown(obj_data.getString("TargetDateDiff"));
								
								//新加数据
								info.setsStudentId(obj_data.getString("sStudentId"));
								mlist.add(info);

							}
							madapter = new StartAnswertTestReportcardAdapter(context, mlist);
							mlistview.setAdapter(madapter);
							LodDialogClass.closeCustomCircleProgressDialog();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out
								.println("findWholeSubmit====================="
										+ result);
					}
				});

	}
	/**
	 * 方法说明：单题提交的(考试当中(心跳)和考试结束后显示的)学生成绩单
	 *
	 */
	private void findSingleSubmitModeStudentExamMark() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		System.out.println("ccId========" + teacherinfo.getString("ccId", "")
				+ "==========paperId=====" + paperId);
		utils.send(HttpMethod.GET,
				Constants.URL_findSingleSubmitModeStudentExamMark
				 + "?ccId=" + teacherinfo.getString("ccId",
				 "")+"&paperId="+paperId, params,
		//				+ "?ccId=29908&paperId=9751", params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						LodDialogClass.closeCustomCircleProgressDialog();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub

						String result = arg0.result;
						try {
							JSONObject obj = new JSONObject(result);
							if (obj.getString("Data").equals("[]")) {
								System.out.println("无数据========");
								txt_nullreport.setVisibility(View.VISIBLE);
								mlistview.setVisibility(View.GONE);
							}else{
								txt_nullreport.setVisibility(View.GONE);
								mlistview.setVisibility(View.VISIBLE);
							}
							JSONArray data = obj.getJSONArray("Data");
							mSinglelist = new ArrayList<AnswerInfo>();
							for (int i = 0; i < data.length(); i++) {
								JSONObject obj_data = data.getJSONObject(i);
								String sName = obj_data.getString("sName");
								int nGender = obj_data.getInt("nGender"); 
								String CostTime = obj_data.getString("CostTime");
								String sCode = obj_data.getString("sCode");
								String IconUrl = obj_data.getString("IconUrl");
								JSONArray exam = obj_data.getJSONArray("examAnswerList");
								examAnswerList = new ArrayList<ExamAnswerListInfo>();
								for (int j = 0; j < exam.length(); j++) {
									JSONObject exam_data = exam.getJSONObject(j);
									String QNumber = exam_data.getString("QNumber");
									// "AnswerContent": "学生答案(当RightCount-ScoreCount<0，答案颜色是红色，否则是绿色)",
									String AnswerContent = exam_data.getString("AnswerContent");
									int RightCount = exam_data.getInt("RightCount");
									int ScoreCount = exam_data.getInt("ScoreCount");
									ExamAnswerListInfo exam_info = new ExamAnswerListInfo(QNumber, AnswerContent, RightCount, ScoreCount);
									examAnswerList.add(exam_info);
								}
								AnswerInfo info = new AnswerInfo(sName,nGender,IconUrl,CostTime,sCode,examAnswerList);
								mSinglelist.add(info);
								System.out.println("mSinglelist====="+mSinglelist.toString());
							}
							wholeadapter = new WholeTestReportAdapter(context, mSinglelist);
							mlistview.setAdapter(wholeadapter);
							LodDialogClass.closeCustomCircleProgressDialog();

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						System.out
								.println("findWholeSubmit====================="
										+ result);
					}
				});
					
	}
	
}
