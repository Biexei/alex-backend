package org.alex.platform.service.impl;

import org.alex.platform.service.TempPostProcessorValueService;
import org.alex.platform.util.NoUtil;
import org.alex.platform.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TempPostProcessorValueServiceImpl implements TempPostProcessorValueService {
    @Autowired
    RedisUtil redisUtil;

    private static final Logger LOG = LoggerFactory.getLogger(TempPostProcessorValueServiceImpl.class);

    /**
     * 获取所有的临时后置处理器值
     *
     * @return map
     */
    @Override
    public Map<Object, Object> findAllTempValue() {
        return redisUtil.hashEntries(NoUtil.TEMP_POST_PROCESSOR_NO);
    }

    /**
     * 获取指定临时变量
     *
     * @param hashKey hashKey
     * @return map
     */
    @Override
    public Map<Object, Object> findTempValueByValue(String hashKey) {
        Object o = redisUtil.hashGet(NoUtil.TEMP_POST_PROCESSOR_NO, hashKey);
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put(hashKey, o);
        return hashMap;
    }

    /**
     * 移除指定临时后置处理器值
     *
     * @param hashKey hashKey
     */
    @Override
    public void removeTempValue(String hashKey) {
        redisUtil.hashDel(NoUtil.TEMP_POST_PROCESSOR_NO, hashKey);
        LOG.info("删除临时后置处理器，hashKey={}", hashKey);
    }

    /**
     * 移除全部临时后置处理器值
     */
    @Override
    public void removeAllTempValue() {
        redisUtil.del(NoUtil.TEMP_POST_PROCESSOR_NO);
        LOG.info("移除全部临时后置处理器值");
    }
}
