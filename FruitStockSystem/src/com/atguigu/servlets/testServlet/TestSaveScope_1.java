package com.atguigu.servlets.testServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 演示保存作用域
@WebServlet("/testSaveScope_1")
public class TestSaveScope_1 extends HttpServlet{
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        // 演示request保存作用域
        // 向request保存作用域
        request.setAttribute("uname", "fizz");
        // 客户端重定向
        // 重定向之后已经不是一个servlet组件了，客户端向servlet2重发request已经无法获取到第一个request的保存作用域信息了
        //response.sendRedirect("testSaveScope_2");
        // 服务器转发
        request.getRequestDispatcher("testSaveScope_2").forward(request, response);
        */

        /*
        // 演示session保存作用域
        request.getSession().setAttribute("uname", "fizz");
        // 即使是重定向，依然是同一个session，可以获取到之前在session保存作用域中保存的信息
        response.sendRedirect("testSaveScope_2");
        //request.getRequestDispatcher("testSaveScope_2").forward(request, response);
        */

        // 演示application保存作用域
        // 向application保存数据
        // ServletContext：servlet上下文(servlet环境)
        // ServletContext随着tomcat启动而启动，停止而停止，也称之为application
        ServletContext application = request.getServletContext();
        application.setAttribute("uname", "fizz");
        response.sendRedirect("testSaveScope_2");

    }
}
