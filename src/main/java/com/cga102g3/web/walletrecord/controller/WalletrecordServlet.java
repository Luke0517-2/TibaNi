package com.cga102g3.web.walletrecord.controller;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.cga102g3.web.walletrecord.model.*;
import com.cga102g3.web.mem.model.*;

@WebServlet("/front-end/walletrecord/walletrecord.do")
public class WalletrecordServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
//		String action2 = req.getParameter("action2");
		
		
		
//		if ("updateMEM".equals(action2)) { // 來自update_MEM_input.jsp的請求
			
//			List<String> errorMsgs = new LinkedList<String>();
//			// Store this set in the request scope, in case we need to
//			// send the ErrorPage view.
//			req.setAttribute("errorMsgs", errorMsgs);
//		
//				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
//Integer mbr_ID = Integer.valueOf(req.getParameter("mbr_ID").trim());
//Integer note = Integer.valueOf(req.getParameter("note"));				
////String note = req.getParameter("note");		
//
//
//				WalletrecordVO walletrecordVO = new WalletrecordVO();
//				walletrecordVO.setNote(note);
//
//
//				// Send the use back to the form, if there were errors
////				if (!errorMsgs.isEmpty()) {
////					req.setAttribute("walletrecordVO", walletrecordVO); // 含有輸入格式錯誤的walletrecordVO物件,也存入req
////					RequestDispatcher failureView = req
////							.getRequestDispatcher("/back-end/walletrecord/update_walletrecord_input.jsp");
////					failureView.forward(req, res);
////					return; //程式中斷
////				}
//				
//				/***************************2.開始修改資料*****************************************/
//				WalletrecordService walletrecordSvc = new WalletrecordService();
//				walletrecordVO = walletrecordSvc.updateWalletrecord(note, mbr_ID);
//				
//				/***************************3.修改完成,準備轉交(Send the Success view)*************/
//				req.setAttribute("walletrecordVO", walletrecordVO); // 資料庫update成功後,正確的的walletrecordVO物件,存入req
//				String url = "/front-end/walletrecord/listAllWalletrecord.jsp";
//				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listAllWalletrecord.jsp
//				successView.forward(req, res);
//		}
//		
		
		
		if ("getOne_For_Display".equals(action)) { // 來自select_page.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String str = req.getParameter("wallet_rec_no");
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入員工編號");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/walletrecord/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				Integer walletrecordID = null;
				try {
					walletrecordID = Integer.valueOf(str);
				} catch (Exception e) {
					errorMsgs.add("員工編號格式不正確");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/walletrecord/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************2.開始查詢資料*****************************************/
				WalletrecordService walletrecordSvc = new WalletrecordService();
				WalletrecordVO walletrecordVO = walletrecordSvc.getOneWalletrecord(walletrecordID);
				if (walletrecordVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/walletrecord/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("walletrecordVO", walletrecordVO); // 資料庫取出的walletrecordVO物件,存入req
				String url = "/walletrecord/listOneWalletrecord.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneWalletrecord.jsp
				successView.forward(req, res);
		}
		
		
		if ("getOne_For_Update".equals(action)) { // 來自listAllWalletrecord.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			
				/***************************1.接收請求參數****************************************/
				Integer walletRecNo = Integer.valueOf(req.getParameter("wallet_rec_no"));
				
				/***************************2.開始查詢資料****************************************/
				WalletrecordService walletrecordSvc = new WalletrecordService();
				WalletrecordVO walletrecordVO = walletrecordSvc.getOneWalletrecord(walletRecNo);
								
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("walletrecordVO", walletrecordVO);         // 資料庫取出的walletrecordVO物件,存入req
				String url = "/walletrecord/update_walletrecord_input.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_walletrecord_input.jsp
				successView.forward(req, res);
		}
		
		
		if ("update".equals(action)) { // 來自update_walletrecord_input.jsp的請求
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
		
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
Integer walletRecNo = Integer.valueOf(req.getParameter("wallet_rec_no").trim());
				
Integer note = Integer.valueOf(req.getParameter("note").trim());	
//				String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
//				if (note == 0) {
				
//					errorMsgs.add("員工姓名: 請勿空白");
//				} else if(!ques.trim().matches(enameReg)) { //以下練習正則(規)表示式(regular-expression)
//					errorMsgs.add("員工姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間");
//	            }
				
Integer amount = Integer.valueOf(req.getParameter("amount").trim());
//				if (ans == null || ans.trim().length() == 0) {
//					errorMsgs.add("職位請勿空白");
//				}	


				WalletrecordVO walletrecordVO = new WalletrecordVO();
				
				walletrecordVO.setNote(note);
				walletrecordVO.setAmount(amount);

					

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
req.setAttribute("walletrecordVO", walletrecordVO); // 含有輸入格式錯誤的walletrecordVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/walletrecord/update_walletrecord_input.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				WalletrecordService walletrecordSvc = new WalletrecordService();
				walletrecordVO = walletrecordSvc.updateWalletrecord(note, amount, walletRecNo);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("walletrecordVO", walletrecordVO); // 資料庫update成功後,正確的的walletrecordVO物件,存入req
				String url = "/walletrecord/listOneWalletrecord.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneWalletrecord.jsp
				successView.forward(req, res);
		}

        if ("insert".equals(action)) { // 來自addWalletrecord.jsp的請求  
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
			
			Integer mbrID = Integer.valueOf(req.getParameter("mbr_ID").trim());			
//			Integer mbr_ID = req.getParameter("mbr_ID").trim();
//			if (mbr_ID == null || mbr_ID.trim().length() == 0) {
//				errorMsgs.add("會員請勿空白");
//			}

			Integer note = Integer.valueOf(req.getParameter("note").trim());	
//			Integer note = req.getParameter("note");

//				if (note == null || note.trim().length() == 0) {
//					errorMsgs.add("錢包使用備註: 請勿空白");
//				} else if(note == 0) { 
//					("會員儲值+");
//	            }
			
				
			Integer amount = Integer.valueOf(req.getParameter("amount").trim());				
//				Integer amount = req.getParameter("amount").trim();
//				if (amount == null || amount.trim().length() == 0) {
//					errorMsgs.add("解答請勿空白");
//				}
				

				WalletrecordVO walletrecordVO = new WalletrecordVO();
				walletrecordVO.setMbrID(mbrID);
				walletrecordVO.setNote(note);
				walletrecordVO.setAmount(amount);

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
req.setAttribute("walletrecordVO", walletrecordVO); // 含有輸入格式錯誤的walletrecordVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front-end/walletrecord/addWalletrecord.jsp");
					failureView.forward(req, res);
					return;
				}
				
				/***************************2.開始新增資料***************************************/
				WalletrecordService walletrecordSvc = new WalletrecordService();
				walletrecordVO = walletrecordSvc.addWalletrecord(walletrecordVO);
				
				//需update member 的錢
				walletrecordVO = walletrecordSvc.updateWalletrecord(note, mbrID);
				
				MemService memSvc = new MemService();
				MemVO memVO = memSvc.getOneMem(mbrID);
				HttpSession session = req.getSession();
				session.setAttribute("memVO", memVO);
				
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				String url = "/front-end/walletrecord/listAllWalletrecord.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
				successView.forward(req, res);				
		}
		
		
		if ("delete".equals(action)) { // 來自listAllWalletrecord.jsp

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
	
				/***************************1.接收請求參數***************************************/
				Integer walletRecNo = Integer.valueOf(req.getParameter("wallet_rec_no"));
				
				/***************************2.開始刪除資料***************************************/
				WalletrecordService walletrecordSvc = new WalletrecordService();
				walletrecordSvc.deleteWalletrecord(walletRecNo);
				
				/***************************3.刪除完成,準備轉交(Send the Success view)***********/								
				String url = "/walletrecord/listAllWalletrecord.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);
		}
	}
}
