package com.atguigu.servlets;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// 演示从HttpSession保存数据域中获取数据
public class Demo05_2Servlet extends HttpServlet{
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object usernameObj = request.getSession().getAttribute("username");
        System.out.println(usernameObj);
    }
}
