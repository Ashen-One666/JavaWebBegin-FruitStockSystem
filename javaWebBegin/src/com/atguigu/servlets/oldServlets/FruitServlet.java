package com.atguigu.servlets.oldServlets;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.myssm.myspringmvc.ViewBaseServlet;
import com.atguigu.myssm.uil.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

//@WebServlet("/fruit.do")
public class FruitServlet extends ViewBaseServlet {

    FruitDAO fruitDAO = new FruitDAOImpl();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        request.getParameter("operate");
        String operate = request.getParameter("operate");
        // 初始化默认是index
        if(StringUtil.isEmpty(operate)){
            operate = "index";
        }

        // 通过反射技术优化代码，这样就不需要写多个switch分支对operate进行判断
        // 获取当前类中所有方法
        Method[] methods = this.getClass().getDeclaredMethods();
        for(Method method : methods){
            // 获取方法名称
            String methodName = method.getName();
            if(operate.equals(methodName)){
                try {
                    // 找到和operate同名的方法，那么通过反射技术调用它
                    method.invoke(this, request, response);
                    return;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // 如果operate的值都不符合就抛出异常
        throw new RuntimeException("operate值非法！");

        // 通过反射优化掉switch
        /*
        switch (operate){
            case "index":
                index(request, response);
                break;
            case "add":
                add(request, response);
                break;
            case "del":
                del(request, response);
                break;
            case "edit":
                edit(request, response);
                break;
            case "update":
                update(request, response);
                break;
            default:
                throw new RuntimeException("operate值非法！");
        }
        */
    }

    // index和查询关键字都使用doGet内的逻辑
    private void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        String fname = request.getParameter("fname");
        int price = Integer.parseInt(request.getParameter("price"));
        int fcount = Integer.parseInt(request.getParameter("fcount"));
        String remark = request.getParameter("remark");

        int nowId = fruitDAO.getFruitCount();
        System.out.println(nowId);
        Fruit fruit = new Fruit(nowId, fname, price, fcount, remark);
        fruitDAO.addFruit(fruit);

        response.sendRedirect("fruit.do");
    }

    private void del(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fidStr = request.getParameter("fid");
        if(StringUtil.isNotEmpty(fidStr)){
            //System.out.println("del");
            int fid = Integer.parseInt(request.getParameter("fid"));
            fruitDAO.delFruit(fid);
            response.sendRedirect("fruit.do");
        }
    }

    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fidStr = request.getParameter("fid");
        if(StringUtil.isNotEmpty(fidStr)){
            int fid = Integer.parseInt(fidStr);
            Fruit fruit = fruitDAO.getFruitByFid(fid);
            request.setAttribute("fruit", fruit);
            // thymeleaf渲染到edit.html页面上
            super.processTemplate("edit", request, response);
        }
    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        response.sendRedirect("fruit.do");
    }

}
