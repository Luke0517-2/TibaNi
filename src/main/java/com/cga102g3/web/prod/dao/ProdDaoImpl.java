package com.cga102g3.web.prod.dao;

import com.cga102g3.core.util.JDBCUtil;
import com.cga102g3.web.book.entity.Book;
import com.cga102g3.web.prod.entity.CarObj;
import com.cga102g3.web.prod.entity.ProdVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: TODO
 * @author: Luke
 * @date: 2022/6/22
 **/
public class ProdDaoImpl implements ProdDao{
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/bookstore?serverTimezone=Asia/Taipei";
    String userid = "root";
    String passwd = "abc123";
    
	private static final String INSERT_STMT = 
			"INSERT INTO product (book_ID,price,status) VALUES (?, ?, ?)";
	
	/**
	 * @description: updateDAO
	 * @author: Alan
	 * @date: 2022/6/27
	 **/
	private static final String UPDATE = 
			"UPDATE product set price=?, status=? where prod_ID = ?";

    private static final String GET_ONE_STRING =
            "select p.status,p.prod_id,p.book_id,b.isbn,b.category_name,b.title,b.author,b.publisher,b.pubdate,b.pages,b.summary,b.table_content,p.price\n"
                    + "from book b\n"
                    + "join product p\n"
                    + "on b.book_id = p.book_id\n"
                    + "where p.prod_id = ?;";

    private static final String GET_ALL_STRING =
           "select prod_id,p.book_id,price,title,status \n" +
                   "from product p\n" +
                   "join book b\n" +
                   "on p.book_ID = b.book_ID";

    private static final String FRONT_SALE_STRING =
            "select b.book_ID, p.prod_ID, p.price AS price1, b.title,\n"
                    + "    case\n"
                    + "  when NOW() between s.sale_start and s.sale_end then ps.sale_price\n"
                    + "        else p.price\n"
                    + " end as price2,\n"
                    + "    case\n"
                    + "  when NOW() between s.sale_start and s.sale_end then 'Y'\n"
                    + "        else 'N'\n"
                    + " end as discount\n"
                    + " from\n"
                    + " product p\n"
                    + " left join prod_sale ps\n"
                    + "  on p.prod_ID = ps.prod_ID\n"
                    + " left join sale s\n"
                    + "  on ps.sale_ID = s.sale_ID\n"
                    + " left join book b\n"
                    + "  on p.book_ID = b.book_ID\n"
                    + "where\n"
                    + " p.status = 1 \n"
                    + "limit 9";

    private static final String FRONT_TOP_SALE =
            "select b.book_ID, p.prod_ID, p.price AS price1, b.title, sum(oi.amount),\n"
                    + "case\n"
                    + "when NOW() between s.sale_start and s.sale_end then ps.sale_price\n"
                    + "else p.price\n"
                    + "  		end as price2,\n"
                    + "  	 case\n"
                    + "  		  when NOW() between s.sale_start and s.sale_end then 'Y'\n"
                    + "  		        else 'N'\n"
                    + "  		 end as discount\n"
                    + "  		 from\n"
                    + "  		 product p\n"
                    + "  		 left join prod_sale ps\n"
                    + "  		  on p.prod_ID = ps.prod_ID\n"
                    + "  		 left join order_item oi\n"
                    + "  		  on p.prod_ID = oi.prod_ID\n"
                    + "			left join sale s\n"
                    + "		 on ps.sale_ID = s.sale_ID\n"
                    + "  		left join book b\n"
                    + "on p.book_ID = b.book_ID\n"
                    + "where\n"
                    + " p.status = 1\n"
                    + "group by p.prod_ID,b.book_ID, s.sale_ID,price1, price2, b.title\n"
                    + "order by sum(oi.amount) desc\n"
                    + "limit 5;";

    @Override
	public void insert(ProdVO prodVO) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setInt(1, prodVO.getBookID());
			pstmt.setInt(2, prodVO.getPrice());
			pstmt.setInt(3, prodVO.getStatus());

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
	public void update(ProdVO prodVO) {

		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);

			pstmt.setInt(1, prodVO.getPrice());
			pstmt.setInt(2, prodVO.getStatus());
			pstmt.setInt(3, prodVO.getProdID());
			pstmt.executeUpdate();

			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. " + se.getMessage());
		}
	}
    
    @Override
    public ProdVO findOneProd(int prodID) {
        Connection con = null;
        PreparedStatement ps = null;
        ProdVO pb = null;

        try{
            Class.forName(driver);
            con = DriverManager.getConnection(url, userid, passwd);
            ps = con.prepareStatement(GET_ONE_STRING);

            ps.setInt(1, prodID);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
            pb = new ProdVO();
            Book book = new Book();
            book.setISBN(rs.getString("isbn"));
            book.setAuthor(rs.getString("author"));
            book.setCategoryName(rs.getString("category_name"));
            book.setTitle(rs.getString("title"));
            book.setPublisher(rs.getString("publisher"));
            book.setPubdate(rs.getDate("pubdate"));
            book.setPages(rs.getInt("pages"));
            book.setSummary(rs.getString("summary"));
            book.setTableContent(rs.getString("table_content"));
            pb.setBook(book);
            pb.setPrice(rs.getInt("price"));
            pb.setBookID(rs.getInt("book_id"));
            pb.setProdID(rs.getInt("prod_id"));
            pb.setStatus(rs.getInt("status"));
            }
            return pb;

        }catch(ClassNotFoundException e) {
            throw new RuntimeException("A database error occured. " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't load database driver. "+ e.getMessage());
        }
    }

    @Override
    public List<Map<String,Object>> findAll() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
//        final String GET_ALL_STRING =
//            "select prod_id,title,price\n" +
//                    "from book b\n" +
//                    "join product p\n" +
//                    "on b.book_ID = p.book_ID\n" +
//                    "where p.status = 1";

        final String GET_All_STRING =
                "select\r\n"
                        + " p.prod_ID, p.price AS price1,b.title,\r\n"
                        + "    case\r\n"
                        + "  when NOW() between s.sale_start and s.sale_end then ps.sale_price\r\n"
                        + "        else p.price\r\n"
                        + " end as price2,\r\n"
                        + "    case\r\n"
                        + "  when NOW() between s.sale_start and s.sale_end then 'Y'\r\n"
                        + "        else 'N'\r\n"
                        + " end as discount\r\n"
                        + "from\r\n"
                        + " product p\r\n"
                        + "    left join prod_sale ps\r\n"
                        + "  on p.prod_ID = ps.prod_ID\r\n"
                        + " left join sale s\r\n"
                        + "  on ps.sale_ID = s.sale_ID\r\n"
                        + " left join book b\r\n"
                        + "  on p.book_ID = b.book_ID\r\n"
                        + "where\r\n"
                        + " p.status = 1";


        try{
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(GET_All_STRING);
            rs = ps.executeQuery();

            while(rs.next()) {
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("prodID", rs.getInt("prod_ID"));
//                map.put("price", rs.getInt("price"));
//                map.put("title", rs.getString("title"));

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("prodID", rs.getInt("prod_ID"));
                map.put("title", rs.getString("title"));
                map.put("price1", rs.getInt("price1"));
                map.put("price2", rs.getInt("price2"));
                map.put("discount", rs.getString("discount"));
                list.add(map);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't load database driver. "+ e.getMessage());
        }finally {
            JDBCUtil.close(con,ps,rs);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> findCategory(int categoryID) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

        final String GET_CATEGORY =
                "SELECT\r\n"
                        + " p.prod_ID, p.price AS price1,b.title,\r\n"
                        + "    CASE\r\n"
                        + "  WHEN NOW() BETWEEN s.sale_start AND s.sale_end THEN ps.sale_price\r\n"
                        + "        ELSE p.price\r\n"
                        + " END AS price2,\r\n"
                        + "    CASE\r\n"
                        + "  WHEN NOW() BETWEEN s.sale_start AND s.sale_end THEN 'Y'\r\n"
                        + "        ELSE 'N'\r\n"
                        + " END AS discount\r\n"
                        + "FROM\r\n"
                        + " product p\r\n"
                        + " LEFT JOIN prod_sale ps\r\n"
                        + "  ON p.prod_ID = ps.prod_ID\r\n"
                        + " LEFT JOIN sale s\r\n"
                        + "  ON ps.sale_ID = s.sale_ID\r\n"
                        + " LEFT JOIN book b\r\n"
                        + "  ON p.book_ID = b.book_ID\r\n"
                        + " LEFT JOIN category c\r\n"
                        + "  ON c.category_name = b.category_name \r\n"
                        + "WHERE\r\n"
                        + " p.status = 1 AND c.category_ID = ?";

        try{
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(GET_CATEGORY);
            ps.setInt(1, categoryID);
            rs = ps.executeQuery();

            while(rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("prodID", rs.getInt("prod_ID"));
                map.put("title", rs.getString("title"));
                map.put("price1", rs.getInt("price1"));
                map.put("price2", rs.getInt("price2"));
                map.put("discount", rs.getString("discount"));
                list.add(map);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't load database driver. "+ e.getMessage());
        }finally {
            JDBCUtil.close(con,ps,rs);
        }
        return list;
    }

    /**
     * @description:查詢符合優惠方案種類的prodID&price
     * @param: [category]
     * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Integer>>
     * @auther: Luke
     * @date: 2022/06/29 10:38:08
     */
    @Override
    public List<Map<String, Integer>> useCategory(String category) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String,Integer>> list = new ArrayList<>();
        final String sql = "select prod_ID,price\n" +
                "from book b\n" +
                "join product p\n" +
                "on b.book_ID = p.book_ID\n" +
                "where category_name = ?;";
        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1,category);
            rs = ps.executeQuery();
            while (rs.next()){
                Map<String,Integer> map = new HashMap<>();
                map.put("prodID",rs.getInt("prod_ID"));
                map.put("price",rs.getInt("price"));
                list.add(map);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtil.close(con,ps,rs);
        }
        return list;
    }

    /** Find products by price range **/
    @Override
    public List<Map<String, Object>> findPrice(int price) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        final String GET_PRICE =
                "select\r\n"
                        + " p.prod_ID, p.price AS price1, b.title,\r\n"
                        + "    case\r\n"
                        + "  when NOW() between s.sale_start and s.sale_end then ps.sale_price\r\n"
                        + "        else p.price\r\n"
                        + " end as price2,\r\n"
                        + "    case\r\n"
                        + "  when NOW() between s.sale_start and s.sale_end then 'Y'\r\n"
                        + "        else 'N'\r\n"
                        + " end as discount\r\n"
                        + "from\r\n"
                        + " product p\r\n"
                        + " left join prod_sale ps\r\n"
                        + "  on p.prod_ID = ps.prod_ID\r\n"
                        + " left join sale s\r\n"
                        + "  on ps.sale_ID = s.sale_ID\r\n"
                        + " left join book b\r\n"
                        + "  on p.book_ID = b.book_ID\r\n"
                        + "where\r\n"
                        + " p.status = 1 and p.price < ?";

        try{
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(GET_PRICE);
            ps.setInt(1, price);
            rs = ps.executeQuery();

            while(rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("prodID", rs.getInt("prod_ID"));
                map.put("title", rs.getString("title"));
                map.put("price1", rs.getInt("price1"));
                map.put("price2", rs.getInt("price2"));
                map.put("discount", rs.getString("discount"));
                list.add(map);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't load database driver. "+ e.getMessage());
        } finally {
            JDBCUtil.close(con,ps,rs);
        }
        return list;
    }


    private List<Map<String, Object>> retrieve2(ResultSet rs) throws SQLException {

        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("bookID", rs.getInt("book_ID"));
            map.put("prodID", rs.getInt("prod_ID"));
            map.put("price1", rs.getInt("price1"));
            map.put("title", rs.getString("title"));
            map.put("price2", rs.getInt("price2"));
            map.put("discount", rs.getString("discount"));
            list.add(map);
        }
        return list;
    }

    /**
     * @description: 購物車專用
     * @param: [prodID]
     * @return: com.cga102g3.web.prod.entity.CarObj
     * @auther: Luke
     * @date: 2022/07/10 12:47:15
     */
    @Override
    public CarObj getForCar(int prodID) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        CarObj obj = null;
        final String sql = "select\n" +
                "\tp.prod_ID, p.price AS price1,b.title,\n" +
                "    case\n" +
                "\t\twhen now() between s.sale_start and s.sale_end then ps.sale_price\n" +
                "        else p.price\n" +
                "\tend as price2,\n" +
                "    case\n" +
                "\t\twhen now() between s.sale_start and s.sale_end then 'Y'\n" +
                "        else 'N'\n" +
                "\tend as discount\n" +
                "from\n" +
                "\tproduct p\n" +
                "    left join prod_sale ps\n" +
                "\t\ton p.prod_ID = ps.prod_ID\n" +
                "\tleft join sale s\n" +
                "\t\ton ps.sale_ID = s.sale_ID\n" +
                "\tleft join book b\n" +
                "\t\ton p.book_ID = b.book_ID\n" +
                "where\n" +
                "\tp.prod_ID = ?\n";
        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1,prodID);
            rs = ps.executeQuery();

            if (rs.next()){
                obj = new CarObj();
                obj.setProdID(rs.getInt("prod_ID"));
                obj.setPrice(rs.getInt("price1"));
                obj.setSalePrice(rs.getInt("price2"));
                obj.setTitle(rs.getString("title"));
                obj.setDiscount(rs.getString("discount"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtil.close(con,ps,rs);
        }
        return obj;
    }

    /**
     * @description: 商品明細頁面確認是否特價
     * @param: [prodID]
     * @return: java.lang.Integer
     * @auther: Luke
     * @date: 2022/07/10 12:47:47
     */
    @Override
    public Integer checkSale(int prodID) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer salePrice = null;
        final String sql = "select sale_price,prod_ID \n" +
                "from prod_sale ps\n" +
                "join sale s\n" +
                "on ps.sale_ID = s.sale_ID\n" +
                "where now() between s.sale_start and s.sale_end and prod_ID = ?";
        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1,prodID);
            rs = ps.executeQuery();

            if (rs.next()){
                salePrice = rs.getInt("sale_price");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return salePrice;
    }
    //Alan Start from there


    @Override
    public List<Map<String,Object>> findAllSale() {

        Connection con = null;
        PreparedStatement ps = null;
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

        try{
            Class.forName(driver);
            con = DriverManager.getConnection(url, userid, passwd);
            ps = con.prepareStatement(FRONT_SALE_STRING);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("bookID", rs.getInt("book_ID"));
                map.put("prodID", rs.getInt("prod_ID"));
                map.put("price1", rs.getInt("price1"));
                map.put("title", rs.getString("title"));
                map.put("price2", rs.getInt("price2"));
                map.put("discount", rs.getString("discount"));
                list.add(map);
            }

        }catch(ClassNotFoundException e) {
            throw new RuntimeException("A database error occured. " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't load database driver. "+ e.getMessage());
        }
        return list;
    }

    @Override
    public List<Map<String,Object>> findTopSale() {

        Connection con = null;
        PreparedStatement ps = null;
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

        try{
            Class.forName(driver);
            con = DriverManager.getConnection(url, userid, passwd);
            ps = con.prepareStatement(FRONT_TOP_SALE);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("bookID", rs.getInt("book_ID"));
                map.put("prodID", rs.getInt("prod_ID"));
                map.put("price1", rs.getInt("price1"));
                map.put("title", rs.getString("title"));
                map.put("price2", rs.getInt("price2"));
                map.put("discount", rs.getString("discount"));
                map.put("amount", rs.getString("sum(oi.amount)"));
                list.add(map);
            }

        }catch(ClassNotFoundException e) {
            throw new RuntimeException("A database error occured. " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't load database driver. "+ e.getMessage());
        }
        return list;
    }

    @Override
    public List<ProdVO> searchTitle(String title) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<ProdVO> list = null;
        final String sql =
                "select p.prod_id,p.book_id, p.price, b.title, p.`status`\n"
                        + "from product p \n"
                        + "left join book b \n"
                        + "on p.book_ID = b.book_ID \n"
                        + "where b.title like ?";
        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + title + "%");
            rs = pstmt.executeQuery();
            list = retrieve(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {JDBCUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> findAll(Integer no) {
        Connection con = null;
        PreparedStatement ps = null;
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

        try{
            Class.forName(driver);
            con = DriverManager.getConnection(url, userid, passwd);
            ps = con.prepareStatement(GET_ALL_STRING);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("prodID", rs.getInt("prod_ID"));
                map.put("bookID", rs.getInt("book_ID"));
                map.put("price", rs.getInt("price"));
                map.put("title", rs.getString("title"));
                map.put("status", rs.getInt("status"));
                list.add(map);
            }

        }catch(ClassNotFoundException e) {
            throw new RuntimeException("A database error occured. " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't load database driver. "+ e.getMessage());
        }
        return list;
    }


    //前台首頁搜尋
    public List<Map<String, Object>> selectTitle(String bookTitle) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = null;
        final String sql =
                " select b.book_ID, p.prod_ID, p.price AS price1, b.title,\n"
                        + "    case\n"
                        + "  when NOW() between s.sale_start and s.sale_end then ps.sale_price\n"
                        + "        else p.price\n"
                        + " end as price2,\n"
                        + "    case\n"
                        + "  when NOW() between s.sale_start and s.sale_end then 'Y'\n"
                        + "        else 'N'\n"
                        + " end as discount\n"
                        + " from product p\n"
                        + " left join prod_sale ps\n"
                        + "  on p.prod_ID = ps.prod_ID\n"
                        + " left join sale s\n"
                        + "  on ps.sale_ID = s.sale_ID\n"
                        + " left join book b\n"
                        + "  on p.book_ID = b.book_ID\n"
                        + "where\n"
                        + " p.status = 1 and b.title like ?";
        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + bookTitle + "%");
            rs = pstmt.executeQuery();
            list = retrieve2(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {JDBCUtil.close(conn, pstmt, rs);
        }
        return list;
    }

    private List<ProdVO> retrieve(ResultSet rs) throws SQLException {
        List<ProdVO> list = new ArrayList<>();
        while (rs.next()) {
            ProdVO prodvo = new ProdVO();
            prodvo.setProdID(rs.getInt("prod_id"));
            prodvo.setBookID(rs.getInt("book_id"));
            prodvo.setPrice(rs.getInt("price"));
            prodvo.setStatus(rs.getInt("status"));
            list.add(prodvo);
        }
        return list;
    }

    @Override
    public List<Map<String,Object>> showAll() {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();

        final String GET_All_STRING =
                "select\r\n"
                        + " p.prod_ID, p.price AS price1,b.title,\r\n"
                        + "    case\r\n"
                        + "  when NOW() between s.sale_start and s.sale_end then ps.sale_price\r\n"
                        + "        else p.price\r\n"
                        + " end as price2,\r\n"
                        + "    case\r\n"
                        + "  when NOW() between s.sale_start and s.sale_end then 'Y'\r\n"
                        + "        else 'N'\r\n"
                        + " end as discount\r\n"
                        + "from\r\n"
                        + " product p\r\n"
                        + "    left join prod_sale ps\r\n"
                        + "  on p.prod_ID = ps.prod_ID\r\n"
                        + " left join sale s\r\n"
                        + "  on ps.sale_ID = s.sale_ID\r\n"
                        + " left join book b\r\n"
                        + "  on p.book_ID = b.book_ID\r\n"
                        + "where\r\n"
                        + " p.status = 1";


        try{
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(GET_All_STRING);
            rs = ps.executeQuery();

            while(rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("prodID", rs.getInt("prod_ID"));
                map.put("title", rs.getString("title"));
                map.put("price1", rs.getInt("price1"));
                map.put("price2", rs.getInt("price2"));
                map.put("discount", rs.getString("discount"));
                list.add(map);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't load database driver. "+ e.getMessage());
        }finally {
            JDBCUtil.close(con,ps,rs);
        }
        return list;
    }
}
