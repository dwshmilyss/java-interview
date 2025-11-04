package com.yiban.springbootdemo.test.debug;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HelloDebug {
    public static void main(String[] args) {
        helloDebug();
//        testDebugStreamChain();
    }

    /**
     * 基础调试功能
     * step into只能跳入普通方法，不能跳入jdk的方法，如果想进入jdk的方法，用force step into
     * show execution point 光标回到代码执行所在的行
     * run to cursor 代码直接运行到光标所在的行
     */
    private static void helloDebug() {
        Map<Integer,String> map = new HashMap<>();
        map.put(0,"aaa");
        map.put(1,"bbb");
        testEmb();
        map.put(2,"aaa");
        map.put(3,"bbb");
    }

    private static void testEmb() {
        System.out.println("1---");
        System.out.println("2---");
        System.out.println("3---");
        System.out.println("4---");
    }

    /**
     * 在这个方法上打断点，调试stream，主要用到 trace current stream chain
     */
    private static void testDebugStreamChain() {
        List<Integer> list = Stream.of(1, 2, 3, 4, 5).filter(x -> x>3).map(x -> x*2).collect(Collectors.toList());
    }

    // reset frame 是让入栈的方法出栈，这样可以重新运行该方法（就像下棋中的悔棋一样），在java中栈管运行，堆管存储。

    // force return  当一个方法的调用链很长的时候，有时候我们会在很早的时候就发现bug所在，这时候我们不要点stop，因为你点stop只是让debug流程停止，但是
    // 程序的运行依然会继续，如果后面的调用有写入数据库的操作，那就会产生垃圾数据，这时候就需要我们点force return。这会让程序直接返回。
    // 注意：点force return的时候会出现弹框让你输入调用方法的返回值，这时给个伪值就行了
}
