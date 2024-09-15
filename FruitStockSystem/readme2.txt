代码结构改进过程
1. v0.1:
    - 一个请求对应一个Servlet
    - 存在问题: Servlet太多了
2. v0.2:
    - 把一系列的请求都对应一个Servlet，即indexServlet/addServlet/EditServlet...合并为FruitServlet
      通过一个operate的值来决定调用Servlet中的哪个方法，使用的是switch-case
    - 存在问题: 随着业务规模扩大会有很多方法，导致switch-case代码块太长
3. v0.3:
    - 使用反射技术优化掉switch-case
      规定operate的值和方法一致，那么接收到的operate的值就表明需要调用对应的方法进行响应，如果找不到就抛异常
    - 存在问题: 每一个Servlet(如fruitServlet/userServlet/orderServlet等)中都存在增删改查方法，都需要写反射来获取operate
4. v0.4:
    - 每一个Servlet中都有类似的反射代码，因此继续抽取，设计了中央控制器类: DispatcherServlet
      DispatcherServlet工作分为两大部分:
        1) 根据URL定位到能够处理这个请求的controller组件:
            - 从URL中提取servletPath: /fruit.do -> fruit
            - 根据fruit找到对应的FruitController，这个对应关系存储在applicationContext.xml中
              <bean id="fruit" class="com.atguigu.fruit.controllers.FruitController/>
              再通过DOM技术解析xml配置文件的对应关系，在中央控制器中形成beanMap容器用来存放所有Controller组件
              这样收到fruit请求就能找到FruitController
            - 根据获取到的operate的值定位到FruitController中需要调用的方法
        2) 调用Controller中的方法:
            - 获取参数
                获取即将要调用的方法的参数签名信息：Parameter[] parameters = method.getParameters();
                通过parameter.getName()获取参数的名称
                准备了 Object[] parameterValues 这个数组来存放对应参数的参数值
                另外，我们需要考虑参数的类型问题，需要做类型转化的工作: 通过parameter.getType().getName()获取参数类型
            - 执行方法
                Object returnObj = method.invoke(controllerBean, parameterValues);
            - 视图处理
                String returnStr = (String) returnObj;
                if(returnStr.startWith("redirect:")){
                ...
                } else {...}