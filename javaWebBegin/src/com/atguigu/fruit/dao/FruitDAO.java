package com.atguigu.fruit.dao;

import com.atguigu.fruit.pojo.Fruit;

import java.util.List;

public interface FruitDAO {
    //获取指定页码上的指定名字(或remark)库存列表信息 , 每页显示5条
    List<Fruit> getFruitList(String keyword , Integer pageNo);

    //获取指定页码上的所有库存列表信息 , 每页显示5条
    public List<Fruit> getFruitList(Integer pageNo);

    //获取所所有库存列表信息
    List<Fruit> getFruitList();

    //根据fid获取特定的水果库存信息
    Fruit getFruitByFid(Integer fid);

    //修改指定的库存记录
    void updateFruit(Fruit fruit);

    //根据fid删除指定的库存记录
    void delFruit(Integer fid);

    //添加新库存记录
    void addFruit(Fruit fruit);

    //根据水果名查询该水果库存记录条数
    int getFruitCount(String keyword);

    //查询水果总库存记录条数
    int getFruitCount();

}
