package com.yxc.irisagent;

import cn.hutool.core.lang.UUID;
import com.yxc.irisagent.app.IrisApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IrisAppTest {

    @Resource
    private IrisApp IrisApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();//保证多轮会话是在同一个会话id内，所以创建一个id
        // 第一轮
        String message = "你好，我是yxc，我想了解一下字节跳动这个企业";
        String answer = IrisApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我想进入字节需要做哪些准备";
        answer = IrisApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我刚才问的你啥来着";
        answer = IrisApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }
}

