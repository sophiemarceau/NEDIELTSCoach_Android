package com.lels.bean;

import java.io.Serializable;
import java.util.List;

public class OptionInfo implements Serializable{
	
	private String OrderNum;
	private String Option;
	private String studentChooseCount;
//	private String SQ_ID;
	private String OptionName;
	private  List<StudentChooseInfo> studentChooseList;
	public String getOrderNum() {
		return OrderNum;
	}
	public void setOrderNum(String orderNum) {
		OrderNum = orderNum;
	}
	public String getOption() {
		return Option;
	}
	public void setOption(String option) {
		Option = option;
	}
	public String getStudentChooseCount() {
		return studentChooseCount;
	}
	public void setStudentChooseCount(String studentChooseCount) {
		this.studentChooseCount = studentChooseCount;
	}
//	public String getSQ_ID() {
//		return SQ_ID;
//	}
//	public void setSQ_ID(String sQ_ID) {
//		SQ_ID = sQ_ID;
//	}
	public String getOptionName() {
		return OptionName;
	}
	public void setOptionName(String optionName) {
		OptionName = optionName;
	}
	public List<StudentChooseInfo> getStudentChooseList() {
		return studentChooseList;
	}
	public void setStudentChooseList(List<StudentChooseInfo> studentChooseList) {
		this.studentChooseList = studentChooseList;
	}
	public OptionInfo(String orderNum, String option,
			String studentChooseCount, String optionName,
			List<StudentChooseInfo> studentChooseList) {
		super();
		OrderNum = orderNum;
		Option = option;
		this.studentChooseCount = studentChooseCount;
	//	SQ_ID = sQ_ID;
		OptionName = optionName;
		this.studentChooseList = studentChooseList;
	}
	
	public OptionInfo(List<StudentChooseInfo> studentChooseList) {
		super();
		this.studentChooseList = studentChooseList;
	}
	
	
	public OptionInfo(String option, List<StudentChooseInfo> studentChooseList) {
		super();
		Option = option;
		this.studentChooseList = studentChooseList;
	}
	@Override
	public String toString() {
		return "OptionInfo [OrderNum=" + OrderNum + ", Option=" + Option
				+ ", studentChooseCount=" + studentChooseCount + 
				  ", OptionName=" + OptionName + ", studentChooseList="
				+ studentChooseList + "]";
	}

	
}
