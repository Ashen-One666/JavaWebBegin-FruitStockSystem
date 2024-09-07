package com.atguigu.servlets.testServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// 演示session
public class Demo05Servlet extends HttpServlet{
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取session，如果获取不到，则新建一个
        HttpSession session = request.getSession();
        System.out.println("session ID =" + session.getId());
        session.invalidate();
    }
}
