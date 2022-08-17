package com.cga102g3.web.faq.controller;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.cga102g3.web.faq.model.*;

@WebServlet("/back-end/faq/faq.do")

public class FaqServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action"); //後臺員工

		
		
		
		if ("getOne_For_Display".equals(action)) { // 來自select_page.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String str = req.getParameter("FAQ_ID");
				Integer faqID = null;
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入FAQ編號");
				}else {
					try {
						faqID = Integer.valueOf(str);
					} catch (Exception e) {
						errorMsgs.add("FAQ編號格式不正確");
					}
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back-end/faq/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************2.開始查詢資料*****************************************/
				FaqService faqSvc = new FaqService();
				FaqVO faqVO = faqSvc.getOneFaq(faqID);
				if (faqVO == null) {
					errorMsgs.add("查無資料，請輸入編號");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back-end/faq/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("faqVO", faqVO); // 資料庫取出的faqVO物件,存入req
				String url = "/back-end/faq/listOneFaq.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneFaq.jsp
				successView.forward(req, res);
		}
		
		
		if ("getOne_For_Update".equals(action)) { // 來自listAllFaq.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			
				/***************************1.接收請求參數****************************************/
				Integer FAQ_ID = Integer.valueOf(req.getParameter("FAQ_ID"));
				
				/***************************2.開始查詢資料****************************************/
				FaqService faqSvc = new FaqService();
				FaqVO faqVO = faqSvc.getOneFaq(FAQ_ID);
								
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("faqVO", faqVO);         // 資料庫取出的faqVO物件,存入req
				String url = "/back-end/faq/update_faq_input.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_faq_input.jsp
				successView.forward(req, res);
		}
		
		if ("getOne_For_Update2".equals(action)) { // 來自listAllFaq.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			
				/***************************1.接收請求參數****************************************/
				Integer FAQ_ID = Integer.valueOf(req.getParameter("FAQ_ID"));
				
				/***************************2.開始查詢資料****************************************/
				FaqService faqSvc = new FaqService();
				FaqVO faqVO = faqSvc.getOneFaq(FAQ_ID);
								
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("faqVO", faqVO);         // 資料庫取出的faqVO物件,存入req
				String url = "/front-end/faq/update_faq_input.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_faq_input.jsp
				successView.forward(req, res);
		}
		
		if ("update".equals(action)) { // 來自update_faq_input.jsp的請求
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
		
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
Integer FAQ_ID = Integer.valueOf(req.getParameter("FAQ_ID").trim());
				
String ques = req.getParameter("ques");
//				String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
				if (ques == null || ques.trim().length() == 0) {
					errorMsgs.add("常見問題 請勿空白");
//				} else if(!ques.trim().matches(enameReg)) { //以下練習正則(規)表示式(regular-expression)
//					errorMsgs.add("FAQ姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間");
	            }
				
String ans = req.getParameter("ans").trim();
				if (ans == null || ans.trim().length() == 0) {
					errorMsgs.add("解答 請勿空白");
				}	
				
				FaqVO faqVO = new FaqVO();
				faqVO.setQues(ques);
				faqVO.setAns(ans);

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
req.setAttribute("faqVO", faqVO); // 含有輸入格式錯誤的faqVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back-end/faq/update_faq_input.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				FaqService faqSvc = new FaqService();
				faqVO = faqSvc.updateFaq(ques, ans, FAQ_ID);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("faqVO", faqVO); // 資料庫update成功後,正確的的faqVO物件,存入req
				String url = "/back-end/faq/listOneFaq.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneFaq.jsp
				successView.forward(req, res);
		}
		
if ("update2".equals(action)) { // 來自update_faq_input.jsp的請求
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
		
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
Integer FAQ_ID = Integer.valueOf(req.getParameter("FAQ_ID").trim());
				
String ques = req.getParameter("ques");
				String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
				if (ques == null || ques.trim().length() == 0) {
					errorMsgs.add("常見問題 請勿空白");
				} else if(!ques.trim().matches(enameReg)) { //以下練習正則(規)表示式(regular-expression)
					errorMsgs.add("FAQ姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間");
	            }
				
String ans = req.getParameter("ans").trim();
				if (ans == null || ans.trim().length() == 0) {
					errorMsgs.add("解答 請勿空白");
				}	
				
				FaqVO faqVO = new FaqVO();
				faqVO.setQues(ques);
				faqVO.setAns(ans);

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
req.setAttribute("faqVO", faqVO); // 含有輸入格式錯誤的faqVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front-end/faq/update_faq_input.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				FaqService faqSvc = new FaqService();
				faqVO = faqSvc.updateFaq(ques, ans, FAQ_ID);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("faqVO", faqVO); // 資料庫update成功後,正確的的faqVO物件,存入req
				String url = "/front-end/faq/listOneFaq.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneFaq.jsp
				successView.forward(req, res);
		}
		

        if ("insert".equals(action)) { // 來自addFaq.jsp的請求  
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
String ques = req.getParameter("ques");
			//	String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
				if (ques == null || ques.trim().length() == 0) {
					errorMsgs.add("常見問題 請勿空白");
			//	} else if(!ques.trim().matches(enameReg)) { //以下練習正則(規)表示式(regular-expression)
			//		errorMsgs.add("FAQ姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間");
	            }
				
String ans = req.getParameter("ans").trim();
				if (ans == null || ans.trim().length() == 0) {
					errorMsgs.add("解答 請勿空白");
				}
				
				FaqVO faqVO = new FaqVO();
				faqVO.setQues(ques);
				faqVO.setAns(ans);			

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
req.setAttribute("faqVO", faqVO); // 含有輸入格式錯誤的faqVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back-end/faq/addFaq.jsp");
					failureView.forward(req, res);
					return;
				}
				
				/***************************2.開始新增資料***************************************/
				FaqService faqSvc = new FaqService();
				faqVO = faqSvc.addFaq(ques, ans);
				
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				String url = "/back-end/faq/listAllFaq.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
				successView.forward(req, res);				
		}
		

if ("insert2".equals(action)) { // 來自addFaq.jsp的請求  
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
String ques = req.getParameter("ques");
			//	String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
				if (ques == null || ques.trim().length() == 0) {
					errorMsgs.add("常見問題 請勿空白");
			//	} else if(!ques.trim().matches(enameReg)) { //以下練習正則(規)表示式(regular-expression)
			//		errorMsgs.add("FAQ姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間");
	            }
				
String ans = req.getParameter("ans").trim();
				if (ans == null || ans.trim().length() == 0) {
					errorMsgs.add("解答 請勿空白");
				}
				
				FaqVO faqVO = new FaqVO();
				faqVO.setQues(ques);
				faqVO.setAns(ans);			

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
req.setAttribute("faqVO", faqVO); // 含有輸入格式錯誤的faqVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front-end/faq/addFaq.jsp");
					failureView.forward(req, res);
					return;
				}
				
				/***************************2.開始新增資料***************************************/
				FaqService faqSvc = new FaqService();
				faqVO = faqSvc.addFaq(ques, ans);
				
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				String url = "/front-end/faq/listAllFaq.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
				successView.forward(req, res);				
		}
        
		if ("delete".equals(action)) { // 來自listAllFaq.jsp

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
	
				/***************************1.接收請求參數***************************************/
				Integer FAQ_ID = Integer.valueOf(req.getParameter("FAQ_ID"));
				
				/***************************2.開始刪除資料***************************************/
				FaqService faqSvc = new FaqService();
				faqSvc.deleteFaq(FAQ_ID);
				
				/***************************3.刪除完成,準備轉交(Send the Success view)***********/								
				String url = "/back-end/faq/listAllFaq.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);
		}
	}
}
