/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015年8月6日 
 * 
 *******************************************************************************/ 
package com.lels.bean;

import java.io.Serializable;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015年8月6日
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class ExamAnswerListInfo implements Serializable{
//	"QNumber": "试题编号",
//    "AnswerContent": "学生答案(当RightCount-ScoreCount<0，答案颜色是红色，否则是绿色)",
//    "RightCount": 正确的得分点,
//    "ScoreCount": 总得分点
	private String QNumber;
	private String AnswerContent;
	private int RightCount;
	private int ScoreCount;
	
	private boolean flag;
	
	
	public String getQNumber() {
		return QNumber;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public void setQNumber(String qNumber) {
		QNumber = qNumber;
	}
	public String getAnswerContent() {
		return AnswerContent;
	}
	public void setAnswerContent(String answerContent) {
		AnswerContent = answerContent;
	}
	public int getRightCount() {
		return RightCount;
	}
	public void setRightCount(int rightCount) {
		RightCount = rightCount;
	}
	public int getScoreCount() {
		return ScoreCount;
	}
	public void setScoreCount(int scoreCount) {
		ScoreCount = scoreCount;
	}
	@Override
	public String toString() {
		return "ExamAnswerListInfo [QNumber=" + QNumber + ", AnswerContent="
				+ AnswerContent + ", RightCount=" + RightCount
				+ ", ScoreCount=" + ScoreCount + "]";
	}

	public ExamAnswerListInfo(String qNumber, String answerContent,
			int rightCount, int scoreCount) {
		super();
		QNumber = qNumber;
		AnswerContent = answerContent;
		RightCount = rightCount;
		ScoreCount = scoreCount;
	}
	
	
}
