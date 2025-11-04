package com.yiban.java.junit.auto_test_frame;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author david.duan
 * @packageName com.yiban.java.junit.auto_test_frame
 * @className AutoTestFrameClient
 * @date 2025/10/22
 * @description
 */
@Slf4j
public class AutoTestFrameClient {
    public static void main(String[] args) {
        CalcDemo calcDemo = new CalcDemo();
        int para1 = 10;
        int para2 = 0;

        Method[] methods = calcDemo.getClass().getMethods();
        AtomicInteger bugCount = new AtomicInteger();

        //要写入的文件路径，如果不存在，则自动创建
        String filePath = "BugReport" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".txt";

        for (int i = 0; i < methods.length; i++) {
            if (methods[i].isAnnotationPresent(MyAutoFrameTest.class)) {
                try {
                    methods[i].invoke(calcDemo, para1, para2);
                } catch (Exception e) {
                    bugCount.getAndIncrement();
                    log.info("异常名称:{},异常原因:{}", e.getCause().getClass().getSimpleName(), e.getCause().getMessage());

                    FileUtil.writeString(methods[i].getName() + "\t" + "出现了异常" + "\n", filePath, "UTF-8");
                    FileUtil.appendString("异常名称:" + e.getCause().getClass().getSimpleName() + "\n", filePath, "UTF-8");
                    FileUtil.appendString("异常原因:" + e.getCause().getMessage() + "\n", filePath, "UTF-8");
                }finally {
                    FileUtil.appendString("异常数:" + bugCount.get() + "\n", filePath, "UTF-8");
                }
            }
        }
    }
}
