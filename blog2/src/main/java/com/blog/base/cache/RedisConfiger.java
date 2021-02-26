package com.blog.base.cache;

import com.blog.base.util.ProtostuffSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Auther: wanglu
 * @Date: 2019/5/18 14:43
 * @Description:
 */
@Slf4j
//@Configuration
public class RedisConfiger {

//    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        log.info("Redis Init ....");
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        ProtostuffSerializer serializer = new ProtostuffSerializer();
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        // 事务开启与否
        template.setEnableTransactionSupport(false);
        template.setConnectionFactory(connectionFactory);
        template.afterPropertiesSet();
        return template;
    }
}
