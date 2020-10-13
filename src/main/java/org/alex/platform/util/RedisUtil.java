package org.alex.platform.util;

import org.alex.platform.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, Object value, long time) throws BusinessException {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            throw new BusinessException("set redis key time invalid");
        }
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void expire(String key, long time) {
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    public void stackPush(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
        redisTemplate.expire(key, 30*60, TimeUnit.SECONDS);
    }

    public Object stackPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public List<Object> stackGetAll(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }
}
