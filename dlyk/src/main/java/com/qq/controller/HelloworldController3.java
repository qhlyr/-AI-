package com.qq.controller;

import com.qq.model.result.R;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/helloworld")
public class HelloworldController3 {
    private final ChatClient dashscopeClient;
    private final ChatClient openaiClient;
    public HelloworldController3(
            @Qualifier("dashscopeClient") ChatClient dashscopeClient,
            @Qualifier("openaiClient") ChatClient openaiClient
    ) {
        this.dashscopeClient = dashscopeClient;
        this.openaiClient = openaiClient;
    }
    @GetMapping("/chat")
    public R chat(@RequestParam("query") String query,
                  @RequestParam("model") String model) {
        ChatClient client;

        switch (model) {
            case "openai" -> client = openaiClient;
            case "dashscope" -> client = dashscopeClient;
            default -> {
                return R.FAIL("不支持的模型: " + model);
            }
        }

        String reply = client.prompt(query).call().content();
        return R.OK(reply);
    }
    

}