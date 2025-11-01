package engineer.hyper.agentic.config;

import engineer.hyper.agentic.service.MyLogAdvisor;
import engineer.hyper.agentic.tools.MyTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Autowired
    private MyTools myTools;
    @Autowired
    private MyLogAdvisor myLogAdvisor;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultTools(myTools)
                .defaultAdvisors(myLogAdvisor)
                .build();
    }
}
