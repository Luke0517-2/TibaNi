package com.cga102g3.web.faq2.model;

public class FaqVO implements java.io.Serializable{
	private Integer FAQID;
	private String ques;
	private String ans;
	public Integer getFAQID() {
		return FAQID;
	}
	public void setFAQID(Integer fAQID) {
		FAQID = fAQID;
	}
	public String getQues() {
		return ques;
	}
	public void setQues(String ques) {
		this.ques = ques;
	}
	public String getAns() {
		return ans;
	}
	public void setAns(String ans) {
		this.ans = ans;
	}
	

	
	
	
}
