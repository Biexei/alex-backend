package org.alex.platform.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, Object value, long expire) {
        if (expire > 0) {
            redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
        } else {
            throw new IllegalArgumentException("set redis key time invalid");
        }
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void expire(String key, long expire) {
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    public void stackPush(String key, Object value) {
        if (key != null) {
            redisTemplate.opsForList().rightPush(key, value);
            redisTemplate.expire(key, 60*60, TimeUnit.SECONDS);
        }
    }

    public void stackPush(String key, Object value, long expire) {
        if (key != null) {
            if (expire > 0) {
                redisTemplate.opsForList().rightPush(key, value);
                redisTemplate.expire(key, expire, TimeUnit.SECONDS);
            } else {
                throw new IllegalArgumentException("set redis key time invalid");
            }
        }
    }

    public void queuePush(String key, Object value) {
        if (key != null) {
            redisTemplate.opsForList().rightPush(key, value);
            redisTemplate.expire(key, 60*60, TimeUnit.SECONDS);
        }
    }

    public void queuePush(String key, Object value, long expire) {
        if (key != null) {
            if (expire > 0) {
                redisTemplate.opsForList().rightPush(key, value);
                redisTemplate.expire(key, expire, TimeUnit.SECONDS);
            } else {
                throw new IllegalArgumentException("set redis key time invalid");
            }
        }
    }

    public Object queuePop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public Object stackPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public List<Object> stackGetAll(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public void hashPut(String key, String hashKey, Object value, long expire) {
        if (expire > 0) {
            redisTemplate.opsForHash().put(key, hashKey, value);
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        } else {
            throw new IllegalArgumentException("set redis key time invalid");
        }
    }

    public void hashPut(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
        redisTemplate.expire(key, 24*60*60, TimeUnit.SECONDS);
    }

    public Object hashGet(String key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public List<Object> hashGetAll(String key) {
        return redisTemplate.opsForHash().values(key);
    }

    public void hashDel(String key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key, hashKeys);
    }

    public Map<Object, Object> hashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public Long lenList(String key) {
        return redisTemplate.opsForList().size(key);
    }

    public boolean exist(String key) {
        Boolean hasKey = redisTemplate.hasKey(key);
        return hasKey == null ? false : hasKey;
    }
}
