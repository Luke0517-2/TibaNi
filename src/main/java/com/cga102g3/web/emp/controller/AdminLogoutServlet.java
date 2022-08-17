package com.cga102g3.web.emp.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/back-end/emp/emplogout")
public class AdminLogoutServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {		
			doGet(req, res);
		}


	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		 req.setCharacterEncoding("UTF-8");
		 res.setContentType("text/html; charset=UTF-8");
		HttpSession session = req.getSession(false);//防止建立Session
		
		session.invalidate();
		res.sendRedirect(req.getContextPath()+"/back-end/emp/empLogin.jsp");
	
}
}
