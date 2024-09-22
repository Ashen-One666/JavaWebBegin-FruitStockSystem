package com.atguigu.myssm.ioc;

public interface BeanFactory {
    // 根据id，获取到对象 (bean标签中, id -> class)
    Object getBean(String id);
}
