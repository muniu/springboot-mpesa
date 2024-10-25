package co.ke.integration.mpesa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "mpesa_account_balances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "balance_query_id", nullable = false)
    private BalanceQuery balanceQuery;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Column(precision = 20, scale = 2)
    private BigDecimal currentBalance;

    @Column(precision = 20, scale = 2)
    private BigDecimal reservedBalance;

    @Column(precision = 20, scale = 2)
    private BigDecimal unclearedBalance;
}

