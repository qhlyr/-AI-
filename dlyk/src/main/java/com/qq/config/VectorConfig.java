package com.qq.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

@Configuration
public class VectorConfig {

    @Bean
    public JedisPooled jedisPooled() {
        return new JedisPooled("localhost", 6379); // 或你的 Redis 地址端口
    }


    @Bean
    public RedisVectorStore redisVectorStore(JedisPooled jedis,
                                             @Qualifier("dashscopeEmbeddingModel") EmbeddingModel model) {
        return RedisVectorStore.builder(jedis, model)
                .indexName("chat-memory")
                .prefix("chat:")
                .initializeSchema(true)
                .metadataFields(RedisVectorStore.MetadataField.tag("sessionId"))
                .build();
    }


}
