package com.atguigu.servlets;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

// Servlet从3.0版本开始支持注解方式注册
@WebServlet("/index")
// thymeleaf: 我们的Servlet继承ViewBaseServlet
public class IndexServlet extends ViewBaseServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FruitDAO fruitDAO = new FruitDAOImpl();
        List<Fruit> fruitList = fruitDAO.getFruitList();
        // 保存到session作用域
        HttpSession session = request.getSession();
        session.setAttribute("fruitList", fruitList);

        /*
        * processTemplate: thymeleaf中处理视图模板
        * 详见 readme.txt -> 七、thymeleaf模板视图技术 -> 5.根据逻辑视图名称得到物理视图名称
        */
        super.processTemplate("index", request, response);

    }
}
