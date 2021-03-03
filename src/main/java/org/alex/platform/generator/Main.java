package org.alex.platform.generator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.alex.platform.enums.CaseRule;
import org.alex.platform.util.NoUtil;
import org.alex.platform.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;


@Component
public class Main {

    @Autowired
    Generator generator;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    Rule rule;

    @SuppressWarnings({"unchecked","rawtypes"})
    public JSONArray generateCase(JSONObject schemaFileObject, CaseRule caseRule) throws Exception {
        // 读取配置文件
        JSONObject property = schemaFileObject.getJSONObject("property");
        Integer projectId = property.getInteger("projectId");
        Integer moduleId = property.getInteger("moduleId");
        String url = property.getString("url");
        String caseDesc = property.getString("desc");
        String method = property.getString("method");
        String doc = property.getString("doc");
        String headers = property.getString("headers");
        String schemaType = property.getString("schemaType");
        JSONObject validEquivalenceClass = property.getJSONObject("validEquivalenceClass");
        JSONObject invalidEquivalenceClass = property.getJSONObject("invalidEquivalenceClass");
        JSONArray validEquivalenceClassAssert = validEquivalenceClass.getJSONArray("asserts");
        JSONArray invalidEquivalenceClassAssert = invalidEquivalenceClass.getJSONArray("asserts");
        String validEquivalenceClassCaseLevel = validEquivalenceClass.getString("caseLevel");
        String invalidEquivalenceClassCaseLevel = invalidEquivalenceClass.getString("caseLevel");
        JSONArray schema = schemaFileObject.getJSONArray("schema");

        JSONArray itemList = new JSONArray();
        JSONArray caseList;
        // 读取schema为每个属性生成条件
        if (schema != null && !schema.isEmpty()) {
            for (int i = 0; i < schema.size(); i++) {
                JSONObject jo = schema.getJSONObject(i);
                String name = jo.getString("name");
                String desc = jo.getString("desc");
                String type = jo.getString("type");
                JSONObject globalConfig = jo.getJSONObject("globalConfig");
                JSONObject privateConfig = jo.getJSONObject("privateConfig");
                itemList.add(generator.genSingleField(name, desc, type, globalConfig, privateConfig));
            }
        }

        // 确定用例生成规则
        if (caseRule == CaseRule.ORT) { // 正交法
            String key = NoUtil.genPyOrtNo();
            caseList = rule.ort(key, itemList);
        } else { // 笛卡尔积
            // 接口总字段数
            int propSize = itemList.size();
            // 每个字段数，生成的条件总数总集合
            ArrayList<Integer> itemSizeList = new ArrayList<>();
            for (int i = 0; i < propSize; i++) {
                int size = itemList.getJSONArray(i).size();
                itemSizeList.add(size);
            }
            caseList = rule.cartesian(itemSizeList, itemList, new JSONArray());
        }

        // 封装成导入用例的格式
        JSONArray result = new JSONArray();
        for (int i = 0; i < caseList.size(); i++) {
            JSONArray fieldArray = caseList.getJSONArray(i);
            JSONObject fieldObject = new JSONObject();
            StringBuilder caseName = new StringBuilder(caseDesc);
            boolean isValidEquivalenceClass = true;
            for (int j = 0; j < fieldArray.size(); j++) {
                JSONObject obj = fieldArray.getJSONObject(j);
                String type = obj.getString("type");
                String key = obj.getString("key");
                String desc = obj.getString("desc");
                Object value = obj.get("value");
                fieldObject.put(key, value);
                caseName.append(" ");
                caseName.append(desc);
                if ("invalidEquivalenceClass".equals(type) && isValidEquivalenceClass) {
                    isValidEquivalenceClass = false;
                }
            }
            JSONObject cs = new JSONObject(new LinkedHashMap());
            cs.put("projectId", projectId);
            cs.put("moduleId", moduleId);
            cs.put("url", url);
            cs.put("method", method);
            cs.put("desc", isValidEquivalenceClass ? "有效等价类 " + caseName.toString() : "无效等价类 " + caseName.toString());
            cs.put("level", isValidEquivalenceClass ? validEquivalenceClassCaseLevel : invalidEquivalenceClassCaseLevel);
            cs.put("doc", doc);
            cs.put("headers", headers);
            if (schemaType.equalsIgnoreCase("data")) {
                cs.put("data", JSON.toJSONString(fieldObject, SerializerFeature.WriteMapNullValue, SerializerFeature.UseSingleQuotes));
            } else if (schemaType.equalsIgnoreCase("json")) {
                cs.put("json", JSON.toJSONString(fieldObject, SerializerFeature.WriteMapNullValue, SerializerFeature.UseSingleQuotes));
            } else {
                cs.put("params", JSON.toJSONString(fieldObject, SerializerFeature.WriteMapNullValue, SerializerFeature.UseSingleQuotes));
            }
            cs.put("asserts", isValidEquivalenceClass ? validEquivalenceClassAssert : invalidEquivalenceClassAssert);
            result.add(cs);
        }
        return result;
    }
}
