package org.alex.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.alex.platform.config.DriverPathConfig;
import org.alex.platform.mapper.PermissionMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValueTest {
    @Autowired
    PermissionMapper mapper;

    @Test
    public void doTest() {
        System.out.println(JSON.toJSONString(mapper.selectPermissionByParentId(0), SerializerFeature.DisableCircularReferenceDetect));
    }
}
