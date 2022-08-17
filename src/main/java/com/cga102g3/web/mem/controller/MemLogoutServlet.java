package com.cga102g3.web.mem.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet("/front-end/mem/memlogout")
public class MemLogoutServlet extends HttpServlet{
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
		
		if(session != null) {
		session.invalidate();
		res.sendRedirect(req.getContextPath()+"/front-end/mem/login.jsp");}
		else {	
			res.sendRedirect(req.getContextPath()+"/front-end/mem/login.jsp");}	
		
	
}
}