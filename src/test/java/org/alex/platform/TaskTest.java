package org.alex.platform;

import com.alibaba.fastjson.JSON;
import org.alex.platform.mapper.TaskMapper;
import org.alex.platform.util.ExceptionUtil;
import org.alex.platform.util.RestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskTest {
    @Autowired
    TaskMapper taskMapper;

    @Test
    public void testTask() {
        List<String> list = new ArrayList<>();
        list.add("123");
        list.add("2");
        list.add("3");
        String[] s = new String[list.size()];
        String[] objects = list.toArray(s);
        System.out.println(objects);
    }

    @Test
    public void testException() {
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            System.out.println(ExceptionUtil.msg(e));
        }
    }

    @Test
    public void doSingle() {
        RestTemplate restTemplate1 = RestUtil.getInstance();
        RestTemplate restTemplate2 = RestUtil.getInstance();
        RestTemplate restTemplate3 = RestUtil.getInstance();
        System.out.println(restTemplate1);
        System.out.println(restTemplate2);
        System.out.println(restTemplate3);
    }
}
