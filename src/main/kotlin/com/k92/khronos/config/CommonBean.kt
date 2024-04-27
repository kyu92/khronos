package com.k92.khronos.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.k92.khronos.service.AttendanceService
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class CommonBean {

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory?): RedisTemplate<String, Any> {
        val redisTemplate: RedisTemplate<String, Any> = RedisTemplate()
        redisTemplate.connectionFactory = redisConnectionFactory
        val stringRedisSerializer = StringRedisSerializer()
        val mapper = ObjectMapper()
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL)
        val jsonRedisSerializer: Jackson2JsonRedisSerializer<Any> = Jackson2JsonRedisSerializer(mapper, Any::class.java)
        redisTemplate.keySerializer = stringRedisSerializer
        redisTemplate.stringSerializer = stringRedisSerializer
        redisTemplate.valueSerializer = jsonRedisSerializer
        redisTemplate.hashKeySerializer = stringRedisSerializer
        redisTemplate.hashValueSerializer = jsonRedisSerializer
        redisTemplate.afterPropertiesSet()
        return redisTemplate
    }

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate();
    }
}