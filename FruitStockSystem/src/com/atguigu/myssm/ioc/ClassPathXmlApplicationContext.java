package com.atguigu.myssm.ioc;

import com.atguigu.myssm.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * IOC容器负责对象的创建(区别于程序员new对象,控制反转)以及对象属性的初始化(属性注入)
 * 对象的创建 - 作用 : Servlet解析URL后得到组件名(如fruit), IOC就能给出叫做fruit的FruitController的实例对象
 * 对象属性的初始化 - 作用 : 如果组件Controller依赖组件Service, IOC能将Controller的一个属性指向Service实例对象
 *                       (不需要在Controller中new，避免耦合)
 */

public class ClassPathXmlApplicationContext implements BeanFactory {

    private Map<String, Object> beanMap = new HashMap<>();
    private String path = "applicationContext.xml";// 默认值

    // 无参构造调用有参构造，即IOC容器的配置文件默认为applicationContext.xml
    public ClassPathXmlApplicationContext(){
        this("applicationContext.xml");
    }

    // 调用有参构造可以指定IOC容器的配置文件
    public ClassPathXmlApplicationContext(String path) {
        if(StringUtil.isEmpty(path)){
            throw new NullPointerException("IOC容器的配置文件没有指定");
        }
        try{
            // DOM解析技术: 这一过程是通过document对象解析xml配置文件文件信息 (可以类比为 用文件指针解析文件信息)
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            // 1. 创建DocumentBuilderFactory对象
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            // 2. 创建DocumentBuilder对象，DocumentBuilder可以将xml文件解析为一个Document对象，然后就可以通过这个对象获取数据了
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            // 3. 创建Document对象
            Document document = documentBuilder.parse(inputStream);
            // 4. 获取所有bean节点, 并将bean的id和实例对象放到beanMap容器中 (实现组件名和组件实例的对应)
            NodeList beanNodeList = document.getElementsByTagName("bean");
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    // 强转是为了调用API，好比new了子类实例并赋给父类对象，但想用子类方法，则需要强转为子类
                    // Element类中有getAttribute方法
                    Element beanElement = (Element) beanNode;
                    // 这样就获取到了xml文件中的id和class属性
                    String beanId = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    // beanId需要对应的是它的实例对象，而不是实例名className，因此通过反射来获取
                    Class BeanClass = Class.forName(className);
                    // 创建bean实例
                    Object beanObj = BeanClass.newInstance();
                    // 将bean实例对象保存到Map容器中
                    beanMap.put(beanId, beanObj);
                    // 需要注意的是，代码到此处 bean和bean之间的依赖关系还未设置
                }
            }
            // 5. 组装bean之间的依赖关系 (property) (实现组件和组件的依赖)
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    // Text是文本节点，Element是元素节点
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    // 如果当前的beanElement有property节点，就需要组装
                    NodeList beanChildNodeList = beanElement.getChildNodes();
                    for (int j = 0; j < beanChildNodeList.getLength(); j++) {
                        Node beanChildNode = beanChildNodeList.item(j);
                        if (beanChildNode.getNodeType() == Node.ELEMENT_NODE && "property".equals(beanChildNode.getNodeName())) {
                            Element propertyElement = (Element) beanChildNode;
                            String propertyName = propertyElement.getAttribute("name");
                            String propertyRef = propertyElement.getAttribute("ref");
                            // 1) 找到propertyRef对应的实例
                            Object refObj = beanMap.get(propertyRef);
                            // 2) 将refObj设置到当前bean对应的实例的property属性上去 (反射实现)
                            Object beanObj = beanMap.get(beanId);
                            Class<?> beanClazz = beanObj.getClass();
                            Field propertyField = beanClazz.getDeclaredField(propertyName);
                            propertyField.setAccessible(true);
                            // 例如Controller组件依赖Service组件，
                            // 则将Controller中的service(这个service指Controller的一个private属性)赋值指向Service实例对象
                            propertyField.set(beanObj, refObj); // 实现依赖注入 (反射)
                        }
                    }
                }
            }

        } catch (ParserConfigurationException e){
            e.printStackTrace();
        } catch (IOException | SAXException | ClassNotFoundException | InstantiationException | IllegalAccessException |
                 NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getBean(String id) {
        return beanMap.get(id);
    }
}
