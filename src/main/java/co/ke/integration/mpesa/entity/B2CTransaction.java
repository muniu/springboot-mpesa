package co.ke.integration.mpesa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "mpesa_b2c_transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class B2CTransaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String transactionId;

    @Column(unique = true)
    private String conversationId;

    private String originatorConversationId;

    @Enumerated(EnumType.STRING)
    private B2CCommandType commandId;  // SalaryPayment, BusinessPayment, PromotionPayment

    private String partyA;  // Business shortcode
    private String partyB;  // Customer phone
    private BigDecimal amount;
    private String remarks;
    private String occasion;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String resultCode;
    private String resultDesc;

    private BigDecimal workingAccountFunds;
    private BigDecimal utilityAccountFunds;
    private LocalDateTime transactionCompletedTime;
    private BigDecimal chargesPaidAccountFunds;

    @Column(columnDefinition = "TEXT")
    private String rawRequest;

    @Column(columnDefinition = "TEXT")
    private String rawCallback;
}

