package com.atguigu.myssm.myspringmvc;

import com.atguigu.myssm.uil.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * DispatcherServlet是中央控制器，是真正的servlet，处理所有.do请求
 * 假设request的URL是： http://localhost:8080/fruit.do?operate=edit&fid=2
 * 1. 定位Controller：
 *      会从request中servletPath里面得到fruit.do，通过在xml配置文件里bean标签描述将fruit和fruitController对应关系
 *      再通过Document解析xml配置文件，将配置文件中每一对bean封装成beanMap中一个一个的对象
 *      这样当收到fruit请求后根据对应关系就能找到fruitController，此时就会去调用fruitController中的方法
 * 2. 调用Controller中的方法：
 *      调用fruitController的方法的过程是通过反射(method.invoke)实现的
 *      再将request中的参数operate=edit传入到该方法中，这样就能正确使用Controller里 edit方法了
 */

// 此处不需要加/，表示拦截所有以.do结尾的请求
@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet{

    // 框架就是： 注解 + 反射

    // 使用中央控制器DispatcherServlet的好处是不需要在每个controller中都写反射的代码，而是把这部分代码统一抽取到这里

    // 把xml配置文件bean标签所有实例对象全部加载，保存在Map对象中
    private Map<String, Object> beaneMap = new HashMap<>();

    // 在Servlet初始化函数中，解析xml配置文件
    public DispatcherServlet() {

    }

    public void init() throws ServletException {
        super.init();
        // 这一过程是通过document对象解析xml配置文件文件信息 (可以类比为 用文件指针解析文件信息)
        try{
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
            // 1. 创建DocumentBuilderFactory对象
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            // 2. 创建DocumentBuilder对象，DocumentBuilder可以将xml文件解析为一个Document对象，然后就可以通过这个对象获取数据了
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            // 3. 创建Document对象
            Document document = documentBuilder.parse(inputStream);
            // 4. 获取所有bean节点
            NodeList beanNodeList = document.getElementsByTagName("bean");
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    // 强转是为了调用API，好比new了子类实例并赋给父类对象，但想用子类方法，则需要强转为子类
                    // Element类中有getAttribute方法
                    Element beanElement = (Element) beanNode;
                    // 这样就获取到了xml文件中的id和class属性
                    String beanId = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    // beanId需要对应的是它的实例对象，而不是实例名className，因此通过反射来获取
                    Class controllerBeanClass = Class.forName(className);
                    Object beanObj = controllerBeanClass.newInstance();

                    // 通过Map来实现对应关系
                    beaneMap.put(beanId, beanObj);
                }
            }

        } catch (ParserConfigurationException e){
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        System.out.println("中央控制器初始化...");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置编码
        request.setCharacterEncoding("utf-8");

        /*
        * 假设URL： http://localhost:8080/fruit.do
        * 那么servletPath是： /fruit.do
        * 思路是：
        *   第一步： /fruit.do -> fruit (发送的/fruit.do转换成fruit)
        *   第二步： fruit -> fruitController (将fruit和fruitController对应上，如何对应 见上面的实例化方法)
        * */
        String servletPath = request.getServletPath();
        //System.out.println(servletPath);
        servletPath = servletPath.substring(1); // 去掉/
        int lastDotIndex = servletPath.lastIndexOf(".do");
        servletPath = servletPath.substring(0, lastDotIndex); // 去掉.do
        //System.out.println("servletPath = " + servletPath);

        Object controllerBeanObj = beaneMap.get(servletPath);


        request.getParameter("operate");
        String operate = request.getParameter("operate");
        // 初始化默认是index
        if(StringUtil.isEmpty(operate)){
            operate = "index";
        }

        // 通过反射技术优化代码，这样就不需要写多个switch分支对operate进行判断
        // 使用 getDeclaredMethod 获取指定operate的方法
        try {
            // 获取到/*.do中*对应的控制器，并根据operate调用该控制器中的方法(反射实现)
            // 如请求是/fruit.do，则bean标签的id=fruit，中央控制器就会去调用该id对应的class(FruitController)中的方法
            Method method = controllerBeanObj.getClass().getDeclaredMethod(operate, HttpServletRequest.class);
            if(method != null){

                // 2. Controller组件中的方法调用
                // controller中每个operate的方法都是private的，需要暴力反射
                method.setAccessible(true);
                // 调用Controller中的方法(根据operate获取的method)
                Object methodReturnObj = method.invoke(controllerBeanObj, request);

                // 3. 视图处理
                String methodReturnStr = methodReturnObj.toString();
                // 如果Controller跳转是进行重定向
                if(methodReturnStr.startsWith("redirect:")){ // 比如: redirect:fruit.do
                    String redirectStr = methodReturnStr.substring("redirect:".length());
                    response.sendRedirect(redirectStr);
                }
                // 如果Controller跳转是进行thymeleaf渲染
                else{ // 比如: "edit"
                    super.processTemplate(methodReturnStr, request, response);
                }

            }
            else{
                throw new RuntimeException("operate值非法！");
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        /*
        // 使用 getDeclaredMethods 获取所有方法，再通过for循环匹配当前方法名是否是operate
        // for循环的方式写起来相较于上面的写法较为繁琐，因此不用
        // 获取当前类中所有方法
        Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
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
        */

    }



}
