package org.alex.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastJsonTest {
    @Test
    public void doTest(){
        String s1 = "123";
        String s2 = new String("123");
        JSONObject object1 = JSONArray.parseObject(s1);
        JSONObject object2 = JSONArray.parseObject(s2);
        System.out.println(object1);
        System.out.println(object2);
        System.out.println(object1==object2);
    }
}
