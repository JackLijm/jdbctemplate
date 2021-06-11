package com.example.billmanager.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @Author lijm
 * @Date 2019/11/14 16:31
 * @Version 1.0
 */
public abstract class BaseRedisHashCache<T> {

    protected long TIMEOUT = 1800;
    @Resource
    protected RedisTemplate redisTemplate;

    /**
     * 根据业务key获得redis中实际存储的key
     *
     * @param key 业务key
     * @return redis中存储的真实key
     */
    protected abstract String getKey(String key);

    /**
     * 获取hash表{@code key}对应{@code hashKey}的值
     *
     * @param key     must not be {@literal null}.
     * @param hashKey must not be {@literal null}.
     */
    public T get(String key, String hashKey) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(getKey(key), hashKey);
    }

    /**
     * 获取hash表{@code key}对应{@code hashKey}的值
     *
     * @param key must not be {@literal null}.
     */
    public Map<String, T> getAll(String key) {
        HashOperations<String, String, T> hashOperations = redisTemplate.opsForHash();
        return hashOperations.entries(getKey(key));
    }

    /**
     * 为hash表设置hashKey和value
     */
    public void put(String key, String hashKey, T value) {
        redisTemplate.opsForHash().put(getKey(key), hashKey, value);
    }



    /**
     *
     */
    public void putAll(String key, Map<String, T> entries) {
        redisTemplate.opsForHash().putAll(getKey(key), entries);
        redisTemplate.expire(getKey(key), TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * 删除hash表对应的hashKey
     */
    public void remove(String key, String... hashKey) {
        redisTemplate.opsForHash().delete(getKey(key), hashKey);
    }

    /**
     * 删除整个key
     */
    public void delete(String key) {
        redisTemplate.delete(getKey(key));
    }

    public boolean hasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(getKey(key), hashKey);
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(getKey(key));
    }

    public List<T> getValues(String key) {
        return redisTemplate.opsForHash().values(getKey(key));
    }

}
