package org.alex.platform.generator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.alex.platform.exception.BusinessException;
import org.alex.platform.util.CommandUtil;
import org.alex.platform.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Rule {
    @Autowired
    RedisUtil redisUtil;
    @Value("${myself.path.python-ort}")
    String pyScriptPath;
    /**
     * 根据用例列表生成笛卡尔积测试用例
     * @param itemSizeList 单属性的用例
     * @param itemList 单属性用例集合
     * @param result 用来接收返回结果，传空数组
     * @return 笛卡尔积
     */
    public JSONArray cartesian(ArrayList<Integer> itemSizeList, JSONArray itemList, JSONArray result) {
        JSONArray var1 = new JSONArray();
        if (itemList.size() < 2) {
            return itemList;
        } else {
            // 将最小item由object转成array包装object
            for (int i = 0; i < itemList.size(); i++) {
                JSONArray var2 = new JSONArray();
                JSONArray singleFieldArray = itemList.getJSONArray(i);
                for (int j = 0; j < singleFieldArray.size(); j++) {
                    JSONArray var3 = new JSONArray();
                    var3.add(singleFieldArray.getJSONObject(j));
                    var2.add(var3);
                }
                var1.add(var2);
            }
            if (var1.size() <= 1) {
                return var1;
            } else {
                return cartesianWrapper(itemSizeList, var1, result);
            }
        }
    }

    /**
     * 根据用例列表根据正交法生成测试用例
     * @param key 调用python脚本，入参以及出参缓存key
     * @param itemList 单属性用例集合
     * @return 正交法
     */
    public JSONArray ort(String key, JSONArray itemList) throws BusinessException {
        if (itemList.size() < 2) {
            return itemList;
        }
        redisUtil.set(key, itemList);
        String pyOrtCmd = String.format("python %s %s", pyScriptPath, key);
        String execResult = CommandUtil.exec(pyOrtCmd);
        if ("".equals(execResult)) {
            JSONArray array = JSON.parseArray(JSON.toJSONString(redisUtil.get(key)));
            redisUtil.del(key);
            return array;
        } else {
            throw new BusinessException("正交法运行失败，" + execResult);
        }
    }

    /**
     * 根据用例列表生成笛卡尔积测试用例
     * @param itemSizeList 单属性的用例
     * @param itemList 单属性用例集合
     * @param result 用来接收返回结果，传空数组
     * @return 笛卡尔积
     */
    private JSONArray cartesianWrapper(ArrayList<Integer> itemSizeList, JSONArray itemList, JSONArray result) {

        if (itemSizeList.size() > 1) {

            itemSizeList.remove(0);
            itemSizeList.remove(0);

            JSONArray currentNode = (JSONArray) itemList.remove(0);
            JSONArray nextNode = (JSONArray) itemList.remove(0);

            result = new JSONArray();
            for (int i = 0; i < currentNode.size(); i++) {
                for (int j = 0; j < nextNode.size(); j++) {
                    JSONArray array1 = new JSONArray();
                    JSONArray var1 = currentNode.getJSONArray(i);
                    JSONArray var2 = nextNode.getJSONArray(j);
                    array1.addAll(var1);
                    array1.addAll(var2);
                    result.add(array1);
                }
            }
            itemList.add(result);
            itemSizeList.add(result.size());
            result = cartesianWrapper(itemSizeList, itemList, result);
        }
        return result;
    }
}
