package com.cga102g3.web.review.model;

import java.util.List;

public class BookReviewService {

	private BookReviewDAOInterface dao;

	public BookReviewService() {
		dao = new BookReviewJDBCDAO();
	}

	public BookReviewVO insert(BookReviewVO bookreviewVO) {
		dao.insert(bookreviewVO);
		return bookreviewVO;
	}

	public BookReviewVO update(Integer reviewID, Integer bookID, Integer mbrID, String reviewContent, java.sql.Timestamp reviewDate,
			Integer reviewStatus) {

		BookReviewVO bookreviewVO2 = new BookReviewVO();

		bookreviewVO2.setReviewID(reviewID);
		bookreviewVO2.setBookID(bookID);
		bookreviewVO2.setMbrID(mbrID);
		bookreviewVO2.setReviewContent(reviewContent);
		bookreviewVO2.setReviewTime(reviewDate);
		bookreviewVO2.setReviewStatus(reviewStatus);
		dao.update(bookreviewVO2);

		return bookreviewVO2;
	}

	public void delete(Integer reviewID) {
		dao.delete(reviewID);
	}

	public BookReviewVO findByPrimaryKey(Integer reviewID) {
		return dao.findByPrimaryKey(reviewID);
	}

	public List<BookReviewVO> getAll() {
		return dao.getAll();
	}

}
