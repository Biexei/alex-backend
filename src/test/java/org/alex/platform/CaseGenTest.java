package org.alex.platform;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.alex.platform.enums.CaseRule;
import org.alex.platform.generator.Inventory;
import org.alex.platform.generator.StaticGenerator;
import org.alex.platform.generator.Main;
import org.alex.platform.util.CommandUtil;
import org.alex.platform.util.FileUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CaseGenTest {
    @Autowired
    StaticGenerator staticGenerator;
    @Autowired
    Main main;
    @Autowired
    Inventory inventory;

    @Test
    public void doStringTest() throws Exception {
        String key = "name";
        String desc = "姓名";
        String type = "String";

        JSONObject privateConfig = new JSONObject();
        privateConfig.put("allowIllegal", false);
        privateConfig.put("allowEmpty", false);
        privateConfig.put("minLen", 2);
        privateConfig.put("maxLen", 2);
        privateConfig.put("allowNull", false);
        privateConfig.put("allowRepeat", false);

        System.out.println(inventory.genSingleField(staticGenerator, key, desc, type, privateConfig));
    }

    @Test
    public void doNumberTest() throws Exception {
        String key = "name";
        String desc = "姓名";
        String type = "Number";

        JSONObject privateConfig = new JSONObject();
        privateConfig.put("min", -0.1);
        privateConfig.put("max", 100);
        privateConfig.put("allowNull", false);
        privateConfig.put("allowRepeat", false);

        System.out.println(inventory.genSingleField(staticGenerator, key, desc, type, privateConfig));
    }

    @Test
    public void doInDbTest() throws Exception {
        String key = "name";
        String desc = "姓名";
        String type = "NotInDb";

        JSONObject privateConfig = new JSONObject();
        privateConfig.put("dbId", 1);
        privateConfig.put("sql", "select `desc` from t_db");
        privateConfig.put("elementType", "str");
        privateConfig.put("allowNull", false);
        privateConfig.put("allowRepeat", false);

        System.out.println(inventory.genSingleField(staticGenerator, key, desc, type, privateConfig));
    }

    @Test
    public void doInArrayTest() throws Exception {
        String key = "name";
        String desc = "姓名";
        String type = "notInArray";


        JSONObject privateConfig = new JSONObject();
        JSONArray array = new JSONArray();
        array.add(1);
        array.add(1);
        array.add(1);
        privateConfig.put("value", array);
        privateConfig.put("elementType", "integer");
        privateConfig.put("allowNull", false);
        privateConfig.put("allowRepeat", false);

        System.out.println(inventory.genSingleField(staticGenerator, key, desc, type, privateConfig));
    }

    @Test
    public void testMain() throws Exception {
        String jsonStr = FileUtil.readByBuffer("C:\\Users\\beix\\IdeaProjects\\platform\\src\\main\\resources\\template\\用户注册接口自动生成用例约束示例.json", StandardCharsets.UTF_8);
        JSONObject schema = JSONObject.parseObject(jsonStr);
        System.out.println(JSON.toJSONString(main.generateCase(schema, CaseRule.CARTESIAN, false, 1),
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue));
    }

    @Test
    public void test1() throws Exception {
        JSONObject object = new JSONObject();
        object.put("123", "123");
        JSONArray array = new JSONArray();
        array.add(object);
        System.out.println(array);
        object = null;
        System.out.println(array);
    }

    @Test
    public void test2() {
        String cmd = "python src\\main\\resources\\python\\ort.py";
        System.out.println(CommandUtil.exec(cmd));
    }

    @Test
    public void tt() {
        String name = "timestamp";
        String param0 = "";
        String param1 = "123";
        String[] param2 = {"123", "a"};
        System.out.println(staticGenerator.function(name));
        System.out.println(staticGenerator.function(name, param0));
        System.out.println(staticGenerator.function(name, param1));
        System.out.println(staticGenerator.function(name, param2));
    }
}
