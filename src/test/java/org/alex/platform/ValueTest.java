package org.alex.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.alex.platform.core.parser.Parser;
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
    @Autowired
    Parser parser;

    @Test
    public void doTest() {
        //System.out.println(JSON.toJSONString(mapper.selectPermissionByParentId(0), SerializerFeature.DisableCircularReferenceDetect));
        String test1 = "${name}, ${timestamp()} ${subString(\"123\")} $next[1]} ${last[1]} #{haha} ${last(\"#{token}\")}";
        System.out.println(parser.extractDependencyName(test1));
        System.out.println(parser.extractProcessorName(test1));
    }

    public String function(String name, String... params) {
        if (params.length == 0) {
            return String.format("${%s()}", name);
        }
        StringBuilder p = new StringBuilder();
        for (String param : params) {
            p.append("\"").append(param).append("\", ");
        }
        p = new StringBuilder(p.substring(0, p.length() - 2));
        return String.format("${%s(%s)}", name, p.toString());
    }
}
