import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.dao.impl.FruitDAOImpl;
import com.atguigu.fruit.pojo.Fruit;

public class test {
    public static void main(String[] args) {
        FruitDAO fruitDAO = new FruitDAOImpl();
        fruitDAO.addFruit(new Fruit(0, "苹果", 10, 5, "hello"));
    }
}
