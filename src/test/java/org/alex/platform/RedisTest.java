package org.alex.platform;

import com.alibaba.fastjson.JSONObject;
import org.alex.platform.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    RedisUtil redisUtil;

    @Test
    public void testSet() {
        redisUtil.del("tempPostProcessor");
    }
}
