CREATE TABLE chat_messages (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  conversation_id VARCHAR(200) NOT NULL,
  message_type VARCHAR(50) NOT NULL,
  author VARCHAR(200),
  text_content CLOB,
  metadata_json CLOB,
  created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_conv_created ON chat_messages (conversation_id, created_at);
CREATE INDEX idx_conv_type ON chat_messages (conversation_id, message_type);