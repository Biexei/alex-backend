package org.alex.platform.controller;

import org.alex.platform.common.Result;
import org.alex.platform.service.TempPostProcessorValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TempPostProcessorValueController {
    @Autowired
    TempPostProcessorValueService tempPostProcessorValueService;

    /**
     * 获取所有的临时后置处理器值
     *
     * @return Result
     */
    @GetMapping("/env")
    public Result findAllTempValue(@RequestParam  HashMap<String,String> query) {
        ArrayList<Object> list = new ArrayList<>();
        String key = query.get("key");
        Map<Object, Object> map;
        if (key == null || key.isEmpty()) {
            map = tempPostProcessorValueService.findAllTempValue();
        } else {
            map = (tempPostProcessorValueService.findTempValueByValue(key));
        }
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("key", entry.getKey());
            hashMap.put("value", entry.getValue());
            list.add(hashMap);
        }
        return Result.success(list);
    }

    /**
     * 移除指定临时后置处理器值
     *
     * @param key hashKey
     * @return Result
     */
    @GetMapping("/env/remove/{key}")
    public Result removeTempValue(@PathVariable String key) {
        tempPostProcessorValueService.removeTempValue(key);
        return Result.success("删除成功");
    }

    /**
     * 移除全部临时后置处理器值
     *
     * @return Result
     */
    @GetMapping("/env/removeAll")
    public Result removeAllTempValue() {
        tempPostProcessorValueService.removeAllTempValue();
        return Result.success("删除成功");
    }
}
