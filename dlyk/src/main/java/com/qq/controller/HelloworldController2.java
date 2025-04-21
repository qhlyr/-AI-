package com.qq.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.qq.model.result.R;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/helloworld")
public class HelloworldController2 {
    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";
    private final ChatModel dashScopeChatModel;
    private final Set<String> modelList = Set.of(
            "deepseek-r1",
            "deepseek-v3",
            "qwen-plus",
            "qwen-max"
    );

    public HelloworldController2 (
            @Qualifier("dashscopeChatModel") ChatModel dashScopeChatModel
    ) {
        this.dashScopeChatModel = dashScopeChatModel;
    }

    @GetMapping("/simple/chat/{model}/{prompt}")
    public R simpleChat(@PathVariable("model") String model,
                        @PathVariable("prompt") String prompt) {

        {

            if (!modelList.contains(model)) {
                return R.OK("model not exist");
            }
            System.out.println("===============================================");
            System.out.println("当前输入的模型为：" + model);
            System.out.println("默认模型为：" + DashScopeApi.ChatModel.QWEN_PLUS.getModel());
            System.out.println("===============================================");
            ChatOptions runtimeOptions = ChatOptions.builder().model(model).build();
            Generation gen = dashScopeChatModel.call(
                            new Prompt(prompt, runtimeOptions))
                    .getResult();
            // 使用 R 返回 JSON 格式的响应
            return R.OK(gen.getOutput().getText()); // 将字符串包装在 R 对象中并返回
        }




//
//    @GetMapping(value = "/simple/chat2", produces = "text/html;charset=UTF-8")
//  public Flux<String> simpleChat2(@RequestParam("query") String query,
//                                  @RequestParam(value = "cid", defaultValue = "default-user") String conversationId) {
//        return dashScopeChatModel
//                .prompt()
//                .user(query)
//                .stream()
//                .content();
//    }



    }}