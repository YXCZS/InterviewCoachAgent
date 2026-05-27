package com.yxc.irisagent.demo;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.demo.ai-runner.enabled", havingValue = "true")
public class SpringAiAiInvoke implements CommandLineRunner {

    private final ChatModel chatModel;

    public SpringAiAiInvoke(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public void run(String... args) {
        AssistantMessage output = chatModel.call(new Prompt("Hello, I am yxc."))
                .getResult()
                .getOutput();
        System.out.println(output.getText());
    }
}
