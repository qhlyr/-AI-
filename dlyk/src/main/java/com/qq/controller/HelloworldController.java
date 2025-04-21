package com.qq.controller;

import com.qq.model.result.R;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/helloworld")
public class HelloworldController {
  private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";
  private final ChatClient dashScopeChatClient;

    public HelloworldController(@Qualifier("dashscopeClient") ChatClient dashScopeChatClient) {
        this.dashScopeChatClient = dashScopeChatClient;
    }

    @GetMapping("/simple/chat")
    public R simpleChat(String query) {
        System.out.println("Received query: " + query); // 打印收到的 query
        String responseMessage = dashScopeChatClient.prompt(query).call().content();
        System.out.println("Response: " + responseMessage); // 打印返回的响应

        // 使用 R 返回 JSON 格式的响应
        return R.OK(responseMessage); // 将字符串包装在 R 对象中并返回
    }





    @GetMapping(value = "/simple/chat2", produces = "text/html;charset=UTF-8")
  public Flux<String> simpleChat2(@RequestParam("query") String query,
                                  @RequestParam(value = "cid", defaultValue = "default-user") String conversationId) {
        return dashScopeChatClient
                .prompt()
                .user(query)
                .stream()
                .content();
    }



}