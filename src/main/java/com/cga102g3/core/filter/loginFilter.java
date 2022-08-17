package com.cga102g3.core.filter;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class loginFilter implements Filter {

//    private FilterConfig config;
//
//    public void init(FilterConfig config) {
//        this.config = config;
//    }
//
//    public void destroy() {
//        config = null;
//    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession();
     // 【從 session 判斷此user是否登入過】
        Integer mbrID = (Integer)session.getAttribute("mbrID");
        if (mbrID == null ) {        //失敗
            session.setAttribute("location", req.getRequestURI());
            res.sendRedirect(req.getContextPath() + "/front-end/mem/login.jsp");
            return;
        } else {        //成功
            chain.doFilter(request, response);
        }
    }
}