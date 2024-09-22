package com.atguigu.servlets.oldServlets;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.myssm.myspringmvc.ViewBaseServlet;
import com.atguigu.myssm.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
    // index和查询关键字都使用doGet内的逻辑
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");


        // --------------------------------------- 获取参数信息 ---------------------------------------
        HttpSession session = request.getSession();
        // 第一次请求时显示第一页
        int pageNo = 1;

        // 无论是index还是search都采用keyword，search的keyword是输入的关键字，非search的keyword为空
        // search方法中需要保存keyword到session作用域中是因为search后也需要翻页，翻页时显示的数据是基于keyword的
        String operation = request.getParameter("operation");
        String keyword = null;
        // 如果operation不为空，说明是search表单传过来的，如果为空则是index中的其他操作
        if(StringUtil.isNotEmpty(operation) && "search".equals(operation)){
            // 说明是点击查询关键字按钮发过来的请求，此时pageNo应复原为1，keyword应该从请求参数中获取
            pageNo = 1;
            keyword = request.getParameter("keyword");
            if(StringUtil.isEmpty(keyword)){
                // 如果keyword为null，需要设置为""，否则查询时会拼接成 %null% 而不是 %%
                keyword = "";
            }
            session.setAttribute("keyword", keyword);
        }
        else {
            // 说明不是点击查询关键字按钮发过来的请求，例如点击的"上一页""下一页"
            // 此时keyword应从session作用域中获取
            String PageNoStr = request.getParameter("pageNo");
            if(StringUtil.isNotEmpty(PageNoStr)){
                pageNo = Integer.parseInt(PageNoStr);
            }
            Object keywordObj = session.getAttribute("keyword");
            if(keywordObj != null){
                keyword = (String) keywordObj;
            }
            else{
                keyword = "";
            }
        }

        // 将页码pageNo更新到session作用域中，下次翻页时index.html中的翻页button获取到的就是当前的页码了
        /*
        eg. button“下一页”获取到参数session.pageNo(当前页)，并将pageNo+1传到js函数page()中，js再跳转到IndexServlet中，
            同时将pageNo = pageNo+1传进来，那么在IndexServlet会将pageNo+1更新到session中，此时当前页就变成下一页了
        */
        session.setAttribute("pageNo", pageNo);

        // --------------------------------------- 查询数据库并渲染 ---------------------------------------
        FruitDAO fruitDAO = new FruitDAOImpl();
        // keyword为空时返回所有记录(因为查询使用的通配符 %keyword%)
        List<Fruit> fruitList = fruitDAO.getFruitList(keyword, pageNo);
        // fruitList保存到session作用域
        session.setAttribute("fruitList", fruitList);

        int fruitCount = fruitDAO.getFruitCount(keyword);
        // 5条记录为1页
        int pageCount = (fruitCount + 5 - 1) / 5;
        // 总页数pageCount保存到session作用域
        session.setAttribute("pageCount", pageCount);


        /*
        * processTemplate: thymeleaf中处理视图模板
        * 详见 readme.txt -> 七、thymeleaf模板视图技术 -> 5.根据逻辑视图名称得到物理视图名称
        */
        super.processTemplate("index", request, response);

    }

    // search提交表单使用post方法，与index整合到doGet中了
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
