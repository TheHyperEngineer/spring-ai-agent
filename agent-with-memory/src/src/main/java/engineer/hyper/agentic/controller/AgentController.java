package engineer.hyper.agentic.controller;

import engineer.hyper.agentic.memory.PersistentMessageWindowChatMemory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AgentController {

    private final ChatClient chatClient;
    private final PersistentMessageWindowChatMemory chatMemory;

    public AgentController(ChatClient.Builder builder, PersistentMessageWindowChatMemory chatMemory) {
        this.chatClient = builder.build();
        this.chatMemory = chatMemory;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String question) {
        return chatClient
                .prompt()
                .system("You are a helpful chat assistant.")
                .user(question)
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .call()
                .content();
    }
}
