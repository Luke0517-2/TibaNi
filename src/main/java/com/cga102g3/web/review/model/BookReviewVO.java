package com.cga102g3.web.review.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class BookReviewVO implements Serializable {

	private Integer reviewID;
	private Integer bookID;
	private Integer mbrID;
	private String reviewContent;
	private Timestamp reviewTime;
	private Integer reviewStatus;
	


	public Integer getReviewID() {
		return reviewID;
	}

	public void setReviewID(Integer reviewID) {
		this.reviewID = reviewID;
	}

	public Integer getBookID() {
		return bookID;
	}

	public void setBookID(Integer bookID) {
		this.bookID = bookID;
	}

	public Integer getMbrID() {
		return mbrID;
	}

	public void setMbrID(Integer mbrID) {
		this.mbrID = mbrID;
	}

	public String getReviewContent() {
		return reviewContent;
	}

	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}

	public Timestamp getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Timestamp reviewDate) {
		this.reviewTime = reviewDate;
	}

	public Integer getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(Integer reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	
    // for join mbrname from mbrID
    public com.cga102g3.web.mem.model.MemVO getMemVO() {
    	com.cga102g3.web.mem.model.MemService mbrSvc = new com.cga102g3.web.mem.model.MemService();
    	com.cga102g3.web.mem.model.MemVO memVO = mbrSvc.getOneMem(mbrID);
	    return memVO;
    }
    
}
