package com.atguigu.servlets.testServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 演示保存作用域
@WebServlet("/testSaveScope_2")
public class TestSaveScope_2 extends HttpServlet{
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*
        // 演示request保存作用域
        // 获取request保存作用域的数据，key为uname
        Object unameObj = request.getAttribute("uname");
        System.out.println("uname = " + unameObj);
        */

        /*
        // 演示session保存作用域
        Object unameObj = request.getSession().getAttribute("uname");
        System.out.println("uname = " + unameObj);
        */

        // 演示application保存作用域
        ServletContext application = request.getServletContext();
        Object unameObj = application.getAttribute("uname");
        System.out.println("uname = " + unameObj);
    }
}
