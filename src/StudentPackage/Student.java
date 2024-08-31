package StudentPackage;

public class Student implements Person {
    public String name;
    public int age;
    private String address;
    private String school;

    public Student(){}

    public Student(String name, int age, String address, String school){
        this.name = name;
        this.age = age;
        this.address = address;
        this.school = school;
    }

    public void eat(){
        System.out.println("EAT");
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return this.age;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress(){
        return this.address;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSchool(){
        return this.school;
    }
}
