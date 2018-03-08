package person;

/**
 * Created by ASUS on 2018/2/21.
 */

public class Person {
    private static String token;
    final private String name;
    final private int mima;


    public Person(String name,int mima,String token){
        this.name = name;
        this.mima = mima;
        this.token = token;
    }

    public String getName(){return name;}
    public int getMima(){return mima;};
    public static String getToken(){return token;};
}
