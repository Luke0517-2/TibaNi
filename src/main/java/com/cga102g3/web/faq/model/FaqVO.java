package com.cga102g3.web.faq.model;

public class FaqVO implements java.io.Serializable{
	private Integer FAQID;
	private String ques;
	private String ans;
	public Integer getFAQID() {
		return FAQID;
	}
	public void setFAQ_ID(Integer fAQ_ID) {
		FAQID = fAQ_ID;
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
