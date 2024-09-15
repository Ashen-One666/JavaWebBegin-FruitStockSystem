package com.atguigu.fruit.biz.impl;

import com.atguigu.fruit.biz.FruitService;
import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.fruit.pojo.Fruit;

import java.util.Collections;
import java.util.List;

// 业务层BO就是对DAO中的方法进行组合利用，并包装成一个类
// Controller调用Service(BO层)中的方法实现某业务需求(如登录)，Service方法组合调用多个DAO中的方法，DAO再访问数据库
// 只是这个项目业务比较简单，每个方法只调用了一个DAO方法，实际项目中是多个方法的组合
public class FruitServiceImpl implements FruitService {
    private FruitDAO fruitDAO = new FruitDAOImpl();

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNo) {
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
