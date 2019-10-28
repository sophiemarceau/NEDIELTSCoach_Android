package com.lels.bean;

import java.io.Serializable;

public class StudentChooseInfo implements Serializable{
	private String QSValue;
	private String TargetDateDiff;
	private String sCode;
	private String UID;
	private String sqId;
	private String IconUrl;
	private String AnswerContent;
	private String ScoreCount;
	private String RightCount;
	private String sName;
	private String Code;
	private String Title;
	private String nGender;
	public String getQSValue() {
		return QSValue;
	}
	public void setQSValue(String qSValue) {
		QSValue = qSValue;
	}
	public String getTargetDateDiff() {
		return TargetDateDiff;
	}
	public void setTargetDateDiff(String targetDateDiff) {
		TargetDateDiff = targetDateDiff;
	}
	public String getsCode() {
		return sCode;
	}
	public void setsCode(String sCode) {
		this.sCode = sCode;
	}
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public String getSqId() {
		return sqId;
	}
	public void setSqId(String sqId) {
		this.sqId = sqId;
	}
	public String getIconUrl() {
		return IconUrl;
	}
	public void setIconUrl(String iconUrl) {
		IconUrl = iconUrl;
	}
	public String getAnswerContent() {
		return AnswerContent;
	}
	public void setAnswerContent(String answerContent) {
		AnswerContent = answerContent;
	}
	public String getScoreCount() {
		return ScoreCount;
	}
	public void setScoreCount(String scoreCount) {
		ScoreCount = scoreCount;
	}
	public String getRightCount() {
		return RightCount;
	}
	public void setRightCount(String rightCount) {
		RightCount = rightCount;
	}
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public String getCode() {
		return Code;
	}
	public void setCode(String code) {
		Code = code;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getnGender() {
		return nGender;
	}
	public void setnGender(String nGender) {
		this.nGender = nGender;
	}
	public StudentChooseInfo(String qSValue, String targetDateDiff,
			String sCode, String uID, String sqId, String iconUrl,
			String answerContent, String scoreCount, String rightCount,
			String sName, String code, String title, String nGender) {
		super();
		QSValue = qSValue;
		TargetDateDiff = targetDateDiff;
		this.sCode = sCode;
		UID = uID;
		this.sqId = sqId;
		IconUrl = iconUrl;
		AnswerContent = answerContent;
		ScoreCount = scoreCount;
		RightCount = rightCount;
		this.sName = sName;
		Code = code;
		Title = title;
		this.nGender = nGender;
	}
	@Override
	public String toString() {
		return "StudentChooseInfo [QSValue=" + QSValue + ", TargetDateDiff="
				+ TargetDateDiff + ", sCode=" + sCode + ", UID=" + UID
				+ ", sqId=" + sqId + ", IconUrl=" + IconUrl
				+ ", AnswerContent=" + AnswerContent + ", ScoreCount="
				+ ScoreCount + ", RightCount=" + RightCount + ", sName="
				+ sName + ", Code=" + Code + ", Title=" + Title + ", nGender="
				+ nGender + "]";
	}
	
}
