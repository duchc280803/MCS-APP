package com.example.orderservice.application.service.impl;

import com.example.orderservice.application.service.BaseRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class BaseRedisServiceImpl implements BaseRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final HashOperations<String, String, Object> objectHashOperations;

    @Autowired
    public BaseRedisServiceImpl(
            RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectHashOperations = redisTemplate.opsForHash();
    }

    /**
     * Stores a key-value pair in Redis
     * @param key
     * @param value
     */
    @Override
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * Set the lifetime for a key in Redis
     * @param key
     * @param timeOutInDay
     */
    @Override
    public void setTimeToLive(String key, long timeOutInDay) {
        redisTemplate.expire(key, timeOutInDay, TimeUnit.DAYS);
    }

    /**
     * Store a key-field-value pair in a Redis hash and set the key lifetime.
     * @param key
     * @param field
     * @param value
     */
    @Override
    public void hashSetTolive(String key, String field, Object value) {
        objectHashOperations.put(key, field, value);
    }

    /**
     * Checks if a field exists in a Redis hash.
     * @param key
     * @param field
     * @return
     */
    @Override
    public boolean hashExits(String key, String field) {
        return objectHashOperations.hasKey(key, field);
    }

    /**
     * Get the value corresponding to a key from Redis.
     * @param key
     * @return
     */
    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Get all fields and their corresponding values ​​from a Redis hash.
     * @param key
     * @return
     */
    @Override
    public Map<String, Object> getField(String key) {
        return objectHashOperations.entries(key);
    }

    /**
     * Get the value of a field from a Redis hash.
     * @param key
     * @param field
     * @return
     */
    @Override
    public Object hashGet(String key, String field) {
        return objectHashOperations.get(key, field);
    }

    /**
     * Gets a list of values of fields with a specific prefix from a Redis hash.
     * @param key
     * @param fieldPrefix
     * @return
     */
    @Override
    public List<Object> hashGetByFieldPrefix(String key, String fieldPrefix) {
        List<Object> objectList = new ArrayList<>();

        Map<String, Object> objectMap = objectHashOperations.entries(key);

        for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
            if (entry.getKey().startsWith(fieldPrefix)) {
                objectList.add(entry.getValue());
            }
        }
        return objectList;
    }

    /**
     * Get all fields in a Redis hash.
     * @param key
     * @return
     */
    @Override
    public Set<String> getFieldPrefixes(String key) {
        return objectHashOperations.entries(key).keySet();
    }

    /**
     * Delete a key from Redis.
     * @param key
     */
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Delete a field from a Redis hash.
     * @param key
     * @param field
     */
    @Override
    public void delete(String key, String field) {
        objectHashOperations.delete(key, field);
    }

    /**
     * Delete a list of fields from a Redis hash.
     * @param key
     * @param field
     */
    @Override
    public void delete(String key, List<String> field) {
        for (String s : field) {
            objectHashOperations.delete(key, s);
        }
    }
}
