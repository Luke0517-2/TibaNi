package com.cga102g3.web.prod_sale.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cga102g3.core.util.JDBCUtil;
import com.cga102g3.web.prod_sale.dao.ProdSaleDAOInterface;
import com.cga102g3.web.prod_sale.entity.ProdSaleVO;

public class ProdSaleJDBCDAO implements ProdSaleDAOInterface{
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/bookstore?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "abc123";
	
	private static final String INSERT_STMT = 
		"INSERT INTO prod_sale (sale_ID, prod_ID, sale_price) VALUES (?, ?, ?)";
	private static final String GET_ALL_STMT = 
		"SELECT sale_ID, prod_ID, sale_price FROM prod_sale order by sale_ID";
	private static final String GET_ONE_STMT = 
		"SELECT sale_ID, prod_ID, sale_price FROM prod_sale where sale_ID = ? and prod_ID = ?";
	private static final String DELETE = 
		"DELETE FROM prod_sale where sale_ID = ? and prod_ID = ?";
	private static final String UPDATE = 
		"UPDATE prod_sale set sale_price = ? where sale_ID = ? and prod_ID = ?";
	
	
	@Override
	public void insert(ProdSaleVO prodsaleVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setInt(1, prodsaleVO.getSaleID());
			pstmt.setInt(2, prodsaleVO.getProdID());
			pstmt.setInt(3, prodsaleVO.getSalePrice());
			
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
	public void update(ProdSaleVO prodsaleVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setInt(1, prodsaleVO.getSalePrice());
			pstmt.setInt(2, prodsaleVO.getSaleID());
			pstmt.setInt(3, prodsaleVO.getProdID());

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
	public void delete(Integer sale_ID, Integer prod_ID) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setInt(1, sale_ID);
			pstmt.setInt(2, prod_ID);

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
	public ProdSaleVO findByPrimaryKey(Integer saleID, Integer prodID) {

		ProdSaleVO prodsaleVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setInt(1, saleID);
			pstmt.setInt(2, prodID);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				// prodsaleVO �]�٬� Domain objects
				prodsaleVO = new ProdSaleVO();
				prodsaleVO.setSaleID(rs.getInt("sale_ID"));
				prodsaleVO.setProdID(rs.getInt("prod_ID"));
				prodsaleVO.setSalePrice(rs.getInt("sale_price"));
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
		return prodsaleVO;
	}
	
	@Override
	public List<ProdSaleVO> getAll() {
		List<ProdSaleVO> list = new ArrayList<ProdSaleVO>();
		ProdSaleVO prodsaleVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// prodsaleVO �]�٬� Domain objects
				prodsaleVO = new ProdSaleVO();
				prodsaleVO.setSaleID(rs.getInt("sale_ID"));
				prodsaleVO.setProdID(rs.getInt("prod_ID"));
				prodsaleVO.setSalePrice(rs.getInt("sale_price"));
				list.add(prodsaleVO); // Store the row in the list
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

	/**
	 * @description: 同時新增促銷專案及產品
	 * @param: [prodSaleVO, con]
	 * @return: void
	 * @auther: Luke
	 * @date: 2022/06/29 10:54:54
	 */
	@Override
	public void insert2(ProdSaleVO prodSaleVO, Connection con) {
		PreparedStatement ps = null;
		final String sql = "INSERT INTO prod_sale (sale_ID, prod_ID, sale_price) VALUES (?, ?, ?)";
		System.out.println("dao");
		try {
			System.out.println("dao2");
			ps = con.prepareStatement(sql);
			ps.setInt(1,prodSaleVO.getSaleID());
			ps.setInt(2,prodSaleVO.getProdID());
			ps.setInt(3,prodSaleVO.getSalePrice());
			ps.executeUpdate();
			System.out.println("dao3");
		} catch (SQLException se) {
			if (con != null) {
				try {
					// 3●設定於當有exception發生時之catch區塊內
					System.err.print("Transaction is being ");
					System.err.println("rolled back-由-ProdSale");
					con.rollback();
				} catch (SQLException excep) {
					throw new RuntimeException("rollback error occured. "
							+ excep.getMessage());
				}
			}
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		}finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
		}
	}

	/**
	 * @description: 利用saleID返回單一ProdSaleVO
	 * @param: [saleID]
	 * @return: com.cga102g3.web.prod_sale.entity.ProdSaleVO
	 * @auther: Luke
	 * @date: 2022/07/01 10:57:45
	 */
	@Override
	public List<Map<String,Object>> getBySaleID(Integer saleID) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Map<String,Object>> list = new ArrayList<>();
		final String sql = "select p.prod_ID,ps.sale_price,b.title\n" +
				"from product p\n" +
				"\tjoin prod_sale ps\n" +
				"\ton ps.prod_ID = p.prod_ID\n" +
				"    join book b\n" +
				"    on p.book_ID = b.book_ID\n" +
				"where ps.sale_ID = ?";
		try {
			con = JDBCUtil.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1,saleID);
			rs = ps.executeQuery();
			while (rs.next()){
				Map<String,Object> map = new HashMap<>();
				map.put("prodID",rs.getInt("prod_ID"));
				map.put("salePrice",rs.getInt("sale_price"));
				map.put("title",rs.getString("title"));
				list.add(map);
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			JDBCUtil.close(con,ps,rs);
		}
		return list;
	}

	public static void main(String[] args) {

		ProdSaleJDBCDAO dao = new ProdSaleJDBCDAO();
		
		// �s�W
//		ProdSaleVO prodsaleVO1 = new ProdSaleVO();
//		prodsaleVO1.setSaleID(4);
//		prodsaleVO1.setProdID(5);
//		prodsaleVO1.setSalePrice(200);
//		dao.insert(prodsaleVO1);

//		// �ק� ���D��ɡA�O�o��� PK ���ȭn�� DB �̭��������@�ˡA����ۤv�ö�
//		ProdSaleVO prodsaleVO2 = new ProdSaleVO();
//		prodsaleVO2.setSalePrice(350);
//		prodsaleVO2.setSaleID(4);
//		prodsaleVO2.setProdID(5);
//		dao.update(prodsaleVO2);

		// �R��
//		dao.delete(4,5);

		// �d��
//		ProdSaleVO prodsaleVO3 = dao.findByPrimaryKey(1,1);
//		System.out.println("SaleID = " + prodsaleVO3.getSaleID() + ",");
//		System.out.println("ProdID = " +prodsaleVO3.getProdID() + ",");
//		System.out.println("Price = " +prodsaleVO3.getSalePrice() + ".");

		// �d��2
		List<ProdSaleVO> list = dao.getAll();
		for (ProdSaleVO aProdSale : list) {
			System.out.print("SaleID = " + aProdSale.getSaleID() + ",");
			System.out.print("ProdID = " +aProdSale.getProdID() + ",");
			System.out.print("Price = " +aProdSale.getSalePrice() + ".");
			System.out.println();
		}
	}

}
