package com.cga102g3.web.emp.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cga102g3.web.emp.model.AdminAuthService;
import com.cga102g3.web.emp.model.AdminAuthVO;
import com.cga102g3.web.emp.model.AdminService;
import com.cga102g3.web.emp.model.AdminVO;


@WebServlet("/back-end/emp/emp.do")
@MultipartConfig
public class AdminServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}
	

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
	
		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");

		if ("getOne_For_Display".equals(action)) { // 來自select_page.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
			String str = req.getParameter("adminID");
			if (str == null || (str.trim()).length() == 0) {
				errorMsgs.add("員工編號格式不正確");
			}
			// Send the use back to the form, if there were errors
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/emp/select_page.jsp");
				failureView.forward(req, res);
				return;//  程式中斷
			}

			Integer adminID = null;
			try {
				adminID = Integer.valueOf(str);
			} catch (Exception e) {
				errorMsgs.add("查無資料");
			}
			// Send the use back to the form, if there were errors
			if (!errorMsgs.isEmpty()) {
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/emp/select_page.jsp");
				failureView.forward(req, res);
				return;// 程式中斷
			}

			/*************************** 2.開始查詢資料 *****************************************/
			AdminService empSvc = new AdminService();
			AdminVO adminVO = empSvc.getOneEmp(adminID);
//			if (adminVO == null) {
//				errorMsgs.add("查無此資料");
//			}
			// Send the use back to the form, if there were errors
//			if (!errorMsgs.isEmpty()) {
//				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/emp/select_page.jsp");
//				failureView.forward(req, res);
//				return;// 程式中斷_
//			}

			/*************************** 3.查詢完成,準備轉交(Send the Success view) *************/
			req.setAttribute("AdminVO", adminVO); // 資料庫取出的empVO物件,存入req
			String url = "/back-end/emp/listOneAdmin.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneEmp.jsp
			successView.forward(req, res);
		}

		if ("insert".equals(action)) { // 新增員工 // 來自addEmp.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			/*********************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z *************************/
			
			
			
			String account = req.getParameter("account").trim();
			if (account == null || account.trim().length() == 0) {
				errorMsgs.add("請輸入帳號");
			}
			AdminService adminsv = new AdminService();
            List<AdminVO> adminVOs = adminsv.getAll();
            for (AdminVO adminVO : adminVOs) {
                if (account.equals(adminVO.getAdminAccount())) {
                    errorMsgs.add("帳號已被使用");
                }

            }
			
			
			String password = req.getParameter("password").trim();
			String passwordReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{4,10}$";
			if (password == null || password.trim().length() == 0) {
				errorMsgs.add("請輸入密碼");
			}else if (!password.trim().matches(passwordReg)) { // �H�U�m�ߥ��h(�W)��ܦ�(regular-expression)
				errorMsgs.add("密碼只能是中、英文字母、數字和_ , 且長度必需在4到10之間");
			}
			
			String name = req.getParameter("name");
			String nameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
			if (name == null || name.trim().length() == 0) {
				errorMsgs.add("請輸入姓名");
			} else if (!name.trim().matches(nameReg)) { // �H�U�m�ߥ��h(�W)��ܦ�(regular-expression)
				errorMsgs.add("姓名只能是中、英文字母、數字和_ , 且長度必需在2到10之間");
			}

			String phone = req.getParameter("phone").trim();
			if (phone == null || phone.trim().length() == 0) {
				errorMsgs.add("請輸入手機");
			}

			AdminVO adminVO = new AdminVO();
			adminVO.setAdminAccount(account);
			adminVO.setAdminPassword(password);
			adminVO.setAdminName(name);
			adminVO.setAdminPhone(phone);

			// Send the use back to the form, if there were errors
			if (!errorMsgs.isEmpty()) {
				req.setAttribute("adminVO", adminVO); // �t����J�榡���~��empVO����,�]�s�Jreq
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/emp/addAdmin.jsp");
				failureView.forward(req, res);
				return;
			}

			/*************************** 2.�}�l�s�W��� ***************************************/
			AdminService adminSvc = new AdminService();
			adminVO = adminSvc.addEmp(account, password, name, phone);

			/*************************** 3.�s�W����,�ǳ����(Send the Success view) ***********/
			String url = "/back-end/emp/listAllAdmin.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
			successView.forward(req, res);
		}

		if ("getOne_For_Update".equals(action)) { // / 來自listAllAdmin.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.接收請求參數 ****************************************/
			Integer adminID = Integer.valueOf(req.getParameter("adminID"));

			/*************************** 2.開始查詢資料 ****************************************/
			AdminService adminSvc = new AdminService();
			AdminVO adminVO = adminSvc.getOneEmp(adminID);

			/*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
			req.setAttribute("adminVO", adminVO); // // 資料庫取出的empVO物件,存入req
			String url = "/back-end/emp/update_admin_input.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_admin.input.jsp
			successView.forward(req, res);
		}

		if ("update".equals(action)) { // �ק���u��� �Ӧ�update_emp_input.jsp���ШD

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			Integer adminID = Integer.valueOf(req.getParameter("adminID").trim());

			String account = req.getParameter("account").trim();
			if (account == null || account.trim().length() == 0) {
				errorMsgs.add("請輸入帳號");
			}
			String password = req.getParameter("password").trim();
			String passwordReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{4,10}$";
			if (password == null || password.trim().length() == 0) {
				errorMsgs.add("請輸入密碼");
			}else if (!password.trim().matches(passwordReg)) { // �H�U�m�ߥ��h(�W)��ܦ�(regular-expression)
				errorMsgs.add("密碼只能是中、英文字母、數字和_ , 且長度必需在4到10之間");
			}
			
			String name = req.getParameter("name");
			String nameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
			if (name == null || name.trim().length() == 0) {
				errorMsgs.add("請輸入姓名");
			} else if (!name.trim().matches(nameReg)) { // �H�U�m�ߥ��h(�W)��ܦ�(regular-expression)
				errorMsgs.add("姓名只能是中、英文字母、數字和_ , 且長度必需在2到10之間");
			}

			String phone = req.getParameter("phone").trim();
			if (phone == null || phone.trim().length() == 0) {
				errorMsgs.add("請輸入手機");
			}

			AdminVO adminVO = new AdminVO();
			adminVO.setAdminID(adminID);
			adminVO.setAdminAccount(account);
			adminVO.setAdminPassword(password);
			adminVO.setAdminName(name);
			adminVO.setAdminPhone(phone);
			
			// Send the use back to the form, if there were errors
			if (!errorMsgs.isEmpty()) {
				req.setAttribute("adminVO", adminVO); // �t����J�榡���~��empVO����,�]�s�Jreq
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/emp/update_admin_input.jsp");
				failureView.forward(req, res);
				return;
			}

			/*************************** 2.�}�l�ק��� ***************************************/
			AdminService adminSvc = new AdminService();
			adminVO = adminSvc.updateEmp(adminID, account, password, name, phone);
			
			req.getPart("upfile1").write(getServletContext().getRealPath("/static/images/emp")+ "/" + adminID + ".png");
//			System.out.println(getServletContext().getRealPath("/images/emp")+ "/" + adminID + ".png");
			
			
		
			/*************************** 3.�ק粒��,�ǳ����(Send the Success view) ***********/
			req.setAttribute("adminVO", adminVO);
			String url = "/back-end/emp/listAllAdmin.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
			successView.forward(req, res);
		}

		if ("delete".equals(action)) { // �R�����u // �Ӧ�listAllEmp.jsp

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.�����ШD�Ѽ� ***************************************/
			Integer adminID = Integer.valueOf(req.getParameter("adminID"));

			/*************************** 2.�}�l�R����� ***************************************/
			AdminService empSvc = new AdminService();
			empSvc.deleteEmp(adminID);

			/*************************** 3.�R������,�ǳ����(Send the Success view) ***********/
			String url = "/back-end/emp/listAllAdmin.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);// �R�����\��,���^�e�X�R�����ӷ�����
			successView.forward(req, res);
		}
		
		
		if ("getOne_For_UpdateAuth".equals(action)) { // / 來自listAllAdmin.jsp的請求

			/*************************** 1.接收請求參數 ****************************************/
			Integer adminID = Integer.valueOf(req.getParameter("adminID"));

			/*************************** 2.開始查詢資料 ****************************************/
			AdminService adminSvc = new AdminService();
			AdminVO adminVO = adminSvc.getOneEmp(adminID);
			AdminAuthService adminAuthService = new AdminAuthService();
			List<AdminAuthVO> adminAuthVO =  adminAuthService.getAdminAuth(adminID);

			/*************************** 3.查詢完成,準備轉交(Send the Success view) ************/
			req.setAttribute("adminVO", adminVO); // // 資料庫取出的empVO物件,存入req
			req.setAttribute("adminAuthVO", adminAuthVO);
			String url = "/back-end/emp/update_admin_auth.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_admin.input.jsp
			successView.forward(req, res);
		}

		if ("updateAuth".equals(action)) { // �ק���u��� �Ӧ�update_emp_input.jsp���ШD

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

			/*************************** 1.�����ШD�Ѽ� - ��J�榡�����~�B�z **********************/
			Integer adminID = Integer.valueOf(req.getParameter("adminID").trim());			
			AdminVO adminVO = new AdminVO();
			adminVO.setAdminID(adminID);
			
			String[] auth =req.getParameterValues("auth");
			if (auth == null || auth.length == 0) {
				errorMsgs.add("請選擇權限");
			}
			
			
			// Send the use back to the form, if there were errors
			if (!errorMsgs.isEmpty()) {
				req.setAttribute("adminVO", adminVO); // �t����J�榡���~��empVO����,�]�s�Jreq
				RequestDispatcher failureView = req.getRequestDispatcher("/back-end/emp/update_admin_auth.jsp");
				failureView.forward(req, res);
				return;
			}

			/*************************** 2.�}�l�ק��� ***************************************/
			AdminAuthService adminAuthSvc = new AdminAuthService();
			adminAuthSvc.delete(adminID);
			for(int i=0;i < auth.length; i++) {
				adminAuthSvc.insert(adminID, Integer.valueOf(auth[i]));
			}
//			List <AdminAuthVO> adminAuthVO = adminAuthSvc.getAdminAuth(adminID);
			
			/*************************** 3.�ק粒��,�ǳ����(Send the Success view) ***********/
			req.setAttribute("adminVO", adminVO);
//			req.setAttribute("AdminAuthVO", adminAuthVO);
			String url = "/back-end/emp/listAdminAuth.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // �s�W���\�����listAllEmp.jsp
			successView.forward(req, res);
		}
		
		
		
		
		// test upfile
//		if ("upfile".equals(action)) {
//			req.getPart("upfile1").write(getServletContext().getRealPath("/images")+ "/" + getFileNameFromPart(req.getPart("upfile1")));
//			
//			String url = "/emp/upFile.jsp";
//			RequestDispatcher successView = req.getRequestDispatcher(url);// �R�����\��,���^�e�X�R�����ӷ�����
//			successView.forward(req, res);
//		}
		
	}
//	public String getFileNameFromPart(Part part) {
//		String header = part.getHeader("content-disposition");
//		System.out.println("header=" + header); // ���ե�
//		String filename = new File(header.substring(header.lastIndexOf("=") + 2, header.length() - 1)).getName();
//		System.out.println("filename=" + filename); // ���ե�
//		if (filename.length() == 0) {
//			return null;
//		}
//		return filename;
//	}
}
