package com.yiban.java.debug;

import java.util.ArrayList;
import java.util.List;

/**
 * @author david.duan
 * @packageName com.yiban.java.debug
 * @className ArrayListResizeDebugDemo
 * @date 2025/10/21
 * @description
 * run to cursor 是让debug直接跳到光标所在的行
 * 还有一个smart step into，这个按钮的功能还需要进一步验证
 * trace current stream chain主要是观测stream中的变量
 * reset frame 是让方法重新入栈，也就是方法重新执行一次，相当于悔棋
 */
public class ArrayListResizeDebugDemo {
    public static void main(String[] args) {
        //主要是看jdk ArrayList的源码
        List<Integer> list = new ArrayList<>();
        //这里可以在断点上点击鼠标右键，然后在condition栏直接设置 i == 10,这样可以让循环直接跳到第十次
        for (int i = 1; i <= 15; i++) {
            //这里要想进入jdk自带的方法，必须使用force step into。
            //在源码中，我们还可以观测变量的值，在要观测的变量上点击鼠标右键，在弹出框中选择Add to Watches
            list.add(i);
        }

        System.out.println(list.size());
    }
}
