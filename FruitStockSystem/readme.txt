一、Tomcat部署相关
    (项目放到tomcat容器的过程叫部署)
    1. project新建module
    2. project module(右上角设置图标里面) -> Modules -> 点对应的module -> 点+号 > 添加Web
    3. Edit Configurations -> 点+号 -> tomcat Server local 如果tomcat配置好系统环境变量则idea会自动识别
    4. deploy -> 点+号 -> Artifact -> war.explored -> Application context(就是context root) 可以只命名为/ 以省略
    5. URl 内容代表运行程序后访问的网页资源， http + 端口 + context root名(即第4步Application context) + 访问内容
       eg. http://localhost:8080/mainHtml.html 为运行mainHtml.html
       eg. http://localhost:8080/demo03 为发送action=demo03的请求
    6. servlet配置内容写在web/WEB-INF/web.xml中
    7. 写servlet：新建java类如xxxServlet，并加入servlet依赖
       (注意需要导入tomcat的lib：project module -> Modules -> dependencies -> 点+号 -> 2 libraries -> tomcat lib)
    8. //

二、Servlet继承关系： - 重点查看服务方法( service() )
    1.继承关系
    javax.servlet.Servlet接口
        javax.servlet.GenericServlet抽象类
            javax.servlet.http.HttpServlet抽象子类

    2.相关方法
    javax.servlet.Servlet接口：
        void init(config) - 初始化方法
        void service(request, response) - 服务方法
        void destroy() - 销毁方法

    javax.servlet.GenericServlet抽象类：
        void service(request, response) - 仍然是抽象的

    java.servlet.http.HttpServlet抽象子类：
        void service(request, response) - 不是抽象的
        - String method = req.getMethod(); 获取请求的方式
        - 各种if判断，根据请求方式不同去调用不同的do方法
        - 在httpServlet中，do方法基本差不多

小结：
    1. 继承关系：Servlet -> GenericServlet -> HttpServlet
    2. servlet核心方法： init(), service(), destroy()
    3. 服务方法： 有请求过来时，service自动响应(其实是tomcat容器调用的)
                在HttpServlet中会分析请求方式(get, post, head, delete等)
                再决定用哪个do方法
                HttpServlet中do方法默认跳405
                因此我们子类要自己实现对应的do方法，报405错误就是自己没实现请求方式对应do方法
    4. 新建servlet时，通过请求方法，决定重写哪个do方法

三、servlet生命周期
    (测试代码：Demo03Servlet  URL：http://localhost:8080/demo03)
    1. 生命周期：从出生到死亡的过程。对应servlet三个核心方法： init(), service(), destroy()
    2. 默认情况下：
        - 第一次接受请求时，该Servlet会实例化(调用构造方法)、初始化(调用init())，然后接受服务(调用service())
        - 从第二次开始，每一次都是服务(只调用service())
        - 容器关闭时，其中所有servlet都销毁，调用destroy()
    3. Servlet实例tomcat只会创建一个，所有请求都是这个实例去响应(只有第一次请求tomcat才去实例和初始化。缺点：第一次请求时间较长)
        (tomcat通过反射去实现init()的，如果重写了private的构造方法，tomcat就无法反射了，服务器会报500错误)
    4. 为了减少第一次请求时间，需规划servlet初始化时机：
        - 默认第一次接收请求时，实例化，初始化
        - 通过<load-on-startup>来设置servlet启动的先后顺序，数字越小启动越靠前，最小为0
    5. servlet在容器中：是单例的(对应第3条)，是线程不安全的
        - 单例：所有请求都是同一个实例去响应
        - 线程不安全：一个线程需要根据这个实例中某个成员变量值做逻辑判断，但是在中途某时机另一个线程改变了该成员变量的值导致出问题
        - 启发：尽量不要在servlet中定义成员变量，如果一定要定义，不要修改成员变量值，也不要根据其值做逻辑判断

四、HTTP协议
    1. Http 称之为 超文本传输协议
    2. Http是无状态的(见五)
    3. Http请求响应包含两部分：请求和响应
        - 请求：
            请求包含3个部分：1)请求行 2)请求消息头 3)请求主体
            - 请求行包含3个信息：1)请求的方式 2) 请求的URL 3)请求的协议 (一般都是http1.1)
            - 请求消息头包含了很多客户端需要告诉服务器的信息，比如：我的浏览器型号，版本，我能接受内容的类型，内容长度等
            - 请求体，3种情况：
                get方式，没有请求体，但有一个queryString
                post方式，有请求体，form data
                json格式，有请求体，request payload
        - 响应：
            响应也包含3个部分：1)响应行 2)响应头 3)响应体
            - 响应行包含3个信息：1)协议 2)响应状态码 3)响应状态
            - 响应头：包含服务器的信息，服务器发给浏览器的信息(内容的媒体类型、编码、内容长度等)
            - 响应体：响应的实际内容 (比如请求add.html页面时，响应内容是<html><head><body><form...)

    4. Http响应码
        - 200： 正常响应
        - 302： 重定向
        - 404： 找不到资源
        - 405： 请求方式不支持
        - 500： 服务器内部错误

五、会话
    1. Http是无状态的
        - 无状态：服务器无法区分两次请求是否是同一个客户端发来的
        - 无状态带来的问题：比如第一次请求是添加商品，第二次是结账，如果无法区分是否是同一用户，会导致混乱
        - 解决方案：通过会话跟踪技术(session)
            (测试代码： Demo05Servlet URL：http://localhost:8080/demo05)
    2. 会话跟踪技术
        - 客户端第一次发请求给服务器，服务器获取session，获取不到，则创建新的，然后响应给服务器
        - 下次客户端给服务器发请求时，会把sessionID带给服务器，那么服务器就能获取到了，此时就能判断是否是同一个客户端
        - 采用API：
            request.getSession() -> 获取当前的会话，没有则新建
            request.getSession(true) -> 效果和不带参数相同
            request.getSession(false) -> 获取当前的会话，没有则返回null，不会新建

            session.getId() -> 获取sessionID
            session.isNew() -> 判断session是否是新的
            session.getMaxInactiveInterval() -> session的非激活时间时长，默认1800秒
            session.invalidate() -> 让会话立即失效
            session.getLastAccessedTime() -> 获取最近一次访问时间
    3. session保存作用域
        - session保存作用域是和具体某一个session对应的
        - 常用API：
            void session.setAttribute(k, v)
            Object session.getAttribute(k)
            void session.removeAttribute(k)
        - 原始情况下，保存作用域有4个：page(页面级别，现在不用), request(一次请求响应范围),
          session(一次会话范围),application(整个应用程序范围)
              - request：一次请求响应范围
              - session：一次会话范围有效
              - application：一次应用程序范围有效
              // 注： 组件指代Servlet实例
        - 路径问题
            - 相对路径
            - 绝对路径(尽量使用绝对路径)

六、服务器内部转发以及客户端重定向
    1. 服务器内部转发：request.getRequestDispatcher("...").forward(request, response);
        - 一次请求响应的过程，客户端不知道内部有多少次转发
        - 地址栏无变化
    2. 客户端重定向：response.sendRedirect("...");
        - 两次请求响应的过程，客户端知道内部转发过程
        - 地址栏有变化

七、Thymeleaf - 视图模板技术
    1. 添加thymeleaf的jar包 (参考"数据库相关.txt")
    2. 新建一个Servlet类ViewBaseServlet
    3. 在web.xml中添加配置
        - 配置前缀：view-prefix
        - 配置后缀：view-suffix
    4. 使得我们的Servlet继承ViewBaseServlet
    5. 根据逻辑视图名称得到物理视图名称
        - Servlet调用函数： super.processTemplate("index", request, response);
        - "index": 视图名称
        - 那么thymeleaf会将这个 逻辑视图名称 对应到 物理视图名称 上去
        - 逻辑视图名称: index
        - 物理视图名称:       view-prefix + 逻辑视图名称 + view-suffix
        - 因此真实的视图名称是:       /         index         .html
        - 在web.xml设置prefix和suffix名称的意义就是将真实的视图名称与web根目录下的index.html对应上
        - 最后运行/index，会跳转到index.html上 (相当于thymeleaf帮我们做了服务器内部转发)
    6. 总体运作流程：
        客户端请求 /index -> indexServlet收到，执行doGet()，跳转到FruitDAOImpl -> FruitDAOImpl转到BaseDAO
        -> BaseDAO连接数据库，发送查询语句 -> 数据库返回数据给BaseDAO -> BaseDAO返回数据给FruitDAOImpl
        -> FruitDAOImpl向indexServlet返回数据(List<Fruit> fruitList = fruitDAO.getFruitList())，同时让
        indexServlet把数据加到会话保存域(session.setAttribute("fruitList,fruitList"))，最后调用thymeleaf的函数
        super.processTemplate("index", request, response)，将这份数据渲染到页面index.html上
    7. 使用thymeleaf标签
        - th:if , th:unless, th:each, th:text
        - 注意，如果当前页面没有结果thymeleaf渲染，则 th: 标签不会生效
          例如，edit.html是经过 super.processTemplate("edit", request, response) 跳转到的，因此在edit.html中可以使用th标签


新建项目配置步骤：
    1. Project Structure -> Module -> dependencies -> add library
    2. Project Structure -> Artifact -> add -> web application: explored
    2. Edit Configurations -> add Tomcat server: local -> 配置server和deployment





