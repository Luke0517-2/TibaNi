package com.cga102g3.web.review.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookReviewJDBCDAO implements BookReviewDAOInterface {

	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/bookstore?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "abc123";

	private static final String INSERT_STMT = "INSERT INTO book_review(book_ID, mbr_ID, review_content, review_date, review_status) VALUES (?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = "SELECT review_ID, book_ID, mbr_ID, review_content, review_date, review_status FROM book_review order by review_ID";
	private static final String GET_ONE_STMT = "SELECT review_ID, book_ID, mbr_ID, review_content, review_date, review_status FROM book_review where review_ID = ?";
	private static final String DELETE = "DELETE FROM book_review where review_ID = ?";
	private static final String UPDATE = "UPDATE book_review set book_ID=?, mbr_ID=?, review_content=?, review_date=?, review_status=? where review_ID = ?";

	@Override
	public void insert(BookReviewVO bookreviewVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setInt(1, bookreviewVO.getBookID());
			pstmt.setInt(2, bookreviewVO.getMbrID());
			pstmt.setString(3, bookreviewVO.getReviewContent());
			pstmt.setTimestamp(4, bookreviewVO.getReviewTime());
			pstmt.setInt(5, bookreviewVO.getReviewStatus());
			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

	@Override
	public void update(BookReviewVO bookreviewVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setInt(1, bookreviewVO.getBookID());
			pstmt.setInt(2, bookreviewVO.getMbrID());
			pstmt.setString(3, bookreviewVO.getReviewContent());
			pstmt.setTimestamp(4, bookreviewVO.getReviewTime());
			pstmt.setInt(5, bookreviewVO.getReviewStatus());
			pstmt.setInt(6, bookreviewVO.getReviewID());
			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

	@Override
	public void delete(Integer reviewID) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setInt(1, reviewID);
			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
	}

	@Override
	public BookReviewVO findByPrimaryKey(Integer reviewID) {

		BookReviewVO bookreviewVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setInt(1, reviewID);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// BookReviewVO 也稱?�� Domain objects
				bookreviewVO = new BookReviewVO();
				bookreviewVO.setReviewID(rs.getInt("review_ID"));
				bookreviewVO.setBookID(rs.getInt("book_ID"));
				bookreviewVO.setMbrID(rs.getInt("mbr_ID"));
				bookreviewVO.setReviewContent(rs.getString("review_Content"));
				bookreviewVO.setReviewTime(rs.getTimestamp("review_Date"));
				bookreviewVO.setReviewStatus(rs.getInt("review_Status"));
			}

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return bookreviewVO;
	}

	@Override
	public List<BookReviewVO> getAll() {
		List<BookReviewVO> list = new ArrayList<BookReviewVO>();
		BookReviewVO bookreviewVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				bookreviewVO = new BookReviewVO();
				bookreviewVO.setReviewID(rs.getInt("review_ID"));
				bookreviewVO.setBookID(rs.getInt("book_ID"));
				bookreviewVO.setMbrID(rs.getInt("mbr_ID"));
				bookreviewVO.setReviewContent(rs.getString("review_content"));
				bookreviewVO.setReviewTime(rs.getTimestamp("review_date"));
				bookreviewVO.setReviewStatus(rs.getInt("review_status"));
				list.add(bookreviewVO); // Store the row in the list
			}
			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}

	public static void main(String[] args) {

		BookReviewJDBCDAO dao = new BookReviewJDBCDAO();

		// ?���?
//		BookReviewVO bookreviewVO1 = new BookReviewVO();
//		bookreviewVO1.setBookID(5);
//		bookreviewVO1.setMbrID(3);
//		bookreviewVO1.setReviewContent("?�本?��??��?��?�錯??��??");
//		bookreviewVO1.setReviewTime(new Timestamp(System.currentTimeMillis()));
//		bookreviewVO1.setReviewStatus(0);
//		dao.insert(bookreviewVO1);

//		// 修改
//		BookReviewVO bookreviewVO2 = new BookReviewVO();
//		bookreviewVO2.setReviewID(5);
//		bookreviewVO2.setBookID(1);
//		bookreviewVO2.setMbrID(1);
//		bookreviewVO2.setReviewContent("??��?��?�發?��?�本?��??�好");
//		bookreviewVO2.setReviewTime(new Timestamp(System.currentTimeMillis()));
//		bookreviewVO2.setReviewStatus(1);
//		dao.update(bookreviewVO2);

//		// ?��?��
//		dao.delete(5);

		// FK?���?
//		BookReviewVO bookreviewVO3 = dao.findByPrimaryKey(2);
//		System.out.println(bookreviewVO3.getReviewID() + ",");
//		System.out.println(bookreviewVO3.getBookID() + ",");
//		System.out.println(bookreviewVO3.getMbrID() + ",");
//		System.out.println(bookreviewVO3.getReviewContent() + ",");
//		System.out.println(bookreviewVO3.getReviewTime() + ",");
//		System.out.println(bookreviewVO3.getReviewStatus() + ",");
//		System.out.println("---------------------");

		// ?��詢All
		List<BookReviewVO> list = dao.getAll();
		for (BookReviewVO aBookReview : list) {
			System.out.print(aBookReview.getReviewID() + ",");
			System.out.print(aBookReview.getBookID() + ",");
			System.out.print(aBookReview.getMbrID() + ",");
			System.out.print(aBookReview.getReviewContent() + ",");
			System.out.print(aBookReview.getReviewTime() + ",");
			System.out.println(aBookReview.getReviewStatus() + ",");
			System.out.println("---------------------");
		}
	}
}
