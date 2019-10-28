/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年6月27日 
 * 
 *******************************************************************************/ 
package com.lels.bean;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年6月27日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class AnswerSheetInfo {
	private String testname;
	private int testnum;
	public String getTestname() {
		return testname;
	}
	public void setTestname(String testname) {
		this.testname = testname;
	}
	public int getTestnum() {
		return testnum;
	}
	public void setTestnum(int testnum) {
		this.testnum = testnum;
	}
	public AnswerSheetInfo() {
		super();
	}
	@Override
	public String toString() {
		return "AnswerSheetInfo [testname=" + testname + ", testnum=" + testnum
				+ "]";
	}
	

}
