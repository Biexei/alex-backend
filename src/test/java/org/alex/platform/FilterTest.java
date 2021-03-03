package org.alex.platform;

import com.alibaba.fastjson.JSONObject;
import org.alex.platform.exception.ParseException;
import org.alex.platform.exception.ValidException;
import org.alex.platform.util.JsonUtil;
import org.alex.platform.util.ValidUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.alex.platform.util.ValidUtil;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilterTest {
    @Test
    public void testValidJsonPath() throws ValidException {
        ValidUtil.isJsonPath("$['store']['book'][0]['title']");
        ValidUtil.isJsonPath("$.store.book[0].title");
        ValidUtil.isJsonPath("123");
    }

    @Test
    public void testValidXpath() throws ValidException {
        ValidUtil.isXpath("\\text\\@title");
        ValidUtil.isXpath("\\\\123\\@title");
        ValidUtil.isXpath("123");
    }

    @Test
    public void testIsJsonObject() throws ValidException {
        //ValidUtil.isJson( "[1, 2, 3]");
        ValidUtil.isJson( "{\"a\":1}");
        ValidUtil.isJson( "123");
    }

    @Test
    public void testIsWord() throws ValidException {
        ValidUtil.isWordUnderline("asdv_");
        ValidUtil.isWordUnderline("__");
        ValidUtil.isWordUnderline("123");
    }

    @Test
    public void doTest() throws ParseException {
        HashMap map1 = new HashMap();
        map1.put("a", "1");
        HashMap map2 = new HashMap();
        map2.put("a", "a");
        map2.put("b", "b");
        map1.putAll(map2);
        System.out.println(map1);
    }
}
