package co.ke.integration.mpesa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mpesa_balance_queries")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceQuery extends BaseEntity {

    @Column(nullable = false)
    private String partyA;

    @Column(nullable = false)
    private String commandId;

    @Column(nullable = false)
    private String identifierType;

    @Column(nullable = false, unique = true)
    private String originatorConversationId;

    @Column(nullable = false, unique = true)
    private String conversationId;

    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    private String resultCode;

    private String resultDesc;

    private LocalDateTime completedAt;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @OneToMany(mappedBy = "balanceQuery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountBalanceInfo> accountBalances = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String rawRequest;

    @Column(columnDefinition = "TEXT")
    private String rawResponse;

    @Enumerated(EnumType.STRING)
    private Currency currency;
}
