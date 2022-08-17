package com.cga102g3.web.note.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cga102g3.web.note.model.NoteService;
import com.cga102g3.web.note.model.NoteVO;

@WebServlet("/front-end/note/note.do")
public class NoteServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
		
		
		if ("getOne_For_Display".equals(action)) { // 來自select_page.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
				String str = req.getParameter("note_ID");
				if (str == null || (str.trim()).length() == 0) {
					errorMsgs.add("請輸入員工編號");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front-end/note/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				Integer note_ID = null;
				try {
					note_ID = Integer.valueOf(str);
				} catch (Exception e) {
					errorMsgs.add("員工編號格式不正確");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front-end/note/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************2.開始查詢資料*****************************************/
				NoteService noteSvc = new NoteService();
				NoteVO noteVO = noteSvc.getOneNote(note_ID);
				if (noteVO == null) {
					errorMsgs.add("查無資料");
				}
				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front-end/note/select_page.jsp");
					failureView.forward(req, res);
					return;//程式中斷
				}
				
				/***************************3.查詢完成,準備轉交(Send the Success view)*************/
				req.setAttribute("noteVO", noteVO); // 資料庫取出的noteVO物件,存入req
				String url = "/front-end/note/listOneNote.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 成功轉交 listOneNote.jsp
				successView.forward(req, res);
		}
		
		
		if ("getOne_For_Update".equals(action)) { // 來自listAllNote.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
			
				/***************************1.接收請求參數****************************************/
				Integer note_ID = Integer.valueOf(req.getParameter("note_ID"));
				
				/***************************2.開始查詢資料****************************************/
				NoteService noteSvc = new NoteService();
				NoteVO noteVO = noteSvc.getOneNote(note_ID);
								
				/***************************3.查詢完成,準備轉交(Send the Success view)************/
				req.setAttribute("noteVO", noteVO);         // 資料庫取出的noteVO物件,存入req
				String url = "/front-end/note/update_note_input.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_note_input.jsp
				successView.forward(req, res);
		}
		
		
if ("update".equals(action)) { // 來自update_note_input.jsp的請求
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
		
				/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
Integer noteID = Integer.valueOf(req.getParameter("note_ID").trim());

Integer mbrID = Integer.valueOf(req.getParameter("mbr_ID").trim());

//if (mbr_ID == null || mbr_ID.trim().length() == 0) {
//	errorMsgs.add("解答 請勿空白");
//}	


Integer noteContentType = Integer.valueOf(req.getParameter("note_content_type").trim());

//Integer note_content_type = req.getParameter("note_content_type");
//				String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
//				if (note_content_type == null || note_content_type.trim().length() == 0) {
//					errorMsgs.add("常見問題 請勿空白");
//				} else if(!note_content_type.trim().matches(enameReg)) { //以下練習正則(規)表示式(regular-expression)
//					errorMsgs.add("員工姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間");
//	            }
//				
String noteContent = req.getParameter("note_content").trim();
//				if (note_content == null || note_content.trim().length() == 0) {
//					errorMsgs.add("解答 請勿空白");
//				}	
				
				NoteVO noteVO = new NoteVO();
				
				noteVO.setMbrID(mbrID);
				noteVO.setNoteTime(new Timestamp(System.currentTimeMillis()));	
				noteVO.setNoteContentType(noteContentType);
				noteVO.setNoteContent(noteContent);

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
req.setAttribute("noteVO", noteVO); // 含有輸入格式錯誤的fanoteVO,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/back-end/note/update_note_input.jsp");
					failureView.forward(req, res);
					return; //程式中斷
				}
				
				/***************************2.開始修改資料*****************************************/
				NoteService noteSvc = new NoteService();
				noteVO = noteSvc.updateNote(mbrID,noteContentType, noteContent, noteID);
				
				/***************************3.修改完成,準備轉交(Send the Success view)*************/
				req.setAttribute("noteVO", noteVO); // 資料庫update成功後,正確的的noteVO物件,存入req
				String url = "/back-end/note/listOneNote.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneNote.jsp
				successView.forward(req, res);
		}



        if ("insert".equals(action)) { // 來自addNote.jsp的請求  
			
			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);

				/***********************1.接收請求參數 - 輸入格式的錯誤處理*************************/
			
Integer mbrID = Integer.valueOf(req.getParameter("mbr_ID"));

Integer noteContentType = Integer.valueOf(req.getParameter("note_content_type"));

//				String enameReg = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$";
				if (noteContentType == null) {
					errorMsgs.add("通知類型 請勿空白");
//				} else if(!note_content_type.trim().matches(enameReg)) { //以下練習正則(規)表示式(regular-expression)
//					errorMsgs.add("員工姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間");
	            }
				
String noteContent = req.getParameter("note_content").trim();
				if (noteContent == null || noteContent.trim().length() == 0) {
					errorMsgs.add("留言內容 請勿空白");
				}
				
				

				NoteVO noteVO = new NoteVO();
				noteVO.setMbrID(mbrID);
				noteVO.setNoteContentType(noteContentType);
				noteVO.setNoteContent(noteContent);
				

				// Send the use back to the form, if there were errors
				if (!errorMsgs.isEmpty()) {
req.setAttribute("noteVO", noteVO); // 含有輸入格式錯誤的noteVO物件,也存入req
					RequestDispatcher failureView = req
							.getRequestDispatcher("/front-end/note/addNote.jsp");
					failureView.forward(req, res);
					return;
				}
				
				/***************************2.開始新增資料***************************************/
				NoteService noteSvc = new NoteService();
				noteSvc.addNote(mbrID, noteContentType, noteContent);
				
				/***************************3.新增完成,準備轉交(Send the Success view)***********/
				String url = "/front-end/note/listAllNote.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url); // 新增成功後轉交listAllEmp.jsp
				successView.forward(req, res);				
		}
		
		
		if ("delete".equals(action)) { // 來自listAllNote.jsp

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
	
				/***************************1.接收請求參數***************************************/
				Integer note_ID = Integer.valueOf(req.getParameter("note_ID"));
				
				/***************************2.開始刪除資料***************************************/
				NoteService noteSvc = new NoteService();
				noteSvc.deleteNote(note_ID);
				
				/***************************3.刪除完成,準備轉交(Send the Success view)***********/								
				String url = "/back-end/note/listAllNote.jsp";
				RequestDispatcher successView = req.getRequestDispatcher(url);// 刪除成功後,轉交回送出刪除的來源網頁
				successView.forward(req, res);
		}
	}
}
