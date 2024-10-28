package co.ke.integration.mpesa.repository;

import co.ke.integration.mpesa.entity.B2CTransaction;
import co.ke.integration.mpesa.entity.B2CCommandType;
import co.ke.integration.mpesa.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface B2CRepository extends JpaRepository<B2CTransaction, Long> {

    Optional<B2CTransaction> findByTransactionId(String transactionId);

    Optional<B2CTransaction> findByConversationId(String conversationId);

    Optional<B2CTransaction> findByOriginatorConversationId(String originatorConversationId);

    List<B2CTransaction> findByPartyB(String phoneNumber);

    List<B2CTransaction> findByStatus(TransactionStatus status);

    List<B2CTransaction> findByCommandId(B2CCommandType commandId);

    List<B2CTransaction> findByPartyA(String shortCode);

    @Query("SELECT t FROM B2CTransaction t WHERE " +
            "t.createdAt BETWEEN :startDate AND :endDate")
    List<B2CTransaction> findTransactionsInDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT t FROM B2CTransaction t WHERE " +
            "t.status = :status AND " +
            "t.createdAt <= :timeout")
    List<B2CTransaction> findStuckTransactions(
            @Param("status") TransactionStatus status,
            @Param("timeout") LocalDateTime timeout
    );

    @Query("SELECT COUNT(t) FROM B2CTransaction t WHERE " +
            "t.partyB = :phoneNumber AND " +
            "t.status = :status AND " +
            "t.createdAt BETWEEN :startDate AND :endDate")
    long countTransactionsByPhoneAndStatus(
            @Param("phoneNumber") String phoneNumber,
            @Param("status") TransactionStatus status,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    boolean existsByTransactionId(String transactionId);

    boolean existsByConversationId(String conversationId);
}
