package org.alex.platform;

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
        String s1 = "";
        System.out.println(JSONObject.parseObject(s1, HashMap.class));
    }
}
