package com.atguigu.fruit.service.impl;

import com.atguigu.fruit.service.FruitService;
import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.myssm.basedao.ConnUtil;

import java.util.List;

// 业务层BO就是对DAO中的方法进行组合利用，并包装成一个类
// Controller调用Service(BO层)中的方法实现某业务需求(如登录)，Service方法组合调用多个DAO中的方法，DAO再访问数据库
// 只是这个项目业务比较简单，每个方法只调用了一个DAO方法，实际项目中是多个方法的组合
public class FruitServiceImpl implements FruitService {
    // 解耦合： = null
    // 不会报空指针，因为applicationContext.xml已经配置了fruitService中 private属性fruitDAO 和 fruitDAO类的对应关系
    // 然后BeanFactory的实现类ClassPathXmlApplicationContext通过DOM解析xml，依靠反射完成了上述配置
    private FruitDAO fruitDAO = null;

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNo) {
        //System.out.println("getFruitList -> " + ConnUtil.getConn());
        return fruitDAO.getFruitList(keyword, pageNo);
    }

    @Override
    public void addFruit(Fruit fruit) {
        fruitDAO.addFruit(fruit);
    }

    @Override
    public Fruit getFruitByFid(Integer fid) {
        return fruitDAO.getFruitByFid(fid);
    }

    @Override
    public void delFruit(Integer fid) {
        fruitDAO.delFruit(fid);
    }

    @Override
    public Integer getPageCount(String keyword) {
        //System.out.println("getPageCount -> " + ConnUtil.getConn());
        int count = fruitDAO.getFruitCount(keyword);
        // 5条记录为1页
        int pageCount = (count + 5 - 1) / 5;
        return pageCount;
    }

    @Override
    public void updateFruit(Fruit fruit) {
        fruitDAO.updateFruit(fruit);
    }
}
