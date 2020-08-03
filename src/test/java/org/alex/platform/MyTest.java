package org.alex.platform;

import com.alibaba.fastjson.JSON;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.service.ProjectService;
import org.alex.platform.util.RestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {
    @Autowired
    ProjectService ps;

    @Test
    public void doTest() throws BusinessException {
        HashMap map = new HashMap();
        map.put("name", "项目名称3");
        map.put("domain", "http://www.baidu.com/");
        System.out.println(RestUtil.postJson("http://localhost:7777/project/save", null, map).getBody());
    }
}
