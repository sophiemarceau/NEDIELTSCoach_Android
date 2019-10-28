/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015-10-21 
 * 
 *******************************************************************************/ 
package com.lels.bean;

import java.util.List;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015-10-21
 * 作者:	 于耀东
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class VotehisVotes {
	private String ID;
	private String ActiveClassID;
	private String Subject;
	private List<Voteopts> Voteoptslist;
	/**
	 * @return ssssssss
	 */
	public String getID() {
		return ID;
	}
	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}
	/**
	 * @return ssssssss
	 */
	public String getActiveClassID() {
		return ActiveClassID;
	}
	/**
	 * @param activeClassID the activeClassID to set
	 */
	public void setActiveClassID(String activeClassID) {
		ActiveClassID = activeClassID;
	}
	/**
	 * @return ssssssss
	 */
	public String getSubject() {
		return Subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		Subject = subject;
	}
	/**
	 * @return ssssssss
	 */
	public List<Voteopts> getVoteoptslist() {
		return Voteoptslist;
	}
	/**
	 * @param voteoptslist the voteoptslist to set
	 */
	public void setVoteoptslist(List<Voteopts> voteoptslist) {
		Voteoptslist = voteoptslist;
	}
	/**
	 * @param iD
	 * @param activeClassID
	 * @param subject
	 * @param voteoptslist
	 */
	public VotehisVotes(String iD, String activeClassID, String subject,
			List<Voteopts> voteoptslist) {
		super();
		ID = iD;
		ActiveClassID = activeClassID;
		Subject = subject;
		Voteoptslist = voteoptslist;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VotehisVotes [ID=" + ID + ", ActiveClassID=" + ActiveClassID
				+ ", Subject=" + Subject + ", Voteoptslist=" + Voteoptslist
				+ "]";
	}
	
	

}
