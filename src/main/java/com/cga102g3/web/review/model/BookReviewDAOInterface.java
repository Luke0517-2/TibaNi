package com.cga102g3.web.review.model;

import java.util.List;

public interface BookReviewDAOInterface {

	public void insert(BookReviewVO bookreviewVO);

	public void update(BookReviewVO bookreviewVO);

	void delete(Integer reviewID);

	public BookReviewVO findByPrimaryKey(Integer reviewID);

	public List<BookReviewVO> getAll();

	// ?��?��複�?�查�?(?��?��??�數??��?�Map)(??�傳 List)
	// public List<BookReviewVO> getAll(Map<String, String[]> map);

}
