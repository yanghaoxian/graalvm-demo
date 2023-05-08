package com.example.demo;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author: yanghaoxian
 * @created: 2023/05/05
 * @description:
 */
@Component
public class Test implements ApplicationRunner {



    @Override
    public void run(ApplicationArguments args) throws Exception {
        String data = "{\"date\": \"lab\"}";
        Data jsonObject =JSONObject.parseObject(data, Data.class);
        System.out.println(jsonObject.getDate());

    }
}
