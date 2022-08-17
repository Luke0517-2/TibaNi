package com.cga102g3.web.emp.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthJDBCDAO implements AuthDAOInterface{
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/bookstore?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "abc123";
	
	private static final String INSERT_STMT = "INSERT INTO authorization (auth_name) VALUES (?)";
	private static final String GET_ALL_STMT = "SELECT * FROM authorization order by auth_ID";
	private static final String GET_ONE_STMT = "SELECT * FROM authorization where auth_ID = ?";
	private static final String DELETE = "DELETE FROM authorization where auth_ID = ?";
	private static final String UPDATE = "UPDATE authorization set auth_name=? where auth_ID = ?";

	@Override
	public void insert(AuthVO authVO) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setString(1, authVO.getAuthName());
		
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
	public void update(AuthVO authVO) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setString(1, authVO.getAuthName());
			pstmt.setInt(2, authVO.getAuthID());
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
	public void delete(Integer authID) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setInt(1, authID);

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
	public AuthVO findByPrimaryKey(Integer authID) {
		// TODO Auto-generated method stub
		AuthVO authVO4 = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);
			pstmt.setInt(1, authID);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				// empVo �]�٬� Domain objects

				authVO4 = new AuthVO();

				authVO4.setAuthID(rs.getInt("auth_ID"));
				authVO4.setAuthName(rs.getString("auth_name"));

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
		return authVO4;
	}

	@Override
	public List<AuthVO> getAll() {
		// TODO Auto-generated method stub
		List<AuthVO> list = new ArrayList<AuthVO>();
		AuthVO authVO5 = null;

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
				authVO5 = new AuthVO();
				authVO5.setAuthID(rs.getInt("auth_ID"));
				authVO5.setAuthName(rs.getString("auth_name"));
				list.add(authVO5); // Store the row in the list
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
	
}
