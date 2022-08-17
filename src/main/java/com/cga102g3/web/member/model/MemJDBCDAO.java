package com.cga102g3.web.member.model;

import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MemJDBCDAO implements MemDAOInterface {
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/bookstore?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "abc123";

	private static final String INSERT_STMT = "INSERT INTO member (mbr_account,mbr_password,mbr_status,mbr_name,mbr_gender,mbr_mobile,mbr_addr,mbr_email,mbr_birth,Tcoin_bal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = "SELECT * FROM member order by mbr_ID";
	private static final String GET_ONE_STMT = "SELECT * FROM member where mbr_ID = ?";
	private static final String DELETE = "DELETE FROM member where mbr_ID = ?";
	private static final String UPDATE = "UPDATE member set mbr_name=?, mbr_mobile=?, mbr_addr=?, mbr_email=?, mbr_birth=? where mbr_ID = ?";

	@Override
	public void insert(MemVO memVO) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, memVO.getMbrAccount());
			pstmt.setString(2, memVO.getMbrPassword());
			pstmt.setInt(3, memVO.getMbrStatus());
			pstmt.setString(4, memVO.getMbrName());
			pstmt.setInt(5, memVO.getMbrGender());
			pstmt.setString(6, memVO.getMbrMobile());
			pstmt.setString(7, memVO.getMbrAddr());
			pstmt.setString(8, memVO.getMbrEmail());
			pstmt.setDate(9, memVO.getMbrBirth());
			pstmt.setInt(10, memVO.getTcoinBal());

			pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException s) {
			s.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException ss) {
				ss.printStackTrace();
			}
		}
	}

	@Override
	public void update(MemVO memVO) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, memVO.getMbrName());
			pstmt.setString(2, memVO.getMbrMobile());
			pstmt.setString(3, memVO.getMbrAddr());
			pstmt.setString(4, memVO.getMbrEmail());
			pstmt.setDate(5, memVO.getMbrBirth());
			pstmt.setInt(6, memVO.getMbrID());

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
	public void delete(Integer mbrID) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setInt(1, mbrID);

			pstmt.executeUpdate();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException s) {
			s.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException ss) {
				ss.printStackTrace();
			}
		}
	}

	@Override
	public MemVO findByPrimaryKey(Integer mbrID) {
		// TODO Auto-generated method stub
		MemVO memVO4 = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);
			pstmt.setInt(1, mbrID);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				// empVo �]�٬� Domain objects

				memVO4 = new MemVO();

				memVO4.setMbrID(rs.getInt("mbr_ID"));
				memVO4.setMbrAccount(rs.getString("mbr_account"));
				memVO4.setMbrPassword(rs.getString("mbr_password"));
				memVO4.setMbrStatus(rs.getInt("mbr_status"));
				memVO4.setMbrName(rs.getString("mbr_name"));
				memVO4.setMbrGender(rs.getInt("mbr_gender"));
				memVO4.setMbrMobile(rs.getString("mbr_mobile"));
				memVO4.setMbrAddr(rs.getString("mbr_addr"));
				memVO4.setMbrEmail(rs.getString("mbr_email"));
				memVO4.setMbrBirth(rs.getDate("mbr_birth"));
				memVO4.setMbrJointime(rs.getDate("mbr_jointime"));
				memVO4.setTcoinBal(rs.getInt("Tcoin_bal"));
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException s) {
			s.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException ss) {
				ss.printStackTrace();
			}
		}
		return memVO4;
	}

	@Override
	public List<MemVO> getAll() {
		// TODO Auto-generated method stub
		List<MemVO> list = new ArrayList<MemVO>();
		MemVO memVO5 = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
//				 empVO �]�٬� Domain objects
				memVO5 = new MemVO();
				memVO5.setMbrID(rs.getInt("mbr_ID"));
				memVO5.setMbrAccount(rs.getString("mbr_account"));
				memVO5.setMbrPassword(rs.getString("mbr_password"));
				memVO5.setMbrStatus(rs.getInt("mbr_status"));
				memVO5.setMbrName(rs.getString("mbr_name"));
				memVO5.setMbrGender(rs.getInt("mbr_gender"));
				memVO5.setMbrMobile(rs.getString("mbr_mobile"));
				memVO5.setMbrAddr(rs.getString("mbr_addr"));
				memVO5.setMbrEmail(rs.getString("mbr_email"));
				memVO5.setMbrBirth(rs.getDate("mbr_birth"));
				memVO5.setMbrJointime(rs.getDate("mbr_jointime"));
				memVO5.setTcoinBal(rs.getInt("Tcoin_bal"));
				list.add(memVO5); // Store the row in the list
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
		MemJDBCDAO dao = new MemJDBCDAO();

		// �s�W
//	MemVO memVO1 = new MemVO();
//	memVO1.setMbrAccount("wwwwwww");
//	memVO1.setMbrPassword("123456");
//	memVO1.setMbrStatus(2);
//	memVO1.setMbrName("�d�ç�");
//	memVO1.setMbrGender(0);	
//	memVO1.setMbrMobile("0999999999");
//	memVO1.setMbrAddr("�x�_���j�w��");
//	memVO1.setMbrEmail("WU@yahoo.com");
//	memVO1.setMbrBirth(java.sql.Date.valueOf("1990-01-01"));
//	memVO1.setTcoinBal(5999);
//	dao.insert(memVO1);

		// �ק�

//		MemVO memVO2 = new MemVO();
//		memVO2.setMbrName("�d�ç�2");
//		memVO2.setMbrMobile("0933333333");
//		memVO2.setMbrAddr("�x�_���ק��");
//		memVO2.setMbrEmail("test@yahoo.com");
//		memVO2.setMbrBirth(java.sql.Date.valueOf("1900-01-01"));
//		memVO2.setMbrID(6);
//		dao.update(memVO2);

//	// �R��

//	dao.delete(6);

		// findPrimaryKey
//	MemVO memVO3 = dao.findByPrimaryKey(1);
//	System.out.println("Mmeber_id: " + memVO3.getMbrID() + ",");
//	System.out.println("Member_account: " + memVO3.getMbrAccount() + ",");
//	System.out.println("Member_password: " + memVO3.getMbrPassword() + ",");
//	System.out.println("Member_status: " + memVO3.getMbrStatus() + ",");
//	System.out.println("Member_name: " + memVO3.getMbrName() + ",");
//	System.out.println("Member_gender: " + memVO3.getMbrGender() + ",");
//	System.out.println("Member_mobile: " + memVO3.getMbrMobile()+ ",");
//	System.out.println("Member_addr: " + memVO3.getMbrAddr()+ ",");
//	System.out.println("Member_email: " + memVO3.getMbrEmail()+ ",");
//	System.out.println("Member_birth: " + memVO3.getMbrBirth()+ ",");
//	System.out.println("Member_joinTime: " + memVO3.getMbrJointime()+ ",");
//	System.out.println("Member_Tcoin: " + memVO3.getTcoinBal()+ ",");
//	System.out.println("---------------------");

		// list����
		List<MemVO> list = dao.getAll();
		for (MemVO aEmp : list) {
			System.out.println("Mmeber_id: " + aEmp.getMbrID() + ",");
			System.out.println("Member_account: " + aEmp.getMbrAccount() + ",");
			System.out.println("Member_password: " + aEmp.getMbrPassword() + ",");
			System.out.println("Member_status: " + aEmp.getMbrStatus() + ",");
			System.out.println("Member_name: " + aEmp.getMbrName() + ",");
			System.out.println("Member_gender: " + aEmp.getMbrGender() + ",");
			System.out.println("Member_mobile: " + aEmp.getMbrMobile() + ",");
			System.out.println("Member_addr: " + aEmp.getMbrAddr() + ",");
			System.out.println("Member_email: " + aEmp.getMbrEmail() + ",");
			System.out.println("Member_birth: " + aEmp.getMbrBirth() + ",");
			System.out.println("Member_joinTime: " + aEmp.getMbrJointime() + ",");
			System.out.println("Member_Tcoin: " + aEmp.getTcoinBal() + ",");
			System.out.println("---------------------");
		}
	}
}
