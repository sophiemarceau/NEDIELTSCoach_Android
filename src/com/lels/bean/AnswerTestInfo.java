/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年6月29日 
 * 
 *******************************************************************************/ 
package com.lels.bean;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年6月29日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class AnswerTestInfo {
	private String SectionID;
	private String PID;
	private String Q_ID;
	private String testname;
	private String testnum;
	private boolean ischexck = false;
	
	public String getQ_ID() {
		return Q_ID;
	}
	public void setQ_ID(String q_ID) {
		Q_ID = q_ID;
	}
	
	public String getTestnum() {
		return testnum;
	}
	public void setTestnum(String testnum) {
		this.testnum = testnum;
	}

	public String getTestname() {
		return testname;
	}
	public void setTestname(String testname) {
		this.testname = testname;
	}
	public boolean isIschexck() {
		return ischexck;
	}
	public void setIschexck(boolean ischexck) {
		this.ischexck = ischexck;
	}
	public String getSectionID() {
		return SectionID;
	}
	public void setSectionID(String sectionID) {
		SectionID = sectionID;
	}
	public String getPID() {
		return PID;
	}
	public void setPID(String pID) {
		PID = pID;
	}
	
	
	

}
