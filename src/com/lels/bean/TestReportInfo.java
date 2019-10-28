/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年7月9日 
 * 
 *******************************************************************************/ 
package com.lels.bean;

import java.io.Serializable;

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
public class TestReportInfo implements Serializable{
	private int questionType;
	private String testname;
	private String testanswer;
	private int progress;
	
	public int getQuestionType() {
		return questionType;
	}
	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}
	public String getTestname() {
		return testname;
	}
	public void setTestname(String testname) {
		this.testname = testname;
	}
	public String getTestanswer() {
		return testanswer;
	}
	public void setTestanswer(String testanswer) {
		this.testanswer = testanswer;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public TestReportInfo() {
		super();
	}
	@Override
	public String toString() {
		return "TestReportInfo [questionType=" + questionType + ", testname="
				+ testname + ", testanswer=" + testanswer + ", progress="
				+ progress + "]";
	}
	
	

}
