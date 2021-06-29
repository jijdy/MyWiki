package com.example.mywiki.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Component
public class ReidsRepeatUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ReidsRepeatUtil.class);

    @Resource
    private RedisTemplate redisTemplate;

    /*
    * 对数据进行判断，若redis中有相应的键值对，则返回false
    * 若无，则添加如redis并返回true
    * */
    public boolean RedisRepeat(String key, Long second) {

        if (redisTemplate.hasKey(key)) {
            LOG.info("key以存在：{}",key);
            return false;
        } else {
            LOG.info("key不已存在，放入key{}",key);
            redisTemplate.opsForValue().set(key, 1, second, TimeUnit.SECONDS);
            return true;

        }
    }
}
