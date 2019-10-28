/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年6月30日 
 * 
 *******************************************************************************/ 
package com.lels.bean;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年6月30日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class TeacherClassInfo {
	private String id;
	private String passCode;
	private String nLessonNo;
	private int lessonType;
	
	
	public TeacherClassInfo(String id, String passCode, String nLessonNo,
			int lessonType) {
		super();
		this.id = id;
		this.passCode = passCode;
		this.nLessonNo = nLessonNo;
		this.lessonType = lessonType;
	}


	public TeacherClassInfo() {
		super();
	}
	
	//private 

}
