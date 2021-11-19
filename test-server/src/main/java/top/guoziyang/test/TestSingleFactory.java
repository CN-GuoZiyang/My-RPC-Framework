package top.guoziyang.test;

import top.guoziyang.rpc.entity.RpcRequest;
import top.guoziyang.rpc.factory.SingletonFactory;


/**
 * 测试单例工厂是否单例
 * @Author: Tang World
 * @Date: 19/11/2021 下午5:12
 */
public class TestSingleFactory {
    public static void main(String[] args) {
        // 验证是否identityHashCode代表能唯一代表一个对象,即不同对象,identityHashCode不相同

        // str重写了hashcode方法, 对于相同的字符串,hash值相同, 证据: 源码中附带的计算公式 ->s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
        String str1 = new String("abc");
        String str2 = new String("abc");
        System.out.println("str1 hashCode: " + str1.hashCode());
        System.out.println("str2 hashCode: " + str2.hashCode());
        System.out.println("str1 identityHashCode: " + System.identityHashCode(str1));
        System.out.println("str2 identityHashCode: " + System.identityHashCode(str2));
        System.out.println("str1==str2: " + (str1 == str2));

        // user类没有重写hashcode方法,应该获取到最初的引用内存计算的hashcode,并且hashcode和identityHashCode相同
        User user1 = new User(1,"first user");
        User user2 = new User(2,"second user");
        System.out.println("user1 hashCode: " + user1.hashCode());
        System.out.println("user2 hashCode: " + user2.hashCode());
        System.out.println("user1 identityHashCode: " + System.identityHashCode(user1));
        System.out.println("user2 identityHashCode: " + System.identityHashCode(user2));
        System.out.println("user1==user2: " + (user1 == user2));

        // 验证singletonFactory是否为单例模式
        for (int i = 0;i<5;i++) {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    RpcRequest instance = SingletonFactory.getInstance(RpcRequest.class);
                    // 如果为单例模式,则这里打印的code都相同
                    System.out.println(System.identityHashCode(instance));
                }
            }).start();
        }
    }
}


class User{
    int id;
    String name;

    public User(int id,String name){
        this.id = id;
        this.name = name;
    }
}