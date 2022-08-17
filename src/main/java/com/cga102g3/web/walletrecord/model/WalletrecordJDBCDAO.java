package com.cga102g3.web.walletrecord.model;

import java.util.*;
import java.sql.*;

public class WalletrecordJDBCDAO implements WalletrecordDAO_interface {
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/bookstore?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "abc123";

	private static final String INSERT_STMT = 
		"INSERT INTO `wallet_record` (`mbr_ID`, `note`, `amount`)  VALUES (?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT `wallet_rec_no`,`mbr_ID`,`note`, `amount`, `rec_time` FROM `wallet_record` order by wallet_rec_no desc";
	private static final String GET_ONE_STMT = 
		"SELECT `wallet_rec_no`,`mbr_ID`,`note`, `amount`, `rec_time` FROM `wallet_record` where wallet_rec_no = ?";
	private static final String DELETE = 
		"DELETE FROM `wallet_record` where wallet_rec_no = ?";
	private static final String UPDATE = 
		"UPDATE `wallet_record` set `note`=?, `amount`=? where wallet_rec_no = ?";

	
	private static final String GET_ONE_MBR = 
		"SELECT `wallet_rec_no`,`mbr_ID`,`note`, `amount`, `rec_time` FROM `wallet_record` where `mbr_ID` = ? order by wallet_rec_no desc";
	
	private static final String UPDATE_MBR = 
		"UPDATE `member` set `Tcoin_bal`= (SELECT amount FROM wallet_record where note = ? order by wallet_rec_no desc limit 1) + Tcoin_bal where mbr_ID = ?";
	//   update member set Tcoin_bal = (select amount from wallet_record where note = 0 order by wallet_rec_no desc limit 1) + Tcoin_bal where mbr_ID = 7;
	
	@Override
	public void insert(WalletrecordVO walletrecordVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			
			pstmt.setInt(1, walletrecordVO.getMbrID());
			pstmt.setInt(2, walletrecordVO.getNote());
			pstmt.setInt(3, walletrecordVO.getAmount());


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
	public void update(WalletrecordVO walletrecordVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			
		//	pstmt.setInt(1, walletrecordVO.getMbr_ID());
			pstmt.setInt(1, walletrecordVO.getNote());
			pstmt.setInt(2, walletrecordVO.getAmount());
		//	pstmt.setTimestamp(4, walletrecordVO.getRec_time());
			pstmt.setInt(3, walletrecordVO.getWalletRecNo()); //�Ĥ@��PK

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
	public List<WalletrecordVO> findByMemberId(Integer mbr_ID) {
		List<WalletrecordVO> list = new ArrayList<WalletrecordVO>();
		WalletrecordVO walletrecordVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_MBR);

			pstmt.setInt(1, mbr_ID);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// walletrecordVo �]�٬� Domain objects
				walletrecordVO = new WalletrecordVO();
				walletrecordVO.setWalletRecNo(rs.getInt("Wallet_rec_no"));
				walletrecordVO.setMbrID(rs.getInt("Mbr_ID"));
				walletrecordVO.setNote(rs.getInt("Note"));
				walletrecordVO.setAmount(rs.getInt("Amount"));
				walletrecordVO.setRecTime(rs.getTimestamp("Rec_time"));
				list.add(walletrecordVO);
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

	
	@Override
	public WalletrecordVO findByPrimaryKey(Integer wallet_rec_no) {

		WalletrecordVO walletrecordVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setInt(1, wallet_rec_no);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// walletrecordVo �]�٬� Domain objects
				walletrecordVO = new WalletrecordVO();
				walletrecordVO.setWalletRecNo(rs.getInt("Wallet_rec_no"));
				walletrecordVO.setMbrID(rs.getInt("Mbr_ID"));
				walletrecordVO.setNote(rs.getInt("Note"));
				walletrecordVO.setAmount(rs.getInt("Amount"));
				walletrecordVO.setRecTime(rs.getTimestamp("Rec_time"));
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
		return walletrecordVO;
	}
	
	
	@Override
	public List<WalletrecordVO> getAll() {
		List<WalletrecordVO> list = new ArrayList<WalletrecordVO>();
		WalletrecordVO walletrecordVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// walletrecordVO �]�٬� Domain objects
				walletrecordVO = new WalletrecordVO();
				walletrecordVO.setWalletRecNo(rs.getInt("Wallet_rec_no"));
				walletrecordVO.setMbrID(rs.getInt("Mbr_ID"));
				walletrecordVO.setNote(rs.getInt("Note"));
				walletrecordVO.setAmount(rs.getInt("Amount"));
				walletrecordVO.setRecTime(rs.getTimestamp("Rec_time"));
				list.add(walletrecordVO); // Store the row in the list
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

	@Override
	public void updateMEM(WalletrecordVO walletrecordVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE_MBR);
		
			pstmt.setInt(1, walletrecordVO.getNote());
			pstmt.setInt(2, walletrecordVO.getMbrID());

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
	
	
}