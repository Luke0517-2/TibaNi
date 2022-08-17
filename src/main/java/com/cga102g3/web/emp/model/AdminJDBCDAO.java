package com.cga102g3.web.emp.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AdminJDBCDAO implements AdminDAOInterface {
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/bookstore?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "abc123";

	private static final String INSERT_STMT = "INSERT INTO admin (admin_account, admin_password, admin_name, admin_phone) VALUES (?, ?, ?, ?)";
	private static final String GET_ALL_STMT = "SELECT * FROM admin order by admin_ID";
	private static final String GET_ONE_STMT = "SELECT * FROM admin where admin_ID = ?";
	private static final String DELETE = "DELETE FROM admin where admin_ID = ?";
	private static final String UPDATE = "UPDATE admin set admin_account=?, admin_password=?, admin_name=?, admin_phone=? where admin_ID = ?";
	private static final String LOGIN = "SELECT * FROM admin where admin_account = ? and admin_password=  ?";
	@Override
	public void insert(AdminVO adminVO) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, adminVO.getAdminAccount());
			pstmt.setString(2, adminVO.getAdminPassword());
			pstmt.setString(3, adminVO.getAdminName());
			pstmt.setString(4, adminVO.getAdminPhone());

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
	public void update(AdminVO adminVO) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, adminVO.getAdminAccount());
			pstmt.setString(2, adminVO.getAdminPassword());
			pstmt.setString(3, adminVO.getAdminName());
			pstmt.setString(4, adminVO.getAdminPhone());
			pstmt.setInt(5, adminVO.getAdminID());
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
	public void delete(Integer adminID) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setInt(1, adminID);

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
	public AdminVO findByPrimaryKey(Integer adminID) {
		// TODO Auto-generated method stub
		AdminVO adminVO4 = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);
			pstmt.setInt(1, adminID);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				// empVo �]�٬� Domain objects

				adminVO4 = new AdminVO();

				adminVO4.setAdminID(rs.getInt("admin_ID"));
				adminVO4.setAdminAccount(rs.getString("admin_account"));
				adminVO4.setAdminPassword(rs.getString("admin_password"));
				adminVO4.setAdminName(rs.getString("admin_name"));
				adminVO4.setAdminPhone(rs.getString("admin_phone"));
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
		return adminVO4;
	}
	
	@Override
	public AdminVO findAdminAccountAndPassword(String adminAccount, String adminPassword) {
		 AdminVO adminVO06 = null;
	        Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;

	        try {
	            Class.forName(driver);
	            con = DriverManager.getConnection(url, userid, passwd);
	            pstmt = con.prepareStatement(LOGIN);
	            pstmt.setString(1, adminAccount);
	            pstmt.setString(2, adminPassword);

	            rs = pstmt.executeQuery();
	            while (rs.next()) {

	            	adminVO06 = new AdminVO();

	            	adminVO06.setAdminID(rs.getInt("admin_ID"));
	            	adminVO06.setAdminAccount(rs.getString("admin_account"));
	            	adminVO06.setAdminPassword(rs.getString("admin_password"));
	            	adminVO06.setAdminName(rs.getString("admin_name"));
	            	adminVO06.setAdminPhone(rs.getString("admin_phone"));
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
			return adminVO06;
	    };

	@Override
	public List<AdminVO> getAll() {
		// TODO Auto-generated method stub
		List<AdminVO> list = new ArrayList<AdminVO>();
		AdminVO adminVO5 = null;

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
				adminVO5 = new AdminVO();
				adminVO5.setAdminID(rs.getInt("admin_ID"));
				adminVO5.setAdminAccount(rs.getString("admin_account"));
				adminVO5.setAdminPassword(rs.getString("admin_password"));
				adminVO5.setAdminName(rs.getString("admin_name"));
				adminVO5.setAdminPhone(rs.getString("admin_phone"));
				list.add(adminVO5); // Store the row in the list
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
		AdminJDBCDAO dao = new 	AdminJDBCDAO();

		// �s�W
//	AdminVO adminVO1 = new 	AdminVO();
//	adminVO1.setAdminAccount("wwwwwww");
//	adminVO1.setAdminPassword("123456");
//	adminVO1.setAdminName("�d�ç�");
//	adminVO1.setAdminPhone("0911111111");
//	dao.insert(adminVO1);

		// �ק�

//		AdminVO adminVO2 = new AdminVO();
//		adminVO2.setAdminAccount("bbbbbb");
//		adminVO2.setAdminPassword("123456");
//		adminVO2.setAdminName("�d�ç�2");
//		adminVO2.setAdminPhone("0922222222");
//		adminVO2.setAdminID(2);
//		dao.update(adminVO2);

//	// �R��

//	dao.delete(3);

		// findPrimaryKey
//	AdminVO adminVO3 = dao.findByPrimaryKey(1);
//	System.out.println("Admin_id: " + adminVO3.getAdminID() + ",");
//	System.out.println("Admin_account: " + adminVO3.getAdminAccount() + ",");
//	System.out.println("Admin_password: " + adminVO3.getAdminPassword() + ",");
//	System.out.println("Admin_name: " + adminVO3.getAdminName() + ",");
//	System.out.println("Admiv_phone: " + adminVO3.getAdminPhone() + ",");
//	System.out.println("---------------------");

		// findAdminAccountAndPassword
		AdminVO adminVO6 = dao.findAdminAccountAndPassword("abcdef", "123456");
		System.out.println("Admin_id: " + adminVO6.getAdminID() + ",");
		System.out.println("Admin_account: " + adminVO6.getAdminAccount() + ",");
		System.out.println("Admin_password: " + adminVO6.getAdminPassword() + ",");
		System.out.println("Admin_name: " + adminVO6.getAdminName() + ",");
		System.out.println("Admiv_phone: " + adminVO6.getAdminPhone() + ",");
		System.out.println("---------------------");

		
		
		
		// list����
//		List<AdminVO> list = dao.getAll();
//		for (AdminVO adm : list) {
//			System.out.println("Admin_id: " + adm.getAdminID() + ",");
//			System.out.println("Admin_account: " + adm.getAdminAccount() + ",");
//			System.out.println("Admin_password: " + adm.getAdminPassword() + ",");
//			System.out.println("Admin_name: " + adm.getAdminName() + ",");
//			System.out.println("Admiv_phone: " + adm.getAdminPhone() + ",");
//			System.out.println("---------------------");
//		}
	}
}
