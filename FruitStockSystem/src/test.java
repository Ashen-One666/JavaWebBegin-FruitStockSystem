import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.fruit.pojo.Fruit;

public class test {
    public static void main(String[] args) {
        FruitDAO fruitDAO = new FruitDAOImpl();
        //fruitDAO.addFruit(new Fruit(0, "苹果", 5, 5, "记录0"));
        //fruitDAO.addFruit(new Fruit(1, "梨子", 10, 10, "记录1"));
        //fruitDAO.addFruit(new Fruit(2, "香蕉", 20, 50, "记录2"));
        //fruitDAO.delFruit(100);
        //int cnt = fruitDAO.getFruitCount("苹果");

        //System.out.println("苹果记录条数：" + cnt);
        fruitDAO.addFruit(new Fruit(0, "西瓜", 5, 10, "我是西瓜"));
        int cntSum = fruitDAO.getFruitCount();
        System.out.println("总记录条数：" + cntSum);
    }
}
