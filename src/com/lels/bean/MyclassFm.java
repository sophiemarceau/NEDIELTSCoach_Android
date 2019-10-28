package com.lels.bean;

public class MyclassFm {
	private String classNum;
	private String averageNum;
	private String timeQuantum;
	private String totleNum;
	private String tag;
	public MyclassFm(String classNum, String averageNum, String timeQuantum,
			String totleNum, String tag) {
		super();
		this.classNum = classNum;
		this.averageNum = averageNum;
		this.timeQuantum = timeQuantum;
		this.totleNum = totleNum;
		this.tag = tag;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getClassNum() {
		return classNum;
	}
	public void setClassNum(String classNum) {
		this.classNum = classNum;
	}
	public String getAverageNum() {
		return averageNum;
	}
	public void setAverageNum(String averageNum) {
		this.averageNum = averageNum;
	}
	public String getTimeQuantum() {
		return timeQuantum;
	}
	public void setTimeQuantum(String timeQuantum) {
		this.timeQuantum = timeQuantum;
	}
	public String getTotleNum() {
		return totleNum;
	}
	public void setTotleNum(String totleNum) {
		this.totleNum = totleNum;
	}
	public MyclassFm() {
		super();
	}
	public MyclassFm(String classNum, String averageNum, String timeQuantum,
			String totleNum) {
		super();
		this.classNum = classNum;
		this.averageNum = averageNum;
		this.timeQuantum = timeQuantum;
		this.totleNum = totleNum;
	}
	@Override
	public String toString() {
		return "MyclassFm [classNum=" + classNum + ", averageNum=" + averageNum
				+ ", timeQuantum=" + timeQuantum + ", totleNum=" + totleNum
				+ "]";
	}
	
}
