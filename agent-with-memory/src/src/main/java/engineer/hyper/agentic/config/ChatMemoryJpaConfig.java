package engineer.hyper.agentic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import engineer.hyper.agentic.entity.ChatMessageConverter;
import engineer.hyper.agentic.memory.PersistentMessageWindowChatMemory;
import engineer.hyper.agentic.repository.ChatMessageJpaRepository;
import engineer.hyper.agentic.repository.JpaChatMemoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "engineer.hyper.agentic.repository")
@EntityScan(basePackages = "engineer.hyper.agentic.entity")
public class ChatMemoryJpaConfig {

    @Bean
    public ChatMessageConverter chatMessageConverter(ObjectMapper objectMapper) {
        return new ChatMessageConverter(objectMapper);
    }

    @Bean
    public PersistentMessageWindowChatMemory persistentMessageWindowChatMemory(JpaChatMemoryRepository repository,
                                                                               ChatMessageConverter converter,
                                                                               ChatMessageJpaRepository jpaRepo,
                                                                               @Value("${chat.memory.maxMessages:100}") int maxMessages) {
        return new PersistentMessageWindowChatMemory(repository, converter, jpaRepo, maxMessages);
    }
}