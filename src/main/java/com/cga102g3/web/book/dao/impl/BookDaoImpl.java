package com.cga102g3.web.book.dao.impl;


import com.cga102g3.core.util.JDBCUtil;
import com.cga102g3.web.book.dao.BookDao;
import com.cga102g3.web.book.entity.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 書籍書目資料存儲物件
 * @Author Robert
 * @Version
 * @Date 2022-06-05 下午 04:41
 */
public class BookDaoImpl implements BookDao {


    @Override
    public int insert(Book book) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
       // int rowCount = 0;  // 引響的資料筆數
        int book_ID = -1;

        final String sql =
                "insert into book(ISBN, edition, category_name, title, author,\n" +
                "                 translator, publisher, pubdate, pages, summary,\n" +
                "                 table_content)\n" +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        final String[] columns = {"book_ID"};

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql, columns);
            setBookInfo(book, pstmt);
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                book_ID = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return book_ID;
    }

    /**
     * 將pojo屬性設置給佔位符
     * @param book
     * @param pstmt
     * @throws SQLException
     */
    private void setBookInfo(Book book, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, book.getISBN());
        pstmt.setInt(2, book.getEdition());
        pstmt.setString(3, book.getCategoryName());
        pstmt.setString(4, book.getTitle());
        pstmt.setString(5, book.getAuthor());
        pstmt.setString(6, book.getTranslator());
        pstmt.setString(7, book.getPublisher());
        pstmt.setDate(8, book.getPubdate());
        pstmt.setInt(9, book.getPages() == null ? 0 : book.getPages());
        pstmt.setString(10, book.getSummary());
        pstmt.setString(11, book.getTableContent());

    }

    /**
     * 未實現
     * @param id
     * @return
     */
    @Override
    public int deleteById(Integer id) {
        return 0;
    }

    @Override
    public int update(Book book) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int res = 0;

        final String sql =
                "update book set ISBN = ?, edition = ?, category_name = ?, title = ?, author = ?,\n" +
                "                translator = ?, publisher = ?, pubdate = ?, pages = ?, summary = ?,\n" +
                "                table_content = ?\n" +
                "where book_ID = ?;";
        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            setBookInfo(book, pstmt);
            pstmt.setInt(12, book.getBookID());
            res = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return res;

    }

    @Override
    public Book selectByPrimaryKey(Integer id) {
        Book book = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        final String GET_ONE_BY_ID_STMT =
                "select book_ID, ISBN, edition, category_name,\n" +
                "       title, author, translator, publisher,\n" +
                "       pubdate, pages, summary, table_content\n" +
                "from book\n" +
                "where book_ID = ?;";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(GET_ONE_BY_ID_STMT);

            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            List<Book> bookList = retrieve(rs);
            if (bookList.size() != 0) {
                book = bookList.get(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return book;
    }

    /**
     *
     * @return
     */
    @Override
    public List<Book> selectAll() {
        final String GET_ALL_STMT =
                "select book_ID, ISBN, edition, category_name, title,\n" +
                        "       author, translator, publisher, pubdate, pages,\n" +
                        "       summary, table_content\n" +
                        "from book;";

        List<Book> bookList = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(GET_ALL_STMT);
            rs = pstmt.executeQuery();
            bookList = retrieve(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }

        return bookList;
    }

    /**
     * 查詢全部書籍書目，每頁12筆資料
     * @param page
     * @return
     */
    @Override
    public List<Book> selectAll(Integer page) {
        // service再判斷是否有下一頁
        final String GET_ALL_STMT =
                "select book_ID, ISBN, edition, category_name, title,\n" +
                "       author, translator, publisher, pubdate, pages,\n" +
                "       summary, table_content\n" +
                "from book \n" +
                "order by book_ID desc\n" +
                "limit ? , ?;";

        List<Book> bookList = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(GET_ALL_STMT);
            pstmt.setInt(1, pageSize * (page - 1));
            pstmt.setInt(2, pageSize + 1);

            rs = pstmt.executeQuery();

            bookList = retrieve(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }

        return bookList;
    }

    /**
     * 依isbn查詢書籍書目，每頁12筆資料
     * @param ISBN
     * @param page
     * @return
     */
    @Override
    public List<Book> selectByISBN(String ISBN, int page) {
        List<Book> bookList = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final String GET_BOOKS_BY_NAME =
                "select book_ID, ISBN, edition, category_name, title,\n" +
                "       author, translator, publisher, pubdate, pages,\n" +
                "       summary, table_content\n" +
                "from book\n" +
                "where ISBN = ?\n" +
                "limit ?, ?;";
        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(GET_BOOKS_BY_NAME);

            pstmt.setString(1, ISBN);
            pstmt.setInt(2, pageSize * (page - 1));
            pstmt.setInt(3, pageSize + 1);

            rs = pstmt.executeQuery();
            bookList = retrieve(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }

        return bookList;
    }

    @Override
    public List<Book> selectByISBN(String ISBN) {
        List<Book> bookList = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final String GET_BOOKS_BY_NAME =
                "select book_ID, ISBN, edition, category_name, title,\n" +
                "       author, translator, publisher, pubdate, pages,\n" +
                "       summary, table_content\n" +
                "from book\n" +
                "where ISBN = ?;";
        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(GET_BOOKS_BY_NAME);

            pstmt.setString(1, ISBN);

            rs = pstmt.executeQuery();
            bookList = retrieve(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }

        return bookList;
    }

    /**
     * 依書籍名稱查詢書籍書目，每頁12筆資料
     * @param title
     * @param page
     * @return
     */
    @Override
    public List<Book> selectByTitle(String title, int page) {
        List<Book> bookList = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final String GET_BOOKS_BY_NAME =
                "select book_ID, ISBN, edition, category_name, title,\n" +
                "       author, translator, publisher, pubdate, pages,\n" +
                "       summary, table_content\n" +
                "from book\n" +
                "where lower(title)  like concat('%', lower(?), '%')\n" +
                "limit ?, ?;";
        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(GET_BOOKS_BY_NAME);

            pstmt.setString(1, title);
            pstmt.setInt(2, pageSize * (page - 1));
            pstmt.setInt(3, pageSize + 1);

            rs = pstmt.executeQuery();
            bookList = retrieve(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }

        return bookList;

    }

    /**
     * 依書目分類查詢書籍書目，每頁12筆資料
     * @param categoryName
     * @param page
     * @return
     */
    @Override
    public List<Book> selectByCategoryName(String categoryName, int page) {
        List<Book> bookList = null;
        Connection conn = null;
        PreparedStatement pstmt = null;

        final String GET_BOOKS_BY_CATEGORY_NAME =
                "select book_ID, ISBN, edition, category_name, title,\n" +
                "       author, translator, publisher, pubdate, pages,\n" +
                "       summary, table_content\n" +
                "from book\n" +
                "where lower(category_name) regexp concat(lower(?), '')\n" +
                "limit ?, ?;";

        ResultSet rs = null;

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(GET_BOOKS_BY_CATEGORY_NAME);

            pstmt.setString(1, categoryName);
            pstmt.setInt(2, pageSize * (page - 1));
            pstmt.setInt(3, pageSize + 1);

            rs = pstmt.executeQuery();
            bookList = retrieve(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return bookList;
    }

    /**
     * 依 isbn 和 版次 查詢特定書籍書目
     * @param ISBN
     * @param edition
     * @return
     */
    @Override
    public Book selectByISBNAndEdition(String ISBN, Integer edition) {
        Book book = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        final String GET_ONE_BY_ID_STMT =
                "select book_ID, ISBN, edition, category_name,\n" +
                "       title, author, translator, publisher,\n" +
                "       pubdate, pages, summary, table_content\n" +
                "from book\n" +
                "where ISBN = ? AND edition = ?;";

        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(GET_ONE_BY_ID_STMT);

            pstmt.setString(1, ISBN);
            pstmt.setInt(2, edition);

            rs = pstmt.executeQuery();

            List<Book> bookList = retrieve(rs);
            if (bookList.size() > 0) {
                book = bookList.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }
        return book;
    }

    @Override
    public List<Book> selectByISBNExclID(String ISBN, Integer bookID) {
        List<Book> bookList = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final String sql =
                "select book_ID, ISBN, edition, category_name, title,\n" +
                "       author, translator, publisher, pubdate, pages,\n" +
                "       summary, table_content\n" +
                "from book\n" +
                "where ISBN = ? AND book_ID != ?;";
        try {
            conn = JDBCUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, ISBN);
            pstmt.setInt(2, bookID);

            rs = pstmt.executeQuery();
            bookList = retrieve(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(conn, pstmt, rs);
        }

        return bookList;
    }


    /**
     * 將查詢結果放入list中，並返回
     * @param rs
     * @return
     * @throws SQLException
     */
    private List<Book> retrieve(ResultSet rs) throws SQLException {
        List<Book> bookList = new ArrayList<>();
        while (rs.next()) {
            Book book = new Book();
            book.setBookID(rs.getInt("book_ID"));
            book.setISBN(rs.getString("ISBN"));
            book.setEdition(rs.getInt("edition"));
            book.setCategoryName(rs.getString("category_name"));
            book.setTitle(rs.getString("title"));
            book.setAuthor(rs.getString("author"));
            book.setTranslator(rs.getString("translator"));
            book.setPublisher(rs.getString("publisher"));
            book.setPubdate(rs.getDate("pubdate"));
            book.setPages(rs.getInt("pages"));
            book.setSummary(rs.getString("summary"));
            book.setTableContent(rs.getString("table_content"));
            bookList.add(book);
        }
        return bookList;
    }

    public static void main(String[] args) {
        // 依主鍵查詢
//        BookDao bookDao = new BookDaoImpl();
//        Book book = bookDao.selectByPrimaryKey(1);
//        System.out.println(book);

        // 查詢全部書籍，每頁12筆
//        BookDao bookDao = new BookDaoImpl();
//        List<Book> books = bookDao.selectAll(2);
//        for (Book book :
//                books) {
//            System.out.println(book.getBookID());
//        }

        // isbn查詢書籍，每頁12筆
//        BookDao bookDao = new BookDaoImpl();
//        List<Book> books = bookDao.selectByCategoryName("java", 1);
//        for (Book book :
//                books) {
//            System.out.println(book.getBookID());
//        }

        // 依書名查詢書籍，每頁12筆
//        BookDao bookDao = new BookDaoImpl();
//        List<Book> books = bookDao.selectByTitle("深入淺出", 1);
//        for (Book book :
//                books) {
//            System.out.println(book.getTitle());
//        }

//        BookDao bookDao = new BookDaoImpl();
//        List<Book> books = bookDao.selectByISBN("9789865021726", 1);
//        for (Book book :
//                books) {
//            System.out.println(book.getTitle());
//        }

        // 測試添加功能
//        Book book = new Book();
//        book.setISBN("9789864768592");
//        book.setEdition(1);
//        book.setCategoryName("Kotlin");
//        book.setTitle("Kotlin 實戰手冊");
//        book.setAuthor("Dmitry Jemerov/Svetlana Isakova");
//        book.setTranslator("蔡明志");
//        String reqDate = "2018/7/19";
//        Date pubdate = null;
//        try {
//            pubdate = (Date) new SimpleDateFormat("yyyy/MM/dd").parse(reqDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if (pubdate != null) {
//            book.setPubdate(new java.sql.Date(pubdate.getTime()));
//        }
//
//        BookDao bookDao = new BookDaoImpl();
//        int book_ID = bookDao.insert(book);
//        System.out.println(book_ID);

        // 測試修改功能
//        BookDao bookDao = new BookDaoImpl();
//        Book book = bookDao.selectByPrimaryKey(31);
//        book.setPages(334);
//        int res = bookDao.update(book);

        // 測試依isbn與edition查找書籍
//        BookDao bookDao = new BookDaoImpl();
//        Book book = bookDao.selectByISBNAndEdition("9786263330634", 1);
//        System.out.println(book.getTitle());

//        BookDao bookDao = new BookDaoImpl();
//        List<Book> books = bookDao.selectByISBN("9786263330634");
//        System.out.println(books);

//        BookDao bookDao = new BookDaoImpl();
//        List<Book> books = bookDao.selectByISBNExclID("9787121408564", 1);
//        System.out.println(books);

        BookDao bookDao = new BookDaoImpl();
        List<Book> books = bookDao.selectByTitle("c", 1);
        for (Book book :
                books) {
            System.out.println(book.getTitle());
        }

    }
}
