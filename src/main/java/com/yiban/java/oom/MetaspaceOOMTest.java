package com.yiban.java.oom;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author david.duan
 * @packageName com.yiban.java.oom
 * @className MetaspaceOOMTest
 * @date 2025/10/10
 * @description
 * java8之后 Metaspace不在堆中了，用的是直接内存。
 * 里面存储：
 * 1. 虚拟机加载的类的信息
 * 2. 运行时常量池（字面量、符号引用 → 对象引用）
 * 3. 静态变量
 * 4. JIT 编译产生的本地代码元数据
 * 5. 注解、反射数据
 * 6. 方法元数据、字节码信息
 */
public class MetaspaceOOMTest {

    static class OOMTest{}
    //-XX:MetaspaceSize=10m -XX:MaxMetaspaceSize=10m
    public static void main(String[] args) {
        int i = 0;
        try {
            while (true) {
                i++;
                Enhancer enhancer = new Enhancer();
                //Enhancer.create() 的确会在运行时生成一个子类（字节码），加载进 JVM。
                //	•	但是！ 如果你传入相同的参数（同一个父类、同一个 Callback 策略），CGLIB 内部有缓存机制（默认开启）。
                //	•	所以实际上 并没有不停地产生新类，而是反复使用已经生成过的代理类。
                //	•	这样一来，Metaspace 占用几乎不增长，你就看不到 OOM。
                enhancer.setUseCache(false); //一定要加上这行代码，不然不会撑爆元空间
                enhancer.setSuperclass(OOMTest.class);
                enhancer.setCallback(new MethodInterceptor() {
                    @Override
                    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                        return proxy.invokeSuper(obj, args);
                    }
                });
                enhancer.create();
            }
        } catch (Throwable e) {
            System.out.println("**********多少次后发生了错误 : " + i);
            e.printStackTrace();
        }
    }
}
