package com.cga102g3.web.mem.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cga102g3.web.mem.model.MailService;
import com.cga102g3.web.mem.model.MemService;
import com.cga102g3.web.mem.model.MemVO;

@WebServlet("/back-end/mem/mem.do")
@MultipartConfig
public class MemServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");

		if ("signup".equals(action)) { // 來自signup.jsp的請求

//			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();
//			req.setAttribute("errorMsgs", errorMsgs);
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/

			String mbrAccount = req.getParameter("username");
			String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{4,10}$";
			if (mbrAccount == null || mbrAccount.trim().length() == 0) {
				errorMsgs.add("帳號請勿空白");
			} else if (!mbrAccount.trim().matches(enameReg)) { // 以下練習正則(規)表示式(regular-expression)
				errorMsgs.add("帳號只能是中、英文字母、數字和_ , 且長度必需在4到10之間");
			}

			MemService memsv = new MemService();
			List<MemVO> memVOs = memsv.getAll();
			for (MemVO memVO : memVOs) {
				if (mbrAccount.equals(memVO.getMbrAccount())) {
					errorMsgs.add("帳號已被註冊過");
				}

			}

			String mbrPassword = req.getParameter("password");
			String passwordReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{4,10}$";
			if (mbrPassword == null || mbrPassword.trim().length() == 0) {
				errorMsgs.add("密碼請勿空白");
			} else if (!mbrPassword.trim().matches(passwordReg)) { // 以下練習正則(規)表示式(regular-expression)
				errorMsgs.add("密碼只能是中、英文字母、數字和_ , 且長度必需在4到10之間");
			}

			String mbrName = req.getParameter("name");
			Integer mbrGender = Integer.valueOf(req.getParameter("gender").trim());
			if (mbrGender == null) {
				errorMsgs.add("請選擇性別");
			}

			String mbrMobile = req.getParameter("phone");
			String city = req.getParameter("city");
			String town = req.getParameter("town");
			String Addr = req.getParameter("address");
			String mbrAddr = city + town + Addr;

			String mbrEmail = req.getParameter("email");
			MemService memmail = new MemService();
			List<MemVO> memVOmail = memmail.getAll();
			for (MemVO memVO : memVOmail) {
				if (mbrEmail.equals(memVO.getMbrEmail())) {
					errorMsgs.add("信箱已被註冊過");
				}

			}

			java.sql.Date hiredate = null;
			try {
				hiredate = java.sql.Date.valueOf(req.getParameter("date").trim());
			} catch (IllegalArgumentException e) {
				errorMsgs.add("請輸入日期");
			}

			MemVO memVO = new MemVO();
			memVO.setMbrAccount(mbrAccount);
			memVO.setMbrPassword(mbrPassword);
			memVO.setMbrName(mbrName);
			memVO.setMbrGender(mbrGender);
			memVO.setMbrMobile(mbrMobile);
			memVO.setMbrAddr(Addr);
			memVO.setMbrEmail(mbrEmail);

			// Send the use back to the form, if there were errors
			if (!errorMsgs.isEmpty()) {
				req.setAttribute("memVO", memVO);
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/mem/signup.jsp");
				failureView.forward(req, res);
				return; // 程式中斷
			}

			/*************************** 2.開始新增資料 *****************************************/
			MemService memSvc = new MemService();
			memSvc.signup(mbrAccount, mbrPassword, mbrName, mbrGender, mbrMobile, mbrAddr, mbrEmail, hiredate);
			MemVO memVO2 = memSvc.signupStatus(memVO); // 從mail與取出mbrID
			/*************************** 3.寄送驗證信 *****************************************/
			MailService mailService = new MailService();
			mailService.sendMail(mbrEmail, "Tibani書城_會員認證信",
					req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath()
							+ "/back-end/mem/mem.do?mbrID=" + memVO2.getMbrID() + "&action=verifyStatus");
			/*************************** 4.修改完成,準備轉交(Send the Success view) *************/
//				req.setAttribute("memVO", memVO); // 資料庫update成功後,正確的的empVO物件,存入req
			String url = "/index.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneEmp.jsp
			successView.forward(req, res);
		}

		//點擊驗證信改變會員狀態為已開通
		if ("verifyStatus".equals(action)) {
			/*************************** 1.接收請求參數 ****************************************/
			Integer mbrID = Integer.valueOf(req.getParameter("mbrID"));
			/*************************** 2.開始更改資料 ****************************************/
			MemService memSvc = new MemService();
			memSvc.updateStatus(mbrID);
			String url = "/index.jsp";
			System.out.println(url);
			RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交index.jsp
			successView.forward(req, res);

		}
		//員工改會員狀態用
		if ("verifyStatusByEMP".equals(action)) {
			/*************************** 1.接收請求參數 ***************************************/
			Integer mbrID = Integer.valueOf(req.getParameter("mbrID"));
			Integer mbrStatus = Integer.valueOf(req.getParameter("mbrStatus"));
			/*************************** 2.開始修改資料 *****************************************/
			MemService memberSvc = new MemService();
			memberSvc.updateStatusByEMP(mbrID, mbrStatus);

			/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
			String url = "/back-end/mem/listAllMember.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listAllMember.jsp
			successView.forward(req, res);
			return;
		}

		if ("getOne_For_Update".equals(action)) { // / 來自memlist.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 ****************************************/
			Integer mbrID = Integer.valueOf(req.getParameter("mbrID"));

			/*************************** 2.開始查詢資料 ****************************************/
			MemService memSvc = new MemService();
			MemVO memVO = memSvc.getOneMem(mbrID);

			/*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
			req.setAttribute("memVO", memVO); // // 資料庫取出的empVO物件,存入req
			String url = "/front-end/mem/update.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update.jsp
			successView.forward(req, res);
		}

		if ("update".equals(action)) {// 來自getOne_For_Update的請求

			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/

			Integer mbrID = Integer.valueOf(req.getParameter("mbrID").trim());
			String mbrAccount = req.getParameter("username");
			String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{3,10}$";
			if (mbrAccount == null || mbrAccount.trim().length() == 0) {
				errorMsgs.add("帳號: 請勿空白");
			} else if (!mbrAccount.trim().matches(enameReg)) { // 以下練習正則(規)表示式(regular-expression)
				errorMsgs.add("帳號: 只能是中、英文字母、數字和_,且長度必需在3到10之間");
			}

			String mbrPassword = req.getParameter("password");
			String passwordReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{4,10}$";
			if (mbrPassword == null || mbrPassword.trim().length() == 0) {
				errorMsgs.add("密碼: 請勿空白");
			} else if (!mbrPassword.trim().matches(passwordReg)) { // 以下練習正則(規)表示式(regular-expression)
				errorMsgs.add("密碼: 只能是中、英文字母、數字和_,且長度必需在4到10之間");
			}

			String mbrName = req.getParameter("name");
//		Integer mbrGender = Integer.valueOf(req.getParameter("gender").trim());
			String mbrMobile = req.getParameter("phone");
			String city = req.getParameter("city");
			String town = req.getParameter("town");
			String Addr = req.getParameter("address");
			String mbrAddr = city + town + Addr;
			String mbrEmail = req.getParameter("email");

			java.sql.Date date = null;
			try {
				date = java.sql.Date.valueOf(req.getParameter("date").trim());
			} catch (IllegalArgumentException e) {
				errorMsgs.add("請輸入日期");
			}
			MemVO memVO = new MemVO();
			memVO.setMbrID(mbrID);
			memVO.setMbrAccount(mbrAccount);
			memVO.setMbrPassword(mbrPassword);
			memVO.setMbrName(mbrName);
			memVO.setMbrMobile(mbrMobile);
			memVO.setMbrAddr(mbrAddr);
			memVO.setMbrEmail(mbrEmail);

			// Send the use back to the form, if there were errors
			if (!errorMsgs.isEmpty()) {
				req.setAttribute("memVO", memVO);
				RequestDispatcher failureView = req.getRequestDispatcher("/front-end/mem/update.jsp");
				failureView.forward(req, res);
				return; // 程式中斷
			}

			/*************************** 2.開始新增資料 *****************************************/
			MemService memSvc = new MemService();
			memVO = memSvc.update(mbrID, mbrPassword, mbrName, mbrMobile, mbrAddr, mbrEmail, date);
			memVO = memSvc.getOneMem(mbrID);

			HttpSession session = req.getSession();
			session.setAttribute("memVO", memVO);
			/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
			req.setAttribute("memVO", memVO); // 資料庫update成功後,正確的的empVO物件,存入req
			String url = "/front-end/mem/memlist.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交memlist.jsp
			successView.forward(req, res);
		}

		if ("updatePic".equals(action)) {
			Integer mbrID = Integer.valueOf(req.getParameter("mbrID").trim());
			req.getPart("upfile1").write(getServletContext().getRealPath("/static/images/mem") + "/" + mbrID + ".png");

			String url = "/front-end/mem/memlist.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交memlist.jsp
			successView.forward(req, res);
		}

		if ("delete".equals(action)) { // 來自listAllEmp.jsp

			Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 ***************************************/
			Integer mbrID = Integer.valueOf(req.getParameter("mbrID"));

			/*************************** 2.開始刪除資料 ***************************************/
			MemService memSvc = new MemService();
			memSvc.deleteMem(mbrID);

			/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
			String url = "/back-end/mem/listAllMember.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
			successView.forward(req, res);
		}

	}
}
