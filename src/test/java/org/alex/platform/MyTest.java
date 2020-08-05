package org.alex.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
        HashMap<String, String> json = new HashMap();
        json.put("name", "json");
        json.put("domain", "json");

        HashMap<String, String> data = new HashMap();
        json.put("name", "data");
        json.put("domain", "data");

        HashMap<String, String> headers = new HashMap();
        headers.put("name", "headers");
        headers.put("domain", "headers");

        HashMap<String, String> params = new HashMap();
        params.put("name", "params");
        params.put("domain", "params");

        System.out.println(RestUtil.post("http://localhost:7777/project/save", headers, params, data).getBody());
    }
}
