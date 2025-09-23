package com.icastar.platform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "subscriptions")
@Data
@EqualsAndHashCode(callSuper = true)
public class Subscription extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_plan_id", nullable = false)
    private SubscriptionPlan subscriptionPlan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubscriptionStatus status;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "auto_renew", nullable = false)
    private Boolean autoRenew = false;

    @Column(name = "amount_paid", columnDefinition = "DECIMAL(10,2)")
    private BigDecimal amountPaid;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "auditions_used")
    private Integer auditionsUsed = 0;

    @Column(name = "messages_used")
    private Integer messagesUsed = 0;

    @Column(name = "job_boosts_used")
    private Integer jobBoostsUsed = 0;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Payment> payments;

    public enum SubscriptionStatus {
        ACTIVE, EXPIRED, CANCELLED, SUSPENDED
    }
}
