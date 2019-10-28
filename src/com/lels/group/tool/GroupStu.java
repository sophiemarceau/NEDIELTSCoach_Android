package com.lels.group.tool;

import java.io.Serializable;

/*
"Data": {
	  "size": 用于判断是否有分组，0没有,大于0有；只有大于0时，才会有下面信息
	  "groupMode": 分组规则,
	  "groupCnt": 分组数,
	  "groupInfos": [ 多个分组LIST
	   [{
	    "GroupMethod": 分组规则,
	    "GroupCnt": 分组数,
	    "GroupNum": 组号
	    "sName": 学生姓名,
	    "IconUrl": 学生头像,
	    "GroupOrder": 组内顺序号
	   }]
	  ]
	 } */

/*
"Data": {   "groupInfos": [[{
	 *     "ActiveClassGroupID": 分组ID,     "StudentID": 学生UID,
	 *     "GroupNum": 组号     "sName": 学生姓名,     "IconUrl": 学生头像,
	 *     "GroupOrder": 组内顺序号    }]]  }*/

public class GroupStu implements Serializable{
	private String ActiveClassGroupID;//
	private String StudentID;//
	private String GroupMethod;
	private String GroupCnt;
	private String GroupNum;//
	private String sName;//
	private String IconUrl;//
	private String GroupOrder;//
	
	public GroupStu() {
		super();
	}
	
	

	public GroupStu(String groupCnt, String groupNum, String sName,
			String iconUrl) {
		super();
		this.GroupCnt = groupCnt;
		this.GroupNum = groupNum;
		this.sName = sName;
		this.IconUrl = iconUrl;
	}



	public GroupStu(String GroupCnt, String groupMethod, String groupNum,
			String sName, String iconUrl) {
		super();
		GroupCnt = GroupCnt;
		GroupMethod = groupMethod;
		GroupNum = groupNum;
		this.sName = sName;
		IconUrl = iconUrl;
		
	}



	public GroupStu(String activeClassGroupID, String studentID,
			String groupNum, String sName, String iconUrl, String groupOrder) {
		super();
		ActiveClassGroupID = activeClassGroupID;
		StudentID = studentID;
		GroupNum = groupNum;
		this.sName = sName;
		IconUrl = iconUrl;
		GroupOrder = groupOrder;
	}

	public String getActiveClassGroupID() {
		return ActiveClassGroupID;
	}
	public void setActiveClassGroupID(String activeClassGroupID) {
		ActiveClassGroupID = activeClassGroupID;
	}
	public String getStudentID() {
		return StudentID;
	}
	public void setStudentID(String studentID) {
		StudentID = studentID;
	}
	public String getGroupMethod() {
		return GroupMethod;
	}
	public void setGroupMethod(String groupMethod) {
		GroupMethod = groupMethod;
	}
	public String getGroupCnt() {
		return GroupCnt;
	}
	public void setGroupCnt(String groupCnt) {
		GroupCnt = groupCnt;
	}
	public String getGroupNum() {
		return GroupNum;
	}
	public void setGroupNum(String groupNum) {
		GroupNum = groupNum;
	}
	public String getsName() {
		return sName;
	}
	public void setsName(String sName) {
		this.sName = sName;
	}
	public String getIconUrl() {
		return IconUrl;
	}
	public void setIconUrl(String iconUrl) {
		IconUrl = iconUrl;
	}
	public String getGroupOrder() {
		return GroupOrder;
	}
	public void setGroupOrder(String groupOrder) {
		GroupOrder = groupOrder;
	}
	@Override
	public String toString() {
		return "GroupStu [ActiveClassGroupID=" + ActiveClassGroupID
				+ ", StudentID=" + StudentID + ", GroupMethod=" + GroupMethod
				+ ", GroupCnt=" + GroupCnt + ", GroupNum=" + GroupNum
				+ ", sName=" + sName + ", IconUrl=" + IconUrl + ", GroupOrder="
				+ GroupOrder + "]";
	}
	
	
}
