package Test;
import java.lang.reflect.*;

/**
 * 通过反射实现对Student类的反编译
 */

public class TestReflect {
    public static void main(String[] args) throws Exception {
        // 获取Student类的Class对象
        // 在main函数后面throws异常就不用写try了
        Class cls = Class.forName("StudentPackage.Student");

        // 获取包名
        Package packageName = cls.getPackage();
        System.out.println("包名为: " + packageName);

        // 获取权限修饰符
        int modifiers = cls.getModifiers();
        //把int类型的权限修饰符转换为String类型
        String modifier = Modifier.toString(modifiers);
        System.out.print(modifier);

        // 获取类名
        // 注: getName()是返回全称(带包名), 如"StudentPackage.Student"
        String className = cls.getSimpleName();
        System.out.print(" class " + className);

        // 获取继承的父类
        String parentClassName = cls.getSuperclass().getSimpleName();
        System.out.print(" extends " + parentClassName);

        // 获取实现的接口
        System.out.print(" implements ");
        Class[] interfaces = cls.getInterfaces();
        for(int i = 0; i < interfaces.length; i++){
            System.out.print(interfaces[i].getSimpleName());
            if(i<interfaces.length-1){
                System.out.print(", ");
            }
        }
        System.out.println(" { ");

        System.out.println("=========获取成员变量==========");
        // 获取全部成员变量
        Field[] fields = cls.getDeclaredFields();
        for (int i = 0; i < fields.length; i++){
            // 权限修饰符
            String fieldModifier =Modifier.toString(fields[i].getModifiers());
            System.out.print("\t" + fieldModifier + " ");
            // 限定名
            String fieldName = fields[i].getType().getName();
            System.out.print(fieldName + " ");
            // 自定义名
            String name=fields[i].getName();
            System.out.println(name + " ;");
        }

        System.out.println("=========获取构造器==========");
        Constructor[] constructors = cls.getDeclaredConstructors();
        for (int i = 0; i < constructors.length; i++) {
            // 权限修饰符
            String constructorModifier = Modifier.toString(constructors[i].getModifiers());
            System.out.print("\t" + constructorModifier + " ");
            // 限定名
            // 在Constructor中无法直接获取构造器的限定名，但是构造器的限定名和类名一样，所以可以直接使用类名
            System.out.print(className + "(");
            // 构造器的所有参数
            Class[] prams = constructors[i].getParameterTypes();
            for (int j = 0; j <prams.length ; j++) {
                String pramName = prams[j].getSimpleName();
                System.out.print(pramName + " args" + j);
                if(j <prams.length - 1){
                    System.out.print(" , ");
                }
            }
            System.out.print(")");
            System.out.println("{}");
        }

        System.out.println("=========获取成员方法==========");
        Method[] methods = cls.getDeclaredMethods();
        for (int i = 0; i < methods.length ; i++) {
            // 权限修饰符
            String methodModifier = Modifier.toString(methods[i].getModifiers());
            System.out.print("\t" + methodModifier + " ");
            // 限定名
            String methodSimpleName = methods[i].getReturnType().getSimpleName();
            System.out.print(methodSimpleName + " ");
            // 方法名
            String methodName=methods[i].getName();
            System.out.print(methodName);
            System.out.print("(");
            // 获取所有参数
            Class[] prams=methods[i].getParameterTypes();
            for (int j = 0; j <prams.length ; j++) {
                String pramName=prams[j].getSimpleName();
                System.out.print(pramName+" args"+j);
                if(j <prams.length-1){
                    System.out.print(" , ");
                }
            }
            System.out.print(")");
            System.out.println("{}");
        }
        System.out.println("}");
    }

}
