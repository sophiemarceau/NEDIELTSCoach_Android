/*******************************************************************************
 * Copyright (c) 2015 by dennis Corporation all right reserved.
 * 2015-10-21 
 * 
 *******************************************************************************/ 
package com.lels.bean;

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
public class Voteopts {
	private String OptionNum;
	private String OptionDesc;
	private String voteNum;
	private String ownVote;
	/**
	 * @return the ownVote
	 */
	public String getOwnVote() {
		return ownVote;
	}
	/**
	 * @param ownVote the ownVote to set
	 */
	public void setOwnVote(String ownVote) {
		this.ownVote = ownVote;
	}
	/**
	 * @return ssssssss
	 */
	public String getOptionNum() {
		return OptionNum;
	}
	/**
	 * @param optionNum the optionNum to set
	 */
	public void setOptionNum(String optionNum) {
		OptionNum = optionNum;
	}
	/**
	 * @return ssssssss
	 */
	public String getOptionDesc() {
		return OptionDesc;
	}
	/**
	 * @param optionDesc the optionDesc to set
	 */
	public void setOptionDesc(String optionDesc) {
		OptionDesc = optionDesc;
	}
	/**
	 * @return ssssssss
	 */
	public String getVoteNum() {
		return voteNum;
	}
	/**
	 * @param voteNum the voteNum to set
	 */
	public void setVoteNum(String voteNum) {
		this.voteNum = voteNum;
	}
	/**
	 * @param optionNum
	 * @param optionDesc
	 * @param voteNum
	 * @param ownVote
	 */
	public Voteopts(String optionNum, String optionDesc, String voteNum,
			String ownVote) {
		super();
		OptionNum = optionNum;
		OptionDesc = optionDesc;
		this.voteNum = voteNum;
		this.ownVote = ownVote;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Voteopts [OptionNum=" + OptionNum + ", OptionDesc="
				+ OptionDesc + ", voteNum=" + voteNum + "]";
	}
	

}
