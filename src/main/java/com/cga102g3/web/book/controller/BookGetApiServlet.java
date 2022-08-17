/**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-06-15 下午 01:43
 */
package com.cga102g3.web.book.controller;


import static com.cga102g3.core.util.CommonUtil.writePojo2Json;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cga102g3.core.controller.BaseGetAPIServlet;
import com.cga102g3.core.pojo.ErrMsg;
import com.cga102g3.web.book.service.BookService;
import com.cga102g3.web.book.service.impl.BookServiceImpl;
import com.cga102g3.web.category.service.CategoryService;
import com.cga102g3.web.category.service.impl.CategoryServiceImpl;


@WebServlet(name = "BookApiServlet", urlPatterns = {"/book/api/*"})
public class BookGetApiServlet extends BaseGetAPIServlet {
    private final BookService service = new BookServiceImpl();
    private final CategoryService categoryService = new CategoryServiceImpl();

    /**
     * 路由: /book/api/getEditionsByISBN?ISBN={String}
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getEditionsByISBN(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ISBN = request.getParameter("ISBN");
//        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
//        PrintWriter out = response.getWriter();
        if (ISBN == null) {
            ErrMsg errMsg = new ErrMsg();
            errMsg.setMsg("未傳輸入isbn參數");
            writePojo2Json(response, errMsg);
        }

        writePojo2Json(response, service.findEditionsByISBN(ISBN));
    }

    /**
     * /book/api/getCategories
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getCategories(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        writePojo2Json(response, categoryService.getAll());
    }

    /**
     * /book/api/getEditionsByISBNExclID?ISBN={String}&bookID={int}
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getEditionsByISBNExclID(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ISBN = request.getParameter("ISBN");
        String strBookId = request.getParameter("bookID");
        Integer bookID = null;
        if (strBookId != null) {
            bookID = Integer.valueOf(strBookId);
        }

//        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
//        PrintWriter out = response.getWriter();
        if (ISBN == null || bookID == null) {
            ErrMsg errMsg = new ErrMsg();
            errMsg.setMsg("未傳輸入isbn參數");
            writePojo2Json(response, errMsg);
        }

        writePojo2Json(response, service.findEditionsByISBNExclID(ISBN, bookID));
    }

    /**
     * /book/api/getBooksByKeyword?keyword={isbn, title}&page={int}
     * 書籍關鍵字查詢，若沒有輸入關鍵字，則返回書籍書目排序結果
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void getBooksByKeyword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        keyword = keyword.trim();
        int page = Integer.parseInt(request.getParameter("page"));
//        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        writePojo2Json(response, service.getBooksByKeyword(keyword, page));
    }
}
