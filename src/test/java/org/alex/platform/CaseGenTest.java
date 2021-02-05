package org.alex.platform;

import com.alibaba.fastjson.JSONObject;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.generator.Generator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CaseGenTest {
    @Autowired
    Generator generator;

    @Test
    public void doTest() throws BusinessException {
        String key = "name";
        String desc = "姓名";
        String type = "String";

        JSONObject publicConfig = new JSONObject();
        publicConfig.put("allowNull", false);
        publicConfig.put("allowRepeat", false);

        JSONObject privateConfig = new JSONObject();
        privateConfig.put("allowIllegal", false);
        privateConfig.put("allowEmpty", false);
        privateConfig.put("minLen", 0);
        privateConfig.put("maxLen", 10);

        System.out.println(generator.genSingleField(key, desc, type, publicConfig, privateConfig));
    }

    @Test
    public void test1(){
        test1();
    }
}
