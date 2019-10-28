package com.lels.bean;

public class Teacher {
	private String url;
	private String name;
	private String function;
	
	public Teacher() {
		super();
	}
	public Teacher(String url,String name, String function) {
		super();
		this.url = url;
		this.name = name;
		this.function = function;
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
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	@Override
	public String toString() {
		return "Teacher [url=" + url + ", name=" + name + ", function="
				+ function + "]";
	}
	
}
