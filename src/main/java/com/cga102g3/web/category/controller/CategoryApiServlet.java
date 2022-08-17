package com.cga102g3.web.category.controller; /**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-06-14 下午 10:25
 */


import com.cga102g3.web.category.service.CategoryService;
import com.cga102g3.web.category.service.impl.CategoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.cga102g3.core.util.CommonUtil.writePojo2Json;


@WebServlet(name = "CategoryApiServlet", value = "/book/categories")
public class CategoryApiServlet extends HttpServlet {
    private final CategoryService service = new CategoryServiceImpl();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("utf-8");
        writePojo2Json(response, service.getAll());
    }


}
