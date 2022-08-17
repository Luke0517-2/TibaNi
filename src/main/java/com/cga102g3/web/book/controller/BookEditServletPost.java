/**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-06-15 下午 11:16
 */
package com.cga102g3.web.book.controller;


import static com.cga102g3.core.util.CommonUtil.writePojo2Json;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.cga102g3.core.controller.PostBaseServlet;
import com.cga102g3.core.pojo.ErrMsg;
import com.cga102g3.core.util.WebUtils;
import com.cga102g3.web.book.entity.Book;
import com.cga102g3.web.book.service.BookService;
import com.cga102g3.web.book.service.impl.BookServiceImpl;
import com.cga102g3.web.book.util.BookEditParamUtil;


@WebServlet(name = "BookInsertServlet", value = "/book/edit")
@MultipartConfig(fileSizeThreshold = 1,
        maxFileSize = 15*1024*1024, maxRequestSize = 50*1024*1024)
public class BookEditServletPost extends PostBaseServlet {
    private static final String saveDir = "/static/images/books";
    private static String destDirPath;
    private final BookService service = new BookServiceImpl();

    /**
     * 路由: /book/edit?action=insert&bookID={int}&...
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void insert(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
//        String destDirPath = getServletContext().getRealPath(saveDir);
        getDestDirPath();
        // 開發環境下文件位置 realPath=C:\CGA102_WebApp\apache-tomcat-9.0.56\webapps\bookmall\images\books
        // 包裝數據 和 驗證數據
        Map<String, String[]> parameterMap = BookEditParamUtil.handleParams(request.getParameterMap());

        Book book = WebUtils.copyParams2Bean(parameterMap, new Book());
//        System.out.println(book);
        // 請求參數驗證方法
        Set<String> errMsgs = BookEditParamUtil.chkInsertBookParams(book);
        if (!errMsgs.isEmpty()) {
            writePojo2Json(response, errMsgs);
            return;
        }

        // 獲取上傳檔案流
        Part part = request.getPart("uploadImg");

        boolean res= service.addBook(book, part, destDirPath);
        PrintWriter out = response.getWriter();
        if (res) {
            // 跳轉去查全部頁面
//            response.sendRedirect(request.getContextPath() + "/back-end/book/back_book_view.jsp");
            ErrMsg errMsg = new ErrMsg();
            errMsg.setErr(false);
            writePojo2Json(response, errMsg);
        } else {
            // 正常操作不會到這
            out.println("fail");
        }

    }

    /**
     * 路由: /book/edit?action=update&bookID={int}&...
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void update(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        // 為destDirPath賦值
        getDestDirPath();
        // 包裝數據 和 驗證數據
        Map<String, String[]> parameterMap = BookEditParamUtil.handleParams(request.getParameterMap());

        Book book = WebUtils.copyParams2Bean(parameterMap, new Book());

        // 請求參數驗證
        Set<String> errMsgs = BookEditParamUtil.chkUpdateBookParams(book);
        if (!errMsgs.isEmpty()) {
            writePojo2Json(response, errMsgs);
            return;
        }

        // 獲取上傳檔案流
        Part part = request.getPart("uploadImg");
        // 處理書籍更新業務邏輯
        boolean res= service.updateBook(book, part, destDirPath);
        PrintWriter out = response.getWriter();
        if (res) {
            // 跳轉去查全部頁面
//            response.sendRedirect(request.getContextPath() + "/back-end/book/back_book_view.jsp");
            ErrMsg errMsg = new ErrMsg();
            errMsg.setErr(false);
            writePojo2Json(response, errMsg);
        } else {
            // 正常操作不會到這
            out.println("fail");
        }

    }



    /**
     * Singleton設計模式
     * destDirPath 只有一份
     * @return
     */
    private  void getDestDirPath(){
        if(destDirPath == null){
            synchronized(BookEditServletPost.class){
                if(destDirPath == null){
                    destDirPath = getServletContext().getRealPath(saveDir);
                }
            }
        }
    }

}
