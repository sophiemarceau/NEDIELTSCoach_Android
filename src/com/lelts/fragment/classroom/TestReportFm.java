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
import com.lels.bean.LodDialogClass;
import com.lels.bean.OptionInfo;
import com.lels.bean.QuestionData;
import com.lels.bean.StudentChooseInfo;
import com.lels.bean.TestReportInfo;
import com.lels.constants.Constants;
import com.lelts.activity.classroomconnection.adapter.TestReportAdapter;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


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
public class TestReportFm extends Fragment{
	private View mview;
	private ListView mlistview;
	private Context context;
	private TestReportAdapter madapter;
	private SharedPreferences share, teacherinfo;
	private String ccId,paperId;
	private Bundle bun;
	//private List<TestReportInfo> mlist;
	private int AccuracyStudentChooseCount,QuestionType;
	private String QSValue,Accuracy,Title;
	private QuestionData date;
	private List<QuestionData> list;
	private List<StudentChooseInfo> accuracyStudentChooseList;
	private List<StudentChooseInfo> studentChooseList;
	private List<OptionInfo> optionList;
	private int choose;
	private TextView txt_left_test_report,txt_rigth_test_report,txt_report_titlename;
	
	//传过来的标题
	private String titlename;
	//判断是否有报告 
	private RelativeLayout txt_nullreport;
//	"Data": {
//	  "子试题标识(子试题ID)": {
//	   "Title": "试题名称",
//	   "QSValue": "正确答案",
//	   "Accuracy": "正确率",
//	   "AccuracyStudentChooseCount": "点击正确率,进入下一级展示的人员列表个数"
//	   AccuracyStudentChooseList": 点击正确率,进入下一级展示的人员列表[{
//	    ...同下面studentChooseList中的字段一致...
//	   }]
//	   "QuestionType": 试题类型 1选择题2是非选择题3填空题,
//	   "optionList": [{
//	    "studentChooseCount": 选择此项的学生个数,
//	    "SQ_ID": 子试题ID,
//	    "OptionName": "选项名称",
//	    "OrderNum": 选项编号,
//	    "Option": "选项内容",
//	    "studentChooseList": 具体选择此项的学生信息[{
//	     "QSValue": "正确答案",
//	     "sCode": "学生编号",
//	     "UID": 学生ID,
//		"stuAnswerTotalCount"
//	     "IconUrl": "学生图像",
//	     "TargetDateDiff": "雅思考试倒计时的天数",
//	     "AnswerContent": "学生答案",
//	     "sName": "学生姓名",
//	     "nGender": 学生性别(1男2女)
//	    }]
//	   }]
//	  },
//	 }
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mview = inflater.inflate(R.layout.fragment_test_report, null);

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
		if(choose ==1){
			//整套提交
			findWholeSubmitModeStudentExerciseReport();
		}else if(choose==2){
			//单套提交
			findSingleSubmitModeStudentExerciseReport();
		}else if(choose==3){
			
			txt_left_test_report.setVisibility(View.GONE);
			txt_rigth_test_report.setVisibility(View.GONE);
			txt_report_titlename.setVisibility(View.VISIBLE);
			
			//查看报告
			Intent intent = getActivity().getIntent();
			if(intent==null){
				
			}else{
				ccId = intent.getStringExtra("ccId");
				paperId = intent.getStringExtra("pId");
				titlename = intent.getStringExtra("titlename");
				txt_report_titlename.setText(titlename);
				findWholeSubmitModeStudentExerciseReport();
				System.out.println("ccId========" + ccId
						+ "==========paperId=====" + paperId+"=====choose==="+choose);
			}

		}
	
	}

	/**
	 * 方法说明：初始化控件
	 *
	 */
	private void initview() {
		// TODO Auto-generated method stub
		context = getActivity();
		txt_nullreport  = (RelativeLayout) getActivity().findViewById(R.id.txt_nullreport);
		txt_report_titlename = (TextView) getActivity().findViewById(R.id.txt_report_titlename);
		txt_rigth_test_report = (TextView) getActivity().findViewById(R.id.txt_rigth_test_report);
		txt_left_test_report = (TextView) getActivity().findViewById(R.id.txt_left_test_report);
		list = new ArrayList<QuestionData>();
		mlistview = (ListView) getActivity().findViewById(R.id.listview_testreport);
		share =getActivity().getSharedPreferences("userinfo", getActivity().MODE_PRIVATE);
		teacherinfo =getActivity().getSharedPreferences("teacherinfo", getActivity().MODE_PRIVATE);
		ccId = teacherinfo.getString("ccId", "");
		paperId = teacherinfo.getString("paperId", "");
		choose = teacherinfo.getInt("choose", -1);
		System.out.println("ccId========" + ccId
				+ "==========paperId=====" + paperId+"=====choose==="+choose);
	}
	

	
	/**
	 * 方法说明：整套提交的学生练习报告
	 *
	 */
	private void findWholeSubmitModeStudentExerciseReport() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		utils.send(HttpMethod.GET,
				Constants.URL_findWholeSubmitModeStudentExerciseReport
				 + "?ccId=" + ccId+"&paperId="+paperId, params,
//						+ "?ccId=29908&paperId=9751", params,
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
						System.out.println("data=========>"+result);
						
						try {
							JSONObject obj = new JSONObject(result);
							System.out.println("====="+obj.getString("Data"));
							if(obj.getString("Data").equals("{}")||obj.getString("Data").equals("null")||obj.getString("Data").endsWith("[]")){
								System.out.println("data====为空！");
								//无报告
								mlistview.setVisibility(View.GONE);
								txt_nullreport.setVisibility(View.VISIBLE);
								LodDialogClass.closeCustomCircleProgressDialog();
							}else{
								//有报告
								mlistview.setVisibility(View.VISIBLE);
								txt_nullreport.setVisibility(View.GONE);
//								JSONObject data = obj.getJSONObject("Data");
//								System.out.println("data=======>"+data);
//								JSONArray array = data.names();
//								for (int i = 0; i < array.length(); i++) {
								JSONArray data = obj.getJSONArray("Data");
								System.out.println("data=======>"+data);
								for (int i = 0; i < data.length(); i++) {
									JSONObject s = data.getJSONObject(i);
//									JSONObject s = data.getJSONObject(array.getString(i));
									String QSValue = s.getString("QSValue");
									String QuestionType = s.getString("QuestionType");
									String AccuracyStudentChooseCount = s
											.getString("AccuracyStudentChooseCount");
									String Accuracy = s.getString("Accuracy");
									int stuAnswerTotalCount = s.getInt("stuAnswerTotalCount");
									String Title = s.getString("Title");
									accuracyStudentChooseList = new ArrayList<StudentChooseInfo>();
									String str1 = s.getString("AccuracyStudentChooseList");
									if(str1 == null||str1.equals("null")||str1.equals(null)||str1.isEmpty()||str1.length()<0){
										System.out.println("str1====AccuracyStudentChooseList 为空！");
									}else{
										System.out.println("str1=====AccuracyStudentChooseList"+str1);
										JSONArray arr1 = new JSONArray(str1);
										for (int j = 0; j < arr1.length(); j++) {
											String QSValue1 = arr1.getJSONObject(j)
													.getString("QSValue");
											String TargetDateDiff = arr1.getJSONObject(j).getString(
													"TargetDateDiff");
											String sCode = arr1.getJSONObject(j).getString("sCode");
											String UID = arr1.getJSONObject(j).getString("UID");
											String sqId = arr1.getJSONObject(j).getString("sqId");
											String IconUrl = arr1.getJSONObject(j).getString("IconUrl");
											String AnswerContent = arr1.getJSONObject(j).getString(
													"AnswerContent");
											String ScoreCount = arr1.getJSONObject(j).getString(
													"ScoreCount");
											String RightCount = arr1.getJSONObject(j).getString(
													"RightCount");
											String sName = arr1.getJSONObject(j).getString("sName");
											String Code = arr1.getJSONObject(j).getString("Code");
											String Title1 = arr1.getJSONObject(j).getString("Title");
											String nGender = arr1.getJSONObject(j).getString("nGender");
											StudentChooseInfo info = new StudentChooseInfo(
													QSValue1, TargetDateDiff, sCode, UID, sqId,
													IconUrl, AnswerContent, ScoreCount, RightCount,
													sName, Code, Title1, nGender);
											accuracyStudentChooseList.add(info);
										}
									}
									optionList = new ArrayList<OptionInfo>();
									String str2 = s.getString("optionList");
									if(str2 == null||str2.equals("null")||str2.equals(null)){
										System.out.println("str2==OptionInfo==为空！");
									}else{
										JSONArray arr2 = new JSONArray(str2);
										System.out.println("str2==OptionInfo=="+str2);
										for (int j = 0; j < arr2.length(); j++) {
											String OrderNum = arr2.getJSONObject(j).getString(
													"OrderNum");
											String Option = arr2.getJSONObject(j).getString("Option");
											String studentChooseCount = arr2.getJSONObject(j)
													.getString("studentChooseCount");
										//	String SQ_ID = arr2.getJSONObject(j).getString("SQ_ID");
											String OptionName = arr2.getJSONObject(j).getString(
													"OptionName");
											studentChooseList = new ArrayList<StudentChooseInfo>();
											String str3 = arr2.getJSONObject(j).getString(
													"studentChooseList");
											if(str3 == null||str3.equals("null")||str3.equals(null)){
												System.out.println("str3==studentChooseList==为空！");
											}else{
												System.out.println("str3==studentChooseList=="+str3);
												JSONArray arr3 = new JSONArray(str3);
												
												for (int k = 0; k < arr3.length(); k++) {
													String QSValue3 = arr3.getJSONObject(k).getString(
															"QSValue");
													String TargetDateDiff = arr3.getJSONObject(k)
															.getString("TargetDateDiff");
													String sCode = arr3.getJSONObject(k).getString("sCode");
													String UID = arr3.getJSONObject(k).getString("UID");
													String sqId = arr3.getJSONObject(k).getString("sqId");
													String IconUrl = arr3.getJSONObject(k).getString(
															"IconUrl");
													String AnswerContent = arr3.getJSONObject(k).getString(
															"AnswerContent");
													String ScoreCount = arr3.getJSONObject(k).getString(
															"ScoreCount");
													String RightCount = arr3.getJSONObject(k).getString(
															"RightCount");
													String sName = arr3.getJSONObject(k).getString("sName");
													String Code = arr3.getJSONObject(k).getString("Code");
													String Title3 = arr3.getJSONObject(k)
															.getString("Title");
													String nGender = arr3.getJSONObject(k).getString(
															"nGender");
													StudentChooseInfo info = new StudentChooseInfo(
															QSValue3, TargetDateDiff, sCode, UID, sqId,
															IconUrl, AnswerContent, ScoreCount, RightCount,
															sName, Code, Title3, nGender);
													studentChooseList.add(info);
												}
											}
											
											OptionInfo oInfo = new OptionInfo(OrderNum, Option,
													studentChooseCount, OptionName,
													studentChooseList);
											optionList.add(oInfo);
											
										}
									}
								
									QuestionData qInfo = new QuestionData(QSValue, QuestionType,
											AccuracyStudentChooseCount, accuracyStudentChooseList,
											Accuracy, optionList, (i+1)+"."+Title,stuAnswerTotalCount);
									list.add(qInfo);
								//	System.out.println("list=====>"+list.toString());
									System.out.println("chooselist======数据===="+optionList.get(0).getStudentChooseList().toString());
								}
							}
							System.out.println("list=====>"+list.toString());
							madapter = new TestReportAdapter(list, context);
							mlistview.setAdapter(madapter);
							LodDialogClass.closeCustomCircleProgressDialog();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						System.out
								.println("findWholeSubmitModeStudentExerciseReport====================="
										+ result);
					}
				});
	}
	
	
	/**
	 * 方法说明：单套提交的学生练习报告
	 *
	 */
	private void findSingleSubmitModeStudentExerciseReport() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.addHeader("Authentication", share.getString("Token", ""));
		HttpUtils utils = new HttpUtils();
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);
		utils.send(HttpMethod.GET,
				Constants.URL_findSingleSubmitModeStudentExerciseReport
				 + "?ccId=" + teacherinfo.getString("ccId",
				 "")+"&paperId="+paperId, params,
//						+ "?ccId=29908&paperId=9751", params,
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
						System.out.println("data=========>"+result);
						
						try {
							JSONObject obj = new JSONObject(result);
							System.out.println("====="+obj.getString("Data"));
							if(obj.getString("Data").equals("{}")||obj.getString("Data").equals("null")||obj.getString("Data").endsWith("[]")){
								System.out.println("data====为空！");
								//无报告
								mlistview.setVisibility(View.GONE);
								txt_nullreport.setVisibility(View.VISIBLE);
								LodDialogClass.closeCustomCircleProgressDialog();
							}else{
								//无报告
								mlistview.setVisibility(View.VISIBLE);
								txt_nullreport.setVisibility(View.GONE);
//								JSONArray data_array = obj.getJSONArray("Data");
//								System.out.println("data=======>"+data_array);
//								for (int i = 0; i < data_array.length(); i++) {
//									JSONObject Data = data_array.getJSONObject(i);
//									JSONArray optionList = Data.getJSONArray("optionList");
//									System.out.println("optionList====="+optionList);
//									for (int j = 0; j < optionList.length(); j++) {
//										 
//									}
//								}
								
								
//								JSONObject data = obj.getJSONObject("Data");
//								System.out.println("data=======>"+data);
//								JSONArray array = data.names();
								JSONArray data = obj.getJSONArray("Data");
								System.out.println("data=======>"+data);
								for (int i = 0; i < data.length(); i++) {
									JSONObject s = data.getJSONObject(i);
//								}
								
//								for (int i = 0; i < array.length(); i++) {
//									JSONObject s = data.getJSONObject(array.getString(i));
									String QSValue = s.getString("QSValue");
									String QuestionType = s.getString("QuestionType");
									String AccuracyStudentChooseCount = s
											.getString("AccuracyStudentChooseCount");
									String Accuracy = s.getString("Accuracy");
									int stuAnswerTotalCount = s.getInt("stuAnswerTotalCount");
									String Title = s.getString("Title");
									accuracyStudentChooseList = new ArrayList<StudentChooseInfo>();
									String str1 = s.getString("AccuracyStudentChooseList");
									if(str1 == null||str1.equals("null")||str1.equals(null)||str1.isEmpty()||str1.length()<0){
										System.out.println("str1====AccuracyStudentChooseList 为空！");
									}else{
										System.out.println("str1=====AccuracyStudentChooseList"+str1);
										JSONArray arr1 = new JSONArray(str1);
										for (int j = 0; j < arr1.length(); j++) {
											String QSValue1 = arr1.getJSONObject(j)
													.getString("QSValue");
											String TargetDateDiff = arr1.getJSONObject(j).getString(
													"TargetDateDiff");
											String sCode = arr1.getJSONObject(j).getString("sCode");
											String UID = arr1.getJSONObject(j).getString("UID");
											String sqId = arr1.getJSONObject(j).getString("sqId");
											String IconUrl = arr1.getJSONObject(j).getString("IconUrl");
											String AnswerContent = arr1.getJSONObject(j).getString(
													"AnswerContent");
											String ScoreCount = arr1.getJSONObject(j).getString(
													"ScoreCount");
											String RightCount = arr1.getJSONObject(j).getString(
													"RightCount");
											String sName = arr1.getJSONObject(j).getString("sName");
											String Code = arr1.getJSONObject(j).getString("Code");
											String Title1 = arr1.getJSONObject(j).getString("Title");
											String nGender = arr1.getJSONObject(j).getString("nGender");
											StudentChooseInfo info = new StudentChooseInfo(
													QSValue1, TargetDateDiff, sCode, UID, sqId,
													IconUrl, AnswerContent, ScoreCount, RightCount,
													sName, Code, Title1, nGender);
											accuracyStudentChooseList.add(info);
										}
									}
									optionList = new ArrayList<OptionInfo>();
									String str2 = s.getString("optionList");
									if(str2 == null||str2.equals("null")||str2.equals(null)){
										System.out.println("str2==OptionInfo==为空！");
									}else{
										JSONArray arr2 = new JSONArray(str2);
										System.out.println("str2==OptionInfo=="+str2);
										for (int j = 0; j < arr2.length(); j++) {
											String OrderNum = arr2.getJSONObject(j).getString(
													"OrderNum");
											String Option = arr2.getJSONObject(j).getString("Option");
											String studentChooseCount = arr2.getJSONObject(j)
													.getString("studentChooseCount");
										//	String SQ_ID = arr2.getJSONObject(j).getString("SQ_ID");
											String OptionName = arr2.getJSONObject(j).getString(
													"OptionName");
											studentChooseList = new ArrayList<StudentChooseInfo>();
											String str3 = arr2.getJSONObject(j).getString(
													"studentChooseList");
											if(str3 == null||str3.equals("null")||str3.equals(null)){
												System.out.println("str3==studentChooseList==为空！");
											}else{
												System.out.println("str3==studentChooseList=="+str3);
												JSONArray arr3 = new JSONArray(str3);
												
												for (int k = 0; k < arr3.length(); k++) {
													String QSValue3 = arr3.getJSONObject(k).getString(
															"QSValue");
													String TargetDateDiff = arr3.getJSONObject(k)
															.getString("TargetDateDiff");
													String sCode = arr3.getJSONObject(k).getString("sCode");
													String UID = arr3.getJSONObject(k).getString("UID");
													String sqId = arr3.getJSONObject(k).getString("sqId");
													String IconUrl = arr3.getJSONObject(k).getString(
															"IconUrl");
													String AnswerContent = arr3.getJSONObject(k).getString(
															"AnswerContent");
													String ScoreCount = arr3.getJSONObject(k).getString(
															"ScoreCount");
													String RightCount = arr3.getJSONObject(k).getString(
															"RightCount");
													String sName = arr3.getJSONObject(k).getString("sName");
													String Code = arr3.getJSONObject(k).getString("Code");
													String Title3 = arr3.getJSONObject(k)
															.getString("Title");
													String nGender = arr3.getJSONObject(k).getString(
															"nGender");
													StudentChooseInfo info = new StudentChooseInfo(
															QSValue3, TargetDateDiff, sCode, UID, sqId,
															IconUrl, AnswerContent, ScoreCount, RightCount,
															sName, Code, Title3, nGender);
													studentChooseList.add(info);
												}
											}
											
											OptionInfo oInfo = new OptionInfo(OrderNum, Option,
													studentChooseCount, OptionName,
													studentChooseList);
											optionList.add(oInfo);
											
										}
									}
								
									QuestionData qInfo = new QuestionData(QSValue, QuestionType,
											AccuracyStudentChooseCount, accuracyStudentChooseList,
											Accuracy, optionList, (i+1)+"."+Title,stuAnswerTotalCount);
									list.add(qInfo);
								//	System.out.println("list=====>"+list.toString());
									System.out.println("chooselist======数据===="+optionList.get(0).getStudentChooseList().toString());
								}
							}
							System.out.println("list=====>"+list.toString());
							madapter = new TestReportAdapter(list, context);
							mlistview.setAdapter(madapter);
							LodDialogClass.closeCustomCircleProgressDialog();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						System.out
								.println("findWholeSubmitModeStudentExerciseReport====================="
										+ result);
					}
				});
	}
	

	
}
