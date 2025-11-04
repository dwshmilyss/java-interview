package com.yiban.java.debug;

/**
 * @author david.duan
 * @packageName com.yiban.java.debug
 * @className DebugDemo
 * @date 2025/10/21
 * @description
 */
public class DebugDemo {
    public static void main(String[] args) {
        //3.5 测试force return的作用,如果方法中的调用链比较长，如果我们已经找到异常的地方，不想让方法全部执行完，这时候我们应该用force return按钮，而不是直接点结束
        // 因为我们即使点了结束，结束的也只是debug操作，程序并没有终止，这样即使我们在代码中发现了异常，那么异常后面的方法还是会被调用，如果这些方法中涉及到操作数据库的
        // 就会产生垃圾数据。
        // 注意：在点击force return的时候，会让我们输入线程结束的返回值(Boolean)，随便输入 false或者true都可以
        forceReturnAndStop();

        //4.2 Field Breadpoint(眼睛图标) 在某个变量上打断点，所有涉及到该变量的地方会自动带有断点
        /*setFieldVal();*/

        //4.3 Method Breakpoint(菱形图标)
        //4.3.1 在接口的方法上打断点，这样所有实现了该方法的地方都会自动带有断点
/*        Phone phone = new Phone();
        phone.insert();
        Computer computer = new Computer();
        computer.insert();*/

        //4.4 Exception Breakpoint(闪电图标)
        //第一步：在debug界面点击View Breakpoints，按照异常类型新增一个Java Exception Breakpoint(闪电图标)，比如NullPointException
        //第二步：异常堆栈stack trace这里打上勾
        //第三步：运行代码，不需要提前打断点，程序会自动提示报异常的代码行数
/*        Integer i = null;
        System.out.println(i.intValue());*/
    }

    private static int x;
    public static void setFieldVal() {
        x = 55;
        setFieldVal1();
    }

    private static void setFieldVal1() {
        x = 77;
    }

    private static void forceReturnAndStop() {
        System.out.println("1.执行MySQL相关操作");
        baseEmbed();
        System.out.println("3.执行Redis相关操作");
        System.out.println("4.执行kafka相关操作");
        System.out.println("5.执行hudi相关操作");
    }

    //
    private static void baseEmbed() {
        System.out.println("embed=1");
        System.out.println("embed=2");
        System.out.println("embed=3");
        System.out.println("embed=4");
    }

}
interface USB{
    void insert();
}

class Phone implements USB {

    @Override
    public void insert() {
        System.out.println("手机支持USB");
    }
}

class Computer implements USB {

    @Override
    public void insert() {
        System.out.println("电脑支持USB");
    }
}
