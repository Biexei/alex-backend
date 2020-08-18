package org.alex.platform;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.service.InterfaceCaseService;
import org.alex.platform.util.RestUtil;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewRestTest {
    @Autowired
    InterfaceCaseService caseService;

    @Test
    public void doTest() throws BusinessException {
        String url = "http://haha/{params1}";
        HashMap<String, String> headers = new HashMap();
        headers.put("headers1", "headers1");
        headers.put("headers2", "headers2");
        HashMap<String, String> params = new HashMap<>();
        //params.put("params1", "params1");
        //params.put("params2", "params2");
        HashMap<String, String> data = new HashMap<>();
        data.put("data1", "data1");
        data.put("data2", "data2");
        String json = "{\"json1\": 1}";
        System.out.println(RestUtil.header(RestUtil.get("http://www.meidekan.com/", null, null)));
    }
}
