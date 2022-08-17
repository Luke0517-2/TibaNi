package com.cga102g3.web.note.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteJDBCDAO implements NoteDAOInterface {
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/bookstore?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "abc123";

	private static final String INSERT_STMT = 
		"INSERT INTO `note` (`mbr_ID`, `note_content_type`, `note_content`)  VALUES (?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT `note_ID`,`mbr_ID`,`note_time`, `note_content_type`, `note_content` FROM `note` order by note_ID desc";
	private static final String GET_ONE_STMT = 
		"SELECT `note_ID`,`mbr_ID`,`note_time`, `note_content_type`, `note_content` FROM `note` where note_ID = ?";
	private static final String DELETE = 
		"DELETE FROM `note` where note_ID = ?";
	private static final String UPDATE = 
		"UPDATE `note` set `mbr_ID`=?, `Note_time`, `note_content_type`, `note_content` where note_ID = ?";

	@Override
	public void insert(NoteVO noteVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			
			pstmt.setInt(1, noteVO.getMbrID());
			pstmt.setInt(2, noteVO.getNoteContentType());
			pstmt.setString(3, noteVO.getNoteContent());


			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	public void update(NoteVO noteVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			
			pstmt.setInt(1, noteVO.getMbrID());
			pstmt.setTimestamp(2, noteVO.getNoteTime());
			pstmt.setInt(3, noteVO.getNoteContentType());
			pstmt.setString(4, noteVO.getNoteContent());
			pstmt.setInt(5, noteVO.getNoteID()); //�Ĥ@��PK

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	public void delete(Integer note_ID) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setInt(1, note_ID);

			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
	public NoteVO findByPrimaryKey(Integer note_ID) {

		NoteVO noteVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setInt(1, note_ID);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// noteVo �]�٬� Domain objects
				noteVO = new NoteVO();
				noteVO.setNoteID(rs.getInt("Note_ID"));
				noteVO.setMbrID(rs.getInt("Mbr_ID"));
				noteVO.setNoteTime(rs.getTimestamp("Note_time"));
				noteVO.setNoteContentType(rs.getInt("Note_content_type"));
				noteVO.setNoteContent(rs.getString("Note_content"));
			}

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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
		return noteVO;
	}

	@Override
	public List<NoteVO> getAll() {
		List<NoteVO> list = new ArrayList<NoteVO>();
		NoteVO noteVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// noteVO �]�٬� Domain objects
				noteVO = new NoteVO();
				noteVO.setNoteID(rs.getInt("Note_ID"));
				noteVO.setMbrID(rs.getInt("Mbr_ID"));
				noteVO.setNoteTime(rs.getTimestamp("Note_time"));
				noteVO.setNoteContentType(rs.getInt("Note_content_type"));
				noteVO.setNoteContent(rs.getString("Note_content"));
				list.add(noteVO); // Store the row in the list
			}

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
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

	
}