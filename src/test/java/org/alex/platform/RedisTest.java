package org.alex.platform;

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
        System.out.println(redisUtil.get("49a232b7-1a26-460c-b710-284a11b4aae11"));
    }
}
