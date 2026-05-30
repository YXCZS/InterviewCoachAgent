package com.yxc.InterviewCoachAgent;

import cn.hutool.core.lang.UUID;
import com.yxc.InterviewCoachAgent.app.InterviewCoachApp;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InterviewCoachAppTest {

    @Resource
    private InterviewCoachApp interviewCoachApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();//保证多轮会话是在同一个会话id内，所以创建一个id
        // 第一轮
        String message = "你好，我是yxc，我想了解一下面试的流程";
        String answer = interviewCoachApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        message = "我想面试需要做哪些准备";
        answer = interviewCoachApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我刚才问的你啥来着";
        answer = interviewCoachApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }


    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是yxc，我想学习一下面试的流程以及我该如何准备面试";
        InterviewCoachApp.ResearchReport researchReport = interviewCoachApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(researchReport);
    }

}

