package com.lels.bean;



public class Students {
	private String stu_img_url;
	private String stu_name;
	private String stu_num;
	public void setTag(String tag) {
		this.tag = tag;
	}
	private String stu_finish_num;
	private String stu_downdays;
	private String tag;
	private String stu_sex_img;
	public Students(String stu_name, String stu_num, String stu_finish_num,
			String stu_downdays, String tag) {
		super();
		this.stu_name = stu_name;
		this.stu_num = stu_num;
		this.stu_finish_num = stu_finish_num;
		this.stu_downdays = stu_downdays;
		this.tag = tag;
		
	}
	public String getTag() {
		return tag;
	}
	public String getStu_downdays() {
		return stu_downdays;
	}
	public void setStu_downdays(String stu_downdays) {
		this.stu_downdays = stu_downdays;
	}
	
	public String getStu_img_url() {
		return stu_img_url;
	}
	public void setStu_img_url(String stu_img_url) {
		this.stu_img_url = stu_img_url;
	}
	public String getStu_name() {
		return stu_name;
	}
	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}
	public String getStu_num() {
		return stu_num;
	}
	public void setStu_num(String stu_num) {
		this.stu_num = stu_num;
	}
	public String getStu_finish_num() {
		return stu_finish_num;
	}
	public void setStu_finish_num(String stu_finish_num) {
		this.stu_finish_num = stu_finish_num;
	}
	public String getStu_sex_img() {
		return stu_sex_img;
	}
	public void setStu_sex_img(String stu_sex_img) {
		this.stu_sex_img = stu_sex_img;
	}
	public Students(String stu_name, String stu_num, String stu_finish_num,
			String stu_downdays) {
		super();
		this.stu_name = stu_name;
		this.stu_num = stu_num;
		this.stu_finish_num = stu_finish_num;
		this.stu_downdays = stu_downdays;
	}
	public Students(String stu_name, String stu_num, String stu_finish_num) {
		super();
		this.stu_name = stu_name;
		this.stu_num = stu_num;
		this.stu_finish_num = stu_finish_num;
	}
	public Students() {
		super();
	}
	
}
