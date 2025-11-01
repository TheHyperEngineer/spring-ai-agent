package engineer.hyper.agentic.memory;

import engineer.hyper.agentic.entity.ChatMessageConverter;
import engineer.hyper.agentic.repository.ChatMessageJpaRepository;
import engineer.hyper.agentic.repository.JpaChatMemoryRepository;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Component
public class PersistentMessageWindowChatMemory implements ChatMemory {

    private static final int DEFAULT_MAX_MESSAGES = 100; // configurable
    private final JpaChatMemoryRepository repository;
    private final ChatMessageConverter converter;
    private final int maxMessages;
    private final ChatMessageJpaRepository jpaRepo;

    public PersistentMessageWindowChatMemory(JpaChatMemoryRepository repository,
                                             ChatMessageConverter converter,
                                             ChatMessageJpaRepository jpaRepo,
                                             int maxMessages) {
        this.repository = repository;
        this.converter = converter;
        this.jpaRepo = jpaRepo;
        this.maxMessages = maxMessages > 0 ? maxMessages : DEFAULT_MAX_MESSAGES;
    }

    @Override
    @Transactional
    public void add(String conversationId, List<Message> messages) {
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        Assert.notNull(messages, "messages cannot be null");
        Assert.noNullElements(messages, "messages cannot contain null elements");

        List<Message> memoryMessages = repository.findByConversationId(conversationId);
        List<Message> processedMessages = process(memoryMessages, messages);
        repository.saveAll(conversationId, processedMessages);

        // If we exceed maxMessages, trim oldest non-system messages first.
        long total = jpaRepo.countByConversationId(conversationId);
        if (total > maxMessages) {
            int toRemove = (int) (total - maxMessages);
            // Try to delete by native query (delete oldest rows), fallback to loading and selective deletes.
            jpaRepo.deleteByConversationIdCustom(conversationId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> get(String conversationId) {
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        return repository.findByConversationId(conversationId);
    }

    @Override
    @Transactional
    public void clear(String conversationId) {
        Assert.hasText(conversationId, "conversationId cannot be null or empty");
        repository.deleteByConversationId(conversationId);
    }

    // Reuse same logic as MessageWindowChatMemory.process
    private List<Message> process(List<Message> memoryMessages, List<Message> newMessages) {
        List<Message> processedMessages = new ArrayList<>();
        Set<Message> memoryMessagesSet = new HashSet<>(memoryMessages);
        Stream<Message> newStream = newMessages.stream();
        boolean hasNewSystemMessage = newStream.filter(SystemMessage.class::isInstance)
                                              .anyMatch(m -> !memoryMessagesSet.contains(m));
        memoryMessages.stream()
                      .filter(m -> !hasNewSystemMessage || !(m instanceof SystemMessage))
                      .forEach(processedMessages::add);
        processedMessages.addAll(newMessages);

        if (processedMessages.size() <= this.maxMessages) {
            return processedMessages;
        } else {
            int messagesToRemove = processedMessages.size() - this.maxMessages;
            List<Message> trimmedMessages = new ArrayList<>();
            int removed = 0;

            for (Message message : processedMessages) {
                if (!(message instanceof SystemMessage) && removed < messagesToRemove) {
                    ++removed;
                } else {
                    trimmedMessages.add(message);
                }
            }
            return trimmedMessages;
        }
    }
}