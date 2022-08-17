package com.cga102g3.web.mem.model;

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
	private static final String SIGNOUT = "INSERT INTO member (mbr_account,mbr_password,mbr_name,mbr_gender,mbr_mobile,mbr_addr,mbr_email,mbr_birth) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT = "SELECT * FROM member order by mbr_ID";
	private static final String GET_ONE_STMT = "SELECT * FROM member where mbr_ID = ?";
	private static final String DELETE = "DELETE FROM member where mbr_ID = ?";
	private static final String UPDATE = "UPDATE member set mbr_password=?, mbr_name=?, mbr_mobile=?, mbr_addr=?, mbr_email=?, mbr_birth=? where mbr_ID = ?";
	private static final String LOGIN = "SELECT * FROM member where mbr_account = ? and mbr_password=  ?";
	private static final String FORGOT = "SELECT mbr_name, mbr_password  FROM member where mbr_email = ?";
	private static final String UPDATESTATUS = "UPDATE member set mbr_status= 1 where mbr_ID = ?";
	private static final String UPDATESTATUSBYEMP = "UPDATE member set mbr_status= ? where mbr_ID = ?";
	private static final String SIGNUPSTATUS = "SELECT mbr_ID FROM member where mbr_email = ?";
	
	public void signup(MemVO memVO) {
		// TODO Auto-generated method stub
				Connection con = null;
				PreparedStatement pstmt = null;
				try {
					Class.forName(driver);
					con = DriverManager.getConnection(url, userid, passwd);
					pstmt = con.prepareStatement(SIGNOUT);

					pstmt.setString(1, memVO.getMbrAccount());
					pstmt.setString(2, memVO.getMbrPassword());
					pstmt.setString(3, memVO.getMbrName());
					pstmt.setInt(4, memVO.getMbrGender());
					pstmt.setString(5, memVO.getMbrMobile());
					pstmt.setString(6, memVO.getMbrAddr());
					pstmt.setString(7, memVO.getMbrEmail());
					pstmt.setDate(8, memVO.getMbrBirth());
					
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
			
			pstmt.setString(1, memVO.getMbrPassword());
			pstmt.setString(2, memVO.getMbrName());
			pstmt.setString(3, memVO.getMbrMobile());
			pstmt.setString(4, memVO.getMbrAddr());
			pstmt.setString(5, memVO.getMbrEmail());
			pstmt.setDate(6, memVO.getMbrBirth());
			pstmt.setInt(7, memVO.getMbrID());

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
	public MemVO findMemAccountAndPassword(String mbrAccount, String mbrPassword) {
		MemVO memVO06 = null;
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
		
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, userid, passwd);
            pstmt = con.prepareStatement(LOGIN);
            pstmt.setString(1, mbrAccount);
            pstmt.setString(2, mbrPassword);

            rs = pstmt.executeQuery();
            while (rs.next()) {
            	memVO06 = new MemVO();
            	memVO06.setMbrID(rs.getInt("mbr_ID"));
            	memVO06.setMbrAccount(rs.getString("mbr_account"));
            	memVO06.setMbrPassword(rs.getString("mbr_password"));
            	memVO06.setMbrStatus(rs.getInt("mbr_status"));
            	memVO06.setMbrName(rs.getString("mbr_name"));
            	memVO06.setMbrGender(rs.getInt("mbr_gender"));
            	memVO06.setMbrMobile(rs.getString("mbr_mobile"));
            	memVO06.setMbrAddr(rs.getString("mbr_addr"));
            	memVO06.setMbrEmail(rs.getString("mbr_email"));
				memVO06.setMbrBirth(rs.getDate("mbr_birth"));
				memVO06.setMbrJointime(rs.getDate("mbr_jointime"));
				memVO06.setTcoinBal(rs.getInt("Tcoin_bal"));
            }
	}catch (ClassNotFoundException e) {
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
        return memVO06;
	}
		
	public MemVO findNamePassWordByEmail(String mbrEmail) {
		MemVO memVO07 = null;
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
		
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, userid, passwd);
            pstmt = con.prepareStatement(FORGOT);
            pstmt.setString(1, mbrEmail);

            rs = pstmt.executeQuery();
            while (rs.next()) {
            	memVO07 = new MemVO();
            	memVO07.setMbrPassword(rs.getString("mbr_password"));
            	memVO07.setMbrName(rs.getString("mbr_name"));

            }
	}catch (ClassNotFoundException e) {
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
        return memVO07;
	};
	

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
	// 點擊Mail後改變狀態為1
	public void updateStatus(Integer mbrID) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATESTATUS);
			
			pstmt.setInt(1, mbrID);

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
	
	public void updateStatusByEmp(Integer mbrID, Integer mbrStatus) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATESTATUSBYEMP);
			
			pstmt.setInt(1, mbrStatus);
			pstmt.setInt(2, mbrID);

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
	
	
	// 從mail取出會員ID
	 public MemVO signupStatus(MemVO memVO) {
		 MemVO memVO08 = null;
			Connection con = null;
	        PreparedStatement pstmt = null;
	        ResultSet rs = null;
			
	        try {
	            Class.forName(driver);
	            con = DriverManager.getConnection(url, userid, passwd);
	            pstmt = con.prepareStatement(SIGNUPSTATUS);
	            pstmt.setString(1, memVO.getMbrEmail());

	            rs = pstmt.executeQuery();
	            while (rs.next()) {
	            	memVO08 = new MemVO();
	            	memVO08.setMbrID(rs.getInt("mbr_ID"));
	            }
		}catch (ClassNotFoundException e) {
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
	        return memVO08;		 
	 }
	

}
