1. servlet保存作用域
    原始情况下，保存作用域有4个：page(页面级别，现在不用), request(一次请求响应范围), session(一次会话范围),
                            application(整个应用程序范围)
    // 组件：指代Servlet实例
    - request：一次请求响应范围
    - session：一次会话范围有效
    - application：一次应用程序范围有效

2. 路径问题
    - 相对路径
    - 绝对路径(尽量使用绝对路径)