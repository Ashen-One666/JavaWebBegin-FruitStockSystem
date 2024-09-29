package com.atguigu.myssm.listeners;

import com.atguigu.myssm.ioc.BeanFactory;
import com.atguigu.myssm.ioc.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

// 监听上下文启动，在上下文启动时创建IOC容器，将其保存到application作用域，中央控制器在application作用域获取容器
@WebListener
public class ContextLoaderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // application启动时就创建bean工厂

        // 1. 获取ServletContext对象
        ServletContext application = sce.getServletContext();
        // 2. 获取上下文的初始化参数
        String path = application.getInitParameter("contextConfigLocation"); // 在web.xml中配置IOC配置文件路径
        // 3. 创建IOC容器
        BeanFactory beanFactory = new ClassPathXmlApplicationContext(path); // 指定IOC容器配置文件
        // 4. 将IOC容器保存到application作用域
        application.setAttribute("beanFactory", beanFactory);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
