package com.cga102g3.web.faq2.model;

import java.util.List;

public class FaqService {

	private FaqDAOInterface dao;

	public FaqService() {
		//dao = new EmpDAO();
		dao = new FaqJDBCDAO();
	}

	public FaqVO addFaq(String ques, String ans) {

		FaqVO faqVO = new FaqVO();

		faqVO.setQues(ques);
		faqVO.setAns(ans);
		
		dao.insert(faqVO);

		return faqVO;
	}

	public FaqVO updateFaq(String ques, String ans, Integer fAQ_ID) {

		FaqVO faqVO = new FaqVO();

		faqVO.setQues(ques);
		faqVO.setAns(ans);
		faqVO.setFAQID(fAQ_ID);
		
		dao.update(faqVO);

		return faqVO;
	}

	public void deleteFaq(Integer FAQ_ID) {
		dao.delete(FAQ_ID);
	}

	public FaqVO getOneFaq(Integer FAQ_ID) {
		return dao.findByPrimaryKey(FAQ_ID);
	}

	public List<FaqVO> getAll() {
		return dao.getAll();
	}


}
