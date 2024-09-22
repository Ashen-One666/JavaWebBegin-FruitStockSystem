package com.atguigu.servlets.oldServlets;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.myssm.myspringmvc.ViewBaseServlet;
import com.atguigu.myssm.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/del.do")
public class DelServlet extends ViewBaseServlet {
    // 发过来的只要不是form表单且method="post"，一概都是get方法

    private FruitDAO fruitDAO = new FruitDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fidStr = request.getParameter("fid");
        if(StringUtil.isNotEmpty(fidStr)){
            //System.out.println("del");
            int fid = Integer.parseInt(request.getParameter("fid"));
            fruitDAO.delFruit(fid);
            response.sendRedirect("index");
        }
    }
}
