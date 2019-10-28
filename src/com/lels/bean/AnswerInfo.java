/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年7月14日 
 * 
 *******************************************************************************/ 
package com.lels.bean;

import java.util.List;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年7月14日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class AnswerInfo {
//	"sName": "学生姓名",
//	   "nGender": 学生性别(1男2女),
//	   "sCode": "学生编号",
//	   "IconUrl": "学生图像",
//	   "CostTime": "时间",
//	   "examAnswerList": [
//	    {
//	     "QNumber": "试题编号",
//	     "AnswerContent": "学生答案(当RightCount-ScoreCount<0，答案颜色是红色，否则是绿色)",
//	     "RightCount": 正确的得分点,
//	     "ScoreCount": 总得
	
	
	
	private Boolean flag = false;
	private String name;
	private int  nGender;
	private String IconUrl;
	private String CostTime;
	private String sCode;
	private List<ExamAnswerListInfo> examAnswerList;
	
	public Boolean getFlag() {
		return flag;
	}
	public void setFlag(Boolean flag) {
		this.flag = flag;
	}
	public String getsCode() {
		return sCode;
	}
	public void setsCode(String sCode) {
		this.sCode = sCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getnGender() {
		return nGender;
	}
	public void setnGender(int nGender) {
		this.nGender = nGender;
	}
	public String getIconUrl() {
		return IconUrl;
	}
	public void setIconUrl(String iconUrl) {
		IconUrl = iconUrl;
	}
	public String getCostTime() {
		return CostTime;
	}
	public void setCostTime(String costTime) {
		CostTime = costTime;
	}
	public List<ExamAnswerListInfo> getExamAnswerList() {
		return examAnswerList;
	}
	public void setExamAnswerList(List<ExamAnswerListInfo> examAnswerList) {
		this.examAnswerList = examAnswerList;
	}


	public AnswerInfo(String name, int nGender, String iconUrl,
			String costTime, String sCode,
			List<ExamAnswerListInfo> examAnswerList) {
		super();
		this.name = name;
		this.nGender = nGender;
		IconUrl = iconUrl;
		CostTime = costTime;
		this.sCode = sCode;
		this.examAnswerList = examAnswerList;
	}
	@Override
	public String toString() {
		return "AnswerInfo [name=" + name + ", nGender=" + nGender
				+ ", IconUrl=" + IconUrl + ", CostTime=" + CostTime
				+ ", sCode=" + sCode + ", examAnswerList=" + examAnswerList
				+ "]";
	}

	
	
	
	

}
