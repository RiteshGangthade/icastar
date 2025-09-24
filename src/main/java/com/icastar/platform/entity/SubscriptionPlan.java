package com.icastar.platform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "subscription_plans")
@Data
@EqualsAndHashCode(callSuper = true)
public class SubscriptionPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment primary key
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan_type", nullable = false)
    private PlanType planType;

    @Column(name = "price", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false)
    private BillingCycle billingCycle;

    @Column(name = "max_auditions")
    private Integer maxAuditions;

    @Column(name = "max_messages")
    private Integer maxMessages;

    @Column(name = "unlimited_auditions", nullable = false)
    private Boolean unlimitedAuditions = false;

    @Column(name = "unlimited_messages", nullable = false)
    private Boolean unlimitedMessages = false;

    @Column(name = "job_boost_credits")
    private Integer jobBoostCredits = 0;

    @Column(name = "priority_support", nullable = false)
    private Boolean prioritySupport = false;

    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    @OneToMany(mappedBy = "subscriptionPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subscription> subscriptions;

    public enum PlanType {
        FREE, PREMIUM, ENTERPRISE
    }

    public enum BillingCycle {
        MONTHLY, YEARLY, ONE_TIME
    }
}
