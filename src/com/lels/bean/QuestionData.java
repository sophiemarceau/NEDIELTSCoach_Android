package com.lels.bean;

import java.io.Serializable;
import java.util.List;

public class QuestionData implements Serializable{
	private String QSValue;
	private String QuestionType;
	private String AccuracyStudentChooseCount;
	private List<StudentChooseInfo> AccuracyStudentChooseList;
	private String Accuracy;
	private List<OptionInfo> optionList;
	private String Title;
	private int stuAnswerTotalCount;
	
	public int getStuAnswerTotalCount() {
		return stuAnswerTotalCount;
	}
	public void setStuAnswerTotalCount(int stuAnswerTotalCount) {
		this.stuAnswerTotalCount = stuAnswerTotalCount;
	}
	public String getQSValue() {
		return QSValue;
	}
	public void setQSValue(String qSValue) {
		QSValue = qSValue;
	}
	public String getQuestionType() {
		return QuestionType;
	}
	public void setQuestionType(String questionType) {
		QuestionType = questionType;
	}
	public String getAccuracyStudentChooseCount() {
		return AccuracyStudentChooseCount;
	}
	public void setAccuracyStudentChooseCount(String accuracyStudentChooseCount) {
		AccuracyStudentChooseCount = accuracyStudentChooseCount;
	}
	public List<StudentChooseInfo> getAccuracyStudentChooseList() {
		return AccuracyStudentChooseList;
	}
	public void setAccuracyStudentChooseList(
			List<StudentChooseInfo> accuracyStudentChooseList) {
		AccuracyStudentChooseList = accuracyStudentChooseList;
	}
	public String getAccuracy() {
		return Accuracy;
	}
	public void setAccuracy(String accuracy) {
		Accuracy = accuracy;
	}
	public List<OptionInfo> getOptionList() {
		return optionList;
	}
	public void setOptionList(List<OptionInfo> optionList) {
		this.optionList = optionList;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}

	
	public QuestionData(String qSValue, String questionType,
			String accuracyStudentChooseCount,
			List<StudentChooseInfo> accuracyStudentChooseList, String accuracy,
			List<OptionInfo> optionList, String title, int stuAnswerTotalCount) {
		super();
		QSValue = qSValue;
		QuestionType = questionType;
		AccuracyStudentChooseCount = accuracyStudentChooseCount;
		AccuracyStudentChooseList = accuracyStudentChooseList;
		Accuracy = accuracy;
		this.optionList = optionList;
		Title = title;
		this.stuAnswerTotalCount = stuAnswerTotalCount;
	}
	@Override
	public String toString() {
		return "QuestionData [QSValue=" + QSValue + ", QuestionType="
				+ QuestionType + ", AccuracyStudentChooseCount="
				+ AccuracyStudentChooseCount + ", AccuracyStudentChooseList="
				+ AccuracyStudentChooseList + ", Accuracy=" + Accuracy
				+ ", optionList=" + optionList + ", Title=" + Title + "]";
	}
	public QuestionData(List<StudentChooseInfo> accuracyStudentChooseList) {
		super();
		AccuracyStudentChooseList = accuracyStudentChooseList;
	}
	
	
	
}
