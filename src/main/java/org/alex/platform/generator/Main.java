package org.alex.platform.generator;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
public class Main {

    @Autowired
    Generator generator;

    public JSONArray generateCase(JSONObject schema) throws Exception {
        JSONObject property = schema.getJSONObject("property");
        Integer projectId = property.getInteger("projectId");
        Integer moduleId = property.getInteger("moduleId");
        String url = property.getString("url");
        String method = property.getString("desc");
        String level = property.getString("level");
        String doc = property.getString("doc");
        String headers = property.getString("headers");
        JSONArray validEquivalenceClassAsserts = property.getJSONArray("validEquivalenceClassAsserts");
        JSONArray invalidEquivalenceClassAsserts = property.getJSONArray("invalidEquivalenceClassAsserts");

        JSONArray dataSchema = schema.getJSONArray("dataSchema");
        JSONArray dataResult = new JSONArray();
        JSONArray paramsSchema = schema.getJSONArray("paramsSchema");
        JSONArray paramsResult = new JSONArray();
        JSONArray jsonSchema = schema.getJSONArray("jsonSchema");
        JSONArray jsonResult = new JSONArray();
        JSONArray caseList = new JSONArray();

        if (dataSchema != null && !dataSchema.isEmpty()) {
            for (int i = 0; i < dataSchema.size(); i++) {
                JSONObject jo = dataSchema.getJSONObject(i);
                String name = jo.getString("name");
                String desc = jo.getString("desc");
                String type = jo.getString("type");

                JSONObject globalConfig = jo.getJSONObject("globalConfig");

                JSONObject privateConfig = jo.getJSONObject("privateConfig");

                dataResult.add(generator.genSingleField(name, desc, type, globalConfig, privateConfig));
            }
        }

        // 接口总字段数
        int propSize = dataResult.size();..

        // 每个字段数，生成的条件总数总集合
        int itemSize = 1;
        for (int i = 0; i < propSize; i++) {
            itemSize *= dataResult.getJSONArray(i).size();
        }

        System.out.println(propSize); // 3
        System.out.println(itemSize); // 4*9*9

//        for (int i = 0; i < size; i++) {
//            JSONArray singleFieldArray = dataResult.getJSONArray(i);
//            JSONArray var1 = new JSONArray();
//            for (int j = 0; j < singleFieldArray.size(); j++) {
//                JSONObject fieldObject = singleFieldArray.getJSONObject(j);
//                var1.add(fieldObject);
//                String type = fieldObject.getString("type");
//                Object value = fieldObject.get("value");
//                String key = fieldObject.getString("key");
//                String desc = fieldObject.getString("desc");
//            }
//        }
        return caseList;
    }
}
