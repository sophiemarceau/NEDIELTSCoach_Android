package com.lels.group.tool;

import java.io.Serializable;
import java.util.List;

public class GroupList implements Serializable{
	private List<GroupStu> stuList;

	public List<GroupStu> getStuList() {
		return stuList;
	}

	public void setStuList(List<GroupStu> stuList) {
		this.stuList = stuList;
	}

	public GroupList(List<GroupStu> stuList) {
		super();
		this.stuList = stuList;
	}

	@Override
	public String toString() {
		return "GroupList [stuList=" + stuList + "]";
	}
	
	
}
