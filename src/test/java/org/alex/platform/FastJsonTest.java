package org.alex.platform;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastJsonTest {
    @Test
    public void doTest(){
        String s = "{\"name\": \"123\"}";
        String s2 = "";
        System.out.println(JSONObject.parseObject(s2, HashMap.class));
    }
}
