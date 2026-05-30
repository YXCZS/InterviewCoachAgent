package com.yxc.InterviewCoachAgent.app;

import com.yxc.InterviewCoachAgent.advisor.MyLoggerAdvisor;
import com.yxc.InterviewCoachAgent.chatmemory.FileBasedChatMemory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class InterviewCoachApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = """
            你是一名资深面试辅导教练，负责帮助用户准备求职面试，包含自我介绍、项目表达、技术问答、HR 问题、模拟面试和面试复盘。
            回答时优先给出直接建议，再补充回答思路、示例表达、常见追问和改进建议。
            如果用户提供的信息不完整，主动追问目标岗位、工作年限、技术栈、项目经历和面试轮次。
            如果用户是在做模拟面试，要以面试官视角连续追问，并在回答后给出点评。
            如果命中了知识库内容，要尽量基于知识库回答，不要无依据扩展。
            """;

    /**
     * 初始化 AI 客户端
     *
     * @param chatClientBuilder chat client builder
     */
    public InterviewCoachApp(ChatClient.Builder chatClientBuilder) {
//        ChatMemory chatMemory = MessageWindowChatMemory.builder()
//                .chatMemoryRepository(new InMemoryChatMemoryRepository())
//                .maxMessages(20)
//                .build();
        String fileDir = System.getProperty("user.dir") + "/tmp/chat-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);

        this.chatClient = chatClientBuilder
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new MyLoggerAdvisor()
                )
                .build();
    }

    /**
     * AI 基础对话，支持多轮对话记忆
     *
     * @param message user message
     * @param chatId conversation id
     * @return ai response
     */
    public String doChat(String message, String chatId) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    public record ResearchReport(String title, List<String> keyFindings) {
    }

    public ResearchReport doChatWithReport(String message, String chatId) {
        ResearchReport researchReport = chatClient.prompt()
                .system(SYSTEM_PROMPT + """
                        每次对话后都要生成一份面试辅导报告，标题为用户当前问题对应的面试辅导报告，内容为本次回答的关键建议和后续提升方向。
                        """)
                .user(message)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .entity(ResearchReport.class);
        log.info("researchReport: {}", researchReport);
        return researchReport;
    }
}
