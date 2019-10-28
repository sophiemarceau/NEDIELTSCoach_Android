package com.lels.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.lels.group.tool.GroupList;

public class GroupMapClass implements Serializable {
	private List<GroupList> list;

	private HashMap<Integer, GroupList> map;

	public List<GroupList> getList() {
		return list;
	}

	public void setList(List<GroupList> list) {
		this.list = list;
	}

	public GroupMapClass() {
		super();
	}

	public GroupMapClass(HashMap<Integer, GroupList> map) {
		super();
		this.map = map;
	}

	public HashMap<Integer, GroupList> getMap() {
		return map;
	}

	public void setMap(HashMap<Integer, GroupList> map2) {
		this.map = map2;
	}

}
