package com.cga102g3.web.emp.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminAuthJDBCDAO implements AdminAuthDAOInterface {
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/bookstore?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "abc123";

	private static final String INSERT_STMT = "INSERT INTO admin_auth (admin_id, auth_id) VALUES (?, ?)";
//	private static final String GET_ALL_STMT = "SELECT * FROM admin_auth order by admin_ID";
	private static final String GET_ONE_STMT = "SELECT auth_id FROM admin_auth where admin_ID = ?";
	private static final String DELETE = "DELETE FROM admin_auth where admin_ID = ?";
//	private static final String UPDATE = "UPDATE admin_auth set admin_id =?, auth_id =? where admin_ID = ?";

	@Override
	public void insert(AdminAuthVO adminAuthVO) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setInt(1, adminAuthVO.getAdminmID());
			pstmt.setInt(2, adminAuthVO.getAuthID());
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
	public void update(AdminAuthVO adminAuthVO) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(AdminAuthVO adminAuthVO) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setInt(1, adminAuthVO.getAdminmID());
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
	public List<AdminAuthVO> findByPrimaryKey(Integer adminID) {
		// TODO Auto-generated method stub
		List list = new ArrayList<AdminAuthVO>();
		AdminAuthVO adminAuthVO = null;
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

				adminAuthVO = new AdminAuthVO();
//				adminAuthVO.setAdminmID(rs.getInt("admin_ID"));
				adminAuthVO.setAuthID(rs.getInt("auth_ID"));
				list.add(adminAuthVO);
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
		return list;
	}

	@Override
	public List<AdminAuthVO> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {

//		AdminAuthJDBCDAO dao = new AdminAuthJDBCDAO();
//		List<AdminAuthVO> list = dao.findByPrimaryKey(1);
//		for (AdminAuthVO adau : list) {
//			System.out.println("admin_id: " + adau.getAdminmID() + ",");
//			System.out.println("Auth_id: " + adau.getAuthID() + ",");
//			System.out.println("---------------------");
//
//		}
}
}
