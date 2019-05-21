package com.run.reids;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * Redis缓存配置类
 */
@Configuration
@EnableCaching//开启缓存功能
@EnableTransactionManagement//开启事务
public class RedisConfig extends CachingConfigurerSupport {

    private static Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.timeout}")
    private int timeout;

    /**
     * RedisTemplate 对象
     *
     * @param factory
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        logger.info(">>>>>>>>>>>>>rebuild redisTemplate  ...");
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        StringRedisSerializer redisSerializer = new StringRedisSerializer();
        //key采用String的序列化方式
        template.setKeySerializer(redisSerializer);
        //hash的key也采用String的序列化方式
        template.setHashKeySerializer(redisSerializer);
        //value序列化方式采用jackson
        template.setValueSerializer(redisSerializer);
        //hash的value序列化方式采用jackson
        template.setHashValueSerializer(redisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 缓存管理器
     *
     * @param factory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        //解决查询缓存转换异常的问题
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                //设置缓存管理器管理的缓存的默认过期时间
                .entryTtl(Duration.ofSeconds(timeout))
                //设置 key为string序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                //设置value为json序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                //不缓存空值
                .disableCachingNullValues();
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .build();
        return cacheManager;
    }

    /**
     * 自定义的缓存key生成策略：方法名
     * 此处需要注意，如果加参数名，则controller层传参数不能使用@PathVariable注解传值，否则得到的是参数值而不是参数名称
     *
     * @return
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
//                sb.append(target.getClass().getName());
                sb.append(method.getName());
//                for(Object obj:params){
//                    sb.append(obj.toString());
//                }
                logger.info(">>>>>>>>>>>>>rebuild keyGenerator ...");
                logger.info(">>>>>>>>>>>>>cache key is :" + sb.toString());
                return sb.toString();
            }
        };
    }
}
