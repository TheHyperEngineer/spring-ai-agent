package engineer.hyper.agentic.repository;

import engineer.hyper.agentic.entity.ChatMessageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findByConversationIdOrderByCreatedAtAsc(String conversationId);

    @Query("select m.id from ChatMessageEntity m where m.conversationId = :conversationId order by m.createdAt asc")
    List<Long> findIdsByConversationIdOrderByCreatedAtAsc(@Param("conversationId") String conversationId, Pageable pageable);

    long countByConversationId(String conversationId);

    @Modifying
    @Query("delete from ChatMessageEntity m where m.conversationId = :conversationId")
    void deleteByConversationIdCustom(@Param("conversationId") String conversationId);

    @Modifying
    @Query("delete from ChatMessageEntity m where m.id in :ids")
    int deleteByIds(@Param("ids") Collection<Long> ids);
}
