package com.yiban.aop.interface_analyze;

import java.lang.annotation.*;

@Target(ElementType.METHOD) //作用在方法上
@Retention(RetentionPolicy.RUNTIME) //运行时生效
public @interface MethodExporter {
}
