package com.atguigu.fruit.controllers;

import com.atguigu.fruit.service.FruitService;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.myssm.uil.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

// Controller只需要考虑业务操作，而不需要考虑参数获取、资源重定向这些(交给中央控制器)，这样可以更专注于业务控制

// FruitController不是一个servlet组件，而是一个控制器
public class FruitController {

    // Controller不直接调用DAO，而是调用Service方法，Service再组合调用DAO方法
    // 解耦合：fruitService = ( new fruitServiceImpl -> null )
    private FruitService fruitService = null;

    // FruitController不再是servlet组件，因此不需要重写service方法了

    // index和查询关键字都使用doGet内的逻辑
    private String index(String operation, String keyword, Integer pageNo, HttpServletRequest request) {

        // --------------------------------------- 处理传入的参数 ---------------------------------------
        HttpSession session = request.getSession();
        // 第一次请求时显示第一页
        if(pageNo == null){
            pageNo = 1;
        }

        // 无论是index还是search都采用keyword，search的keyword是输入的关键字，非search的keyword为空
        // search方法中需要保存keyword到session作用域中是因为search后也需要翻页，翻页时显示的数据是基于keyword的

        // 如果operation不为空，说明是search表单传过来的，如果为空则是index中的其他操作
        if(StringUtil.isNotEmpty(operation) && "search".equals(operation)){
            // 说明是点击查询关键字按钮发过来的请求，此时pageNo应复原为1，keyword应该从请求参数中获取
            pageNo = 1;
            if(StringUtil.isEmpty(keyword)){
                // 如果keyword为null，需要设置为""，否则查询时会拼接成 %null% 而不是 %%
                keyword = "";
            }
            session.setAttribute("keyword", keyword);
        }
        else {
            // 说明不是点击查询关键字按钮发过来的请求，例如点击的"上一页""下一页"
            // 此时keyword应从session作用域中获取
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

        // --------------------------------------- 查询数据库 ---------------------------------------

        // keyword为空时返回所有记录(因为查询使用的通配符 %keyword%)
        List<Fruit> fruitList = fruitService.getFruitList(keyword, pageNo);
        // fruitList保存到session作用域
        session.setAttribute("fruitList", fruitList);

        int pageCount = fruitService.getPageCount(keyword);
        // 5条记录为1页
        //int pageCount = (fruitCount + 5 - 1) / 5;
        // 总页数pageCount保存到session作用域
        session.setAttribute("pageCount", pageCount);


        /*
         * processTemplate: thymeleaf中处理视图模板
         * 详见 readme.txt -> 七、thymeleaf模板视图技术 -> 5.根据逻辑视图名称得到物理视图名称
         */
        //super.processTemplate("index", request, response);
        return "index";
    }

    private String add(String fname, Integer price, Integer fcount, String remark) {
        Fruit fruit = new Fruit(0, fname, price, fcount, remark);
        fruitService.addFruit(fruit);

        //response.sendRedirect("fruit.do");
        return "redirect:fruit.do";
    }

    private String del(Integer fid)  {
        if(fid!=null){
            //System.out.println("del");
            fruitService.delFruit(fid);
            //response.sendRedirect("fruit.do");
            return "redirect:fruit.do";
        }
        return "error";
    }

    private String edit(Integer fid, HttpServletRequest request) {
        //String fidStr = request.getParameter("fid");
        if(fid!=null){
            Fruit fruit = fruitService.getFruitByFid(fid);
            request.setAttribute("fruit", fruit);
            // thymeleaf渲染到edit.html页面上
            //super.processTemplate("edit", request, response);
            return "edit";
        }
        return "error";
    }

    private String update(Integer fid, String fname, Integer price, Integer fcount, String remark) {
        // 1. 设置编码 (已放到DispatcherServlet中)
        //request.setCharacterEncoding("utf-8");

        // 2. 获取参数(不需要了，从 request参数中获取 已经改为 传参进来)

        // 3. 执行更新
        fruitService.updateFruit(new Fruit(fid, fname, price, fcount, remark));

        // 4. 资源跳转：回到index页面上

        // 这句话相当于request.getRequestDispatcher("index.html").forward(request, response)，跳转到老页面上，页面数据未更新
        //super.processTemplate("index", request, response); // 这里面的"index"会添加前后缀，效果相当于"index.html"
        // 此处需要得到重定向的效果，让客户端重新给IndexServlet发请求然后覆盖到session中，这样客户端获得的index页面才是最新的
        //response.sendRedirect("fruit.do");

        // 转发重定向等跳转工作放到DispatcherServlet中，Controller组件不再关心这个问题，只需要返回字符串给DispatcherServlet
        return "redirect:fruit.do";
    }

}
