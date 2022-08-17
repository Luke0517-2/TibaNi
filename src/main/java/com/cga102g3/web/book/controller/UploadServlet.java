/**
 * @Description
 * @Author Robert
 * @Version
 * @Date 2022-06-16 上午 10:36
 */
package com.cga102g3.web.book.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

@MultipartConfig(fileSizeThreshold = 1,
        maxFileSize = 15*1024*1024, maxRequestSize = 50*1024*1024)
@WebServlet(name = "UploadServlet", value = "/upload")
public class UploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String saveDirectory = "/images/books";
        request.setCharacterEncoding("UTF-8");
        String realPath = getServletContext().getRealPath(saveDirectory);
        // 開發環境下文件位置 realPath=C:\Users\u8360\IdeaProjects\bookmall\target\bookmall-1.0-SNAPSHOT\images_uploaded
        // 在service層處理照片儲存邏輯
        File fsaveDirectory = new File(realPath);
        if (!fsaveDirectory.exists()) {
            fsaveDirectory.mkdirs(); // 於 ContextPath 之下,自動建立目地目錄
        }
        System.out.println(fsaveDirectory);

        Collection<Part> parts = request.getParts();
        request.getPart("uploadImg");
        String title = request.getParameter("title");
        System.out.println(title);

        for (Part part : parts) {
            String filename = part.getSubmittedFileName();

            if (filename!= null && part.getContentType()!=null) {

//                String name = part.getName();
//                String ContentType = part.getContentType();  // 不要用來作為檔案上傳類型判斷，改副檔名，chrome傻傻放行
//                long size = part.getSize();
                File f = new File(fsaveDirectory, filename);

                // 利用File物件,寫入目地目錄,上傳成功
                part.write(f.toString());  // write(檔案路徑)

                // 額外測試 InputStream 與 byte[] (幫將來model的VO預作準備)
//				InputStream in = part.getInputStream();
//				byte[] buf = new byte[in.available()];
//				in.read(buf);
//				in.close();
//				out.println("buffer length: " + buf.length);

            }
        }
    }
}
