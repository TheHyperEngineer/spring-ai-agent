package engineer.hyper.agentic.repository;

import engineer.hyper.agentic.entity.ChatMessageConverter;
import engineer.hyper.agentic.entity.ChatMessageEntity;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JpaChatMemoryRepository implements ChatMemoryRepository {

    private final ChatMessageJpaRepository jpa;
    private final ChatMessageConverter converter;

    public JpaChatMemoryRepository(ChatMessageJpaRepository jpa, ChatMessageConverter converter) {
        this.jpa = jpa;
        this.converter = converter;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> findByConversationId(String conversationId) {
        return jpa.findByConversationIdOrderByCreatedAtAsc(conversationId).stream()
                .map(converter::toMessage)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveAll(String conversationId, List<Message> messages) {
        jpa.deleteByConversationIdCustom(conversationId);
        Instant now = Instant.now();
        List<ChatMessageEntity> entities = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++) {
            // increment createdAt slightly to preserve order if necessary
            entities.add(converter.toEntity(conversationId, messages.get(i), now.plusMillis(i)));
        }
        jpa.saveAll(entities);
    }

    @Override
    @Transactional
    public void deleteByConversationId(String conversationId) {
        jpa.deleteByConversationIdCustom(conversationId);
    }

    @Override
    public List<String> findConversationIds() {
        // implement a projection query if needed; for now throw or implement using EntityManager
        throw new UnsupportedOperationException("Implement findConversationIds with a projection query if required");
    }
}