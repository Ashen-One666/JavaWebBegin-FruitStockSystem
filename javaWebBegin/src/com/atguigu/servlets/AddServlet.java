package com.atguigu.servlets;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.fruit.pojo.Fruit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        // post模式下, 设置编码防止乱码 (get方式目前不需要设置编码)
        request.setCharacterEncoding("UTF-8");
        String fname = request.getParameter("fname");
        String fpriceStr = request.getParameter("fprice");
        int fprice = Integer.parseInt(fpriceStr);
        String fcountStr = request.getParameter("fcount");
        int fcount = Integer.parseInt(fcountStr);
        String remark = request.getParameter("remark");

        FruitDAO fruitDAO = new FruitDAOImpl();
        int nowId = fruitDAO.getFruitCountSum();
        fruitDAO.addFruit(new Fruit(100, fname, fprice, fcount, remark));
        System.out.println(fruitDAO.getFruitCountSum());
        //System.out.println(fname + " " + fprice + " " + fcount + " " + remark);
        //System.out.println("添加成功！");
    }
}
