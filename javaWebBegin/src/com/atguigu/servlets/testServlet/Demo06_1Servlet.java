package com.atguigu.servlets.testServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 演示转发 demo06_1 -> demo06_2
public class Demo06_1Servlet extends HttpServlet{
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Demo06_1Servlet...");
        // 服务器端内部转发
        //request.getRequestDispatcher("demo06_2").forward(request, response);
        // 客户端重定向
        response.sendRedirect("demo06_2");
    }
}
