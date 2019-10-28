package com.lels.bean;

public class StudentStartInfo {
	private String stustart_num;
	private String stustart_content;
	public String getStustart_num() {
		return stustart_num;
	}
	public void setStustart_num(String stustart_num) {
		this.stustart_num = stustart_num;
	}
	public String getStustart_content() {
		return stustart_content;
	}
	public void setStustart_content(String stustart_content) {
		this.stustart_content = stustart_content;
	}
	public StudentStartInfo() {
		super();
	}
	public StudentStartInfo(String stustart_num, String stustart_content) {
		super();
		this.stustart_num = stustart_num;
		this.stustart_content = stustart_content;
	}
	
}
