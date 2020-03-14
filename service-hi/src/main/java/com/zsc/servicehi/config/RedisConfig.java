package com.zsc.servicehi.config;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.Charset;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {

    RedisConfig(){
        //打开autotype功能,需要强转的类一次添加其后
        ParserConfig.getGlobalInstance()
                .addAccept("model.pollutant.,model.air.,model.result.,model.weather.");
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //设置key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        //设置hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);

        FastJson2JsonRedisSerializer fastJson2JsonRedisSerializer =new FastJson2JsonRedisSerializer<Object>(Object.class);
        //设置value采用的fastjson的序列化方式
        template.setValueSerializer(fastJson2JsonRedisSerializer);
        //设置hash的value采用的fastjson的序列化方式
        template.setHashValueSerializer(fastJson2JsonRedisSerializer);
        //设置其他默认的序列化方式为fastjson
        template.setDefaultSerializer(fastJson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    public static class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T>{
        public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

        private Class<T> clazz;

        public FastJson2JsonRedisSerializer(Class<T> clazz) {
            super();
            this.clazz = clazz;
        }

        @Override
        public byte[] serialize(T t) throws SerializationException {
            if (t == null) {
                return new byte[0];
            }
            return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
        }

        @Override
        public T deserialize(byte[] bytes) throws SerializationException {
            if (bytes == null || bytes.length <= 0) {
                return null;
            }
            String str = new String(bytes, DEFAULT_CHARSET);
            return JSON.parseObject(str, clazz);
        }

    }
}
