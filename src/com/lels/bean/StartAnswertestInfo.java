/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年7月6日 
 * 
 *******************************************************************************/ 
package com.lels.bean;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年7月6日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class StartAnswertestInfo {
	private String url;
	private String name;
	private String code;
	private String answer;
	private String time;
	private String sex;
	private String countdown;
	private String sStudentId;
	
	/**
	 * @return ssssssss
	 */
	public String getsStudentId() {
		return sStudentId;
	}
	/**
	 * @param sStudentId the sStudentId to set
	 */
	public void setsStudentId(String sStudentId) {
		this.sStudentId = sStudentId;
	}
	public String getCountdown() {
		return countdown;
	}
	public void setCountdown(String countdown) {
		this.countdown = countdown;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public StartAnswertestInfo() {
		super();
	}
	

}
