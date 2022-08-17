package com.cga102g3.web.prod.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cga102g3.web.prod.entity.ProdVO;
import com.cga102g3.web.prod.service.ProdService;

@MultipartConfig(fileSizeThreshold = 1, maxFileSize = 15 * 1024 * 1024, maxRequestSize = 50 * 1024 * 1024)
@WebServlet("/back-end/prod/prod.do")

public class BackProdServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doPost(req, res);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("insert".equals(action)) { // 來自back_prod_add.jsp的請求

            Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
            req.setAttribute("errorMsgs", errorMsgs);
            ProdService prodSvc = new ProdService();
            /***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
            Integer bookID = null;
            try {
                bookID = Integer.valueOf(req.getParameter("bookID"));
                if (prodSvc.getOne(bookID) != null) {
                    errorMsgs.put("bookID", "此書目編號已存在");
                }
            } catch (NumberFormatException e) {
                errorMsgs.put("bookID", "編號請勿空白");
            }

            Integer price = null;
            try {
                price = Integer.valueOf(req.getParameter("price"));
            } catch (NumberFormatException e) {
                errorMsgs.put("price", "定價請勿空白");
            }

            Integer status = Integer.valueOf(req.getParameter("status"));

            if (!errorMsgs.isEmpty()) {
                RequestDispatcher failureView = req.
                        getRequestDispatcher("/back-end/prod/back_prod_add.jsp");
                failureView.forward(req, res);
                return;
            }
            /***************************2.開始新增資料***************************************/
            prodSvc.addProd(bookID, price, status);
            /***************************3.新增完成,準備轉交(Send the Success view)***********/
            String url = "/back-end/prod/back_prod_listAll.jsp";
            RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交back_prod_lisAll.jsp
            successView.forward(req, res);
        }


        if ("getOne_For_Update".equals(action)) { // 來自back_prod_listAll.jsp的請求

            Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
            req.setAttribute("errorMsgs", errorMsgs);

            /***************************1.接收請求參數****************************************/
            Integer prodID = Integer.valueOf(req.getParameter("prodID"));

            /***************************2.開始查詢資料****************************************/
            ProdService prodSvc = new ProdService();
            ProdVO prodVO = prodSvc.getOne(prodID);

            /***************************3.查詢完成,準備轉交(Send the Success view)************/
            String param =
                    "?prodID=" + prodVO.getProdID() +
                            "&bookID=" + prodVO.getBookID() +
                            "&price=" + prodVO.getPrice() +
                            "&status=" + prodVO.getStatus();
            String url = "/back-end/prod/back_prod_update.jsp" + param;
            RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 back_prod_update.jsp
            successView.forward(req, res);
        }


        if ("update".equals(action)) { // 來自back_prod_update.jsp的請求

            Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
            req.setAttribute("errorMsgs", errorMsgs);

            /***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
            Integer price = null;
            try {
                price = Integer.valueOf(req.getParameter("price"));
            } catch (NumberFormatException e) {
                errorMsgs.put("price", "定價請勿空白");
            }

            Integer prodID = Integer.valueOf(req.getParameter("prodID"));
            Integer status = Integer.valueOf(req.getParameter("status"));

            if (!errorMsgs.isEmpty()) {
                RequestDispatcher failureView = req.
                        getRequestDispatcher("/back-end/prod/back_prod_update.jsp");
                failureView.forward(req, res);
                return;
            }
            /***************************2.開始新增資料***************************************/
            ProdService prodSvc = new ProdService();
            prodSvc.updateProd(price, status, prodID);

            /***************************3.新增完成,準備轉交(Send the Success view)***********/
            String url = "/back-end/prod/back_prod_listAll.jsp";
            RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交back_prod_lisAll.jsp
            successView.forward(req, res);
        }


        if ("search".equals(action)) { // 來自back_prod_listAll.jsp的請求
            Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
            req.setAttribute("errorMsgs", errorMsgs);

            /***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
            String title = null;
            try {
                title = String.valueOf(req.getParameter("keyword_input"));
            } catch (NumberFormatException e) {
                errorMsgs.put("title", "你搜尋的內容是空白的喔");
            }

            if (!errorMsgs.isEmpty()) {
                RequestDispatcher failureView = req.
                        getRequestDispatcher("/back-end/prod/back_prod_listAll.jsp");
                failureView.forward(req, res);
                return;
            }
            /***************************2.開始新增資料***************************************/
            ProdService prodSvc = new ProdService();
            List<ProdVO> titleList = prodSvc.searchTITLE(title);
//					System.out.println("titleList="+titleList);
            req.setAttribute("titleList", titleList);
            /***************************3.新增完成,準備轉交(Send the Success view)***********/
            String url = "/back-end/prod/back_prod_listAll2.jsp";
            RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交back_prod_lisAll.jsp
            successView.forward(req, res);
        }
    }
}

