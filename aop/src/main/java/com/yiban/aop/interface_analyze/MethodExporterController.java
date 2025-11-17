package com.yiban.aop.interface_analyze;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author david.duan
 * @packageName com.yiban.aop.interface_analyze
 * @className MethodExporterController
 * @date 2025/11/17
 * @description
 */
@RestController
@Slf4j
public class MethodExporterController {
    //http://localhost:8081/method/list?page=1&rows=10
    @GetMapping(value = "/method/list")
    @MethodExporter
    public Map list(@RequestParam(value = "page",defaultValue = "1") int page,
                    @RequestParam(value = "rows",defaultValue = "10") int rows) {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("code", "200");
        result.put("msg", "success");
        try {
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    //http://localhost:8081/method/get
    @GetMapping(value = "/method/get")
    @MethodExporter
    public Map get() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("code", "404");
        result.put("msg", "not found");
        try {
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    //http://localhost:8081/method/update
    @GetMapping(value = "/method/update")
    public String update() {
        System.out.println("update method without @MethodExporter");
        return "ok update";
    }
}
