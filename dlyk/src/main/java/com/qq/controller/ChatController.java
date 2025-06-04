package com.qq.controller;

import com.qq.model.dto.ChatSaveRequest;
import com.qq.service.impl.ChatHistoryService;
import com.qq.model.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/helloworld/chat")
public class ChatController {

    @Autowired
    private ChatHistoryService chatHistoryService;

    @Autowired
    private RedisVectorStore redisVectorStore;

    // 注意：EmbeddingModel 实际返回的是 float[]，不是 Embedding 类型
    @Autowired
    @Qualifier("dashscopeEmbeddingModel")
    private EmbeddingModel embeddingModel;

    /**
     * 保存聊天向量到 Redis 向量数据库
     */
    @PostMapping("/save")
    public R saveToVector(@RequestBody ChatSaveRequest request) {
        String text = request.getText();
        // ✅ 打印 sessionId（调试是否和你 filterExpression 对得上）
        System.out.println("🔖 sessionId = " + request.getSessionId());
        // 1. 获取 float[] 向量
        float[] floatArray = embeddingModel.embed(text);

        // 2. 转换为 List<Float>
        List<Float> vector = new ArrayList<>(floatArray.length);
        for (float v : floatArray) {
            vector.add(v);
        }

        // 3. 创建 Document 向量文档
        // 3. 构造 Document 对象
        Document doc = new Document(
                text,
                Map.of(
                        "sessionId", request.getSessionId(),
                        "isUser", String.valueOf(request.isUser()),
                        "timestamp", String.valueOf(System.currentTimeMillis())
                )
        );

        log.info("✅ 正在保存向量：{}", text);
        log.info("🧠 向量维度：{}", vector.size());

        // 4. 存储向量
        redisVectorStore.add(List.of(doc));

        return R.OK("向量保存成功！");
    }

    /**
     * 查询相似聊天记录
     */
    @GetMapping("/search")
    public R searchSimilar(@RequestParam String sessionId,
                           @RequestParam String query) {
        List<Document> results = redisVectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(3)
                        .filterExpression("sessionId == '" + sessionId + "'")
                        .build()
        );

        List<String> contents = results.stream()
                .map(Document::getText)
                .toList();
        log.info("📥 正在搜索，query='{}', sessionId='{}'", query, sessionId);

        log.info("🔍 相似内容结果：{}", contents);

        return R.OK(contents);
    }
}
