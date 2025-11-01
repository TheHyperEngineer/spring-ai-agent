package engineer.hyper.agentic.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

@Component
public final class ChatMessageConverter {

    private final ObjectMapper objectMapper;

    public ChatMessageConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ChatMessageEntity toEntity(String conversationId, Message message, Instant createdAt) {
        String type = message.getMessageType().name();
        String text = message.getText();
        String metadataJson;
        try {
            metadataJson = objectMapper.writeValueAsString(message.getMetadata());
        } catch (JsonProcessingException e) {
            metadataJson = "{}";
        }
        return new ChatMessageEntity(conversationId, type, text, metadataJson, createdAt);
    }

    public Message toMessage(ChatMessageEntity e) {
        Map<String, Object> metadata = Collections.emptyMap();
        try {
            metadata = objectMapper.readValue(e.getMetadataJson(), new TypeReference<>() {});
        } catch (Exception ex) { /* ignore, fallback empty map */ }

        MessageType mt = MessageType.valueOf(e.getMessageType());
        if (mt == MessageType.SYSTEM) {
            return SystemMessage.builder().text(e.getTextContent()).metadata(metadata).build();
        } else if (mt == MessageType.USER) {
            return UserMessage.builder().text(e.getTextContent()).metadata(metadata).build();
        } else {
            // For other message types, default to AbstractMessage via builder or a generic implementation
            return UserMessage.builder().text(e.getTextContent()).metadata(metadata).build();
        }
    }
}