package com.atguigu.servlets.testServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 测试Servlet生命周期
public class Demo03Servlet extends HttpServlet{
    public Demo03Servlet(){
        System.out.println("正在实例化...");
    }
    public void init() throws ServletException {
        System.out.println("正在初始化...");
    }
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        System.out.println("正在服务...");
    }
    public void destroy() {
        System.out.println("正在销毁...");
    }
}
