package com.atguigu.servlets.oldServlets;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/update.do")
public class UpdateServlet extends ViewBaseServlet {

    private FruitDAO fruitDAO = new FruitDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置编码
        request.setCharacterEncoding("utf-8");

        // 获取参数
        String fidStr = request.getParameter("fid");
        int fid = Integer.parseInt(fidStr);
        String fname = request.getParameter("fname");
        String priceStr = request.getParameter("price");
        int price = Integer.parseInt(priceStr);
        String fcountStr = request.getParameter("fcount");
        int fcount = Integer.parseInt(fcountStr);
        String remark = request.getParameter("remark");

        // 执行更新
        fruitDAO.updateFruit(new Fruit(fid, fname, price, fcount, remark));

        // 资源跳转：回到index页面上
        // 这句话相当于request.getRequestDispatcher("index.html").forward(request, response)，跳转到老页面上，页面数据未更新
        //super.processTemplate("index", request, response); // 这里面的"index"会添加前后缀，效果相当于"index.html"
        // 此处需要得到重定向的效果，让客户端重新给IndexServlet发请求然后覆盖到session中，这样客户端获得的index页面才是最新的
        response.sendRedirect("index");
    }
}
