package co.ke.integration.mpesa.repository;

import co.ke.integration.mpesa.entity.BalanceQuery;
import co.ke.integration.mpesa.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BalanceQueryRepository extends JpaRepository<BalanceQuery, Long>,
        JpaSpecificationExecutor<BalanceQuery> {

    Optional<BalanceQuery> findByConversationId(String conversationId);

    Optional<BalanceQuery> findByOriginatorConversationId(String originatorConversationId);

    List<BalanceQuery> findByStatus(TransactionStatus status);

    @Query("""
            SELECT bq FROM BalanceQuery bq 
            WHERE bq.status = :status 
            AND bq.createdAt <= :timeout
            AND bq.completedAt IS NULL
            """)
    List<BalanceQuery> findStuckTransactions(
            @Param("status") TransactionStatus status,
            @Param("timeout") LocalDateTime timeout
    );
}
