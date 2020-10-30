package org.alex.platform;

import com.alibaba.fastjson.JSONObject;
import org.alex.platform.exception.ValidException;
import org.alex.platform.util.ValidUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.alex.platform.util.ValidUtil;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ValidTest {
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
}
